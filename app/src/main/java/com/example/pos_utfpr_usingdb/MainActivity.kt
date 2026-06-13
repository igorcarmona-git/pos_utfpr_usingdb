package com.example.pos_utfpr_usingdb

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pos_utfpr_usingdb.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var db: SQLiteDatabase

    companion object {
        private const val DB_NAME = "banco.db"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        db = SQLiteDatabase.openOrCreateDatabase(this.getDatabasePath(DB_NAME), null)

        setupListeners()

        db.execSQL(buildString {
            append("CREATE TABLE IF NOT EXISTS ")
            append("cadastro(_id INTEGER PRIMARY KEY AUTOINCREMENT, cod TEXT, nome TEXT, cellphone TEXT)")
        })
    }


    private fun setupListeners() {
        binding.btIncluir.setOnClickListener {
            val values = ContentValues().apply {
                put("cod", binding.etCod.text.toString())
                put("nome", binding.etNome.text.toString())
                put("cellphone", binding.etCellphone.text.toString())
            }
            incluir("cadastro", values)
        }
        binding.btUpdate.setOnClickListener {
            val values = ContentValues().apply {
                put("cod", binding.etCod.text.toString())
                put("nome", binding.etNome.text.toString())
                put("cellphone", binding.etCellphone.text.toString())
            }
            alterar("cadastro", values)
        }
        binding.btDelete.setOnClickListener {
            deletar("cadastro", binding.etCod.text.toString())
        }
        binding.btSearch.setOnClickListener {
            pesquisar()
        }
        binding.btList.setOnClickListener {
            listar()
        }
    }

    private fun listar() {
        val cursor = db.query("cadastro", null, null, null, null, null, null)
        val registers = StringBuilder()
        
        if (cursor.moveToFirst()) {
            do {
                val cod = cursor.getString(cursor.getColumnIndexOrThrow("cod"))
                val nome = cursor.getString(cursor.getColumnIndexOrThrow("nome"))
                val cellphone = cursor.getString(cursor.getColumnIndexOrThrow("cellphone"))
                registers.append("Cod: $cod, Nome: $nome, Cellphone: $cellphone\n")
            } while (cursor.moveToNext())
            
            Toast.makeText(this, registers.toString(), Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Nenhum registro encontrado", Toast.LENGTH_SHORT).show()
        }
        cursor.close()
    }

    private fun pesquisar() {
        val cursor = db.query(
            "cadastro",
            null,
            "cod = ?",
            arrayOf(binding.etCod.text.toString()),
            null,
            null,
            null
        )

        if (cursor.moveToFirst()) {
            binding.etNome.setText(cursor.getString(cursor.getColumnIndexOrThrow("nome")))
            binding.etCellphone.setText(cursor.getString(cursor.getColumnIndexOrThrow("cellphone")))
        } else {
            Toast.makeText(this, "Registro não encontrado!", Toast.LENGTH_SHORT).show()
        }
        cursor.close()
    }

    private fun deletar(tableName: String, cod: String) {
        val result = db.delete(tableName, "cod = ?", arrayOf(cod))
        if (result > 0) {
            Toast.makeText(this, "Deletado com sucesso!", Toast.LENGTH_SHORT).show()
            binding.etCod.text.clear()
            binding.etNome.text.clear()
            binding.etCellphone.text.clear()
        } else {
            Toast.makeText(this, "Erro ao deletar ou registro não encontrado!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun alterar(tableName: String, values: ContentValues) {
        val cod = binding.etCod.text.toString()
        val result = db.update(tableName, values, "cod = ?", arrayOf(cod))
        if (result > 0) {
            Toast.makeText(this, "Alterado com sucesso!", Toast.LENGTH_SHORT).show()
            binding.etCod.text.clear()
            binding.etNome.text.clear()
            binding.etCellphone.text.clear()
        } else {
            Toast.makeText(this, "Erro ao alterar ou registro não encontrado!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun incluir(tableName: String, values: ContentValues) {
        val result = db.insert(tableName, null, values)
        if (result != -1L) {
            Toast.makeText(this, "Inserido com sucesso!", Toast.LENGTH_SHORT).show()
            binding.etCod.text.clear()
            binding.etNome.text.clear()
            binding.etCellphone.text.clear()
        } else {
            Toast.makeText(this, "Erro ao inserir!", Toast.LENGTH_SHORT).show()
        }
    }
}