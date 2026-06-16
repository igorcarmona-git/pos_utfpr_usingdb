package com.example.pos_utfpr_usingdb.views

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }

        cadastroHandler = CadastroHandler(this)
    }

    override fun onResume() {
        super.onResume()
        exibirLista()
    }

    private fun exibirLista() {
        val cadastros = cadastroHandler.list()

        val adapter = ElementoImageListAdapter(
            context = this,
            elements = cadastros
        ) { cadastro, _ ->
            val intent = Intent(this, CadastroActivity::class.java)
            intent.putExtra(CadastroActivity.EXTRA_CADASTRO_ID, cadastro.id)
            startActivity(intent)
        }

        binding.lvCadastro.adapter = adapter
    }
}
