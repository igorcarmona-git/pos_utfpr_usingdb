package com.example.pos_utfpr_usingdb.views

import android.os.Bundle
import android.widget.SimpleCursorAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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
        // Exibir a lista de cadastros no ListView vindo do db
        val cadastros = cadastroHandler.listarCursor()

        // Configurar o adaptador para exibir os dados no ListView usando um layout simples de texto
        val adapter = SimpleCursorAdapter(
            this,
            android.R.layout.simple_list_item_2,
            cadastros,
            arrayOf("cod","nome"),
            intArrayOf(android.R.id.text1, android.R.id.text2),
            0
        )
        
        binding.lvCadastro.adapter = adapter
    }
}
