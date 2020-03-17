package com.olegel.multiplatform_mobile_kotlin

import TestKotlinMP
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.olegel.multiplatform_mobile_kotlin.utils.AppLogger
import com.olegel.multiplatform_mobile_kotlin.utils.getInstance
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import network.ArticlesLoader

class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    private val mLogger = AppLogger(MainActivity::class.java)
    private val mLoader:ArticlesLoader = ArticlesLoader()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val db = Db.getInstance(this)
        db.articlesQueries.insertPlayers(12,"Hello", "World")
        mLogger.d("${db.articlesQueries.selectAll().executeAsList()[0].article_name}")
        launch {
            mLoader.loadFirst(
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
        mLoader.cancel()
        super.onDestroy()
    }
}

class RRR : TestKotlinMP() {
    override fun hello(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}