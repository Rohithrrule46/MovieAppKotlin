package onepiece.whitebeard.movieappkotlin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import onepiece.whitebeard.movieappkotlin.model.responses.PopularMovieResponse
import onepiece.whitebeard.movieappkotlin.model.responses.PopularMovies
import onepiece.whitebeard.movieappkotlin.other.Event
import onepiece.whitebeard.movieappkotlin.other.Resource
import onepiece.whitebeard.movieappkotlin.other.Status
import onepiece.whitebeard.movieappkotlin.repository.MovieRepositotyInterface
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val repo: MovieRepositotyInterface
) : ViewModel() {

    init {

    }

    val movies = MutableLiveData<Resource<List<PopularMovies>>>()

    private val _popularMoviesLiveData = MutableLiveData<Event<Resource<PopularMovieResponse>>>()
    val popularMoviesLiveData: LiveData<Event<Resource<PopularMovieResponse>>> =
        _popularMoviesLiveData

    private val _searchLivedata = MutableLiveData<Event<Resource<PopularMovieResponse>>>()
    val searchLivedata: LiveData<Event<Resource<PopularMovieResponse>>> =
        _searchLivedata

    val searchMovies: MutableLiveData<Resource<PopularMovieResponse>> = MutableLiveData()
    var searchMoviesPage = 1
    var searchMoviesResponse: PopularMovieResponse? = null

    val upcomingMoviesLivedata: MutableLiveData<Resource<PopularMovieResponse>> = MutableLiveData()
    var upcomingMoviesPage = 1
    var upcomingMoviesResponse: PopularMovieResponse? = null

    fun insertOrUpdateMovies(movie: PopularMovies) = viewModelScope.launch {
        repo.insertMovieIntoDatabase(movie)
    }

    fun deleteMovieFromDatabase(movie: PopularMovies) = viewModelScope.launch {
        repo.deleteMovieFromDatabase(movie)
    }

    fun getPopularMoviesFromApi() = viewModelScope.launch(Dispatchers.Unconfined) {
        val response = repo.getMoviesListFromApi()
        when (response.status) {
            Status.LOADING -> {
                movies.postValue(Resource.loading(null))
                _popularMoviesLiveData.postValue(Event(Resource.loading(null)))

            }
            Status.SUCCESS -> {
                movies.postValue(Resource.success(response.data!!.results!!))
                _popularMoviesLiveData.postValue(Event(response))
            }
            Status.ERROR -> {
                movies.postValue(Resource.error("error Fetching Movies", null))
                _popularMoviesLiveData.postValue(Event(Resource.error("error Fetching Movies", null)))
            }
        }
    }

    fun getMovies(): LiveData<Resource<List<PopularMovies>>> {
        return movies
    }

    fun searchForMovies(searchString: String) = viewModelScope.launch {

        val response =
            repo.searchForMovieFromRepo(searchString = searchString, page = searchMoviesPage)

        when (response.status) {
            Status.LOADING -> {
                _searchLivedata.postValue(Event(Resource.loading(null)))
            }
            Status.SUCCESS -> {
                if (searchString.isEmpty()) {
                    searchMoviesPage = 1
                    searchMoviesResponse = null
                    val error = "Please enter a movie name"
                    _searchLivedata.postValue(Event(Resource.error(error)))

                } else {
                    _searchLivedata.postValue(Event(response))
                }
            }
            Status.ERROR -> {
                searchMoviesPage = 1
                searchMoviesResponse = null
                val error = "Please enter a movie name"
                _searchLivedata.postValue(Event(Resource.error(error)))
            }
        }

    }

//    private fun handleSearchStringResponse(response: Response<PopularMovieResponse>): Resource<PopularMovieResponse> {
//        if (response.isSuccessful) {
//            response.body()?.let {
//                searchMoviesPage++
//                if (searchMoviesResponse == null) {
//                    searchMoviesResponse = it
//                } else {
//                    val oldList = searchMoviesResponse?.results
//                    val newList = it.results
//
//                    oldList?.addAll(newList)
//                }
//                return Resource.success(searchMoviesResponse ?: it)
//            }
//        }
//
//        return Resource.error("Please enter a movie name")
//    }


    fun getUpcomingMovies() = viewModelScope.launch {
        upcomingMoviesLivedata.postValue(Resource.loading(null))
        val response = repo.upcomingMovies(upcomingMoviesPage)

        upcomingMoviesLivedata.postValue(handleUpcomingMoviesResponse(response))

    }

    private fun handleUpcomingMoviesResponse(response: Response<PopularMovieResponse>): Resource<PopularMovieResponse>? {
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