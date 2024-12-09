package com.example.proyectoandroid.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectoandroid.R
import com.example.proyectoandroid.models.Usuario

class UsuarioAdapter(
    private var usuarios: MutableList<Usuario>,
    private val onEditClick: (Usuario, Int) -> Unit,
    private val onDeleteClick: (Int) -> Unit
) : RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder>() {

    class UsuarioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombre: TextView = itemView.findViewById(R.id.tvNombre)
        val email: TextView = itemView.findViewById(R.id.tvEmail)
        val btnEliminar: Button = itemView.findViewById(R.id.btnEliminar)
        val btnEditar: Button = itemView.findViewById(R.id.btnEditar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuarioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_usuario, parent, false)
        return UsuarioViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsuarioViewHolder, position: Int) {
        val usuario = usuarios[position]
        holder.nombre.text = usuario.nombre
        holder.email.text = usuario.email

        holder.btnEliminar.setOnClickListener { onDeleteClick(position) }
        holder.btnEditar.setOnClickListener { onEditClick(usuario, position) }
    }

    override fun getItemCount(): Int = usuarios.size
}
