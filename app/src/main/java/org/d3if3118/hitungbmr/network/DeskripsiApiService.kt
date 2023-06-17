package org.d3if3118.hitungbmr.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.d3if3118.hitungbmr.model.Lainnya
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
//import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://raw.githubusercontent.com/" + "yhnag/static-api/main/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface DeskripsiApiService {
    @GET("static-api.json")
    suspend fun getDeskripsi(): List<Lainnya>
}

object DeskripsiApi {
    val service: DeskripsiApiService by lazy {
        retrofit.create(DeskripsiApiService::class.java)
    }

    fun getLainnyaUrl(nama: String): String {
        return "$BASE_URL$nama.jpg"
    }
}

enum class ApiStatus { LOADING, SUCCESS, FAILED }