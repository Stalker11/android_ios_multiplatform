package com.olegel.multiplatform_mobile_kotlin

import GitHubApiClient
import TestKotlinMP
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.ImplicitReflectionSerializer

@ImplicitReflectionSerializer
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        GitHubApiClient().repos(
            successCallback = {
                GlobalScope.launch(Dispatchers.Main) {
                    text.text = it[0]
                }
            }, errorCallback = {
                GlobalScope.launch(Dispatchers.Main) {
                    text.text = it.message
                }
            })
    }
    }
class RRR: TestKotlinMP()