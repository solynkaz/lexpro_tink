package com.example.lexpro_mobile.interceptors

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.activity.ComponentActivity
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException


class GetCookieInterceptor(val context: Context) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse: Response = chain.proceed(chain.request())
        val pref: SharedPreferences = context.getSharedPreferences(
            "prefs",
            ComponentActivity.MODE_PRIVATE
        )
        if (originalResponse.headers()
                .values("Location")[0].equals("http://188.72.76.241:8050/lexpro/")
        ) {
            val cookie = originalResponse.headers().values("Set-Cookie")[0].split(";")[0]
            val expTime = originalResponse.headers()
                .values("Set-Cookie")[0].split(";")[2].filterNot { it.isWhitespace() }
            pref.edit().putString("id", cookie).putString("expTime", expTime).putInt("code", 302)
                .apply()
        } else {
            pref.edit().putString("id", "None").putString("expTime", "None").putInt("code", 200)
                .apply()
        }
        Log.d("cookies", "\n${pref.getString("id","NOVALUE")}\n${pref.getString("expTime","NOVALUE")}\n${pref.getInt("code",404)}")
        return originalResponse
    }
}