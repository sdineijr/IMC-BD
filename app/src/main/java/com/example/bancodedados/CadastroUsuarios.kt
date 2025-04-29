package com.example.bancodedados

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bancodedados.dao.UsuarioDao
import com.example.bancodedados.databinding.ActivityCadastroUsuariosBinding
import com.example.bancodedados.model.Usuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.DecimalFormat

class CadastroUsuarios : AppCompatActivity() {

    private lateinit var binding: ActivityCadastroUsuariosBinding
    private var usuarioDao: UsuarioDao? = null
    private var listaUsuarios: MutableList<Usuario> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastroUsuariosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btCadastrar.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val nome = binding.editNome.text.toString()
                val idade = binding.editIdade.text.toString()
                val altura = binding.editAltura.text.toString()
                val peso = binding.editPeso.text.toString()
                var imcCalculado = 0f
                val mensagem: Boolean

                if(nome.isEmpty() || idade.isEmpty() || altura.isEmpty() || peso.isEmpty()) {
                    mensagem = false
                } else {
                    mensagem = true
                    // Converte e calcula o IMC
                    val alturaDouble = altura.toDoubleOrNull()?.div(100)
                    val pesoDouble = peso.toDoubleOrNull()

                    if (alturaDouble != null) {
                        if (pesoDouble != null) {
                            imcCalculado = if (alturaDouble > 0) (pesoDouble / (alturaDouble * alturaDouble)).toFloat() else 0f
                        }
                    }

                    cadastrar(nome, idade, altura, peso, imcCalculado)
                }

                withContext(Dispatchers.Main) {
                    if(mensagem) {
                        Toast.makeText(
                            applicationContext,
                            "Cadastro realizado com sucesso! IMC: ${"%.2f".format(imcCalculado)}",
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Preencha todos os campos corretamente",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun cadastrar(nome: String, idade: String, altura: String, peso: String, imc: Float) {
        val df = DecimalFormat("#.##") // Formata o IMC com 2 casas decimais
        val imcFormatado = df.format(imc).replace(",", ".").toFloat()

        val usuario = Usuario(
            nome = nome,
            idade = idade,
            altura = altura,
            peso = peso,
            imc = imcFormatado
        )

        listaUsuarios.add(usuario)
        usuarioDao = AppDatabase.getInstance(this).usuarioDao()
        usuarioDao?.inserir(listaUsuarios)
    }
}