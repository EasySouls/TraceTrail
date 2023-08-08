package dev.easysouls.tracetrail.data.weather.mapper

import dev.easysouls.tracetrail.data.weather.remote.WeatherDataDto
import dev.easysouls.tracetrail.data.weather.remote.WeatherDto
import dev.easysouls.tracetrail.domain.weather.model.WeatherData
import dev.easysouls.tracetrail.domain.weather.model.WeatherInfo
import dev.easysouls.tracetrail.domain.weather.model.WeatherType
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


private data class IndexedWeatherData(
    val index: Int,
    val data: WeatherData
)
fun WeatherDataDto.toWeatherDataMap(): Map<Int, List<WeatherData>> {
    return time.mapIndexed { index, time ->
        val temperature = temperatures[index]
        val weatherCode = weatherCodes[index]
        val windSpeed = windSpeeds[index]
        val pressure = pressures[index]
        val humidity = humidities[index]
        IndexedWeatherData(
            index = index,
            data = WeatherData(
                time = LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME),
                temperatureCelsius = temperature,
                pressure = pressure,
                windSpeed = windSpeed,
                humidity = humidity,
                weatherType = WeatherType.fromWMO(weatherCode)
            )
        )
    }.groupBy {
        it.index / 24
    }.mapValues { it ->
        it.value.map { it.data }
    }.also { println("Keys: ${it.keys}") }
}

fun WeatherDto.toWeatherInfo(): WeatherInfo {
    val weatherDataMap = weatherData.toWeatherDataMap()
    val now = LocalDateTime.now()
    val currentWeatherData = weatherDataMap[0]?.find {
        val hour = if (now.minute < 30) now.hour else now.hour + 1
        it.time.hour == hour
    }
    return WeatherInfo(
        weatherDataPerDay = weatherDataMap,
        currentWeatherData = currentWeatherData
    )
}