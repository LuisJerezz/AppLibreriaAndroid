package com.example.proyectoandroid

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.proyectoandroid.databinding.ActivityMainDrawerBinding
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainDrawerBinding
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflar el layout que contiene el DrawerLayout y el Toolbar
        binding = ActivityMainDrawerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar el Toolbar como ActionBar
        setSupportActionBar(binding.toolbar)

        // Configurar Navigation Drawer
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView

        // Configurar NavController desde el NavHostFragment
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Configurar el Drawer con NavController
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        NavigationUI.setupWithNavController(navView, navController)

        // Escuchar la selección de los items del menú del NavigationView
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_ads_list -> {
                    // Navegar al fragmento de anuncios
                    navController.navigate(R.id.nav_ads_list)
                }
                R.id.nav_user_list -> {
                    // Navegar al fragmento de usuarios
                    navController.navigate(R.id.nav_user_list)
                }
                R.id.nav_profile -> {
                    // Navegar al fragmento de perfil
                    navController.navigate(R.id.nav_profile)
                }
                R.id.nav_settings -> {
                    // Navegar al fragmento de ajustes
                    navController.navigate(R.id.nav_settings)
                }
            }
            // Cerrar el Drawer después de la selección
            drawerLayout.closeDrawers()
            true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflar el menú en la Toolbar
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Gestionar los clics en las opciones de la Toolbar
        return when (item.itemId) {
            R.id.action_logout -> {

                true
            }
            R.id.action_settings -> {
                // Navegar a ajustes
                navController.navigate(R.id.nav_settings)
                true
            }
            R.id.action_help -> {
                // Navegar a ayuda
                navController.navigate(R.id.nav_help) // Asegúrate de que tengas este fragmento
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }



    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, binding.drawerLayout)
    }
}
