package br.com.movie_catalog.database

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import br.com.movie_catalog.classes.Movie
import androidx.core.net.toUri

class MovieDAO(context: Context) {

    private val dbHelper = MovieDatabaseHelper(context)

    fun addMovie(movie: Movie): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("title", movie.title)
            put("description", movie.description)
            put("trailer", movie.trailerUri.toString())
            put("image", movie.imageUri.toString())
            put("year", movie.year)
            put("genre", movie.genre)
            put("duration", movie.durationMinutes)
        }

        return db.insert("movies",null, values)
    }

    fun getAllMovies(): List<Movie> {
        val db = dbHelper.readableDatabase

        val cursor = db.query(
            "movies",
            null,null,null,null,null,
            "id DESC"
        )

        val movieList = mutableListOf<Movie>()
        with(cursor) {
            while (moveToNext()) {
                val movie = Movie(
                    id = getInt(getColumnIndexOrThrow("id")),
                    title = getString(getColumnIndexOrThrow("title")),
                    description = getString(getColumnIndexOrThrow("description")),
                    trailerUri = getString(getColumnIndexOrThrow(("trailer"))).toUri(),
                    imageUri = getString(getColumnIndexOrThrow("image")).toUri(),
                    year = getInt(getColumnIndexOrThrow("year")),
                    genre = getString(getColumnIndexOrThrow("genre")),
                    durationMinutes = getInt(getColumnIndexOrThrow("duration")),
                )
                movieList.add(movie)
            }
            close()
        }
        return movieList
    }

    fun deleteMovie(id: Int): Int {
        val db = dbHelper.readableDatabase
        return db.delete("movies", "id = ?", arrayOf(id.toString()))
    }

    fun getMovieById(id: Int): Movie? {
        val db = dbHelper.readableDatabase

        val cursor = db.query(
            "movies",
            null,
            "id = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )

        var movie: Movie? = null

        with(cursor) {
            if (moveToFirst()) {
                movie = Movie(
                    id = getInt(getColumnIndexOrThrow("id")),
                    title = getString(getColumnIndexOrThrow("title")),
                    description = getString(getColumnIndexOrThrow("description")),
                    trailerUri = getString(getColumnIndexOrThrow("trailer")).toUri(),
                    imageUri = getString(getColumnIndexOrThrow("image")).toUri(),
                    year = getInt(getColumnIndexOrThrow("year")),
                    genre = getString(getColumnIndexOrThrow("genre")),
                    durationMinutes = getInt(getColumnIndexOrThrow("duration"))
                )
            }
            close()
        }
        return movie
    }
}