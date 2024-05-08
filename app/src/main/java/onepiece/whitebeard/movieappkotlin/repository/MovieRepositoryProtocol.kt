package onepiece.whitebeard.movieappkotlin.repository

import androidx.lifecycle.LiveData
import onepiece.whitebeard.movieappkotlin.model.responses.PopularMovieResponse
import onepiece.whitebeard.movieappkotlin.model.responses.PopularMovies
import onepiece.whitebeard.movieappkotlin.other.Resource
import retrofit2.Response

interface MovieRepositoryProtocol {


    suspend fun insertMovieIntoDatabase(movie:PopularMovies)

    suspend fun deleteMovieFromDatabase(movie:PopularMovies)

    fun observeMoviesLiveData(): LiveData<List<PopularMovies>>

    suspend fun getMoviesListFromApi() : Resource<PopularMovieResponse>

    suspend fun searchForMovieFromRepo(searchString : String,page : Int) : Resource<PopularMovieResponse>

    suspend fun upcomingMovies(page:Int) : Response<PopularMovieResponse>
}