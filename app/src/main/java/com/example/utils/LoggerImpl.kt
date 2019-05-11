package com.example.utils

import android.util.Log
import javax.inject.Inject

class LoggerImpl
    @Inject
    constructor(): Logger {
    override fun i(tag: String, message: String) {
        Log.i(tag, message)
    }

    override fun i(tag: String, message: String, throwable: Throwable) {
        Log.i(tag, message, throwable)
    }

    override fun w(tag: String, message: String) {
        Log.w(tag, message)
    }

    override fun w(tag: String, message: String, throwable: Throwable) {
        Log.w(tag, message, throwable)
    }

    override fun e(tag: String, message: String) {
        Log.e(tag, message)
    }

    override fun e(tag: String, message: String, throwable: Throwable) {
        Log.e(tag, message, throwable)
    }

}