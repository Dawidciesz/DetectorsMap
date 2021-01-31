package com.fiesta.detector.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.fiesta.detector.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Poi::class], version = 1)
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
                dao.insert(Poi("name1", "descriptopn1", 54.17101743412942, 19.471446511326583))
                dao.insert(Poi("name2", "descriptopn2", 54.03236756587204, 19.33108815051521))
                dao.insert(Poi("name3", "descriptopn3", 53.985000064417505, 19.87172117427078))
                dao.insert(Poi("name4", "descriptopn4", 53.440464997338296, 17.229029943180063))
                dao.insert(Poi("name5", "descriptopn5", 51.47430870070944, 21.085863280370916))
                dao.insert(Poi("name6", "descriptopn6", 51.5700101917381, 16.713304557050854))
            }
        }
    }
}