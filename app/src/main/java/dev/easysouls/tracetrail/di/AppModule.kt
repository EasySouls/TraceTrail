package dev.easysouls.tracetrail.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.easysouls.tracetrail.data.local.MissingPersonDatabase
import dev.easysouls.tracetrail.data.local.MissingPersonRepositoryImpl
import dev.easysouls.tracetrail.domain.missing_person.repository.MissingPersonRepository
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
}