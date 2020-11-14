const val K_ASCII = 'K'.toInt()
const val A_ASCII = 'A'.toInt()

class PlayFair(key: String) {
    private val digram = Array(5) { CharArray(5) }
    init {
        var k = 0
        val alphaMap = BooleanArray(26)
        alphaMap['J'.toInt() - A_ASCII] = true // J = I
        val chars = key.toUpperCase() + ('A'..'Z').joinToString("")
        var i = 0
        var j = 0
        for (k in chars.indices){
            if(chars[k] !in 'A' .. 'Z') continue
            val n = chars[k].toInt() - 65
            if (!alphaMap[n]){
                digram[i][j] = chars[k]
                alphaMap[n] = true
                if (++j == 5){
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
        var parsedText = ""

        var prevChar = '\u0000'  // assuming no null char in the plaintext
        var nextChar: Char
        for (i in text.indices) {
            nextChar = text[i]
            if (nextChar.isDigit())
                nextChar = (nextChar.toString().toInt() + K_ASCII).toChar()

            if (nextChar == 'J') nextChar = 'I'
            if (nextChar != prevChar)
                parsedText += nextChar
            else
                parsedText += "X$nextChar"
            prevChar = nextChar
        }
        // odd length case
        val l = parsedText.length
        if (l % 2 == 1) {
            parsedText += if (parsedText[l - 1] != 'X') 'X' else 'Z'
        }
        return parsedText
    }

    private fun indicesOf(c: Char): Pair<Int, Int> {
        for (i in 0..4)
            for (j in 0..4)
                if (digram[i][j] == c) return Pair(i, j)

        return Pair(-1, -1)
    }

    fun encrypt(plaintext: String): String {
        val parsedTest = parseText(plaintext)
        var cipherText = ""
        if (parsedTest.length % 2 != 0) {
            print("$parsedTest has odd length!")
            return ""
        }
        for (i in parsedTest.indices step 2) {
            val (row1, col1) = indicesOf(parsedTest[i])
            val (row2, col2) = indicesOf(parsedTest[i + 1])
            cipherText += when {
                row1 == row2 -> digram[row1][(col1 + 1) % 5].toString() + digram[row2][(col2 + 1) % 5]
                col1 == col2 -> digram[(row1 + 1) % 5][col1].toString() + digram[(row2 + 1) % 5][col2]
                else -> digram[row1][col2].toString() + digram[row2][col1]
            }
        }
        return cipherText
    }

    private fun parseCipherText(decryptedText: String): String {
        var parsedDecryptedText = ""
        for (c in decryptedText) {
            if (c != 'X' && c != 'Z') {
                val d = c.toInt()
                if (d >= 'K'.toInt() && d <= 'T'.toInt())
                    parsedDecryptedText += (d - K_ASCII).toString()[0]
                else
                    parsedDecryptedText += c
            }
        }
        return parsedDecryptedText
    }

    fun decrypt(cipherText: String): String {
        var decryptedText = ""
        for (i in cipherText.indices step 2) {
            val (row1, col1) = indicesOf(cipherText[i])
            val (row2, col2) = indicesOf(cipherText[i + 1])
            decryptedText += when {
                row1 == row2 -> digram[row1][if (col1 > 0) col1 - 1 else 4].toString() + digram[row2][if (col2 > 0) col2 - 1 else 4]
                col1 == col2 -> digram[if (row1 > 0) row1 - 1 else 4][col1].toString() + digram[if (row2 > 0) row2 - 1 else 4][col2]
                else -> digram[row1][col2].toString() + digram[row2][col1]
            }
        }
        return parseCipherText(decryptedText)
    }
}
