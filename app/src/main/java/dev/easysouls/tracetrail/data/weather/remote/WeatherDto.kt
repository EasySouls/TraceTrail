package dev.easysouls.tracetrail.data.weather.remote

import com.squareup.moshi.Json
import dev.easysouls.tracetrail.data.weather.remote.WeatherDataDto

data class WeatherDto (
    @field:Json(name = "hourly")
    val weatherData: WeatherDataDto
)