package com.olegel.multiplatform_mobile_kotlin

import GitHubApiClient
import TestKotlinMP
import TestSerializer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.ImplicitReflectionSerializer

@ImplicitReflectionSerializer
class MainActivity : AppCompatActivity() {
    private val content = "[{ " +
            "   \"id\":100," +
            "   \"data\":\"27.10.2017\"" +
            "}]"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
      /*  val aa = TestSerializer(content).getResult()
        text.text = aa.first().data*/
        GitHubApiClient("","").repos(
            successCallback = {
                GlobalScope.launch(Dispatchers.Main) {
                    text.text = it[0].data
                }
            }, errorCallback = {
                GlobalScope.launch(Dispatchers.Main) {
                    text.text = it.message
                }
            })
    }
    }
class RRR: TestKotlinMP() {
    override fun hello(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}