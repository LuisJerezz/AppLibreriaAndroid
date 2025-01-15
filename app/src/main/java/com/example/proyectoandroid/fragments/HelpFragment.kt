package com.example.proyectoandroid.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.proyectoandroid.R
import com.example.proyectoandroid.databinding.FragmentHelpBinding

class HelpFragment : Fragment(R.layout.fragment_help) {
    private lateinit var binding: FragmentHelpBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHelpBinding.bind(view)

        // Aquí puedes agregar la lógica para la sección de ayuda
    }
}