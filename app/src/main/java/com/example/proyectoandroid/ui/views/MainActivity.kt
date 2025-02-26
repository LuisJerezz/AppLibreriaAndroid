package com.example.proyectoandroid.ui.views.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectoandroid.R
import com.example.proyectoandroid.databinding.ActivityMainBinding
import com.example.proyectoandroid.ui.views.fragment.UserListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Cargar UserListFragment en lugar de manejar RecyclerView aquí
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, UserListFragment())
            .commit()
    }
}
