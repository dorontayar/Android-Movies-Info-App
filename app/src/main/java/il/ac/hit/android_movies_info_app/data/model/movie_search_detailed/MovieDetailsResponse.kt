package il.ac.hit.android_movies_info_app.data.model.movie_search_detailed

import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import il.ac.hit.android_movies_info_app.data.model.favorite_movie.BelongsToCollectionConverter
import il.ac.hit.android_movies_info_app.data.model.favorite_movie.GenresConverter
import il.ac.hit.android_movies_info_app.data.model.favorite_movie.ImagesConverter
import il.ac.hit.android_movies_info_app.data.model.favorite_movie.ProductionCompanyConverter
import il.ac.hit.android_movies_info_app.data.model.favorite_movie.ProductionCountryConverter
import il.ac.hit.android_movies_info_app.data.model.favorite_movie.SpokenLanguageConverter
import il.ac.hit.android_movies_info_app.data.model.favorite_movie.StringListConverter
import il.ac.hit.android_movies_info_app.data.model.favorite_movie.VideosConverter
import il.ac.hit.android_movies_info_app.data.model.movie_search.GenreIdsConverter

@TypeConverters(
    GenresConverter::class,
    BelongsToCollectionConverter::class,
    ProductionCompanyConverter::class,
    ProductionCountryConverter::class,
    SpokenLanguageConverter::class,
    VideosConverter::class,
    ImagesConverter::class,
    StringListConverter::class
)
data class MovieDetailsResponse(
    val adult: Boolean,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("belongs_to_collection") val belongsToCollection: BelongsToCollection?,
    val budget: Int,
    val genres: List<Genre>,
    val homepage: String?,
    val id: Int,
    @SerializedName("imdb_id") val imdbId: String?,
    @SerializedName("origin_country") val originCountries: List<String>,
    @SerializedName("original_language") val originalLanguage: String,
    @SerializedName("original_title") val originalTitle: String,
    val overview: String,
    val popularity: Double,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("production_companies") val productionCompanies: List<ProductionCompany>,
    @SerializedName("production_countries") val productionCountries: List<ProductionCountry>,
    @SerializedName("release_date") val releaseDate: String,
    val revenue: Int,
    val runtime: Int,
    @SerializedName("spoken_languages") val spokenLanguages: List<SpokenLanguage>,
    val status: String,
    val tagline: String?,
    val title: String,
    val video: Boolean,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("vote_count") val voteCount: Int,
    val videos: Videos,
    val images: Images
)
