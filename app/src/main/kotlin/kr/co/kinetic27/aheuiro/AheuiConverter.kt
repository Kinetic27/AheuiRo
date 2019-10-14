package kr.co.kinetic27.aheuiro

import android.content.Context
import org.json.JSONObject
import java.util.*

/**
 * Created by Kinetic on 2018-05-10.
 */

object AheuiConverter {
    private var numAheuiText: JSONObject? = null
    private var strAheuiText: JSONObject? = null


    private fun ClosedRange<Int>.random() =
            Random().nextInt(endInclusive - start) + start

    private fun Context.loadJson(str: String) =
            this.assets.open(str).bufferedReader().readText().run {
                JSONObject(this)
            }

    fun loadData(context: Context) = with(context) {
        numAheuiText = loadJson("numAheui.json")
        strAheuiText = loadJson("strAheui.json")
    }

    private fun numToAheui(str: String) = try {
            with(numAheuiText?.getJSONArray(str)) {
                this?.getString((0..this.length()).random()) ?: "err"
            }
    } catch (e: Exception) {
        str
    }

    private fun numAheuiDic(c: Char) = try {
        strAheuiText?.getString(c.toString()) ?: "초기화 오류"
    } catch (e: Exception) {
        with(c.toInt().toString()) {
            "빠".repeat(length - 1) + normalToAhuei(split(""))
        }
    }

    private fun normalToAhuei(asciiStr: List<String>) = when (asciiStr.size) {
        1 -> asciiStr.toString()
        else -> {
            asciiStr[1] + asciiStr.joinToString("+*")
                    .substring(4, (asciiStr.size - 1) * 3 - 2)
                    .replace("0+*", "*")
        }
    }


    fun toAheui(str: String): String {
        var isOverlap = false
        var aheuiText = "25*"

        (0 until str.length).forEach { charN ->

            when (isOverlap) { //중복 체크
                true -> isOverlap = false  //중복 -> 한번 쉼
                else -> {
                    if((str[charN].toString().toIntOrNull() ?: 10) in (0..9)) {
                        aheuiText += str[charN] + "망"
                        return@forEach
                    } else {
                        aheuiText += numAheuiDic(str[charN])
                    }
                }
            }

            if (charN + 1 != str.length && str.length > 1 && str[charN] == str[charN + 1]) {
                // 마지막이 아니거나 다음에 같은 문자가 올때 중복(빠) 처리
                aheuiText += "빠"
                isOverlap = true
            }

            aheuiText += "맣"
        }

        return aheuiText.split("").joinToString(separator = "") { numToAheui(it) } + "마희"
    }
}