package com.example.utils

import com.example.utils.rx.SchedulersProvider
import io.reactivex.Single
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.HttpURLConnection.HTTP_OK
import java.net.URL
import javax.inject.Inject

class HttpClientImpl
    @Inject constructor(
        val logger: Logger,
        val schedulersProvider: SchedulersProvider
    ) : HttpClient {
    override fun executeGet(urlStr: String): Single<String> {
        return Single.fromCallable {
            var responseStr = ""
            val url = URL(urlStr)
            logger.i(TAG, "Sending request $url")

            with(url.openConnection() as HttpURLConnection) {
                requestMethod = "GET"

                val inputStreamForRead =
                        if (responseCode == HTTP_OK) {
                            inputStream
                        } else {
                            errorStream
                        }

                try {
                    BufferedReader(InputStreamReader(inputStreamForRead)).use {
                        val response = StringBuffer()

                        var inputLine = it.readLine()
                        while (inputLine != null) {
                            response.append(inputLine)
                            inputLine = it.readLine()
                        }
                        it.close()
                        logger.i(TAG, "Response: $response")
                        responseStr = response.toString()
                    }
                } catch (e : InterruptedException) {
                    logger.w(TAG, "sendSearchRestaurantsRequest() interrupted")
                }
                catch (e: Exception) {
                    logger.w(TAG, "sendSearchRestaurantsRequest()", e)
                }

            }

            responseStr
        }
        .subscribeOn(schedulersProvider.io())
    }

    private companion object {
        val TAG: String = "HttpClient"
    }

}