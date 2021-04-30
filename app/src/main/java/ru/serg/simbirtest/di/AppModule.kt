package ru.serg.simbirtest.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.serg.simbirtest.data.ScheduleRepository
import ru.serg.simbirtest.data.room.AppDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context) =
        AppDatabase.getAppDatabase(context)

    @Provides
    @Singleton
    fun provideScheduleRepository(database: AppDatabase) =
        ScheduleRepository(database.scheduleDao())

}