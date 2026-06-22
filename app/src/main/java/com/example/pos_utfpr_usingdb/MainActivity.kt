package com.example.pos_utfpr_usingdb

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pos_utfpr_usingdb.database.classes.CadastroHandler
import com.example.pos_utfpr_usingdb.databinding.ActivityMainBinding
import com.example.pos_utfpr_usingdb.utils.Utils

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
        binding.btUpdate.setOnClickListener {
            val cod = binding.etCod.text.toString().trim()
            if (Utils.validateFields(
                    this, binding.etNome, binding.etCellphone
                )
            ) {
                val values = Utils.getContentValues(
                    "cod" to cod,
                    "nome" to binding.etNome.text.toString().trim(),
                    "cellphone" to binding.etCellphone.text.toString().trim()
                )

                if (cod.isEmpty()) {
                    val result = cadastroHandler.incluirCadastro(values)
                    if (result > 0) {
                        Toast.makeText(this, "Incluído com sucesso!", Toast.LENGTH_SHORT).show()
                        Utils.clearFields(binding.etCod, binding.etNome, binding.etCellphone)
                    } else {
                        Toast.makeText(this, "Erro ao incluir!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val result = cadastroHandler.alterarCadastro(values, cod)
                    if (result > 0) {
                        Toast.makeText(this, "Alterado com sucesso!", Toast.LENGTH_SHORT).show()
                        Utils.clearFields(binding.etCod, binding.etNome, binding.etCellphone)
                    } else {
                        Toast.makeText(this, "Registro não encontrado!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.btDelete.setOnClickListener {
            val cod = binding.etCod.text.toString().trim()
            if (cod.isEmpty()) {
                Toast.makeText(this, "Informe o código para deletar!", Toast.LENGTH_SHORT).show()
            } else {
                val result = cadastroHandler.deletarCadastro(cod)
                if (result > 0) {
                    Toast.makeText(this, "Deletado com sucesso!", Toast.LENGTH_SHORT).show()
                    Utils.clearFields(binding.etCod, binding.etNome, binding.etCellphone)
                } else {
                    Toast.makeText(this, "Registro não encontrado!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btSearch.setOnClickListener {
            val cod = binding.etCod.text.toString().trim()
            if (cod.isEmpty()) {
                Toast.makeText(this, "Informe o código para pesquisar!", Toast.LENGTH_SHORT).show()
            } else {
                val cadastro = cadastroHandler.search(cod)
                if (cadastro != null) {
                    binding.etNome.setText(cadastro.nome)
                    binding.etCellphone.setText(cadastro.cellphone)
                } else {
                    Toast.makeText(this, "Registro não encontrado!", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

}
