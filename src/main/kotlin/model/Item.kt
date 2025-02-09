package model

import utils.*
import kotlinx.serialization.*
import controller.Mongo
import org.bson.Document

const val itemColumnWidth = 6
const val itemIdColumnWidth = 36
const val itemNameColumnWidth = 30
const val itemDescriptionColumnWidth = 50

/**
 * ItemData object
 *
 * @param id a String version of the ObjectId for this item
 * @param binId the associated bin id where this item resides (or empty if standalone item)
 * @param name a name for this item
 * @param description a description of the item
 *
 * @constructor Create a bin object
 */
@Serializable
data class ItemData(
    val id: String,
    var binId: String,
    val name: String,
    val description: String
) {
    //override the default toString() functionality to facilitate table listings
    override fun toString(): String {
        val itemId = formatColumn(id, itemIdColumnWidth)
        val itemBinId = formatColumn(binId, itemIdColumnWidth)
        val itemName = formatColumn(name, itemNameColumnWidth)
        val itemDescription = formatColumn(description, itemDescriptionColumnWidth)
        val rowString = "| $itemId | $itemBinId | $itemName | $itemDescription |"
        return rowString
    }
    //specialized isEmpty() function when all fields are blank
    fun isEmpty(): Boolean {
        return id.isBlank() && binId.isBlank() && name.isBlank() && description.isBlank()
    }
}

/**
 * Item object
 *   Allows creating, changing, deleting, displaying items
 *   Allows transferring item into a bin or from one bin to another
 *
 * @constructor Create an item object
 */
class Item {

    //Create an item and allow placing it to a bin or not
    fun add() {
        println(cyan("    Please enter your Item details:"))

        //Get name
        print("         Name: ")
        val itemName: String = readln()

        //Get description
        print("         Description: ")
        val itemDescription: String = readln()

        //Confirm user decision to place the item in an existing bin
        var itemBinId = ""
        print(cyan("    Would you like to place this item in a bin right now? ('Y' to confirm)"))
        val placeBin = readln()

        //Place item in a bin
        if (placeBin.uppercase() == "Y") {
            val myBin = Bin()
            val binsMap = myBin.getBins()
            val binToUse = myBin.displayBinTable(binsMap, true, "put item in")
            if (!binToUse.isEmpty()) {
                itemBinId = binToUse.id
            }
        }

        val doc = Document("name", itemName).append("description", itemDescription).append("binId", itemBinId)
        Mongo.add("items", doc)

    }

    //Get list of items (either all or for a single bin)
    fun getItems(binIdFilter: String):  Map<Int, ItemData> {
        var itemNum = 0
        val itemsMap = mutableMapOf<Int, ItemData>()
        val itemsList = Mongo.getList("items").find()

        itemsList.forEach { document ->
            val id = document.getObjectId("_id").toHexString()
            val binId = document.getString("binId")
            val name = document.getString("name")
            val description = document.getString("description")
            val itemData = ItemData(id, binId, name, description)

            if (binIdFilter.isEmpty() || (binIdFilter.isNotEmpty() && binId == binIdFilter)) {
                itemNum++
                itemsMap[itemNum] = itemData
            }
        }

        return itemsMap
    }

    //Display a list of all items
    fun displayList() {
        val itemsMap = getItems("")
        displayItemTable(itemsMap, false, "")
    }

    //Displays the actual data table of an item list
    fun displayItemTable(itemMap: Map<Int, ItemData>, allowSelect:Boolean, action:String ): ItemData {
        var selectedItem = ItemData("","","", "")

        if (itemMap.isEmpty()) {
            println("No items to display.")
            return selectedItem
        }

        val headerWidth = itemColumnWidth + itemIdColumnWidth + itemIdColumnWidth + itemNameColumnWidth + itemDescriptionColumnWidth + 16
        val dashes = "-".repeat(headerWidth)

        //Print header
        println(dashes)
        print(formatColumn("| Item", itemColumnWidth + 3))
        print(formatColumn("| ID", itemIdColumnWidth + 3))
        print(formatColumn("| Bin ID", itemIdColumnWidth + 3))
        print(formatColumn("| Name", itemNameColumnWidth + 3))
        print(formatColumn("| Description", itemDescriptionColumnWidth + 3))
        println("|")
        println(dashes)

        //Iterate through item list to print each as a row in the table
        for ((itemIndex, itemInfo) in itemMap) {
            val itemIndexString = itemIndex.toString().padStart(itemColumnWidth)
            println("| $itemIndexString ${itemInfo.toString()}")
        }

        //Print footer if there are any items
        println(dashes)

        //Dialog section
        if (allowSelect) {
            var displayDialog = true
            var itemSelected:String
            while (displayDialog) {
                print("Please select an item to $action or press 'C' to cancel: ")
                itemSelected = readln()

                //Check for Cancel
                if (itemSelected.uppercase() == "C") {
                    return selectedItem
                }

                //Validate entry
                val itemInt = itemSelected.toIntOrNull()
                if (itemInt == null || itemInt !in itemMap.keys) {
                    println(red(bold("That is an invalid selection. Please try again...")))
                } else {
                    val matchedItemData = itemMap[itemInt]
                    if (matchedItemData != null) {
                        selectedItem = matchedItemData
                    }
                    return selectedItem
                }
            }
        }
        return selectedItem
    }

    //Allow user to delete an item
    fun delete() {
        val itemList = getItems("")
        val itemToDelete = displayItemTable(itemList, true, "delete")

        if (itemToDelete.isEmpty()) {
            return
        }

        print("Are you sure you want to delete this item? ('Y' to confirm): ")
        val response = System.`in`.read().toChar().uppercaseChar()
        flushInputBuffer()

        if (response == 'Y') {
            val selectedItemId = itemToDelete.id
            Mongo.delete("items", selectedItemId)
        }

    }

    //Allow user to change details about an item
    fun change() {
        val itemList = getItems("")
        val itemToChange = displayItemTable(itemList, true, "change")
        if (itemToChange.isEmpty()) {
            return
        }

        //Confirm the changing of an item
        print("Are you sure you want to change this item? ('Y' to confirm): ")
        val response = System.`in`.read().toChar().uppercaseChar()
        flushInputBuffer()

        //Interact with the user to make the changes
        if (response == 'Y' && !itemToChange.isEmpty()) {
            val selectedItemId = itemToChange.id
            val selectedBinId = itemToChange.binId

            println(cyan("    Please change your Item details:"))
            println(cyan("         ID: $selectedItemId"))
            println(cyan("         Bin Id: $selectedBinId"))
            println(cyan("         Name (old): ${itemToChange.name}"))
            print("         Name (new): ")
            val newItemName: String = readln()
            println(cyan("         Description (old): ${itemToChange.description}"))
            print("         Description (new): ")
            val newItemDescription: String = readln()

            //Format data for saving and update
            val newItemData = ItemData(selectedItemId, selectedBinId, newItemName, newItemDescription)
            val doc = Document("name", newItemData.name)
                .append("description", newItemData.description)
                .append("binId", newItemData.binId)
            Mongo.update("items", newItemData.id, doc)

        }
    }

    //This function allows the user to transfer an item to a bin
    fun transferToBin() {
        val itemList = getItems("")
        val itemToTransfer = displayItemTable(itemList, true, "transfer")
        if (itemToTransfer.isEmpty()) {
            return
        }

        //Get transfer confirmation
        print("Are you sure you want to transfer this item? ('Y' to confirm): ")
        val response = System.`in`.read().toChar().uppercaseChar()
        flushInputBuffer()

        //If the user confirms the transfer of a valid item then display list of bins and have user select one
        if (response == 'Y' && !itemToTransfer.isEmpty()) {
            val toBin = Bin()
            val binsMap = toBin.getBins()
            val binToUse = toBin.displayBinTable(binsMap, true, "transfer item to")
            if (!binToUse.isEmpty()) {
                itemToTransfer.binId = binToUse.id
                val doc = Document("name", itemToTransfer.name)
                    .append("description", itemToTransfer.description)
                    .append("binId", itemToTransfer.binId)
                Mongo.update("items", itemToTransfer.id, doc)
            }

        }

    }

}