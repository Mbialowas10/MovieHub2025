package com.mbialowas.moviehub2025.mvvm



import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope


import com.google.android.gms.maps.model.LatLng


import com.mbialowas.moviehub2025.api.model.Theater
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

import kotlinx.coroutines.launch



import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient

import org.json.JSONObject

import okhttp3.Request




class MapViewModel : ViewModel() {
    private val _theaters = MutableStateFlow<List<Theater>>(emptyList())
    val theaters: StateFlow<List<Theater>> get() = _theaters

    private val client = OkHttpClient()

    fun fetchNearbyTheaters(location: LatLng, apiKey: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json" +
                    "?location=${location.latitude},${location.longitude}" +
                    "&radius=10000" +  // 10 km radius
                    "&type=movie_theater" +
                    "&key=$apiKey"
            Log.i("URL", url)
            val request = Request.Builder().url(url).build()

            try {
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()
                if (response.isSuccessful && responseBody != null) {
                    val theatersList = parseTheaterResponse(responseBody)
                    _theaters.value = theatersList
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun parseTheaterResponse(response: String): List<Theater> {
        val theaters = mutableListOf<Theater>()
        val jsonObject = JSONObject(response)
        val resultsArray = jsonObject.getJSONArray("results")
        for (i in 0 until resultsArray.length()) {
            val place = resultsArray.getJSONObject(i)
            val name = place.optString("name", "Unnamed Theater")
            val address = place.optString("vicinity", "Unknown Address")
            val location = place.getJSONObject("geometry").getJSONObject("location")
            val latitude = location.getDouble("lat")
            val longitude = location.getDouble("lng")

            theaters.add(Theater(name, latitude, longitude, address))
        }
        return theaters
    }
}