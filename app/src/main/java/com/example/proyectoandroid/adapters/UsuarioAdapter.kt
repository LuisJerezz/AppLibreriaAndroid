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
    private var usuarios: MutableList<Usuario>
) : RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder>() {

    class UsuarioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombre: TextView = itemView.findViewById(R.id.tvNombre)
        val email: TextView = itemView.findViewById(R.id.tvEmail)
        val btnEliminar: Button = itemView.findViewById(R.id.btnEliminar)
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

        // Acción al hacer clic en "Eliminar"
        holder.btnEliminar.setOnClickListener {
            // Elimina el usuario de la lista
            usuarios.removeAt(position)
            // Notifica al adaptador que el elemento en esta posición fue eliminado
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, usuarios.size)
        }
    }

    override fun getItemCount(): Int = usuarios.size

    // Método para actualizar la lista

}
