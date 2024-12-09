
package com.example.proyectoandroid.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.example.proyectoandroid.models.Usuario
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object PreferencesHelper {
    private const val PREF_NAME = "app_preferences"
    private const val KEY_USUARIOS = "usuarios"

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

    fun clearSession(context: Context) {
        val sharedPreferences = context.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("IS_LOGGED_IN", false) // Marca al usuario como no loggeado
        editor.apply()
    }



}
