package org.d3if3118.hitungbmr.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bmr")
data class BmrEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    var tanggal: Long = System.currentTimeMillis(),
    var isMale: Boolean,
    var usia: Double,
    var berat: Double,
    var tinggi: Double,
    var aktivitas: String
)