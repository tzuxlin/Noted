package com.connie.noted.data.crawler

import com.connie.noted.data.Note
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class NoteCrawlerClass : NoteCrawler {
    override suspend fun getGoogleLocation(url: String): Note  = suspendCoroutine { continuation ->

        val note = Note()
        val doc = Jsoup.connect(url).get()

        note.contentSource = doc.title()

        println(doc.body())
        val metaTags: Elements = doc.getElementsByTag("meta")

        for (metaTag in metaTags) {

            val content = metaTag.attr("content")

            when (metaTag.attr("itemprop")) {
                "name" -> note.title = content
                "image" -> note.images.add(content)
                "description" -> note.summary = content
            }

        }

        note.type = "Location"
        note.url = url

        continuation.resume(note)

    }


    override suspend fun getMediumArticle(url: String): Note  = suspendCoroutine { continuation ->

        val note = Note()
        val doc = Jsoup.connect(url).get()

        val metaTags: Elements = doc.getElementsByTag("meta")

        for (metaTag in metaTags) {

            val content = metaTag.attr("content")

            if (metaTag.attr("property") == "og:site_name") {
                note.contentSource = content
            }

            if (metaTag.attr("property") == "og:title") {
                note.title = content
            }

            if (metaTag.attr("name") == "description") {
                note.summary = content
            }

            if (metaTag.attr("name").contains("image")) {
                note.images.add(content)
            }
            note.type = "Article"
            note.url = url



        }

        continuation.resume(note)

    }

    override suspend fun getYoutube(url: String): Note = suspendCoroutine { continuation ->

        val note = Note()
        val doc = Jsoup.connect(url).get()

        note.contentSource = doc.title()

        val metaTags: Elements = doc.getElementsByTag("meta")

        for (metaTag in metaTags) {

            val content = metaTag.attr("content")


            if (metaTag.attr("itemprop") == "name") {
                note.title = content
            }

            if (metaTag.attr("name") == "description") {
                note.summary = content
            }


        }

        val links = doc.getElementsByTag("link")
        for (link in links){
            if (link.attr("itemprop") == "thumbnailUrl"){
                note.images.add(link.attr("href"))
            }
        }

        note.type = "Video"
        note.url = url


        continuation.resume(note)

    }


}