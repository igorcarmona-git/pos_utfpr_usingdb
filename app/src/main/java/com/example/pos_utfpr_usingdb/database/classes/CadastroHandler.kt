package com.example.pos_utfpr_usingdb.database.classes

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.pos_utfpr_usingdb.database.DatabaseHandler
import com.example.pos_utfpr_usingdb.entity.Cadastro

class CadastroHandler(context: Context) : DatabaseHandler(context) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            "CREATE TABLE IF NOT EXISTS $TABLE_NAME ($COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COL_NOME TEXT, $COL_CELLPHONE TEXT)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldDBVersion: Int, newDBVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun list(): MutableList<Cadastro> {
        val db = this.readableDatabase
        val cursor = db.query(TABLE_NAME, null, null, null, null, null, null)
        val registers = mutableListOf<Cadastro>()

        if (cursor.moveToFirst()) {
            do {
                registers.add(cursorToRegister(cursor))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return registers
    }

    fun findById(id: Int): Cadastro? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_NAME, null, "$COL_ID = ?", arrayOf(id.toString()), null, null, null
        )

        var cadastro: Cadastro? = null
        if (cursor.moveToFirst()) {
            cadastro = cursorToRegister(cursor)
        }
        cursor.close()
        return cadastro
    }

    fun insert(cadastro: Cadastro): Long {
        return dbInsert(TABLE_NAME, contentValuesRegister(cadastro))
    }

    fun update(cadastro: Cadastro): Int {
        if (cadastro.id <= 0) {
            return 0
        }

        return dbUpdate(
            TABLE_NAME,
            contentValuesRegister(cadastro),
            "$COL_ID = ?",
            arrayOf(cadastro.id.toString())
        )
    }

    fun deleteById(id: Int): Int {
        if (id <= 0) {
            return 0
        }
        return dbDelete(TABLE_NAME, "$COL_ID = ?", arrayOf(id.toString()))
    }

    private fun cursorToRegister(cursor: Cursor): Cadastro {
        return Cadastro(
            id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
            nome = cursor.getString(cursor.getColumnIndexOrThrow(COL_NOME)) ?: "",
            cellphone = cursor.getString(cursor.getColumnIndexOrThrow(COL_CELLPHONE)) ?: ""
        )
    }

    private fun contentValuesRegister(cadastro: Cadastro): ContentValues {
        return ContentValues().apply {
            put(COL_NOME, cadastro.nome)
            put(COL_CELLPHONE, cadastro.cellphone)
        }
    }

    companion object {
        private const val TABLE_NAME = "cadastro"
        private const val COL_ID = "_id"
        private const val COL_NOME = "nome"
        private const val COL_CELLPHONE = "cellphone"
    }
}
