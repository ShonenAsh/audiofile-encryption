package infosec

import java.io.File
import kotlin.random.Random
import kotlin.system.exitProcess

// static constant
const val asciiChar = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"

class MonoAlphabetic(key: Int) {
    private val charMap = asciiChar.toList().zip(asciiChar.toList().shuffled(Random(key))).toMap()

    fun inv() = charMap.entries.associate { (k, v) -> v to k }

    fun encrypt(message: String, customCharMap: Map<Char, Char> = charMap) =
        message.map { customCharMap[it] }.joinToString("")

    fun decrypt(message: String) = encrypt(message, inv())
}

fun audioEncrypt(mono: MonoAlphabetic, file: File) {
    if (file.extension != "wav") {
        println("Only wav files are supported!")
        return
    }

    // reading the file contents and encrypt
    val bytes = file.readBytes()
    val data = bytes.joinToString("") { String.format("%02x", it.toInt() and 0xFF) }
    val encrypted = mono.encrypt(data)

    // write to file
    val outFile = File(file.name + ".monocrp")
    outFile.writeText(encrypted)
    println("Encryption complete! \nFile saved as " + outFile.name)
}

fun audioDecrypt(mono: MonoAlphabetic, file: File) {
    if (file.extension != "monocrp") {
        println("Only monocrp files are supported!")
        return
    }

    // read lines and decrypt
    val lines = file.readLines().joinToString("")
    val decrypted = mono.decrypt(lines)

    // convert hex string to bytes format: "%02X"
    val bytes = decrypted.chunked(2).map { it.toUpperCase().toInt(16).toByte() }.toByteArray()
    val outFile = File(file.nameWithoutExtension)
    outFile.writeBytes(bytes)

}

fun main(args: Array<String>) {
    if (args.size != 3) {
        println("Incorrect number of arguments passed!")
        exitProcess(-1)
    }

    val funcType = args[0]
    val file = File(args[1])
    val seed = args[2].toIntOrNull()

    if (seed == null) {
        println("Incorrect seed!")
        exitProcess(-1)
    }

    if (!file.exists()) {
        println("File doesn't exist!")
        exitProcess(-1)
    }

    val mono = MonoAlphabetic(seed)
    when (funcType) {
        "encrypt" -> audioEncrypt(mono, file)
        "decrypt" -> audioDecrypt(mono, file)
        else -> {
            println("Invalid operation! Please refer to the readme file")
            readLine()
        }
    }
}
