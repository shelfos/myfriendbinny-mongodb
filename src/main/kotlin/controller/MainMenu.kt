
package controller

import utils.*
import controller.ItemMenu.*
import controller.BinMenu.*

/**
 * Main Menu to be displayed and handle options
 *
 * @constructor Creates a main menu object.
 */
class MainMenu {
    var validInput: Boolean = true

    fun display() {
        var running = true
        while (running) {
            //Display menu options
            println(green(underline("Main menu        ")))
            println(blue("1. Bin menu"))
            println(blue("2. Item menu"))
            println(magenta("3. Quit"))

            //If invalid choice was selected on prior loop then display error
            if(!validInput) {
                println(red(bold("Invalid choice, please try again.")))
                validInput = true
            }

            //Let user select a menu option
            print("Select an option (1-3): ")
            val key = System.`in`.read().toChar()
            flushInputBuffer()

            //Handle selected menu option
            when (key) {
                '1' -> {
                    val binMenu = BinMenu()
                    clearScreen()
                    displayLogo()
                    binMenu.display()
                }
                '2' -> {
                    val itemMenu = ItemMenu()
                    clearScreen()
                    displayLogo()
                    itemMenu.display()
                }
                '3' -> running = false
                else -> {
                    validInput = false
                }
            }
            clearScreen()
            displayLogo()
        }
        clearScreen()
    }
}