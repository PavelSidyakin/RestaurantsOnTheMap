package com.example.utils

import io.reactivex.Single

interface HttpClient {
    /**
     * Executes GET request
     *
     * @param urlStr requested url
     *
     * @return Single with response string. Emits empty string on error
     *
     * error: no
     * scheduler: io
     */
    fun executeGet(urlStr: String): Single<String>
}