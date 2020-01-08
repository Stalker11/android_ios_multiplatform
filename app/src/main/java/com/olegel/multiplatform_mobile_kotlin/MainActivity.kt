package com.olegel.multiplatform_mobile_kotlin

import GitHubApiClient
import TestKotlinMP
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.olegel.multiplatform_mobile_kotlin.utils.AppLogger
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {
    private val content = "[{ " +
            "   \"id\":100," +
            "   \"data\":\"27.10.2017\"" +
            "}]"
    private val mLogger = AppLogger(MainActivity::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /*  val aa = TestSerializer(content).getResult()
          text.text = aa.first().data*/
        launch(Dispatchers.IO) {
            GitHubApiClient("", "").repos(
                successCallback = {
                    runOnUiThread { text.text = it[0].dateGmt }
                }, errorCallback = {
                    runOnUiThread {
                        text.text = it.message
                        mLogger.d(it.message!!)
                    }
                })
        }
    }

    override fun onDestroy() {
        cancel()
        super.onDestroy()
    }
}
class RRR: TestKotlinMP() {
    override fun hello(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}