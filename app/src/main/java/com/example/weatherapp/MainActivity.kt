package com.example.weatherapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var temperatureText: TextView
    private lateinit var descriptionText: TextView
    private val apiKey = "7f32246536c77950b713bbdb6c5f2d74"  // Replace with your actual API key


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        temperatureText = findViewById(R.id.temperatureText)
        descriptionText = findViewById(R.id.statusText)

        getWeatherData("London")
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
    }

    @SuppressLint("SetTextI18n")
    private fun getWeatherData(city: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = RetrofitInstance.api.getCurrentWeather(city, apiKey)
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    val weatherResponse = response.body()
                    weatherResponse?.let {
                        temperatureText.text = "${it.main.temp}°C"
                        descriptionText.text = it.weather[0].description
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    temperatureText.text = "Error fetching data"
                }
            }
        }
    }
}