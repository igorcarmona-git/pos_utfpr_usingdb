package com.example.pos_utfpr_usingdb.views

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pos_utfpr_usingdb.R
import com.example.pos_utfpr_usingdb.database.classes.CadastroHandler
import com.example.pos_utfpr_usingdb.databinding.ActivityCadastroBinding
import com.example.pos_utfpr_usingdb.entity.Cadastro
import com.example.pos_utfpr_usingdb.utils.MaskUtil

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
            binding.tvTituloTela.setText(R.string.novo_cadastro)
            binding.btDelete.visibility = View.GONE
            return
        }

        cadastroId = id
        binding.tvTituloTela.setText(R.string.editar_cadastro)
        val cadastro = cadastroHandler.findById(id)

        if (cadastro == null) {
            Toast.makeText(this, R.string.cadastro_nao_encontrado, Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        binding.etNome.setText(cadastro.nome)
        binding.etCellphone.setText(cadastro.cellphone)
    }

    private fun setupListeners() {
        binding.etCellphone.addTextChangedListener(MaskUtil.maskCell(binding.etCellphone))

        binding.btSalvar.setOnClickListener {
            saveRegister()
        }

        binding.btCancelar.setOnClickListener {
            finish()
        }

        binding.btDelete.setOnClickListener {
            val id = cadastroId ?: return@setOnClickListener
            val resultado = cadastroHandler.deleteById(id)

            if (resultado > 0) {
                Toast.makeText(this, R.string.cadastro_excluido_sucesso, Toast.LENGTH_SHORT).show()
                finish()
                return@setOnClickListener
            }

            Toast.makeText(this, R.string.cadastro_nao_encontrado, Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveRegister() {
        val nome = binding.etNome.text.toString().trim()
        val cellphoneRaw = binding.etCellphone.text.toString().trim()
        val cellphoneUnmasked = MaskUtil.unmaskCell(cellphoneRaw)

        if (nome.isBlank()) {
            binding.etNome.error = getString(R.string.informe_nome)
            binding.etNome.requestFocus()
            return
        }

        if (cellphoneUnmasked.isBlank()) {
            binding.etCellphone.error = getString(R.string.informe_celular)
            binding.etCellphone.requestFocus()
            return
        }

        val cadastro = Cadastro(
            id = cadastroId ?: 0,
            nome = nome,
            cellphone = cellphoneUnmasked
        )

        if (cadastroId == null) {
            val resultado = cadastroHandler.insert(cadastro)

            if (resultado == -1L) {
                Toast.makeText(this, R.string.erro_salvar_cadastro, Toast.LENGTH_SHORT).show()
                return
            }

            Toast.makeText(this, R.string.cadastro_salvo_sucesso, Toast.LENGTH_SHORT).show()
        } else {
            val resultado = cadastroHandler.update(cadastro)

            if (resultado <= 0) {
                Toast.makeText(this, R.string.cadastro_nao_encontrado, Toast.LENGTH_SHORT).show()
                return
            }

            Toast.makeText(this, R.string.cadastro_atualizado_sucesso, Toast.LENGTH_SHORT).show()
        }

        finish()
    }

    companion object {
        const val EXTRA_CADASTRO_ID = "cadastro_id"
    }
}
