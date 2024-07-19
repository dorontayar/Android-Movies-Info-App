package il.ac.hit.android_movies_info_app.utils

import il.ac.hit.android_movies_info_app.data.model.favorite_movie.FavoriteMovie
import il.ac.hit.android_movies_info_app.data.model.movie_search_detailed.MovieDetailsResponse

fun MovieDetailsResponse.toFavoriteMovie(): FavoriteMovie {
    return FavoriteMovie(
        adult = this.adult,
        backdropPath = this.backdropPath,
        belongsToCollection = this.belongsToCollection,
        budget = this.budget,
        genres = this.genres,
        homepage = this.homepage,
        id = this.id,
        imdbId = this.imdbId,
        originCountries = this.originCountries,
        originalLanguage = this.originalLanguage,
        originalTitle = this.originalTitle,
        overview = this.overview,
        popularity = this.popularity,
        posterPath = this.posterPath,
        productionCompanies = this.productionCompanies,
        productionCountries = this.productionCountries,
        releaseDate = this.releaseDate,
        revenue = this.revenue,
        runtime = this.runtime,
        spokenLanguages = this.spokenLanguages,
        status = this.status,
        tagline = this.tagline,
        title = this.title,
        video = this.video,
        voteAverage = this.voteAverage,
        voteCount = this.voteCount,
        videos = this.videos,
        images = this.images
    )
}
fun shortenText(text: String, maxLength: Int): String {
    if (text.length <= maxLength) {
        return text
    } else {
        // Trim the text to the specified maxLength
        val trimmedText = text.substring(0, maxLength)

        // Append "..." to indicate the text has been shortened
        return "$trimmedText..."
    }
}
interface DrawerController{
    fun openDrawer()
    fun closeDrawer()
}