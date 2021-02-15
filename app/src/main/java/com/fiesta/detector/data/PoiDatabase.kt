package com.fiesta.detector.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.fiesta.detector.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Poi::class], version = 5)
abstract class PoiDatabase : RoomDatabase() {

    abstract fun poiDao(): PoiDao

    class Callback @Inject constructor(

        private val database: Provider<PoiDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val dao = database.get().poiDao()

            applicationScope.launch {
                dao.insert(Poi("wname1", "descriptopn1", 54.17101743412942, 19.471446511326583, ""))
                dao.insert(Poi("wname2", "descriptopn2", 54.17101743412942, 19.471446511326583, ""))
                dao.insert(Poi("wname3", "descriptopn3", 54.17101743412942, 19.471446511326583, ""))
                dao.insert(Poi("wname4", "descriptopn4", 54.17101743412942, 19.471446511326583, ""))
                dao.insert(Poi("wname5", "descriptopn5", 54.17101743412942, 19.471446511326583, ""))
                dao.insert(Poi("wname6", "descriptopn6", 54.17101743412942, 19.471446511326583, ""))
            }
        }
    }
}