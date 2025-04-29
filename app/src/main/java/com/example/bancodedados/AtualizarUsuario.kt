package com.example.bancodedados

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bancodedados.dao.UsuarioDao
import com.example.bancodedados.databinding.ActivityAtualizarUsuarioBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.DecimalFormat

class AtualizarUsuario : AppCompatActivity() {

    private lateinit var binding: ActivityAtualizarUsuarioBinding
    private lateinit var usuarioDao: UsuarioDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAtualizarUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Recupera os dados do intent
        val nomeRecuperado = intent.extras?.getString("nome")
        val idadeRecuperada = intent.extras?.getString("idade")
        val alturaRecuperada = intent.extras?.getString("altura")
        val pesoRecuperado = intent.extras?.getString("peso")
        val uid = intent.extras!!.getInt("uid")

        // Preenche os campos
        binding.editNome.setText(nomeRecuperado)
        binding.editIdade.setText(idadeRecuperada)
        binding.editAltura.setText(alturaRecuperada)
        binding.editPeso.setText(pesoRecuperado)

        binding.btAtualizar.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val nome = binding.editNome.text.toString()
                val idade = binding.editIdade.text.toString()
                val altura = binding.editAltura.text.toString()
                val peso = binding.editPeso.text.toString()
                val mensagem: Boolean

                if (nome.isEmpty() || idade.isEmpty() || altura.isEmpty() || peso.isEmpty()) {
                    mensagem = false
                } else {
                    // Calcula o IMC
                    val alturaDouble = altura.toDoubleOrNull()?.div(100)
                    val pesoDouble = peso.toDoubleOrNull()
                    val imc = if (alturaDouble!! > 0) (pesoDouble?.div((alturaDouble * alturaDouble)))?.toFloat() else 0f

                    val df = DecimalFormat("#.##")
                    val imcFormatado = df.format(imc).replace(",", ".").toFloat()

                    mensagem = true
                    atualizarUsuario(uid, nome, idade, altura, peso, imcFormatado)
                }

                withContext(Dispatchers.Main) {
                    if (mensagem) {
                        Toast.makeText(
                            this@AtualizarUsuario,
                            "Dados atualizados com sucesso!",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    } else {
                        Toast.makeText(
                            this@AtualizarUsuario,
                            "Preencha todos os campos corretamente!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun atualizarUsuario(uid: Int, nome: String, idade: String, altura: String, peso: String, imc: Float) {
        usuarioDao = AppDatabase.getInstance(this).usuarioDao()
        usuarioDao.atualizar(uid, nome, idade, altura, peso, imc)
    }
}