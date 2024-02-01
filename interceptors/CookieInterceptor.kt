package com.example.lexpro_mobile.interceptors

import android.content.Context
import android.content.SharedPreferences
import androidx.activity.ComponentActivity
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class CookieInterceptor(context: Context) : Interceptor {
    val context: Context = context
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()
        val pref: SharedPreferences = context.getSharedPreferences(
            "prefs",
            ComponentActivity.MODE_PRIVATE
        )
        builder.addHeader("Cookie", pref.getString("id", "NOVALUE"))
        builder.addHeader("Content-Type", "application/json; charset=utf-8")
        return chain.proceed(builder.build())
    }
}