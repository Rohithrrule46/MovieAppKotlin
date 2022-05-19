package onepiece.whitebeard.movieappkotlin.api

import onepiece.whitebeard.movieappkotlin.constants.Constant
import onepiece.whitebeard.movieappkotlin.model.responses.PopularMovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("movie/popular?")
    suspend fun getPopularMoviesApi(
        @Query("api_key") apiKey: String = Constant.apiKey,
    ): Response<PopularMovieResponse>

    @GET("search/movie?")
    suspend fun searchForMoviesApi(
        @Query("api_key") apiKey: String = Constant.apiKey,
        @Query("language") lang: String = "en-US",
        @Query("query") searchString: String,
        @Query("page") page: Int = 1
    ): Response<PopularMovieResponse>

    @GET("movie/upcoming?")
    suspend fun upcomingMoviesApi(
        @Query("api_key") apiKey: String = Constant.apiKey,
        @Query("language") lang: String = "en-US",
        @Query("page") page: Int = 1
    ): Response<PopularMovieResponse>

}