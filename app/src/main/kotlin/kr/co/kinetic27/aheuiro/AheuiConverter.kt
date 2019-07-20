package kr.co.kinetic27.aheuiro

import android.content.Context
import org.json.JSONObject
import java.lang.Exception
import java.util.*

/**
 * Created by Kinetic on 2018-05-10.
 */

object AheuiConverter {
    private var numAheuiText: JSONObject? = null
    private var strAheuiText: JSONObject? = null


    private fun ClosedRange<Int>.random() =
            Random().nextInt(endInclusive - start) + start

    fun loadData(context: Context) {
        with(context.assets) {
            open("numAheui.json").bufferedReader().use {
                it.readText()
            }.run {
                numAheuiText = JSONObject(this)
            }

            open("strAheui.json").bufferedReader().use {
                it.readText()
            }.run {
                strAheuiText = JSONObject(this)
            }
        }
    }

    private fun numToAheui(str: String) =
            with(numAheuiText?.getJSONArray(str)) {
                this?.getString((0..this.length()).random()) ?: "초기화 안됨"
            }

    private fun numAheuiDic(str: String) = try {
        strAheuiText?.getString(str) ?: "초기화 안됨"
    } catch (e: Exception) {
        "err"
    }

    fun toAheui(str: String): String {
        var isOverlap = false
        var aheuiText = "25*"
        var convertedStr: String
        var asciiStr: String

        (0 until str.length).forEach { charN ->
            convertedStr = ""

            when (isOverlap) { //중복 체크
                false -> { // 중복 X
                    asciiStr = str[charN].toInt().toString()

                    aheuiText += when {
                        numAheuiDic(str[charN].toString()) != "err" -> numAheuiDic(str[charN].toString()) //사전 우선 검색

                        else -> {
                            convertedStr += "빠".repeat(asciiStr.length) + asciiStr[0] + "*"

                            (0 until asciiStr.length - 2).forEach {
                                if (asciiStr[it + 1].toString() != "0")
                                    convertedStr += asciiStr[it + 1] + "+"

                                convertedStr += "*"
                            }

                            convertedStr += asciiStr[asciiStr.length - 1] + "+"

                            convertedStr
                        }
                    }
                }
                else -> isOverlap = false //중복 -> 한번 쉼
            }

            if (str.length > 1 && charN + 1 != str.length && str[charN] == str[charN + 1]) {
                // 마지막이 아니거나 다음에 같은 문자가 올때 중복(빠) 처리
                aheuiText += "빠"
                isOverlap = true
            }

            aheuiText += "맣"
        }

        aheuiText += "희"

        return aheuiText.split("").joinToString(separator = "") {
            when {
                it.isNotEmpty() -> numToAheui(it)
                else -> ""
            }
        }
    }
}