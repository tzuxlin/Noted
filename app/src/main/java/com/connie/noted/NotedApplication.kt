package com.connie.noted

import android.app.Application
import com.connie.noted.data.crawler.NoteCrawler
import com.connie.noted.data.source.NotedRepository
import com.connie.noted.util.ServiceLocator
import kotlin.properties.Delegates

/**
 * Created by Wayne Chen in Jul. 2019.
 *
 * An application that lazily provides a repository. Note that this Service Locator pattern is
 * used to simplify the sample. Consider a Dependency Injection framework.
 */
class NotedApplication : Application() {

    // Depends on the flavor,
    val notedRepository: NotedRepository
        get() = ServiceLocator.provideTasksRepository(this)


    companion object {
        var instance: NotedApplication by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}
