
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import data.Schema
import data.createQueryWrapper
import kotlin.native.concurrent.AtomicReference
import com.olegel.sqldelight.article.ArticlesDb
import kotlin.native.concurrent.freeze

actual object Db {
  private val driverRef = AtomicReference<SqlDriver?>(null)
  private val dbRef = AtomicReference<ArticlesDb?>(null)

  internal fun dbSetup(driver: SqlDriver) {
    val db = createQueryWrapper(driver)
    driverRef.value = driver.freeze()
    dbRef.value = db.freeze()
  }

  internal fun dbClear() {
    driverRef.value!!.close()
    dbRef.value = null
    driverRef.value = null
  }

  //Called from Swift
  @Suppress("unused")
  fun defaultDriver() {
    Db.dbSetup(NativeSqliteDriver(Schema, "sampledb"))
  }

  val instance: ArticlesDb
    get() = dbRef.value!!
}