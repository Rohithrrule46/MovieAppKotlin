package onepiece.whitebeard.movieappkotlin.viewmodel

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Job
import onepiece.whitebeard.movieappkotlin.model.responses.PopularMovieResponse
import onepiece.whitebeard.movieappkotlin.model.responses.PopularMovies
import onepiece.whitebeard.movieappkotlin.other.Event
import onepiece.whitebeard.movieappkotlin.other.Resource

interface MoviesProtocol {

    var movies : MutableLiveData<Resource<List<PopularMovies>>>
    var popularMoviesLiveData: MutableLiveData<Event<Resource<PopularMovieResponse>>>
    var searchLivedata: MutableLiveData<Event<Resource<PopularMovieResponse>>>

    fun insertOrUpdateMovies(movie: PopularMovies): Job
    fun searchForMovies(searchString: String): Job
    fun getPopularMoviesFromApi(): Job
    fun getUpcomingMovies(): Job
}