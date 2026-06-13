package com.example.pos_utfpr_usingdb.database.classes

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.pos_utfpr_usingdb.database.DatabaseHandler
import com.example.pos_utfpr_usingdb.entity.Cadastro

class CadastroHandler(context: Context) : DatabaseHandler(context) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            "CREATE TABLE IF NOT EXISTS cadastro (_id INTEGER PRIMARY KEY AUTOINCREMENT, cod TEXT, nome TEXT, cellphone TEXT)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldDBVersion: Int, newDBVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS cadastro")
        onCreate(db)
    }

    fun listar(): MutableList<Cadastro> {
        val db = this.readableDatabase
        val cursor = db.query("cadastro", null, null, null, null, null, null)
        val registers = mutableListOf<Cadastro>()

        if (cursor.moveToFirst()) {
            do {
                val cadastro = Cadastro(
                    cod = cursor.getString(cursor.getColumnIndexOrThrow("cod")),
                    nome = cursor.getString(cursor.getColumnIndexOrThrow("nome")),
                    cellphone = cursor.getString(cursor.getColumnIndexOrThrow("cellphone"))
                )
                registers.add(cadastro)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return registers
    }

    fun pesquisar(cod: String): Cadastro? {
        val db = this.readableDatabase
        val cursor = db.query(
            "cadastro",
            null,
            "cod = ?",
            arrayOf(cod),
            null,
            null,
            null
        )

        var cadastro: Cadastro? = null
        if (cursor.moveToFirst()) {
            cadastro = Cadastro(
                cod = cursor.getString(cursor.getColumnIndexOrThrow("cod")),
                nome = cursor.getString(cursor.getColumnIndexOrThrow("nome")),
                cellphone = cursor.getString(cursor.getColumnIndexOrThrow("cellphone"))
            )
        }
        cursor.close()
        return cadastro
    }

    // Métodos específicos para facilitar o uso na Activity, chamando os genéricos da base
    fun incluirCadastro(values: ContentValues): Long {
        return dbIncluir("cadastro", values)
    }

    fun alterarCadastro(values: ContentValues, cod: String): Int {
        return dbAlterar("cadastro", values, "cod = ?", arrayOf(cod))
    }

    fun deletarCadastro(cod: String): Int {
        return dbDeletar("cadastro", "cod = ?", arrayOf(cod))
    }
}