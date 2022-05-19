package onepiece.whitebeard.movieappkotlin.repository

import android.util.Log
import androidx.lifecycle.LiveData
import onepiece.whitebeard.movieappkotlin.api.ApiInterface
import onepiece.whitebeard.movieappkotlin.database.MoviesDao
import onepiece.whitebeard.movieappkotlin.model.responses.PopularMovieResponse
import onepiece.whitebeard.movieappkotlin.model.responses.PopularMovies
import onepiece.whitebeard.movieappkotlin.other.Resource
import retrofit2.Response
import javax.inject.Inject

class MoviesRepository @Inject constructor(
    private val moviesDao: MoviesDao,
    private val apiInterface: ApiInterface
) : MovieRepositotyInterface {
    //
    var searchMoviesPage = 1
    var searchMoviesResponse: PopularMovieResponse? = null

    override suspend fun insertMovieIntoDatabase(movie: PopularMovies) {
        return moviesDao.insertOrUpdateMovies(movie)
    }

    override suspend fun deleteMovieFromDatabase(movie: PopularMovies) {
        return moviesDao.deleteMovieFromDatabase(movie)
    }

    override fun observeMoviesLiveData(): LiveData<List<PopularMovies>> {
        return moviesDao.getAllPopularMoviesFromDatabase()
    }

    fun getMovies(): LiveData<List<PopularMovies>> {
        return moviesDao.getAllPopularMoviesFromDatabase()
    }

    override suspend fun getMoviesListFromApi(): Resource<PopularMovieResponse> {
        return try {
            val response = apiInterface.getPopularMoviesApi()

            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("Network error", null)
            } else {
                Resource.error("Network error", null)
            }
        } catch (e: Exception) {
            return Resource.error("No Internet Connection", null)

        }
    }

    override suspend fun searchForMovieFromRepo(
        searchString: String,
        page: Int
    ): Resource<PopularMovieResponse> {
        return try {
            if (searchString.isEmpty()) {
                return Resource.error("No Search Query", null)
            } else {
                val response = apiInterface.getPopularMoviesApi()
                if (response.isSuccessful) {
                    response.body()?.let {
                        searchMoviesPage++
                        if (searchMoviesResponse == null) {
                            searchMoviesResponse = it
                        } else {
                            val oldList = searchMoviesResponse?.results
                            val newList = it.results

                            oldList?.addAll(newList!!)
                        }
                        return@let Resource.success(searchMoviesResponse ?: it)
                    } ?: Resource.error("Network error", null)
                } else {
                    Resource.error("Network error", null)
                }
            }

        } catch (e: Exception) {
            return Resource.error("No Internet Connection", null)

        }
    }

    override suspend fun upcomingMovies(page: Int): Response<PopularMovieResponse> {
        return apiInterface.upcomingMoviesApi(page = page)
    }

}