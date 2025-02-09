package controller

import com.mongodb.client.*
import com.mongodb.client.model.*
import org.example.dotenv
import utils.*
import org.bson.Document
import org.bson.types.ObjectId
import org.litote.kmongo.deleteOneById

object Mongo {
    lateinit var db: MongoDatabase
        private set
    private lateinit var client: MongoClient

    fun connect(db_username: String, db_password: String): Boolean {
        return try {
            var mongoUri = dotenv["MONGODB_URI"]
            mongoUri = mongoUri.replace("<db_username>", db_username).replace("<db_password>", db_password)
            val client = MongoClients.create(mongoUri)
            db = client.getDatabase("myfriendbinny")
            val command = Document("ping", 1)
            db.runCommand(command)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun disconnect() {
        try {
            client.close()
        } catch (_: Exception) { }
    }

    fun add(collectionName: String, doc: Document) {
        val tb = db.getCollection(collectionName)
        val addResult = tb.insertOne(doc)
        println(addResult)
    }

    fun getList(collectionName: String): MongoCollection<Document> {
        return db.getCollection(collectionName)
    }

    fun delete(collectionName: String, id: String) {
        val tb = db.getCollection(collectionName)
        tb.deleteOneById(ObjectId(id))
    }

    fun update(collectionName: String, id: String, doc: Document) {
        val tb = db.getCollection(collectionName)
        val filter =  Filters.eq("_id", ObjectId(id))
        tb.replaceOne(filter, doc, ReplaceOptions().upsert(false))
    }

    fun clearItemsFromBin(binId: String) {
        val tb = db.getCollection("items")
        val filter = Filters.eq("binId", binId)
        val update = Updates.set("binId", "")
        tb.updateMany(filter, update)
    }
}

/**
 * Login to be displayed and handled
 *
 * @constructor Creates a DatabaseLogin object.
 */
class Database {

    fun login():Boolean {

        var validInput: Boolean = false
        var tries = 1
        println(cyan("Please enter your database username and password:"))
        while(!validInput && tries < 4) {
            if (tries > 1) {
                println(cyan("The database credentials you entered are invalid.  Please try again:"))
            }

            print(magenta("   Username: "))
            val username: String = readln()

            println(magenta("   Password: "))
            val password: String = dotenv["MONGODB_PWD"] //readln()

            validInput = Mongo.connect(username, password)

            tries ++
        }
        return validInput
    }

}