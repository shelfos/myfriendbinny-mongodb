package controller

import utils.*
import model.*

/**
 * Bin Menu to be displayed and handle options
 *
 * @constructor Creates a bin menu object.
 */
class BinMenu {
    private var validInput: Boolean = true

    fun display() {
        var running = true
        while (running) {
            println(green(underline("Bin menu        ")))
            println(blue("1. Display Bin List"))
            println(blue("2. Display Bin Contents"))
            println(blue("3. Add Bin"))
            println(blue("4. Change Bin"))
            println(blue("5. Delete Bin"))
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
                    val newBin = Bin()
                    newBin.displayList()
                }
                '2' -> {
                    val selectedBin = Bin()
                    selectedBin.displayContents()
                }
                '3' -> {
                    val newBin = Bin()
                    newBin.add()
                    clearScreen()
                    newBin.displayList()
                }
                '4' -> {
                    val changeBin = Bin()
                    changeBin.change()
                    clearScreen()
                    changeBin.displayList()
                }
                '5' -> {
                    val deleteBin = Bin()
                    deleteBin.delete()
                    clearScreen()
                    deleteBin.displayList()
                }
                '6' -> running = false
                else -> {
                    validInput=false
                    clearScreen()
                    displayLogo()
                }
            }
        }
    }
}