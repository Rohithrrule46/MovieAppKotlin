package onepiece.whitebeard.movieappkotlin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import onepiece.whitebeard.movieappkotlin.model.responses.PopularMovieResponse
import onepiece.whitebeard.movieappkotlin.model.responses.PopularMovies
import onepiece.whitebeard.movieappkotlin.other.Event
import onepiece.whitebeard.movieappkotlin.other.Resource
import onepiece.whitebeard.movieappkotlin.other.Status
import onepiece.whitebeard.movieappkotlin.repository.MovieRepositoryProtocol
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val repo: MovieRepositoryProtocol
) : ViewModel(), MoviesProtocol {

    init {

    }

    override var movies = MutableLiveData<Resource<List<PopularMovies>>>()

    override var popularMoviesLiveData = MutableLiveData<Event<Resource<PopularMovieResponse>>>()

    override var searchLivedata = MutableLiveData<Event<Resource<PopularMovieResponse>>>()

    val searchMovies: MutableLiveData<Resource<PopularMovieResponse>> = MutableLiveData()
    var searchMoviesPage = 1
    var searchMoviesResponse: PopularMovieResponse? = null

    val upcomingMoviesLivedata: MutableLiveData<Resource<PopularMovieResponse>> = MutableLiveData()
    var upcomingMoviesPage = 1
    var upcomingMoviesResponse: PopularMovieResponse? = null

    override fun insertOrUpdateMovies(movie: PopularMovies) = viewModelScope.launch {
        repo.insertMovieIntoDatabase(movie)
    }

    override fun getPopularMoviesFromApi() = viewModelScope.launch(Dispatchers.Unconfined) {
        val response = repo.getMoviesListFromApi()
        when (response.status) {
            Status.LOADING -> {
                movies.postValue(Resource.loading(null))
                popularMoviesLiveData.postValue(Event(Resource.loading(null)))

            }
            Status.SUCCESS -> {
                movies.postValue(Resource.success(response.data!!.results!!))
                popularMoviesLiveData.postValue(Event(response))
            }
            Status.ERROR -> {
                movies.postValue(Resource.error("error Fetching Movies", null))
                popularMoviesLiveData.postValue(Event(Resource.error("error Fetching Movies", null)))
            }
        }
    }

    override fun searchForMovies(searchString: String) = viewModelScope.launch {

        val response = repo.searchForMovieFromRepo(
            searchString = searchString, page = searchMoviesPage
        )

        when (response.status) {
            Status.LOADING -> {
                searchLivedata.postValue(Event(Resource.loading(null)))
            }
            Status.SUCCESS -> {
                if (searchString.isEmpty()) {
                    searchMoviesPage = 1
                    searchMoviesResponse = null
                    val error = "Please enter a movie name"
                    searchLivedata.postValue(Event(Resource.error(error)))

                } else {
                    searchLivedata.postValue(Event(response))
                }
            }
            Status.ERROR -> {
                searchMoviesPage = 1
                searchMoviesResponse = null
                val error = "Please enter a movie name"
                searchLivedata.postValue(Event(Resource.error(error)))
            }
        }

    }

    override fun getUpcomingMovies() = viewModelScope.launch {
        upcomingMoviesLivedata.postValue(Resource.loading(null))
        val response = repo.upcomingMovies(upcomingMoviesPage)

        upcomingMoviesLivedata.postValue(handleUpcomingMoviesResponse(response))

    }

    fun getMovies(): LiveData<Resource<List<PopularMovies>>> {
        return movies
    }

    private fun handleUpcomingMoviesResponse(
        response: Response<PopularMovieResponse>
    ): Resource<PopularMovieResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                upcomingMoviesPage++
                if (upcomingMoviesResponse == null) {
                    upcomingMoviesResponse = it
                } else {
                    val oldList = upcomingMoviesResponse?.results
                    val newList = it.results

                    oldList?.addAll(newList!!)
                }
                return Resource.success(upcomingMoviesResponse ?: it)
            }
        }

        return Resource.error("Network error")
    }

}