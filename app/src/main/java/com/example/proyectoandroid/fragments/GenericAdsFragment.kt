package com.example.proyectoandroid.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectoandroid.R
import com.example.proyectoandroid.adapters.AdsAdapter
import com.example.proyectoandroid.databinding.FragmentGenericAdsBinding

class GenericAdsFragment : Fragment(R.layout.fragment_generic_ads) {
    private lateinit var binding: FragmentGenericAdsBinding
    private lateinit var adsRecyclerView: RecyclerView
    private lateinit var adsAdapter: AdsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGenericAdsBinding.bind(view)

        // Aquí puedes configurar el RecyclerView
        adsRecyclerView = binding.recyclerViewAds
        adsRecyclerView.layoutManager = LinearLayoutManager(context)
        adsAdapter = AdsAdapter(getDummyAds())  // Usamos un método de ejemplo para los anuncios
        adsRecyclerView.adapter = adsAdapter
    }

    private fun getDummyAds(): List<String> {
        // Aquí puedes reemplazarlo por los anuncios reales
        return listOf("Anuncio 1", "Anuncio 2", "Anuncio 3", "Anuncio 4", "Anuncio 5")
    }
}