package dev.easysouls.tracetrail.data.weather.repository

import dev.easysouls.tracetrail.data.weather.mapper.toWeatherInfo
import dev.easysouls.tracetrail.data.weather.remote.WeatherApi
import dev.easysouls.tracetrail.domain.util.Resource
import dev.easysouls.tracetrail.domain.weather.model.WeatherInfo
import dev.easysouls.tracetrail.domain.weather.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApi
): WeatherRepository {

    override suspend fun getWeatherData(lat: Double, lng: Double): Resource<WeatherInfo> {
        return try {
            Resource.Success(
                data = api.getWeatherData(
                    lat = lat,
                    lng = lng
                ).toWeatherInfo()
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: "An unknown error occurred.")
        }
    }
}