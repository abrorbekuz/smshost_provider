package uc.team.localmessage.managers

import android.content.Context
import android.content.SharedPreferences


class TokenManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_NAME = "access_token_storage"
        private const val KEY_ACCESS_TOKEN = "access_token"
    }

    fun saveToken(accessToken: String?) {
        val editor = prefs.edit()
        editor.putString(KEY_ACCESS_TOKEN, accessToken)
        editor.apply()
    }

    fun getToken(): String? {
        return prefs.getString(KEY_ACCESS_TOKEN, null)
    }

    fun resetToken() {
        saveToken(null)
    }
}