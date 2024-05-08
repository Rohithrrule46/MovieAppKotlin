package onepiece.whitebeard.movieappkotlin.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import onepiece.whitebeard.movieappkotlin.api.ApiInterface
import onepiece.whitebeard.movieappkotlin.database.MoviesDao
import onepiece.whitebeard.movieappkotlin.model.responses.PopularMovieResponse
import onepiece.whitebeard.movieappkotlin.model.responses.PopularMovies
import onepiece.whitebeard.movieappkotlin.other.Resource
import org.mockito.Mockito.mock
import retrofit2.Response
import javax.inject.Inject

class FakeMoviesRepository: MovieRepositoryProtocol {

    private val moviesItem = mutableListOf<PopularMovies>()

    private val observableMovieItem = MutableLiveData<List<PopularMovies>>()

    private var shouldReturnNetworkError = false

    fun setNetworkError(value: Boolean) {
        shouldReturnNetworkError = value
    }

    override suspend fun insertMovieIntoDatabase(movie: PopularMovies) {
        moviesItem.add(movie)
    }

    override suspend fun deleteMovieFromDatabase(movie: PopularMovies) {
        moviesItem.remove(movie)
    }

    override fun observeMoviesLiveData(): LiveData<List<PopularMovies>> {
        return observableMovieItem
    }

    override suspend fun getMoviesListFromApi(): Resource<PopularMovieResponse> {
//        return try {
//            val response = apiInterface.getPopularMoviesApi()
//            if (response.isSuccessful) {
//                response.body()?.let {
//                    return@let Resource.success(it)
//                } ?: Resource.error("Network error", null)
//            } else {
//                Resource.error("Network error", null)
//            }
//        } catch (e: Exception) {
//            return Resource.error("No Internet Connection", null)
//
//        }

        return if (shouldReturnNetworkError) {
            Resource.error("error", null)
        } else {
            Resource.success(PopularMovieResponse("",0, mutableListOf(),0,0))
        }
    }

    override suspend fun searchForMovieFromRepo(
        searchString: String,
        page: Int
    ): Resource<PopularMovieResponse> {
        return if (shouldReturnNetworkError) {
            Resource.error("error", null)
        } else {
            Resource.success(PopularMovieResponse("",0, mutableListOf(),0,0))
        }
    }

    override suspend fun upcomingMovies(page: Int): Response<PopularMovieResponse> {
        TODO("Not yet implemented")
    }
}