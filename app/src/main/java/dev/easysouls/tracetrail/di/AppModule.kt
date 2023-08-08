package dev.easysouls.tracetrail.di

import android.app.Application
import androidx.room.Room
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.easysouls.tracetrail.data.local.MissingPersonDatabase
import dev.easysouls.tracetrail.data.local.MissingPersonRepositoryImpl
import dev.easysouls.tracetrail.data.weather.remote.WeatherApi
import dev.easysouls.tracetrail.domain.missing_person.repository.MissingPersonRepository
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideMissingPersonDatabase(app: Application): MissingPersonDatabase {
        return Room.databaseBuilder(
            app,
            MissingPersonDatabase::class.java,
            "missing_persons.db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideMissingPersonRepository(db: MissingPersonDatabase): MissingPersonRepository {
        return MissingPersonRepositoryImpl(db.dao)
    }

    @Singleton
    @Provides
    fun provideWeatherApi(): WeatherApi {
        return Retrofit.Builder()
            .baseUrl("https://api.open-meteo.com/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create()
    }

    @Singleton
    @Provides
    fun provideFusedLocationProviderClient(app: Application): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(app)
    }
}