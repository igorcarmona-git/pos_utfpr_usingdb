package com.example.pos_utfpr_usingdb.views

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pos_utfpr_usingdb.R
import com.example.pos_utfpr_usingdb.database.DatabaseFirebaseHandler
import com.example.pos_utfpr_usingdb.database.classes.CadastroHandler
import com.example.pos_utfpr_usingdb.databinding.ActivityCadastroBinding
import com.example.pos_utfpr_usingdb.entity.Cadastro
import com.example.pos_utfpr_usingdb.utils.MaskUtil

class CadastroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCadastroBinding
    private lateinit var cadastroHandler: CadastroHandler
    private val db = DatabaseFirebaseHandler()
    
    private var cadastroId: String? = null // Mudado para String

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
        val id = intent.getStringExtra(EXTRA_CADASTRO_ID)

        if (id.isNullOrEmpty()) {
            binding.tvTituloTela.setText(R.string.novo_cadastro)
            binding.btDelete.visibility = View.GONE
            return
        }

        cadastroId = id
        binding.tvTituloTela.setText(R.string.editar_cadastro)
        
        db.findById(id,
            onSuccess = { cadastro ->
                if (cadastro == null) {
                    Toast.makeText(this, R.string.cadastro_nao_encontrado, Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    binding.etNome.setText(cadastro.nome)
                    binding.etCellphone.setText(cadastro.cellphone)
                }
            },
            onFailure = { e ->
                Toast.makeText(this, "Erro ao carregar dados: ${e.message}", Toast.LENGTH_SHORT).show()
                finish()
            }
        )
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

            db.delete(
                id,
                onSuccess = {
                    Toast.makeText(this, R.string.cadastro_excluido_sucesso, Toast.LENGTH_SHORT)
                        .show()
                    finish()
                },
                onFailure = { e ->
                    Toast.makeText(
                        this,
                        "${getString(R.string.erro_salvar_cadastro)}: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
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

        // Criamos o objeto Cadastro. Se cadastroId for null, id será "" (vazio)
        val cadastro = Cadastro(
            id = cadastroId ?: "",
            nome = nome,
            cellphone = cellphoneUnmasked
        )

        val successMessage = if (cadastroId == null) R.string.cadastro_salvo_sucesso else R.string.cadastro_atualizado_sucesso

        // O DatabaseFirebaseHandler cuidará de gerar o ID se cadastro.id estiver vazio
        db.save(
            cadastro,
            onSuccess = {
                Toast.makeText(this, successMessage, Toast.LENGTH_SHORT).show()
                finish()
            },
            onFailure = { e ->
                Toast.makeText(
                    this,
                    "${getString(R.string.erro_salvar_cadastro)}: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
    }

    companion object {
        const val EXTRA_CADASTRO_ID = "cadastro_id"
    }
}
