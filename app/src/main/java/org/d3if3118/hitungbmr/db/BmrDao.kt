package org.d3if3118.hitungbmr.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BmrDao {
    @Insert
    fun insert(bmr: BmrEntity)

    @Query("SELECT * FROM bmr ORDER BY id DESC")
    fun getLastBmr(): LiveData<List<BmrEntity>>

    @Query("DELETE FROM bmr")
    fun clearData()
}