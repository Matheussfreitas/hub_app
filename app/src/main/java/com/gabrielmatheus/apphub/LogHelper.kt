package com.gabrielmatheus.apphub

import android.util.Log

object LogHelper {
    private const val TAG = "AppHub"

    fun v(message: String) {
        Log.v(TAG, message)
    }

    fun d(message: String) {
        Log.d(TAG, message)
    }

    fun i(message: String) {
        Log.i(TAG, message)
    }

    fun w(message: String) {
        Log.w(TAG, message)
    }

    fun e(message: String, error: Throwable? = null) {
        Log.e(TAG, message, error)
    }
}