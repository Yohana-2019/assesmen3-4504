package org.d3if3118.hitungbmr.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [BmrEntity::class], version = 1, exportSchema = false)
abstract class BmrDb : RoomDatabase() {

    abstract val dao: BmrDao

    companion object {

        @Volatile
        private var INSTANCE: BmrDb? = null

        fun getInstance(context: Context): BmrDb {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {

                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        BmrDb::class.java,
                        "bmr.db"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}