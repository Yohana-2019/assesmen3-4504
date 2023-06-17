package org.d3if3118.hitungbmr.ui.hitung

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.d3if3118.hitungbmr.db.BmrDao
import org.d3if3118.hitungbmr.db.BmrEntity
import org.d3if3118.hitungbmr.model.HasilBmr
import org.d3if3118.hitungbmr.model.hitungBmr

class HitungViewModel(private val db: BmrDao) : ViewModel() {
    private val hasilBmr = MutableLiveData<HasilBmr?>()

    fun hitungBmr (isMale: Boolean, usia: Double, berat: Double, tinggi: Double, aktivitas: String) {
        val dataBmr = BmrEntity(
            isMale = isMale,
            usia = usia,
            berat = berat,
            tinggi = tinggi,
            aktivitas = aktivitas
        )
        hasilBmr.value = dataBmr.hitungBmr()

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                db.insert(dataBmr)
            }
        }
    }

    fun getHasilBmr(): LiveData<HasilBmr?> = hasilBmr
}