package com.example.weatherapp

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {

    private lateinit var temperatureText: TextView
    private lateinit var descriptionText: TextView
    private lateinit var locationText: TextView
    private lateinit var minmaxTempText: TextView
    private lateinit var sunriseText: TextView
    private lateinit var sunsetText: TextView
    private lateinit var windText: TextView
    private lateinit var pressureText: TextView
    private lateinit var humidity: TextView
    private lateinit var pollutionText: TextView
    private lateinit var updatedText: TextView
    private val apiKey = "7f32246536c77950b713bbdb6c5f2d74"  // Replace with your actual API key


    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        temperatureText = findViewById(R.id.temperatureText)
        descriptionText = findViewById(R.id.statusText)
        locationText = findViewById(R.id.locationText)
        minmaxTempText = findViewById(R.id.minMaxTempText)
        sunriseText = findViewById(R.id.sunriseText)
        sunsetText = findViewById(R.id.sunsetText)
        windText = findViewById(R.id.wind)
        pressureText = findViewById(R.id.pressure)
        humidity = findViewById(R.id.humidity)
        pollutionText = findViewById(R.id.pollution)
        updatedText = findViewById(R.id.updatedAtText)



        getWeatherData("Brownsville")
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
        locationText.text = "Palo Alto"
    }

    @RequiresApi(Build.VERSION_CODES.O)
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
                        minmaxTempText.text = "${it.main.tempMin}°C - ${it.main.tempMax}°C"
                        sunriseText.text = " Sunrise\n ${convertUnixTimestamp(it.sys.sunrise)}"
                        sunsetText.text = " Sunset\n ${convertUnixTimestamp(it.sys.sunset)}"
                        windText.text = "Wind\n S: ${it.wind.speed} kph\n D: ${it.wind.deg}°"
                        pressureText.text = "Pressure\n ${it.main.pressure} psi"
                        humidity.text = "Humidity\n ${it.main.humidity} %"
                        pollutionText.text = "Cloud Cover\n ${it.clouds.all}"
                        locationText.text = "${it.name}, ${it.sys.country}"
                        updatedText.text = "Updated at: ${convertUnixTimestamp(it.dt)}"
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    temperatureText.text = "Error fetching data"
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun convertUnixTimestamp(unixTimestamp: Long): String {
        // Convert the Unix timestamp to an Instant
        val instant = Instant.ofEpochSecond(unixTimestamp)

        // Format the Instant to a readable date and time
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
            .withZone(ZoneId.systemDefault()) // Use the system's default time zone
        return formatter.format(instant)
    }
}