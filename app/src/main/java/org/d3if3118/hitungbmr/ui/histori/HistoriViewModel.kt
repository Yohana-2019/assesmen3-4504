package org.d3if3118.hitungbmr.ui.histori

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.d3if3118.hitungbmr.db.BmrDao

class HistoriViewModel(private val db: BmrDao) : ViewModel() {
    val data = db.getLastBmr()

    fun hapusData() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            db.clearData() }
    }
}