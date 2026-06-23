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
import com.example.pos_utfpr_usingdb.database.classes.CadastroHandler
import com.example.pos_utfpr_usingdb.databinding.ActivityListarBinding

class ListarActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListarBinding
    private lateinit var cadastroHandler: CadastroHandler

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

        cadastroHandler = CadastroHandler(this)
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
        val input = EditText(this).apply {
            hint = getString(R.string.id_do_cadastro)
            inputType = InputType.TYPE_CLASS_NUMBER
            minHeight = resources.getDimensionPixelSize(R.dimen.dialog_input_min_height)
            isSingleLine = true
        }

        val dialog = AlertDialog.Builder(this)
            .setTitle(R.string.pesquisar_cadastro).setView(input)
            .setNegativeButton(R.string.cancelar, null)
            .setPositiveButton(R.string.pesquisar, null)
            .create()

        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                val id = input.text.toString().trim().toIntOrNull()
                if (id == null || id <= 0) {
                    input.error = getString(R.string.informe_id_valido)
                    input.requestFocus()
                    return@setOnClickListener
                }

                val cadastro = cadastroHandler.findById(id)
                if (cadastro == null) {
                    Toast.makeText(this, R.string.cadastro_nao_encontrado, Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }

                dialog.dismiss()
                openCadastro(cadastro.id)
            }
        }

        dialog.show()
    }

    private fun showList() {
        val cadastros = cadastroHandler.list()

        val adapter = ElementoImageListAdapter(
            context = this, elements = cadastros
        ) { cadastro, _ ->
            openCadastro(cadastro.id)
        }

        binding.lvCadastro.adapter = adapter
    }

    private fun openCadastro(cadastroId: Int? = null) {
        val intent = Intent(this, CadastroActivity::class.java)
        if (cadastroId != null) {
            intent.putExtra(CadastroActivity.EXTRA_CADASTRO_ID, cadastroId)
        }
        startActivity(intent)
    }
}
