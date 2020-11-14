import java.io.File

const val K_ASCII = 'K'.toInt()
const val A_ASCII = 'A'.toInt()

class PlayFair(key: String) {
    private val digram = Array(5) { CharArray(5) }
//    private val charMap: Map<Int, Char> = (0..9).toList().associateWith { (it + K_ASCII).toChar() }
//    private val reverseCharMap = charMap.entries.associate { (k, v) -> v to k }

    init {
        var k = 0
        val alphaMap = BooleanArray(26)
        alphaMap['J'.toInt() - A_ASCII] = true // J = I
        val chars = key.toUpperCase() + ('A'..'Z').joinToString("")
        var i = 0
        var j = 0
        for (k in chars.indices) {
            if (chars[k] !in 'A'..'Z') continue
            val n = chars[k].toInt() - 65
            if (!alphaMap[n]) {
                digram[i][j] = chars[k]
                alphaMap[n] = true
                if (++j == 5) {
                    if (++i == 5) break
                    j = 0
                }
            }
        }
    }

    fun printDigram() = digram.forEach {
        it.forEach { ch -> print("$ch ") }
        print("\n")
    }

    private fun parseText(plaintext: String): String {
        val text = plaintext.toUpperCase()
        val parsedText = StringBuilder()

        var prevChar = '\u0000'  // assuming no null char in the plaintext
        var nextChar: Char
        for (i in text.indices) {
            nextChar = text[i]
            if (nextChar.isDigit())
                nextChar = (nextChar.toString().toInt() + K_ASCII).toChar()

            if (nextChar == 'J') nextChar = 'I'
            if (nextChar != prevChar)
                parsedText.append(nextChar)
            else
                parsedText.append("X$nextChar")
            prevChar = nextChar
        }
        // odd length case
        val l = parsedText.length
        if (l % 2 == 1) {
            parsedText.append(if (parsedText[l - 1] != 'X') 'X' else 'Z')
        }
        return parsedText.toString()
    }

    private fun indicesOf(c: Char): Pair<Int, Int> {
        for (i in 0..4)
            for (j in 0..4)
                if (digram[i][j] == c) return Pair(i, j)

        return Pair(-1, -1)
    }

    fun encrypt(plaintext: String): String {
        val parsedTest = parseText(plaintext)
        val cipherText = StringBuilder()
        if (parsedTest.length % 2 != 0) {
            print("$parsedTest has odd length!")
            return ""
        }
        for (i in parsedTest.indices step 2) {
            val (row1, col1) = indicesOf(parsedTest[i])
            val (row2, col2) = indicesOf(parsedTest[i + 1])
            cipherText.append(when {
                row1 == row2 -> digram[row1][(col1 + 1) % 5].toString() + digram[row2][(col2 + 1) % 5]
                col1 == col2 -> digram[(row1 + 1) % 5][col1].toString() + digram[(row2 + 1) % 5][col2]
                else -> digram[row1][col2].toString() + digram[row2][col1]
            })
        }
        return cipherText.toString()
    }

    private fun parseDecryptedText(decryptedText: StringBuilder): String {
        val parsedDecryptedText = StringBuilder()
        for (c in decryptedText) {
            if (c != 'X' && c != 'Z') {
                val d = c.toInt()
                if (d >= 'K'.toInt() && d <= 'T'.toInt())
                    parsedDecryptedText.append((d - K_ASCII).toString()[0])
                else
                    parsedDecryptedText.append(c)
            }
        }
        return parsedDecryptedText.toString()
    }

    fun decrypt(cipherText: String): String {
        val decryptedText = StringBuilder()
        for (i in cipherText.indices step 2) {
            val (row1, col1) = indicesOf(cipherText[i])
            val (row2, col2) = indicesOf(cipherText[i + 1])
            decryptedText.append(when {
                row1 == row2 -> digram[row1][if (col1 > 0) col1 - 1 else 4].toString() + digram[row2][if (col2 > 0) col2 - 1 else 4]
                col1 == col2 -> digram[if (row1 > 0) row1 - 1 else 4][col1].toString() + digram[if (row2 > 0) row2 - 1 else 4][col2]
                else -> digram[row1][col2].toString() + digram[row2][col1]
            })
        }
        return parseDecryptedText(decryptedText)
    }
}

fun audioEncrypt(playFair: PlayFair, file: File) {
    if (file.extension != "wav") {
        println("Only wav files are supported!")
        return
    }
    println("Encryption started!")

    // reading the file contents and encrypt
    val bytes = file.readBytes()
    val data = bytes.joinToString("") { String.format("%02x", it.toInt() and 0xFF) }
    val encrypted = playFair.encrypt(data)

    println("Encrypted!")

    // write to file
    val outFile = File(file.name + ".pfcrp")
    outFile.writeText(encrypted)
    println("Encryption complete! \nFile saved as " + outFile.name)
}

fun audioDecrypt(playFair: PlayFair, file: File) {
    if (file.extension != "pfcrp") {
        println("Only pfcrp files are supported!")
        return
    }

    // read lines and decrypt
    val lines = file.readLines().joinToString("")
    val decrypted = playFair.decrypt(lines)

    // convert hex string to bytes format: "%02X"
    val bytes = decrypted.chunked(2).map { it.toUpperCase().toInt(16).toByte() }.toByteArray()
    val outFile = File(file.nameWithoutExtension)
    outFile.writeBytes(bytes)

}

fun main() {
    val pf = PlayFair("MONARCHY")
    pf.printDigram()
//    val audioFile = File("./src/examples/sample.wav")
    val encrypedFile = File("./sample.wav.pfcrp")
//    println(pf.encrypt("AAAABBBCCCDDDDD29847467118BDBBFFEEFFFFFFFF"))

//    audioEncrypt(pf, audioFile)
    audioDecrypt(pf, encrypedFile)
}
