package data

import com.olegel.sqldelight.article.ArticlesDb
import com.squareup.sqldelight.db.SqlDriver

fun createQueryWrapper(driver: SqlDriver): ArticlesDb {
    return ArticlesDb(
        driver = driver
    )
}

object Schema : SqlDriver.Schema by ArticlesDb.Schema {
    override fun create(driver: SqlDriver) {
        ArticlesDb.Schema.create(driver)

    }
}
