package controller

import model.*
import utils.*

/**
 * Item Menu to be displayed and handle options
 *
 * @constructor Creates an item menu object.
 */
class ItemMenu {
    private var validInput: Boolean = true

    fun display() {
        var running = true
        while (running) {
            println(green(underline("Item menu        ")))
            println(blue("1. Display All Items"))
            println(blue("2. Add Item"))
            println(blue("3. Transfer Item to Bin"))
            println(blue("4. Change Item"))
            println(blue("5. Delete Item"))
            println(magenta("6. Return to Main Menu"))
            if(!validInput) {
                println(red(bold("Invalid choice, please try again.")))
                validInput = true
            }
            print("Select an option (1-6): ")

            val key = System.`in`.read().toChar()
            flushInputBuffer()

            when (key) {
                '1' -> {
                    clearScreen()
                    val anItem = Item()
                    anItem.displayList()
                }
                '2' -> {
                    val newItem = Item()
                    newItem.add()
                    clearScreen()
                    newItem.displayList()
                }
                '3' -> {
                    val selectedItem = Item()
                    selectedItem.transferToBin()
                    clearScreen()
                    selectedItem.displayList()
                }
                '4' -> {
                    val changeItem = Item()
                    changeItem.change()
                    clearScreen()
                    changeItem.displayList()
                }
                '5' -> {
                    val deleteItem = Item()
                    deleteItem.delete()
                    clearScreen()
                    deleteItem.displayList()
                }
                '6' -> running = false
                else -> println(red(bold("Invalid choice, please try again.")))
            }
        }
    }
}