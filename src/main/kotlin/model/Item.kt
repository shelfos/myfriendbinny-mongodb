package model

import java.util.*
import java.io.File
import utils.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import model.Bin

const val itemsPath = "C:/myFriendBinny/items/"
const val itemColumnWidth = 6
const val itemIdColumnWidth = 36
const val itemNameColumnWidth = 30
const val itemDescriptionColumnWidth = 50

/**
 * ItemData object
 *
 * @param id a random UUID assigned to this item
 * @param binId the associated bin UUID where this item resides (or empty if standalone item)
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

        //Generate id
        val itemId: String = UUID.randomUUID().toString()
        println("         ID (auto-assigned): $itemId")

        //Get name
        print("         Name: ")
        val itemName: String = readln()

        //Get description
        print("         Description: ")
        val itemDescription: String = readln()

        //Confirm user decision to place the item in an existing bin
        var itemBinId: String = ""
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

        //Format data for saving and save to a file
        val itemData = ItemData(itemId, itemBinId, itemName, itemDescription)
        val jsonString = Json.encodeToString(itemData)
        save(itemId, jsonString)

    }

    //Save item
    private fun save(fileName: String, fileContent: String) {
        val file = File("$itemsPath/$fileName")
        file.writeText(fileContent)
    }

    //Get list of items (either all or for a single bin)
    fun getItems(binId: String):  Map<Int, ItemData> {
        var itemNum = 0
        val itemsMap = mutableMapOf<Int, ItemData>()
        File(itemsPath).walk().forEach { file ->
            if (!file.isDirectory) {
                val fileContent = file.readText()
                val itemDataFromFile = Json.decodeFromString<ItemData>(fileContent)
                if (binId.isEmpty() || (binId.isNotEmpty() && binId == itemDataFromFile.binId)) {
                    itemNum ++
                    itemsMap[itemNum] = itemDataFromFile
                }
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
        var selectedItem: ItemData = ItemData("","","", "")

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
                    displayDialog = false
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
            File("$itemsPath$selectedItemId").delete()
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

            //Format data for saving and save to a file
            val newItemData = ItemData(selectedItemId, selectedBinId, newItemName, newItemDescription)
            val jsonString = Json.encodeToString(newItemData)
            save(selectedItemId, jsonString)
        }
    }

    //This function allows the user to transfer an item to a bin
    fun transferToBin() {
        val itemList = getItems("")
        var itemToTransfer = displayItemTable(itemList, true, "transfer")
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
                val jsonString = Json.encodeToString(itemToTransfer)
                save(itemToTransfer.id, jsonString)
            }

        }

    }

}