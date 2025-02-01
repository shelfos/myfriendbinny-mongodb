package org.example
//test update
import utils.*
import controller.MainMenu
import kotlin.io.path.Path
import kotlin.io.path.createDirectories
import com.mongodb.client.MongoClients
import org.litote.kmongo.*
import io.github.cdimascio.dotenv.dotenv

import org.slf4j.Logger
import org.slf4j.LoggerFactory

val dotenv = dotenv()
var mongoUri = dotenv["MONGODB_URI"]

fun main() {

    val logger: Logger = LoggerFactory.getLogger("Main")
    logger.info("Application started")



    // Set up MongoDB client
    var db_username=""
    var db_password=""
    mongoUri = mongoUri.replace("<db_username>",db_username).replace("<db_password>",db_password)
    //println(mongoUri)
    val client = MongoClients.create(mongoUri)
    //val database = client.getDatabase("myfriendbinny")



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