package org.d3if3118.hitungbmr.ui.lainnya

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3if3118.hitungbmr.MainActivity
import org.d3if3118.hitungbmr.model.Lainnya
import org.d3if3118.hitungbmr.network.ApiStatus
import org.d3if3118.hitungbmr.network.DeskripsiApi
import org.d3if3118.hitungbmr.network.UpdateWorker
import java.util.concurrent.TimeUnit

class LainnyaViewModel : ViewModel() {
    private val data = MutableLiveData<List<Lainnya>>()
    private val status = MutableLiveData<ApiStatus>()

    init {
        retrieveData()
    }

    private fun retrieveData() {
        viewModelScope.launch(Dispatchers.IO) {
            status.postValue(ApiStatus.LOADING)
            try {
                data.postValue(DeskripsiApi.service.getDeskripsi())
                status.postValue(ApiStatus.SUCCESS)
            } catch (e: Exception) {
                Log.d("LainnyaViewModel", "Failure: ${e.message}")
                status.postValue(ApiStatus.FAILED)
            }
        }
    }

    fun scheduleUpdater(app: Application) {
        val request = OneTimeWorkRequestBuilder<UpdateWorker>()
            .setInitialDelay(1, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(app).enqueueUniqueWork(
            MainActivity.CHANNEL_ID,
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

    fun getData(): LiveData<List<Lainnya>> = data

    fun getStatus(): LiveData<ApiStatus> = status
}