package com.example.utils

interface Logger {
    fun i(tag: String, message: String)
    fun i(tag: String, message: String, throwable: Throwable)

    fun w(tag: String, message: String)
    fun w(tag: String, message: String, throwable: Throwable)

    fun e(tag: String, message: String)
    fun e(tag: String, message: String, throwable: Throwable)
}