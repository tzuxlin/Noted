package com.connie.noted.data.source.local

import android.content.Context
import com.connie.noted.data.Note
import com.connie.noted.data.source.NotedDataSource

class NotedLocalDataSource(val context: Context): NotedDataSource {


    override suspend fun getNews(url: String): Note {
        TODO("Not yet implemented")
    }
}