package com.example.proyectoandroid.ui.views.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.proyectoandroid.LoginActivity
import com.example.proyectoandroid.R
import com.example.proyectoandroid.databinding.ActivityMainBinding
import com.example.proyectoandroid.ui.views.fragment.UserListFragment
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar SharedPreferences
        sharedPreferences = getSharedPreferences("login-info", MODE_PRIVATE)

        // Configurar Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)

        // Configurar Navigation Drawer
        drawerLayout = findViewById(R.id.drawerLayout)
        navView = findViewById(R.id.navView)

        // Cargar fragmento inicial
        loadFragment(UserListFragment())

        // Configurar listeners
        setupNavigation()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                logoutUser()
                true
            }
            R.id.action_settings -> {
                // Navegar a ajustes
                true
            }
            R.id.action_help -> {
                // Mostrar ayuda
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun logoutUser() {
        // Limpiar credenciales y token
        sharedPreferences.edit().clear().apply()

        // Redirigir a LoginActivity
        startActivity(Intent(this, LoginActivity::class.java))
        finish() // Cerrar MainActivity para evitar volver atrás
    }

    private fun setupNavigation() {
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_user_list -> loadFragment(UserListFragment())
                R.id.nav_ads_list -> { /* Cargar fragmento de anuncios */ }
                R.id.nav_profile -> { /* Cargar perfil */ }
                R.id.nav_settings -> { /* Cargar ajustes */ }
                R.id.nav_help -> { /* Mostrar ayuda */ }
            }
            drawerLayout.closeDrawers()
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}