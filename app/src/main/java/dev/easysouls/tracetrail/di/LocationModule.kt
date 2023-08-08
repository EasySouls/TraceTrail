package dev.easysouls.tracetrail.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.easysouls.tracetrail.data.location.DefaultLocationTracker
import dev.easysouls.tracetrail.domain.location.LocationTracker
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocationModule {

    @Binds
    @Singleton
    abstract fun bindLocationTracker(
        defaultLocationTracker: DefaultLocationTracker
    ): LocationTracker
}