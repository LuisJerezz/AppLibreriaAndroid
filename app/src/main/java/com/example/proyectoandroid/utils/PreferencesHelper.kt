package com.example.proyectoandroid.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.proyectoandroid.models.Usuario
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object PreferencesHelper {
    private const val PREF_NAME = "app_preferences"
    private const val KEY_USUARIOS = "usuarios"
    private const val KEY_IS_LOGGED_IN = "IS_LOGGED_IN"
    private const val KEY_USER_ID = "USER_ID"
    private const val KEY_USER_NAME = "USER_NAME"
    private const val KEY_USER_EMAIL = "USER_EMAIL"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveUsuarios(context: Context, usuarios: List<Usuario>) {
        val gson = Gson()
        val json = gson.toJson(usuarios)
        getPreferences(context).edit().putString(KEY_USUARIOS, json).apply()
    }

    fun getUsuarios(context: Context): MutableList<Usuario> {
        val gson = Gson()
        val json = getPreferences(context).getString(KEY_USUARIOS, null)
        return if (json != null) {
            val type = object : TypeToken<MutableList<Usuario>>() {}.type
            gson.fromJson(json, type)
        } else {
            mutableListOf()
        }
    }

    fun isFirstRun(context: Context): Boolean {
        val prefs = getPreferences(context)
        val isFirstRun = prefs.getBoolean("is_first_run", true)
        if (isFirstRun) {
            prefs.edit().putBoolean("is_first_run", false).apply()
        }
        return isFirstRun
    }

    // Guarda los datos del usuario cuando se loguea
    fun saveLoginData(context: Context, userId: Int, userName: String, userEmail: String) {
        val prefs = getPreferences(context)
        val editor = prefs.edit()
        editor.putBoolean(KEY_IS_LOGGED_IN, true)
        editor.putInt(KEY_USER_ID, userId)
        editor.putString(KEY_USER_NAME, userName)
        editor.putString(KEY_USER_EMAIL, userEmail)
        editor.apply()
    }

    // Recupera el estado de login
    fun isLoggedIn(context: Context): Boolean {
        return getPreferences(context).getBoolean(KEY_IS_LOGGED_IN, false)
    }

    // Recupera los datos del usuario
    fun getLoggedInUser(context: Context): Pair<String, String>? {
        val prefs = getPreferences(context)
        val userName = prefs.getString(KEY_USER_NAME, null)
        val userEmail = prefs.getString(KEY_USER_EMAIL, null)
        return if (userName != null && userEmail != null) {
            Pair(userName, userEmail)
        } else {
            null
        }
    }

    // Borra solo los datos de login
    fun clearSession(context: Context) {
        val prefs = getPreferences(context)
        val editor = prefs.edit()
        editor.putBoolean(KEY_IS_LOGGED_IN, false) // Marca al usuario como no logueado
        editor.remove(KEY_USER_ID)  // Elimina el ID de usuario
        editor.remove(KEY_USER_NAME) // Elimina el nombre de usuario
        editor.remove(KEY_USER_EMAIL) // Elimina el correo del usuario
        editor.apply()
    }
}
