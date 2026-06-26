package com.example.pos_utfpr_usingdb.views

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pos_utfpr_usingdb.R
import com.example.pos_utfpr_usingdb.adapters.ElementoImageListAdapter
import com.example.pos_utfpr_usingdb.database.DatabaseFirebaseHandler
import com.example.pos_utfpr_usingdb.databinding.ActivityListarBinding

class ListarActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListarBinding

    // Esse cadastroHandler eu estou a utilizar funçcoes do SQLite, mas agora eu estou usando
    // Firebase, deixei aqui mais para estudo

    // private lateinit var cadastroHandler: CadastroHandler
    private val db = DatabaseFirebaseHandler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityListarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                systemBars.left, systemBars.top, systemBars.right, systemBars.bottom
            )
            insets
        }

//        cadastroHandler = CadastroHandler(this)
        setupListeners()
    }

    override fun onStart() {
        super.onStart()
        showList()
    }

    private fun setupListeners() {
        binding.btIncluir.setOnClickListener {
            openCadastro()
        }

        binding.btSearch.setOnClickListener {
            showSearchDialog()
        }
    }

    private fun showSearchDialog() {
        val inputEtCod = EditText(this).apply {
            hint = getString(R.string.id_do_cadastro)
            inputType = InputType.TYPE_CLASS_TEXT
        }

        val dialog = AlertDialog.Builder(this)

        dialog.setTitle(R.string.pesquisar_cadastro)
        dialog.setView(inputEtCod)
        dialog.setNegativeButton(R.string.cancelar, null)
        dialog.setPositiveButton(R.string.pesquisar) { _, _ ->
            val id = inputEtCod.text.toString().trim()

            if (id.isEmpty()) {
                Toast.makeText(this, R.string.informe_id_valido, Toast.LENGTH_SHORT).show()
                return@setPositiveButton
            }

            db.findById(
                id,
                onSuccess = { cadastro ->
                    if (cadastro == null) {
                        Toast.makeText(this, R.string.cadastro_nao_encontrado, Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        openCadastro(cadastro.id)
                    }
                },
                onFailure = { e ->
                    Toast.makeText(this, "Erro ao pesquisar: ${e.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            )
        }
        dialog.show()
    }

    private fun showList() {
        db.list(
            onSuccess = { results ->
                val adapter = ElementoImageListAdapter(
                    context = this,
                    elements = results.toMutableList()
                ) { cadastro, _ ->
                    openCadastro(cadastro.id)
                }
                binding.lvCadastro.adapter = adapter
            },
            onFailure = { e ->
                Toast.makeText(this, "Erro ao carregar lista: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        )
    }

    private fun openCadastro(cadastroId: String? = null) {
        val intent = Intent(this, CadastroActivity::class.java)
        if (cadastroId != null) {
            intent.putExtra(CadastroActivity.EXTRA_CADASTRO_ID, cadastroId)
        }
        startActivity(intent)
    }
}
