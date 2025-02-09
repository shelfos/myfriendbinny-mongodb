# Overview

MyFriendBinny is a program for tracking storage bin inventory and the items stored in those bins.  This is the second version and is meant to be run in a console (just like the first), but the difference is that this version uses a Cloud database - specifically MongoDB.  The GUI is the same as in the first version (very basic console), but it includes a login screen so that the user can authenticate and connect to the MongoDB database.  Later versions will include a web application and mobile app, and will allow you to label your bins with a QR code, as well as attach images of all the objects in your bins.

[MyFriendBinny Project Video (new version with database)](https://youtu.be/L2SdNkciU9I)

# Cloud Database
- The database I chose for this project is MongoDB.  This is a NO SQL database that makes use of documents.
- The structure is simple: myfriendbinny is the name of the database and there are two tables: bins and items
- The two tables each have a default _id (ObjectId), a name, and a description.  The items table also contains a binId that forms a relationship with the bins table.

# Development Environment

- Windows
- IDE: IntelliJ IDEA with Gradle.  Because of several dependencies, I used Gradle for building the project.
- Kotlin language (and some Java libraries) - a few notable ones listed here
  - mongodb
  - litote, specifically for the deleteOne (by ID)
  - bson (think JSON, but binary)
- Powershell as my console for running the program

# Useful Websites

- [What is Kotlin (programming language)](https://en.wikipedia.org/wiki/Kotlin_(programming_language))
- [Kotlin](https://kotlinlang.org/)
- [Kotlin Documentation](https://kotlinlang.org/docs/home.html)
- [Learn Kotlin Programming](https://www.programiz.com/kotlin-programming)
- [Kotlin Comparison to Java](https://kotlinlang.org/docs/comparison-to-java.html)
- [Kotlin Serialization](https://github.com/Kotlin/kotlinx.serialization)
- [NoSQL Tutorial](https://www.guru99.com/nosql-tutorial.html)
- [W3Schools Kotlin Tutorial](https://www.w3schools.com/KOTLIN/index.php)
- [IntelliJ IDEA Reference Card](https://resources.jetbrains.com/storage/products/intellij-idea/docs/IntelliJIDEA_ReferenceCard.pdf)
- [MongoDB](https://www.mongodb.com/)
- [W3Schools: MongoDB Tutorial](https://www.w3schools.com/mongodb/)
- [MongoDB University](https://learn.mongodb.com/dashboard)
- [What is a Cloud Database?](https://www.mongodb.com/resources/basics/databases/cloud-databases)
- [Getting Started with MongoDB Atlas](https://learn.mongodb.com/courses/getting-started-with-mongodb-atlas)
- [MongoDB Atlas Login](https://account.mongodb.com/account/login)
- [MongoDB Shell](https://www.mongodb.com/products/tools/shell)
- [Install mongosh](https://www.mongodb.com/docs/mongodb-shell/install/)

# Future Work

- Convert to a mobile app (start with Android)
- Use relational database to track inventory
- Include use of QR codes and item images