package com.example.pos_utfpr_usingdb

import android.content.ContentValues
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pos_utfpr_usingdb.database.classes.CadastroHandler
import com.example.pos_utfpr_usingdb.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var cadastroHandler: CadastroHandler

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

        cadastroHandler = CadastroHandler(this)
        setupListeners()
    }

    private fun setupListeners() {
        binding.btIncluir.setOnClickListener {
            if (validateFields()) {
                val values = getContentValues()
                val result = cadastroHandler.incluirCadastro(values)

                if (result != -1L) {
                    Toast.makeText(this, "Inserido com sucesso!", Toast.LENGTH_SHORT).show()
                    clearFields()
                } else {
                    Toast.makeText(this, "Erro ao inserir!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btUpdate.setOnClickListener {
            val cod = binding.etCod.text.toString()
            if (cod.isEmpty()) {
                Toast.makeText(this, "Informe o código para alterar!", Toast.LENGTH_SHORT).show()
            } else if (validateFields()) {
                val values = getContentValues()
                val result = cadastroHandler.alterarCadastro(values, cod)

                if (result > 0) {
                    Toast.makeText(this, "Alterado com sucesso!", Toast.LENGTH_SHORT).show()
                    clearFields()
                } else {
                    Toast.makeText(this, "Registro não encontrado!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btDelete.setOnClickListener {
            val cod = binding.etCod.text.toString()
            if (cod.isEmpty()) {
                Toast.makeText(this, "Informe o código para deletar!", Toast.LENGTH_SHORT).show()
            } else {
                val result = cadastroHandler.deletarCadastro(cod)
                if (result > 0) {
                    Toast.makeText(this, "Deletado com sucesso!", Toast.LENGTH_SHORT).show()
                    clearFields()
                } else {
                    Toast.makeText(this, "Registro não encontrado!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btSearch.setOnClickListener {
            val cod = binding.etCod.text.toString()
            if (cod.isEmpty()) {
                Toast.makeText(this, "Informe o código para pesquisar!", Toast.LENGTH_SHORT).show()
            } else {
                val cadastro = cadastroHandler.pesquisar(cod)
                if (cadastro != null) {
                    binding.etNome.setText(cadastro.nome)
                    binding.etCellphone.setText(cadastro.cellphone)
                } else {
                    Toast.makeText(this, "Registro não encontrado!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btList.setOnClickListener {
            val registers = cadastroHandler.listar()
            if (registers.isNotEmpty()) {
                val displayText = registers.joinToString("\n") {
                    "Cod: ${it.cod}, Nome: ${it.nome}, Tel: ${it.cellphone}"
                }
                Toast.makeText(this, displayText, Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Nenhum registro encontrado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // ----------------------------------------- HELPERS ------------------------------------------
    private fun validateFields(): Boolean {
        if (binding.etCod.text.isEmpty() || binding.etNome.text.isEmpty() || binding.etCellphone.text.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun getContentValues(): ContentValues {
        return ContentValues().apply {
            put("cod", binding.etCod.text.toString())
            put("nome", binding.etNome.text.toString())
            put("cellphone", binding.etCellphone.text.toString())
        }
    }

    private fun clearFields() {
        binding.etCod.text.clear()
        binding.etNome.text.clear()
        binding.etCellphone.text.clear()
    }
}
