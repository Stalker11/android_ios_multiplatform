import com.olegel.sqldelight.article.ArticlesDb
import com.squareup.sqldelight.db.SqlDriver
import data.createQueryWrapper

actual object Db {
    private var driverRef: SqlDriver? = null
    private var dbRef: ArticlesDb? = null

    val ready: Boolean
        get() = driverRef != null

    fun dbSetup(driver: SqlDriver) {
        val db = createQueryWrapper(driver)
        driverRef = driver
        dbRef = db
    }

    fun dbClear() {
        driverRef!!.close()
        dbRef = null
        driverRef = null
    }

    val instance: ArticlesDb
        get() = dbRef!!
}