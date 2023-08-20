package dev.easysouls.tracetrail.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.easysouls.tracetrail.data.location.DefaultLocationClient
import dev.easysouls.tracetrail.domain.location.LocationClient
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class LocationModule {

    @Binds
    @Singleton
    abstract fun bindLocationTracker(
        defaultLocationTracker: DefaultLocationClient
    ): LocationClient
}
