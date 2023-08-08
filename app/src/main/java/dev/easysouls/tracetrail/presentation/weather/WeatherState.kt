package dev.easysouls.tracetrail.presentation.weather

import dev.easysouls.tracetrail.domain.weather.model.WeatherInfo

data class WeatherState(
    val weatherInfo: WeatherInfo? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
