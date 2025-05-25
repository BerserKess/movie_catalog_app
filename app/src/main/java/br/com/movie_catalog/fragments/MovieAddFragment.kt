package br.com.movie_catalog

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import br.com.movie_catalog.classes.Movie
import br.com.movie_catalog.database.MovieDAO

class MovieAddFragment : Fragment(){
    private lateinit var editTextName: EditText
    private lateinit var editTextDescription: EditText
    private lateinit var editTextYear: EditText
    private lateinit var editTextGenre: EditText
    private lateinit var btnImage: Button
    private lateinit var btnTrailer: Button
    private lateinit var btnSave: Button
    private lateinit var editTextDuration: EditText
    private lateinit var movieDAO: MovieDAO


    private var uriImage: Uri? = null
    private var uriTrailer: Uri? =null

    private val PICK_IMAGE_REQUEST = 1
    private val PICK_TRAILER_REQUEST = 2

    interface  OnMovieAddedListener {
        fun onMovieAdded(movie: Movie)
    }

    private var listener: OnMovieAddedListener? = null

    override fun onAttach(context: android.content.Context) {
        super.onAttach(context)

        if (context is OnMovieAddedListener) {
            listener = context
        } else {
            throw RuntimeException("$context deve implementar OnMovieAddedListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val  view = inflater.inflate(R.layout.fragment_movie_add, container, false)
        // INICIAR BD
        movieDAO = MovieDAO(requireContext())


        editTextName = view.findViewById(R.id.editTextTitle)
        editTextDescription = view.findViewById(R.id.editTextDescription)
        editTextYear = view.findViewById(R.id.editTextYear)
        editTextGenre = view.findViewById(R.id.editTextGenre)
        editTextDuration = view.findViewById(R.id.editTextDuration)
        btnImage = view.findViewById(R.id.btn_selectImage)
        btnTrailer = view.findViewById(R.id.btn_selectTrailer)
        btnSave = view.findViewById(R.id.btn_saveMovie)

        btnImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.type = "image/*"
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        btnTrailer.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.type = "video/*"
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivityForResult(intent, PICK_TRAILER_REQUEST)
        }

        btnSave.setOnClickListener {
            val name = editTextName.text.toString()
            val description = editTextDescription.text.toString()
            val year = editTextYear.text.toString().toIntOrNull()
            val genre = editTextGenre.text.toString()
            val duration = editTextDuration.text.toString().toIntOrNull()

            if (name.isNotBlank() && description.isNotBlank() &&
                year != null && genre.isNotBlank() && duration != null &&
                uriImage != null && uriTrailer != null){

                val movie = Movie(
                    id = 0,
                    title = name,
                    description = description,
                    imageUri = uriImage!!,
                    trailerUri = uriTrailer!!,
                    year = year,
                    genre = genre,
                    durationMinutes = duration
                )
                val result = movieDAO.addMovie(movie)
                if (result != -1L) {
                    Toast.makeText(requireContext(),"Filme salvo com sucesso", Toast.LENGTH_SHORT).show()
                    listener?.onMovieAdded(movie)
                    requireActivity().findViewById<FrameLayout>(R.id.fragmentContainer)
                        .setBackgroundColor(Color.TRANSPARENT)
                    parentFragmentManager.popBackStack()
                } else {
                    Toast.makeText(requireContext(), "Erro ao salvar no database", Toast.LENGTH_SHORT).show()
                }
//                listener?.onMovieAdded(movie)
//                Toast.makeText(requireContext(), "Filme salvo com sucesso!", Toast.LENGTH_SHORT).show()
//                requireActivity().findViewById<FrameLayout>(R.id.fragmentContainer)
//                    .setBackgroundColor(Color.TRANSPARENT)
//                parentFragmentManager.popBackStack() // FECHAR FRAGMENTO
            } else {
                Toast.makeText(requireContext(), "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val uri = data.data
            val flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            when (requestCode) {
                PICK_IMAGE_REQUEST -> {
                    uriImage = uri!!
                    requireContext().contentResolver.takePersistableUriPermission(uri, flags)
                }
                PICK_TRAILER_REQUEST -> {
                    uriTrailer = uri!!
                    requireContext().contentResolver.takePersistableUriPermission(uri, flags)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().findViewById<FrameLayout>(R.id.fragmentContainer)
            .setBackgroundColor(Color.TRANSPARENT)
    }
}


