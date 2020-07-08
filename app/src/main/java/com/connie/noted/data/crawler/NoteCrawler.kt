package com.connie.noted.data.crawler

import com.connie.noted.data.Note

interface NoteCrawler {

    suspend fun getGoogleLocation(url: String): Note

    suspend fun getMediumArticle(url: String): Note

    suspend fun getYoutube(url: String): Note

}