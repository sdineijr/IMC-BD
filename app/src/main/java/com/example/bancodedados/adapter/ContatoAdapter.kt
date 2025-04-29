package com.example.bancodedados.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bancodedados.AppDatabase
import com.example.bancodedados.AtualizarUsuario
import com.example.bancodedados.dao.UsuarioDao
import com.example.bancodedados.databinding.ContatoItemBinding
import com.example.bancodedados.model.Usuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.DecimalFormat

class ContatoAdapter(private val context: Context, private val listaUsuarios: MutableList<Usuario>):
    RecyclerView.Adapter<ContatoAdapter.ContatoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContatoViewHolder {
        val itemLista = ContatoItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ContatoViewHolder(itemLista)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ContatoViewHolder, position: Int) {
        val usuario = listaUsuarios[position]
        val df = DecimalFormat("#.##")

        holder.txtNome.text = usuario.nome
        holder.txtIdade.text = "Idade: ${usuario.idade}"
        holder.txtAltura.text = "Altura: ${usuario.altura}m"
        holder.txtPeso.text = "Peso: ${usuario.peso}kg"
        holder.txtImc.text = "IMC: ${df.format(usuario.imc)}"

        holder.btAtualizar.setOnClickListener {
            val intent = Intent(context, AtualizarUsuario::class.java).apply {
                putExtra("nome", usuario.nome)
                putExtra("idade", usuario.idade)
                putExtra("altura", usuario.altura)
                putExtra("peso", usuario.peso)
                putExtra("uid", usuario.uid)
            }
            context.startActivity(intent)
        }

        holder.btDeletar.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val usuarioDao: UsuarioDao = AppDatabase.getInstance(context).usuarioDao()
                usuarioDao.deletar(usuario.uid)
                listaUsuarios.remove(usuario)

                withContext(Dispatchers.Main) {
                    notifyDataSetChanged()
                }
            }
        }
    }

    override fun getItemCount() = listaUsuarios.size

    inner class ContatoViewHolder(binding: ContatoItemBinding): RecyclerView.ViewHolder(binding.root) {
        val txtNome = binding.txtNome
        val txtIdade = binding.txtIdade
        val txtAltura = binding.txtAltura
        val txtPeso = binding.txtPeso
        val txtImc = binding.txtImc
        val btAtualizar = binding.btAtualizar
        val btDeletar = binding.btdeletar
    }
}