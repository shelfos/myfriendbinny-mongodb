package model

import java.util.*
import java.io.File
import utils.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import model.Item

const val binsPath = "C:/myFriendBinny/bins/"
const val binColumnWidth = 4
const val binIdColumnWidth = 36
const val binNameColumnWidth = 30
const val binDescriptionColumnWidth = 50

/**
 * BinData object
 *
 * @param id a random UUID assigned to this bin
 * @param name a name for this bin
 * @param description a description of the bin
 *
 * @constructor Create a bin object
 */
@Serializable
data class BinData(
    val id: String,
    val name: String,
    val description: String
) {
    //override the default toString() functionality to facilitate table listings
    override fun toString(): String {
        val binId = formatColumn(id, binIdColumnWidth)
        val binName = formatColumn(name, binNameColumnWidth)
        val binDescription = formatColumn(description, binDescriptionColumnWidth)
        val rowString = "| $binId | $binName | $binDescription |"
        return rowString
    }
    //specialized isEmpty() function when all fields are blank
    fun isEmpty(): Boolean {
        return id.isBlank() && name.isBlank() && description.isBlank()
    }
}

/**
 * Bin object
 *   Allows creating, changing, deleting, displaying bins
 *
 * @constructor Create a bin object
 */
class Bin {

    //Create a bin
    fun add() {
        println(cyan("    Please enter your Bin details:"))

        //Generate id
        val binId: String = UUID.randomUUID().toString()
        println("         ID (auto-assigned): $binId")

        //Get name
        print("         Name: ")
        val binName: String = readln()

        //Get description
        print("         Description: ")
        val binDescription: String = readln()

        //Format data for saving and save to a file
        val binData = BinData(binId, binName, binDescription)
        val jsonString = Json.encodeToString(binData)
        save(binId, jsonString)

    }

    //Save bin
    private fun save(fileName: String, fileContent: String) {
        val file = File("$binsPath/$fileName")
        file.writeText(fileContent)
    }

    //Get list of all bins
    fun getBins():  Map<Int, BinData> {
        var binNum = 0
        val binsMap = mutableMapOf<Int, BinData>()
        File(binsPath).walk().forEach { file ->
            if (!file.isDirectory) {
                binNum ++
                val fileContent = file.readText()
                val binDataFromFile = Json.decodeFromString<BinData>(fileContent)
                binsMap[binNum] = binDataFromFile
            }
        }

        return binsMap
    }

    //Display a list of all bins
    fun displayList() {
        val binsMap = getBins()
        displayBinTable(binsMap, false, "")
    }

    //Displays the contents (items) in a specific bin
    fun displayContents() {
        val binsMap = getBins()
        val binToSelect = displayBinTable(binsMap, true, "see its contents")
        if (binToSelect.isEmpty()) {
            return
        }

        val theItem = Item()
        val itemBinId: String = binToSelect.id
        val itemsMap = Item().getItems(itemBinId)
        theItem.displayItemTable(itemsMap, false, "")
    }

    //Displays the actual data table of a bin list
    fun displayBinTable(binMap: Map<Int, BinData>, allowSelect:Boolean, action:String ): BinData {
        var selectedBin: BinData = BinData("","","")

        if (binMap.isEmpty()) {
            println("No bins to display.")
            return selectedBin
        }

        val headerWidth = binColumnWidth + binIdColumnWidth + binNameColumnWidth + binDescriptionColumnWidth + 13
        val dashes = "-".repeat(headerWidth)

        //Print header
        println(dashes)
        print(formatColumn("| Bin", binColumnWidth + 3))
        print(formatColumn("| ID", binIdColumnWidth + 3))
        print(formatColumn("| Name", binNameColumnWidth + 3))
        print(formatColumn("| Description", binDescriptionColumnWidth + 3))
        println("|")
        println(dashes)

        //Iterate through bin list to print each as a row in the table
        for ((binIndex, binInfo) in binMap) {
            val binIndexString = binIndex.toString().padStart(binColumnWidth)
            println("| $binIndexString ${binInfo.toString()}")
        }

        //Print footer if there are any bins
        println(dashes)

        //Dialog section
        if (allowSelect) {
            var displayDialog = true
            var binSelected:String
            while (displayDialog) {
                print("Please select a bin to $action or press 'C' to cancel: ")
                binSelected = readln()

                //Check for Cancel
                if (binSelected.uppercase() == "C") {
                    displayDialog = false
                    return selectedBin
                }

                //Validate entry
                val binInt = binSelected.toIntOrNull()
                if (binInt == null || binInt !in binMap.keys) {
                    println(red(bold("That is an invalid selection. Please try again...")))
                } else {
                    val matchedBinData = binMap[binInt]
                    if (matchedBinData != null) {
                        selectedBin = matchedBinData
                    }
                    return selectedBin
                }
            }
        }
        return selectedBin
    }

    //Allow user to delete a bin
    fun delete() {
        val binList = getBins()
        val binToDelete = displayBinTable(binList, true, "delete")

        if (binToDelete.isEmpty()) {
            return
        }

        print("Are you sure you want to delete this bin? ('Y' to confirm): ")
        val response = System.`in`.read().toChar().uppercaseChar()
        flushInputBuffer()

        if (response == 'Y') {
            val selectedBinId = binToDelete.id
            File("$binsPath$selectedBinId").delete()
        }
    }

    //Allow user to change details of a bin
    fun change() {
        val binList = getBins()
        val binToChange = displayBinTable(binList, true, "change")
        if (binToChange.isEmpty()) {
            return
        }

        //Confirm changing of the bin
        print("Are you sure you want to change this bin? ('Y' to confirm): ")
        val response = System.`in`.read().toChar().uppercaseChar()
        flushInputBuffer()

        //Interact with the user to get actual bin changes
        if (response == 'Y' && !binToChange.isEmpty()) {
            val selectedBinId = binToChange.id

            println(cyan("    Please change your Bin details:"))
            println(cyan("         ID: $selectedBinId"))
            println(cyan("         Name (old): ${binToChange.name}"))
            print("         Name (new): ")
            val newBinName: String = readln()
            println(cyan("         Description (old): ${binToChange.description}"))
            print("         Description (new): ")
            val newBinDescription: String = readln()

            //Format data for saving and save to a file
            val newBinData = BinData(selectedBinId, newBinName, newBinDescription)
            val jsonString = Json.encodeToString(newBinData)
            save(selectedBinId, jsonString)
        }
    }


}