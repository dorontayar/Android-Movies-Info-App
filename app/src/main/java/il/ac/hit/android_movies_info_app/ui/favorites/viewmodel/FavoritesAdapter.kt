package il.ac.hit.android_movies_info_app.ui.favorites.viewmodel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import il.ac.hit.android_movies_info_app.HiltApp
import il.ac.hit.android_movies_info_app.R
import il.ac.hit.android_movies_info_app.data.model.favorite_movie.FavoriteMovie
import il.ac.hit.android_movies_info_app.data.model.movie_search.Movie
import il.ac.hit.android_movies_info_app.databinding.ItemMovieBinding
import il.ac.hit.android_movies_info_app.utils.Constants
import il.ac.hit.android_movies_info_app.utils.Constants.Companion.IMAGE_TYPE_W185
import il.ac.hit.android_movies_info_app.utils.shortenText


class FavoritesAdapter(private val listener : MoviesItemListener) :
    RecyclerView.Adapter<FavoritesAdapter.MovieViewHolder>() {

    val movies = ArrayList<FavoriteMovie>()

    class MovieViewHolder (private val itemBinding: ItemMovieBinding,
                           private val listener: MoviesItemListener)
        : RecyclerView.ViewHolder(itemBinding.root),
        View.OnClickListener {

        private lateinit var movie: FavoriteMovie

        init {
            itemBinding.root.setOnClickListener(this)
        }

        fun bind(item: FavoriteMovie) {

            this.movie = item
            itemBinding.title.text = item.title
            if(item.overview != "") {
                itemBinding.description.text = shortenText(item.overview, 150)
            } else { itemBinding.description.text =
                HiltApp.getContext()?.getString(R.string.description_not_found)}            // can change to other sizes, check Constants.kt
            val imagePath:String = Constants.IMAGE_TYPE_W185 +item.posterPath
            Glide.with(itemBinding.root).load(imagePath).into(itemBinding.image)
        }
        override fun onClick(v: View?) {

            listener.onMovieClick(movie.id)
        }
    }

    fun setMovies(movies : Collection<FavoriteMovie>) {
        this.movies.clear()
        this.movies.addAll(movies)
        notifyDataSetChanged()
    }
    fun removeMovie(position: Int) {
        movies.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MovieViewHolder(binding,listener)
    }

    override fun getItemCount() = movies.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) = holder.bind(movies[position])

    interface MoviesItemListener {
        fun onMovieClick(movieId :Int)
    }
}