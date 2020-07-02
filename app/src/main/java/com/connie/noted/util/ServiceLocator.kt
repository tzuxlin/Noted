package com.connie.noted.util

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.connie.noted.data.source.DefaultNotedRepository
import com.connie.noted.data.source.NotedDataSource
import com.connie.noted.data.source.NotedRepository
import com.connie.noted.data.source.local.NotedLocalDataSource
import com.connie.noted.data.source.remote.NotedRemoteDataSource

/**
 * A Service Locator for the [StylishRepository].
 */
object ServiceLocator {

    @Volatile
    var notedRepository: NotedRepository? = null
        @VisibleForTesting set

    fun provideTasksRepository(context: Context): NotedRepository {
        synchronized(this) {
            return notedRepository
                ?: notedRepository
                ?: createStylishRepository(context)
        }
    }

    private fun createStylishRepository(context: Context): NotedRepository {
        return DefaultNotedRepository(NotedRemoteDataSource,
            createLocalDataSource(context)
        )
    }

    private fun createLocalDataSource(context: Context): NotedDataSource {
        return NotedLocalDataSource(context)
    }
}