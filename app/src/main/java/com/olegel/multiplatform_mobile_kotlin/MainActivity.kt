package com.olegel.multiplatform_mobile_kotlin

import TestKotlinMP
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.olegel.multiplatform_mobile_kotlin.databinding.ActivityMainBinding
import com.olegel.multiplatform_mobile_kotlin.utils.AppLogger
import com.olegel.multiplatform_mobile_kotlin.utils.getInstance
import data.Response
import kotlinx.coroutines.*
import viewmodel.*

class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    private val mLogger = AppLogger(MainActivity::class.java)
    //private val mLoader:ArticlesLoader = ArticlesLoader()
    private lateinit var articledViewModel: ArticleViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val db = Db.getInstance(this)
        initViewModels()
        db.articlesQueries.insertPlayers(12,"Hello", "World")
        mLogger.d("${db.articlesQueries.selectAll().executeAsList()[0].article_name}")
        /*launch {
            mLoader.loadFirst(
                successCallback = {
                    runOnUiThread { binding.text.text = it[0].dateGmt }
                }, errorCallback = {
                    runOnUiThread {
                        binding.text.text = it.message
                        mLogger.d(it.message!!)
                    }
                })
        }*/
    }

    override fun onDestroy() {
        cancel()
       // mLoader.cancel()
        super.onDestroy()
    }
    private fun initViewModels(){
        articledViewModel = ViewModelProvider(this).get(ArticleViewModel::class.java)
        articledViewModel.getGitHubRepoList(1.toString())
        observeViewModel()
    }
    private fun observeViewModel() {
        articledViewModel.mGetGitHubRepoListLiveData.addObserver { getGitHubListState(it)}
    }
    fun getGitHubListState(state: LoadingGetArticlesList) {
        when (state) {
            is SuccessGetArticlesListState -> {
                val response =  state.response as Response.RequestSuccess
            }

            is LoadingGetArticlesListState -> {
            }

            is ErrorGetArticlesListState -> {
                val response =  state.response as Response.Error
            }
        }
    }
}

class RRR : TestKotlinMP() {
    override fun hello(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}