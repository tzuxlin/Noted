package com.connie.noted.data.source

import com.connie.noted.data.Note

class DefaultNotedRepository(
    private val remoteDataSource: NotedDataSource,
    private val localDataSource: NotedDataSource
) : NotedRepository {

    override suspend fun getNews(url: String): Note {
        return remoteDataSource.getNews(url)
    }
}