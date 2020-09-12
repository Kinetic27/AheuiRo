package kr.co.kinetic27.aheuiro

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.google.android.material.snackbar.Snackbar
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.webkit.WebChromeClient
import android.widget.EditText
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kr.co.kinetic27.aheuiroconverter.AheuiConverter


class MainActivity : BaseActivity() {
    private lateinit var mInterstitialAd: InterstitialAd

    override var viewId: Int = R.layout.activity_main
    override var toolbarId: Int? = R.id.toolbar

    @SuppressLint("SetJavaScriptEnabled", "MissingPermission")
    override fun onCreate() {

        MobileAds.initialize(this) {}

        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = "ca-app-pub-7380710508478373/3330847526"
        mInterstitialAd.loadAd(AdRequest.Builder().build())


        AheuiConverter().loadData(this)

        with(webView) {
            with(settings) {
                javaScriptEnabled = true
                javaScriptCanOpenWindowsAutomatically = true

                //useWideViewPort = true
                loadWithOverviewMode = true

                setAppCacheEnabled(true)
                setSupportZoom(true)
                builtInZoomControls = true
                displayZoomControls = false
            }

            webChromeClient = WebChromeClient()
            loadUrl("file:///android_asset/aheui.html")
        }

        change_mode.setOnClickListener {
            first_text.text = when(first_text.text) {
                "아희로" -> {
                    aheuiRo.visibility = View.GONE
                    anheuiRo.visibility = View.VISIBLE

                    "않희로"
                }
                else -> {
                    aheuiRo.visibility = View.VISIBLE
                    anheuiRo.visibility = View.GONE

                    "아희로"
                }
            }
        }

        copy_btn.setOnClickListener {
            (getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip( ClipData.newPlainText("아희로", result_keyboard.text))

            Snackbar.make(main_layout, "클립보드에 복사되었습니다!", Snackbar.LENGTH_SHORT).apply {
                setAction("확인") { dismiss() }
                show()
            }

            //  if(Random().nextInt(10) > 4)
            mInterstitialAd.show()
        }

        hint_keyboard.textChanged {
            if (first_text.text == "아희로")
                result_keyboard.text = when {
                    it.isNotEmpty() -> AheuiConverter().toAheui(it)
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

