package uc.team.localmessage.service

import android.content.Context
import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import uc.team.localmessage.managers.TokenManager

class AuthInterceptor(context: Context) : Interceptor {
    private val sessionManager = TokenManager(context)

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        sessionManager.getToken()?.let {
            Log.d("Auth", it)
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }

        return chain.proceed(requestBuilder.build())
    }
}