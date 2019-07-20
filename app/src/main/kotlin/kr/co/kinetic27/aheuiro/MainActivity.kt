package kr.co.kinetic27.aheuiro

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : BaseActivity() {

    override var viewId: Int = R.layout.activity_main
    override var toolbarId: Int? = R.id.toolbar

    override fun onCreate() {
        AheuiConverter.loadData(this)

        change_mode.setOnClickListener {
            first_text.text = when(first_text.text) {
                "아희로" -> "ㅁㄴㅇㄹ"
                else -> "아희로"
            }
        }

        copy_btn.setOnClickListener {
            (getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).primaryClip = ClipData.newPlainText("아희로", result_keyboard.text)
            Toast.makeText(this, "클립보드에 복사되었습니다!", Toast.LENGTH_SHORT).show()
        }

        hint_keyboard.textChanged {
            result_keyboard.text = when {
                it.isNotEmpty() -> AheuiConverter.toAheui(it)
                else -> "..."
            }
        }
    }


  private fun EditText.textChanged(onTextChanged: (String) -> Unit) =
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) =
                        onTextChanged.invoke(s.toString())

                override fun afterTextChanged(editable: Editable?) {}
            })
}

