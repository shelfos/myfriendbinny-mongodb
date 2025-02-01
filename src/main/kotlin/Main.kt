package org.example
//test test test
import utils.*
import controller.MainMenu
import kotlin.io.path.Path
import kotlin.io.path.createDirectories

fun main() {
    //Initialize myFriendBinny directories
    val binsPath = Path("C:/myFriendBinny/bins")
    binsPath.createDirectories()

    val itemsPath = Path("C:/myFriendBinny/items")
    itemsPath.createDirectories()

    //Clear screen and display logo
    clearScreen()
    displayLogo()

    //Display Main Menu
    val mainMenu = MainMenu()
    mainMenu.display()
}