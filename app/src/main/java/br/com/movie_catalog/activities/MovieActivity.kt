package br.com.movie_catalog.activities

import android.content.pm.ActivityInfo
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.MediaController
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.isVisible
import br.com.movie_catalog.R
import br.com.movie_catalog.database.MovieDAO

class MovieActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

        val imageMovie: ImageView = findViewById(R.id.image_movie)
        val btnPlay: ImageButton = findViewById(R.id.btn_play)
        val btnCloseVideo: ImageButton = findViewById(R.id.btn_close_video)
        val titleText: TextView = findViewById(R.id.txt_title)
        val descriptionText: TextView = findViewById(R.id.txt_description)
        val videoView: VideoView = findViewById(R.id.video_view)
        val txtYear: TextView = findViewById(R.id.txt_year)
        val txtGenre: TextView = findViewById(R.id.txt_genre)
        val txtDuration: TextView = findViewById(R.id.txt_duration)

        // VERIFICA SE O FILME EXISTE NO DB
        val movieId = intent.getIntExtra("movieId", -1)
        if (movieId == -1) {
            Toast.makeText(this, "Filme não encontrado", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val movieDao = MovieDAO(this)
        val movie = movieDao.getMovieById(movieId)

        if (movie == null){
            Toast.makeText(this, "Filme não encontrado", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        titleText.text = movie.title
        descriptionText.text = movie.description
        txtGenre.text = movie.genre.uppercase()

        val yearText = "Ano: ${movie.year}"
        val spannableYear = SpannableString(yearText).apply {
            setSpan(StyleSpan(Typeface.BOLD), 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        txtYear.text = spannableYear

        val durationText = "Duração: ${movie.durationMinutes} minutos"
        val spannableDuration = SpannableString(durationText).apply {
            setSpan(StyleSpan(Typeface.BOLD), 0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        txtDuration.text = spannableDuration

        imageMovie.setImageURI(movie.imageUri)

//        // PEGA O VALOR EM STRING E CONVERTE PARA URI
//        val imageUriString = intent.getStringExtra("image")
//        val trailerUriString = intent.getStringExtra("trailer")
//        val imageUri = imageUriString?.toUri()
//        val trailerUri = trailerUriString?.toUri()
//
//
//        val title = intent.getStringExtra("title")
//        val description = intent.getStringExtra("description")
//        val year = intent.getIntExtra("year", 0)
//        val genre = intent.getStringExtra("genre")
//        val duration = intent.getIntExtra("duration", 0)

//        titleText.text = title
//        descriptionText.text = description
//        txtGenre.text = genre?.uppercase()

        // DEIXAR TEXTOS EM NEGRITO
//        val yearText = "Ano: $year"
//        val spannableYear = SpannableString(yearText)
//        spannableYear.setSpan(
//            StyleSpan(Typeface.BOLD),
//            0,
//            3,
//            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//        )
//        txtYear.text = spannableYear
//
//        val durationText = "Duração: $duration minutos"
//        val spannableDuration = SpannableString(durationText)
//        spannableDuration.setSpan(
//            StyleSpan(Typeface.BOLD),
//            0,
//            7,
//            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//        )
//        txtDuration.text = spannableDuration

        //imageMovie.setImageURI(imageUri)


        val fadeIn = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val zoomIn = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.zoom_in)
        titleText.startAnimation(fadeIn)
        descriptionText.startAnimation(fadeIn)
        btnPlay.startAnimation(zoomIn)

        btnPlay.setOnClickListener {
            val trailerUri = movie.trailerUri

            if ( trailerUri != null) {
                //val videoTeste = "android.resource://${packageName}/${R.raw.trailer}".toUri()
                videoView.visibility = View.VISIBLE
                btnCloseVideo.visibility = View.VISIBLE
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE // FORÇAR ROTAÇÃO
                adjustLayoutForOrientation(resources.configuration.orientation)

                // CONTROLADORES
                val mediaController = MediaController(this)
                mediaController.setAnchorView(videoView)
                videoView.setMediaController(mediaController)

                videoView.setVideoURI(trailerUri)
                videoView.requestFocus()
                videoView.setOnPreparedListener {
                    videoView.seekTo(1)
                    videoView.start()
                }
                videoView.setOnCompletionListener {
                    videoView.visibility = View.GONE
                    imageMovie.visibility = View.VISIBLE
                    btnPlay.visibility = View.VISIBLE
                    btnCloseVideo.visibility = View.GONE
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                }
            } else {
                Toast.makeText(this, "Trailer não disponível", Toast.LENGTH_SHORT). show()
            }
        }

        btnCloseVideo.setOnClickListener {
//            val videoView: VideoView = findViewById(R.id.video_view)
//            val imageMovie: ImageView = findViewById(R.id.image_movie)
//            val btnPlay: ImageButton = findViewById(R.id.btn_play)
//            val titleText: TextView = findViewById(R.id.txt_title)
//            val descriptionText: TextView = findViewById(R.id.txt_description)
            val scrollView: ScrollView = findViewById(R.id.scroll_view)

            videoView.stopPlayback()
            videoView.visibility = View.GONE
            btnCloseVideo.visibility = View.GONE

            imageMovie.visibility = View.VISIBLE
            btnPlay.visibility = View.VISIBLE
            titleText.visibility = View.VISIBLE
            descriptionText.visibility = View.VISIBLE
            scrollView.visibility = View.VISIBLE

            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            adjustLayoutForOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        }

    }

    override fun onConfigurationChanged(newConfig: android.content.res.Configuration) {
        super.onConfigurationChanged(newConfig)
        adjustLayoutForOrientation(newConfig.orientation)
    }

    private fun adjustLayoutForOrientation(orientatiton: Int){
        val imageMovie: ImageView = findViewById(R.id.image_movie)
        val btnPlay: ImageButton = findViewById(R.id.btn_play)
        val titleText: TextView = findViewById(R.id.txt_title)
        val descriptionText: TextView = findViewById(R.id.txt_description)
        val videoView: VideoView = findViewById(R.id.video_view)
        val linearLayout: LinearLayout = findViewById(R.id.linear_movie)

        val scrollView: ScrollView = findViewById(R.id.scroll_view)

        val decorView = window.decorView

        val params = videoView.layoutParams

        if (orientatiton == android.content.res.Configuration.ORIENTATION_LANDSCAPE) {
            scrollView.visibility = View.GONE
            supportActionBar?.hide()
            decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    )

            imageMovie.visibility = View.GONE
            btnPlay.visibility = View.GONE
            titleText.visibility = View.GONE
            descriptionText.visibility = View.GONE

            linearLayout.orientation = LinearLayout.VERTICAL
            params.width = ViewGroup.LayoutParams.MATCH_PARENT
            params.height = ViewGroup.LayoutParams.MATCH_PARENT
        } else {
            scrollView.visibility = View.VISIBLE
            supportActionBar?.show()
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE

            imageMovie.visibility = View.VISIBLE
            btnPlay.visibility = View.VISIBLE
            titleText.visibility = View.VISIBLE
            descriptionText.visibility = View.VISIBLE


            params.height = ViewGroup.LayoutParams.WRAP_CONTENT
            params.width = ViewGroup.LayoutParams.MATCH_PARENT
        }
    }

    override fun onBackPressed() {
        val videoView: VideoView = findViewById(R.id.video_view)

        if (videoView.isVisible && videoView.isPlaying) {
            videoView.stopPlayback()
            videoView.visibility = View.GONE
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            adjustLayoutForOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

            val scrollView: ScrollView = findViewById(R.id.scroll_view)
            scrollView.scrollTo(0,0)
        } else {
            super.onBackPressed()
        }


    }
}