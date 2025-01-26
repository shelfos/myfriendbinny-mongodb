
package utils
fun bold(text: String):String {
    return "\u001B[1m$text\u001B[0m"
}
fun underline(text: String):String {
    return "\u001B[4m$text\u001B[0m"
}
fun red(text: String):String {
    return "\u001b[31m$text\u001b[0m"
}
fun green(text: String):String {
    return "\u001b[32m$text\u001b[0m"
}
fun yellow(text: String):String {
    return "\u001b[33m$text\u001b[0m"
}
fun blue(text: String):String {
    return "\u001b[34m$text\u001b[0m"
}
fun magenta(text: String):String {
    return "\u001b[35m$text\u001b[0m"
}
fun cyan(text: String):String {
    return "\u001b[36m$text\u001b[0m"
}
fun flushInputBuffer() {
    while (System.`in`.available() > 0) {
        System.`in`.read() // Read and discard
    }
}
fun clearScreen() {
    print("\u001b[H\u001b[2J")
    System.out.flush()
}
fun highlightAsciiArt(asciiArt: String): String {
    return "\u001b[30;47m$asciiArt\u001b[0m" // Black text on a white background
}
fun displayLogo() {
    println(cyan("___  ___     ______    _                _______ _"))
    println(cyan("|  \\/  |     |  ___|  (_)              | | ___ (_)"))
    println(cyan("| .  . |_   _| |_ _ __ _  ___ _ __   __| | |_/ /_ _ __  _ __  _   _"))
    println(cyan("| |\\/| | | | |  _| '__| |/ _ \\ '_ \\ / _` | ___ \\ | '_ \\| '_ \\| | | |"))
    println(cyan("| |  | | |_| | | | |  | |  __/ | | | (_| | |_/ / | | | | | | | |_| |"))
    println(cyan("\\_|  |_/\\__, \\_| |_|  |_|\\___|_| |_|\\__,_\\____/|_|_| |_|_| |_|\\__, |"))
    print(cyan("         __/ |"))
    print(blue("               (Ascii art: https://patorjk.com)"))
    println(cyan("  __/ |"))
    println(cyan("        |___/                                                 |___/"))
    println(" ")
}
fun formatColumn(columnData: String, columnWidth: Int): String {
    return if (columnData.length > columnWidth) columnData.substring(0, columnWidth - 3) + "..." else columnData.padEnd(columnWidth)
}