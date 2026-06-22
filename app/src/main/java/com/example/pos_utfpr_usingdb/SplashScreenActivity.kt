package com.example.pos_utfpr_usingdb

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pos_utfpr_usingdb.views.ListarActivity

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // serve para executar um código após um tempo, na thread principal da ‘interface’ do Android
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, ListarActivity::class.java)
            startActivity(intent)
            finish() // Finaliza a SplashScreenActivity após rodar pela primeira vez
        }, 3000)
    }
}