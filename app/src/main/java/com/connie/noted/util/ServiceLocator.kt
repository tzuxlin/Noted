package com.connie.noted.util

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.connie.noted.data.source.DefaultNotedRepository
import com.connie.noted.data.source.NotedRepository
import com.connie.noted.data.source.NotedRemoteDataSource

/**
 * A Service Locator for the [NotedRepository].
 */
object ServiceLocator {

    @Volatile
    var notedRepository: NotedRepository? = null
        @VisibleForTesting set

    fun provideTasksRepository(): NotedRepository {
        synchronized(this) {
            return notedRepository
                ?: notedRepository
                ?: createNotedRepository()
        }
    }

    private fun createNotedRepository(): NotedRepository {
        return DefaultNotedRepository(NotedRemoteDataSource)
    }


}