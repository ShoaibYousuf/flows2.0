package com.example.myapplication

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainViewModel : ViewModel() {
    var arrayList: ArrayList<DataClass> = arrayListOf()

    private val api: ApiCall = Retrofit.Builder()
        .baseUrl("https://dummyjson.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiCall::class.java)

    private val _loadingState = MutableStateFlow(false)
    val loadingState: StateFlow<Boolean>
        get() = _loadingState.asStateFlow()

    private val _data = MutableSharedFlow<DataClass>()
    val data: SharedFlow<DataClass>
        get() = _data.asSharedFlow()

     fun fetchData() {
        viewModelScope.launch {
            _loadingState.value = true
            try {
                val response = api.fetchData()
                response.enqueue(object : Callback<DataClass> {
                    override fun onResponse(call: Call<DataClass>, response: Response<DataClass>) {
                        val responseList: DataClass = response.body()!!
                        if (response.isSuccessful) {
                            arrayList.addAll(listOf(responseList))
                            viewModelScope.launch {
                                _data.emit(responseList)
                            }

                        }
                    }

                    override fun onFailure(call: Call<DataClass>, t: Throwable) {
                        Log.d("MainActivity", "Failure" + t.message)
                    }
                })

            } catch (e: Exception) {
                _data.emit(DataClass())
            } finally {
                _loadingState.value = false
            }
        }
    }
}