package com.example.pos_utfpr_usingdb.views

import android.os.Bundle
import android.widget.SimpleCursorAdapter
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
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        cadastroHandler = CadastroHandler(this)
        exibirLista()
    }

    private fun exibirLista() {
        // Busca os dados reais do banco
        val cadastros = cadastroHandler.listar()

        // Configurar o adaptador customizado que criamos
        val adapter = ElementoImageListAdapter(
            this,
            cadastros
        )
        
        binding.lvCadastro.adapter = adapter
    }
}
