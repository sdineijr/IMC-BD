package com.example.bancodedados

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.bancodedados.dao.UsuarioDao
import com.example.bancodedados.model.Usuario

@Database(entities = [Usuario::class], version = 11)  // Aumentei a versão para 11
abstract class AppDatabase: RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao

    companion object {
        private const val DATABASE_NOME = "DB_USUARIOS_IMC"  // Mudei o nome para refletir a nova estrutura

        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Migração da versão 10 para 11 (se você já tinha dados na versão anterior)
        private val MIGRATION_10_11 = object : Migration(10, 11) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Remover colunas antigas
                database.execSQL("CREATE TABLE IF NOT EXISTS usuarios_new (uid INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, nome TEXT NOT NULL, idade TEXT NOT NULL, altura TEXT NOT NULL, peso TEXT NOT NULL, imc REAL NOT NULL)")
                database.execSQL("INSERT INTO usuarios_new (uid, nome, idade, altura, peso, imc) SELECT uid, nome, idade, '0', '0', 0 FROM tabela_usuarios")
                database.execSQL("DROP TABLE tabela_usuarios")
                database.execSQL("ALTER TABLE usuarios_new RENAME TO tabela_usuarios")
            }
        }

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NOME
                )
                    .addMigrations(MIGRATION_10_11)  // Adiciona a migração
                    .fallbackToDestructiveMigration()  // Só para desenvolvimento - remove em produção
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}