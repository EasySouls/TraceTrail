package dev.easysouls.tracetrail.data.remote.weather

import com.squareup.moshi.Json

data class WeatherDto (
    @field:Json(name = "hourly")
    val weatherData: WeatherDataDto
)