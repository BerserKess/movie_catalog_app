package br.com.movie_catalog.classes

import android.net.Uri

data class Movie(
    val id: Int = 0,
    val title: String,
    val description: String,
    val imageUri: Uri,
    val trailerUri: Uri,
    val year: Int,
    val genre: String,
    val durationMinutes: Int
    )
