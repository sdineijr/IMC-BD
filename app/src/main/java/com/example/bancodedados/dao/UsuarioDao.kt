package com.example.bancodedados.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.bancodedados.model.Usuario

@Dao
interface UsuarioDao {

    @Insert
    fun inserir(listaUsuarios: MutableList<Usuario>)

    @Query("SELECT * FROM tabela_usuarios ORDER BY nome ASC")
    fun get(): MutableList<Usuario>

    @Query("UPDATE tabela_usuarios SET " +
            "nome = :novoNome, " +
            "idade = :novaIdade, " +
            "altura = :novaAltura, " +
            "peso = :novoPeso, " +
            "imc = :novoImc " +
            "WHERE uid = :id")
    fun atualizar(
        id: Int,
        novoNome: String,
        novaIdade: String,
        novaAltura: String,
        novoPeso: String,
        novoImc: Float
    )

    @Query("DELETE FROM tabela_usuarios WHERE uid = :id")
    fun deletar(id: Int)
}