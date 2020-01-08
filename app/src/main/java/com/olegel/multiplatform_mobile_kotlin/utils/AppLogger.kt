package com.olegel.multiplatform_mobile_kotlin.utils

import android.util.Log

class AppLogger (val classType: Class<*>){
    fun d(message:String) = Log.d(classType.simpleName, "$message")
    fun i(message:String) = Log.i(classType.simpleName, "$message")
    fun e(message:String) = Log.e(classType.simpleName, "$message")
}