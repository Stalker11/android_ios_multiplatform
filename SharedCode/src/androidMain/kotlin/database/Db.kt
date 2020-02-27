package database

import com.squareup.sqldelight.db.SqlDriver
import data.createQueryWrapper
import com.olegel.sqldelight.article.ArticlesDb

object Db {
    private var driverRef: SqlDriver? = null
    private var dbRef: ArticlesDb? = null

    val ready: Boolean
        get() = driverRef != null

    fun dbSetup(driver: SqlDriver) {
        val db = createQueryWrapper(driver)
        driverRef = driver
        dbRef = db
    }

    internal fun dbClear() {
        driverRef!!.close()
        dbRef = null
        driverRef = null
    }

    val instance: ArticlesDb
        get() = dbRef!!
}