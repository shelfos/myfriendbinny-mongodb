package org.example

import utils.*
import controller.MainMenu
import controller.Database
import controller.Mongo
import kotlin.io.path.Path
import kotlin.io.path.createDirectories
import io.github.cdimascio.dotenv.dotenv

import org.slf4j.Logger
import org.slf4j.LoggerFactory

val dotenv = dotenv()

fun main() {

    val logger: Logger = LoggerFactory.getLogger("Main")
    logger.info("Application started")

    //Clear screen and display logo
    clearScreen()
    displayLogo()

    val database = Database()
    if (!database.login()) {
        println("")
        println(red("I'm sorry - you cannot use MyFriendBinny right now."))
        return
    }

    //Display Main Menu
    val mainMenu = MainMenu()
    mainMenu.display()

    Mongo.disconnect()
}