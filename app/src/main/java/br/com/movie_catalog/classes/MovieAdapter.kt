package br.com.movie_catalog.classes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import br.com.movie_catalog.R
import androidx.core.net.toUri

class MovieAdapter(
    private val movies: List<Movie>,
    private val onMovieImageClick: (Movie) -> Unit,
    private val onMovieLongClick: (Int) -> Unit
): RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    class MovieViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageViewMovie)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.imageView.setImageURI(movie.imageUri.toString().toUri())

        // ABRIR DETALHES DO FILME
        holder.imageView.setOnClickListener {
            onMovieImageClick(movie)
        }
        // PRESSIONAR PARA DELETAR O FILME
        holder.imageView.setOnLongClickListener {
            onMovieLongClick(position)
            true
        }
    }

    override fun getItemCount(): Int = movies.size
}

