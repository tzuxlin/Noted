package com.connie.noted.data.source.remote

import android.util.Log
import com.connie.noted.data.Note
import com.connie.noted.data.source.NotedDataSource
import org.jsoup.Jsoup
import kotlin.coroutines.suspendCoroutine

object NotedRemoteDataSource : NotedDataSource {

    override suspend fun getNews(url: String): Note = suspendCoroutine { continuation ->
        Jsoup.connect("https://www.popdaily.com.tw/pet/712154")
            .get()
            .getElementById("content")
            .getElementsByTag("a")
            .forEach {
                val href = it.attr("href")
                if (href.contains("gif") || href.contains("jpg") || href.contains("png")) {
                    println(href)
                }
            }


    }
}
