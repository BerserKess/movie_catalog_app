package br.com.movie_catalog.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import br.com.movie_catalog.R

class SplashActvity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)

        val logo: ImageView = findViewById(R.id.logo)

        // ANIMAÇÃO DA LOGO
        val animation = AnimationUtils.loadAnimation(this, R.anim.logo_animation)
        logo.startAnimation(animation)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity((Intent(this,MainActivity::class.java)))
            finish()
        },2000)
    }
}