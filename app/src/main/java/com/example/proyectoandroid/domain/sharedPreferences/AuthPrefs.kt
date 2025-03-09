package com.example.proyectoandroid.domain.sharedPreferences

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class AuthPrefs(context: Context) {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val prefs: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        "auth_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveCredentials(email: String, password: String) {
        prefs.edit().apply {
            putString("email", email)
            putString("password", password)
            apply()
        }
    }

    fun getEmail(): String? = prefs.getString("email", null)

    fun getPassword(): String? = prefs.getString("password", null)

    fun clearCredentials() {
        prefs.edit().clear().apply()
    }
}