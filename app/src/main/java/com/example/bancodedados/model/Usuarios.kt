package com.example.bancodedados.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tabela_usuarios")
data class Usuario (
    @ColumnInfo(name = "nome") val nome: String,
    @ColumnInfo(name = "idade") val idade: String,
    @ColumnInfo(name = "altura") val altura: String,  // em metros (ex: "1.75")
    @ColumnInfo(name = "peso") val peso: String,      // em kg (ex: "68.5")
    @ColumnInfo(name = "imc") val imc: Float          // valor calculado
) {
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0
}