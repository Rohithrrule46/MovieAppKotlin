package onepiece.whitebeard.movieappkotlin.model.responses

data class PopularMovieResponse(
    val date: String?,
    val page: Int,
    val results: MutableList<PopularMovies>?,
    val total_pages: Int,
    val total_results: Int
)