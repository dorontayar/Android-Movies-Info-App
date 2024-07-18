package il.ac.hit.android_movies_info_app.ui.explore.viewmodel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import il.ac.hit.android_movies_info_app.data.model.top_rated_movies.TopRatedMovie
import il.ac.hit.android_movies_info_app.databinding.ItemTopRatedBinding
import il.ac.hit.android_movies_info_app.utils.Constants.Companion.IMAGE_TYPE_W185

class TopRatedAdapter(private val listener : MoviesItemListener) :
    RecyclerView.Adapter<TopRatedAdapter.MovieViewHolder>() {

    private val movies = ArrayList<TopRatedMovie>()

    class MovieViewHolder (private val itemBinding: ItemTopRatedBinding,
        private val listener: MoviesItemListener)
        : RecyclerView.ViewHolder(itemBinding.root),
        View.OnClickListener {

            private lateinit var movie: TopRatedMovie

            init {
                itemBinding.root.setOnClickListener(this)
            }

            fun bind(item: TopRatedMovie) {

                this.movie = item
                itemBinding.movieTitle.text = item.title
                val voteAveragePercent:String = (item.voteAverage * 10).toInt().toString()+"%"
                itemBinding.userScorePercent.text = voteAveragePercent

                // can change to other sizes, check Constants.kt
                val imagePath:String = IMAGE_TYPE_W185+item.posterPath
                Glide.with(itemBinding.root).load(imagePath).into(itemBinding.moviePoster)
            }
            override fun onClick(v: View?) {

                listener.onMovieClick(movie.id)
            }
        }

    fun setMovies(movies : Collection<TopRatedMovie>) {
        this.movies.clear()
        this.movies.addAll(movies)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemTopRatedBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MovieViewHolder(binding,listener)
    }

    override fun getItemCount() = movies.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) = holder.bind(movies[position])

    interface MoviesItemListener {
        fun onMovieClick(movieId :Int)
    }
}