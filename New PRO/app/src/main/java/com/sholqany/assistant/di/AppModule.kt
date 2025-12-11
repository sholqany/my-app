package com.sholqany.assistant.di

import android.app.Application
import androidx.room.Room
import com.sholqany.assistant.data.local.AppDatabase
import com.sholqany.assistant.data.local.dao.FinanceDao
import com.sholqany.assistant.data.local.dao.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            "assistant_db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideTaskDao(db: AppDatabase): TaskDao {
        return db.taskDao()
    }

    @Provides
    @Singleton
    fun provideFinanceDao(db: AppDatabase): FinanceDao {
        return db.financeDao()
    }

    @Provides
    @Singleton
    fun provideWakeWordDetector(): com.sholqany.assistant.feature.voice.WakeWordDetector {
        return com.sholqany.assistant.feature.voice.MockWakeWordDetector()
    }
}
