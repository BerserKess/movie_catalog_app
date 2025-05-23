package br.com.movie_catalog.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.movie_catalog.MovieAddFragment
import br.com.movie_catalog.R
import br.com.movie_catalog.classes.Movie
import br.com.movie_catalog.classes.MovieAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), MovieAddFragment.OnMovieAddedListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var movieAdapter:  MovieAdapter
    private lateinit var fragmentContainer: FrameLayout
    private val movies = mutableListOf<Movie>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        // INICIALIZAR A RECYCLERVIEW
        recyclerView = findViewById(R.id.rv_movies)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        movieAdapter = MovieAdapter(movies,
            onMovieImageClick = {movie -> openMovieDetails(movie)},
            onMovieLongClick = {position -> showDeleteConfirmationDialog(position)}
            )
        recyclerView.adapter = movieAdapter

        // FAB
        val fab: FloatingActionButton = findViewById(R.id.fab_addMovie)
        fab.setOnClickListener {

            // MUDAR BACKGROUND AO EXIBIR O FRAGMENT
            findViewById<FrameLayout>(R.id.fragmentContainer)
                .setBackgroundColor("#CC000000".toColorInt())

            val fragment = MovieAddFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer,fragment)
                .addToBackStack(null)
                .commit()

            findViewById<FrameLayout>(R.id.fragmentContainer).visibility = View.VISIBLE
            fab.hide()
        }

        // FAB APÓS SER PRESSIONADO O BOTÃO DE VOLTAR
        supportFragmentManager.addOnBackStackChangedListener {
            val fragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
            val container: FrameLayout = findViewById(R.id.fragmentContainer)

            if (fragment == null){

                container.visibility = View.GONE
                fab.show()
            }
        }
    }

    override fun onMovieAdded(movie: Movie) {
        movies.add(movie)
        movieAdapter.notifyItemInserted(movies.size - 1)
        supportFragmentManager.popBackStack()
        findViewById<FrameLayout>(R.id.fragmentContainer).visibility = View.GONE
        findViewById<FloatingActionButton>(R.id.fab_addMovie).show()
    }

    private fun openMovieDetails(movie: Movie){
        val intent = Intent(this, MovieActivity::class.java).apply {
            putExtra("title", movie.title)
            putExtra("description", movie.description)
            putExtra("image", movie.imageUri.toString())
            putExtra("trailer", movie.trailerUri.toString())
            putExtra("year", movie.year)
            putExtra("genre", movie.genre)
            putExtra("duration", movie.durationMinutes)
        }

        startActivity(intent)
    }

    // REMOVER FILME
    private fun showDeleteConfirmationDialog(position: Int){
        MaterialAlertDialogBuilder(this,R.style.CustomAlertDialog)
            .setTitle("Apagar Filme")
            .setMessage("Tem certeza que deseja remover este filme?")
            .setPositiveButton("Sim") {_,_ ->
                movies.removeAt(position)
                movieAdapter.notifyItemRemoved(position)
            }.setNegativeButton("Cancelar",null).show()
    }
}