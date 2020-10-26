package utils

import kotlin.math.pow

class MatrixOperations {
    fun transpose(mat: Array<IntArray>): Array<IntArray> {
        val transMat = Array(mat[0].size) { IntArray(mat.size) { 0 } }
        for (i in mat[0].indices) {
            for (j in mat.indices)
                transMat[i][j] = mat[j][i]
        }
        return transMat
    }

    fun determinant(mat: Array<IntArray>, size: Int): Int {
        var res: Int
        if (size == 1)
            res = mat[0][0]
        else if (size == 2)
            res = mat[0][0] * mat[1][1] - mat[1][0] * mat[0][1]
        else {
            res = 0
            for (j1 in 0 until size) {
                val temp = Array(size - 1) { IntArray(size - 1) }
                for (i in 1 until size) {
                    var j2 = 0
                    for (j in 0 until size) {
                        if (j == j1) continue
                        temp[i - 1][j2] = mat[i][j]
                        j2++
                    }
                }
                res += ((-1.0).pow(1.0 + j1 + 1.0) * mat[0][j1]
                        * determinant(temp, size - 1)).toInt()
            }
        }
        return res
    }

    fun keyToMatrix(key: String, len: Int) {
        val keyMatrix = Array(len) { IntArray(len) }
        var c = 0
        for (i in 0 until len) {
            for (j in 0 until len) {
                keyMatrix[i][j] = key[c].toInt() - 97
                c++
            }
        }
    }

}

fun main() {
    TODO()
}