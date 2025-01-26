# Overview

MyFriendBinny is a program for tracking storage bin inventory and the items stored in those bins.  This particular version is meant to be run in a console so the GUI is just basic.  Despite its simplicity (it's not database driven), it accomplishes its purpose.  Later versions will include a web application and mobile app, and will allow you to label your bins with a QR code, as well as attach images of all the objects in your bins.

[MyFriendBinny Kotlin Project](https://youtu.be/L2SdNkciU9I)  (sorry this is longer than 5 minutes)

# Development Environment

- Windows
- IDE: IntelliJ IDEA with Gradle.  Because of some of the features (specifically serialization/JSON), I used Gradle for building the project.
- Kotlin language (and some Java libraries)
- Powershell as my console for running the program

# Programming languages and libraries
- Kotlin: kotlin.io.path.Path, kotlin.io.path.createDirectories, kotlinx.serialization.*, kotlinx.serialization.json.*
- Java: java.util.*, java.io.File

# Useful Websites

- [What is Kotlin (programming language)](https://en.wikipedia.org/wiki/Kotlin_(programming_language))
- [Kotlin](https://kotlinlang.org/)
- [Kotlin Documentation](https://kotlinlang.org/docs/home.html)
- [Learn Kotlin Programming](https://www.programiz.com/kotlin-programming)
- [Kotlin Comparison to Java](https://kotlinlang.org/docs/comparison-to-java.html)
- [Kotlin Serialization](https://github.com/Kotlin/kotlinx.serialization)
- [W3Schools Kotlin Tutorial](https://www.w3schools.com/KOTLIN/index.php)
- [IntelliJ IDEA Reference Card](https://resources.jetbrains.com/storage/products/intellij-idea/docs/IntelliJIDEA_ReferenceCard.pdf)

# Future Work

- Convert to a mobile app (start with Android)
- Use relational database to track inventory
- Include use of QR codes and item images