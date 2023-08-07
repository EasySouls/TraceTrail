package dev.easysouls.tracetrail.domain.weather.repository

import dev.easysouls.tracetrail.domain.util.Resource
import dev.easysouls.tracetrail.domain.weather.model.WeatherInfo

interface WeatherRepository {
    suspend fun getWeatherData(lat: Double, lng: Double): Resource<WeatherInfo>
}