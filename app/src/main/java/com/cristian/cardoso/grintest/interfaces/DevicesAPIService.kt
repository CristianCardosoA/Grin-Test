package com.cristian.cardoso.grintest.interfaces

import com.cristian.cardoso.grintest.models.Device
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST

//Move this to gradle to use it with multiple environments
const val BASE_URL = "https://grin-bluetooth-api.herokuapp.com/"

interface DevicesAPIService {

    @GET("devices/")
    fun getAllBTDevices() : Deferred<List<Device>>

    @POST("add/")
    fun saveBTDevice(device : Device) : Deferred<Device>

    companion object {
        fun create() : DevicesAPIService {
            val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(CoroutineCallAdapterFactory())
                    .build()
            return retrofit.create(DevicesAPIService::class.java)
        }
    }
}
