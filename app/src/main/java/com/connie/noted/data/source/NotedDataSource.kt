package com.connie.noted.data.source

import com.connie.noted.data.Note

interface NotedDataSource {

    suspend fun getNews(url: String): Note
}