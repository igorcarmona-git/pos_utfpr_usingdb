package com.example.pos_utfpr_usingdb.views

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pos_utfpr_usingdb.database.classes.CadastroHandler
import com.example.pos_utfpr_usingdb.databinding.ActivityCadastroBinding
import com.example.pos_utfpr_usingdb.entity.Cadastro

class CadastroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCadastroBinding
    private lateinit var cadastroHandler: CadastroHandler

    private var cadastroId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                systemBars.left, systemBars.top, systemBars.right, systemBars.bottom
            )
            insets
        }

        cadastroHandler = CadastroHandler(this)

        loadRegisterToEdit()
        setupListeners()
    }

    private fun loadRegisterToEdit() {
        val id = intent.getIntExtra(EXTRA_CADASTRO_ID, -1)

        if (id <= 0) {
            binding.tvTituloTela.text = "Novo cadastro"
            return
        }

        cadastroId = id
        binding.tvTituloTela.text = "Editar cadastro"
        val cadastro = cadastroHandler.findById(id)

        if (cadastro == null) {
            Toast.makeText(this, "Cadastro não encontrado", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        binding.etNome.setText(cadastro.nome)
        binding.etCellphone.setText(cadastro.cellphone)
    }

    private fun setupListeners() {
        binding.btSalvar.setOnClickListener {
            saveRegister()
        }

        binding.btCancelar.setOnClickListener {
            finish()
        }
    }

    private fun saveRegister() {
        val nome = binding.etNome.text.toString().trim()
        val cellphone = binding.etCellphone.text.toString().trim()

        if (nome.isBlank()) {
            binding.etNome.error = "Informe o nome"
            binding.etNome.requestFocus()
            return
        }

        if (cellphone.isBlank()) {
            binding.etCellphone.error = "Informe o celular"
            binding.etCellphone.requestFocus()
            return
        }

        val cadastro = Cadastro(
            id = cadastroId ?: 0, nome = nome, cellphone = cellphone
        )

        if (cadastroId == null) {
            val resultado = cadastroHandler.insert(cadastro)

            if (resultado == -1L) {
                Toast.makeText(this, "Erro ao salvar cadastro", Toast.LENGTH_SHORT).show()
                return
            }

            Toast.makeText(this, "Cadastro salvo com sucesso", Toast.LENGTH_SHORT).show()
        } else {
            val resultado = cadastroHandler.update(cadastro)

            if (resultado <= 0) {
                Toast.makeText(this, "Cadastro não encontrado", Toast.LENGTH_SHORT).show()
                return
            }

            Toast.makeText(this, "Cadastro atualizado com sucesso", Toast.LENGTH_SHORT).show()
        }

        finish()
    }

    companion object {
        const val EXTRA_CADASTRO_ID = "cadastro_id"
    }
}
