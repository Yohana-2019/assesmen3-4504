package org.d3if3118.hitungbmr.model

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.d3if3118.hitungbmr.db.BmrEntity

fun BmrEntity.hitungBmr(): HasilBmr {
    if (isMale) {
        val bmr = 66.5 + (13.7 * berat) + (5 * tinggi) - (6.8 * usia)
        val bmrAktivitas = getBmrAktivitas(bmr, aktivitas)
        val beratIdeal = (tinggi - 100) - ((tinggi - 100) * 0.10)

        return HasilBmr(bmr, bmrAktivitas, beratIdeal)
    } else {
        val bmr = 655 + (9.6 * berat) + (1.8 * tinggi) - (4.7 * usia)
        val bmrAktivitas = getBmrAktivitas(bmr, aktivitas)
        val beratIdeal = (tinggi - 100) - ((tinggi - 100) * 0.15)

        return HasilBmr(bmr, bmrAktivitas, beratIdeal)
    }
}

private fun getBmrAktivitas(bmr: Double, aktivitas: String): Double {
    val bmrAktivitas = when (aktivitas) {
        "Hampir tidak pernah berolahraga" -> bmr*1.2
        "Jarang berolahraga" -> bmr*1.3
        else -> bmr*1.4
    }
    return bmrAktivitas
}