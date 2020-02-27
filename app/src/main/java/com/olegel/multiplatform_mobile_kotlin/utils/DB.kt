package com.olegel.multiplatform_mobile_kotlin.utils

import android.content.Context
import com.olegel.sqldelight.article.ArticlesDb
import com.squareup.sqldelight.android.AndroidSqliteDriver
import data.Schema
import database.Db

fun Db.getInstance(context: Context): ArticlesDb {
    if (!Db.ready) {
        Db.dbSetup(AndroidSqliteDriver(Schema, context, "sampledb"))
    }
    return Db.instance
}