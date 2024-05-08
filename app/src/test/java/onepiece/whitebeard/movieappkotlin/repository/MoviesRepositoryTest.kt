package onepiece.whitebeard.movieappkotlin.repository

import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import onepiece.whitebeard.movieappkotlin.api.ApiInterface
import onepiece.whitebeard.movieappkotlin.database.MoviesDao
import onepiece.whitebeard.movieappkotlin.model.responses.PopularMovieResponse
import onepiece.whitebeard.movieappkotlin.other.Resource
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MoviesRepositoryTest {

    lateinit var moviesRepository: MoviesRepositoryImpl
    lateinit var mockWebServer: MockWebServer
    lateinit var apiInterface: ApiInterface
    lateinit var gson: Gson
    lateinit var dao: MoviesDao

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        gson = Gson()

        mockWebServer = MockWebServer()
        apiInterface = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build().create(ApiInterface::class.java)
        dao = mock(MoviesDao::class.java)
        moviesRepository = MoviesRepositoryImpl(dao, apiInterface)
    }


    @Test
    fun `get popular movie from repository test`() = runBlocking {

        moviesRepository = mock(MoviesRepositoryImpl::class.java)
        val mockResponse = MockResponse()

        // Given : Writing the response type which we will be getting from repository method
        val popularMovieResponse = PopularMovieResponse("", 0, mutableListOf(), 0, 0)

        mockWebServer.enqueue(mockResponse.setBody(gson.toJson(popularMovieResponse)))

        // When
        Mockito.`when`(moviesRepository.getMoviesListFromApi())
            .thenReturn(Resource.success(popularMovieResponse))

        // Then
        val response = apiInterface.getPopularMoviesApi()

        // Assertion
        val expected = Resource.success(popularMovieResponse).data
        val actual = response.body()
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `get searched movie from repository without search string test`() = runBlocking {

        moviesRepository = mock(MoviesRepositoryImpl::class.java)
        val mockResponse = MockResponse()

        // Given : Writing the response type which we will be getting from repository method
        val popularMovieResponse = PopularMovieResponse("", 0, mutableListOf(), 0, 0)

        mockWebServer.enqueue(mockResponse.setBody(gson.toJson(popularMovieResponse)))

        // When
        Mockito.`when`(moviesRepository.searchForMovieFromRepo("", 1))
            .thenReturn(Resource.error("No Search Query", popularMovieResponse))

        // Then
        val response = apiInterface.searchForMoviesApi(searchString = "")

        // Assertion
        val expected = Resource.error("No Search Query", popularMovieResponse)
        val actual = Resource.error("No Search Query", response.body())
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `get searched movie from repository with search string test`() = runBlocking {

        moviesRepository = mock(MoviesRepositoryImpl::class.java)
        val mockResponse = MockResponse()

        // Given : Writing the response type which we will be getting from repository method
        val popularMovieResponse = PopularMovieResponse("", 0, mutableListOf(), 0, 0)

        mockWebServer.enqueue(mockResponse.setBody(gson.toJson(popularMovieResponse)))

        // When
        Mockito.`when`(moviesRepository.searchForMovieFromRepo("spiderman", 1))
            .thenReturn(Resource.success(popularMovieResponse))

        // Then
        val response = apiInterface.searchForMoviesApi(searchString = "spiderman")

        // Assertion
        val expected = Resource.success(popularMovieResponse).data
        val actual = response.body()
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `get upcoming movie from repository test`() = runBlocking {

        moviesRepository = mock(MoviesRepositoryImpl::class.java)
        val mockResponse = MockResponse()

        // Given : Writing the response type which we will be getting from repository method
        val popularMovieResponse = PopularMovieResponse("", 0, mutableListOf(), 0, 0)

        mockWebServer.enqueue(mockResponse.setBody(gson.toJson(popularMovieResponse)))

        // When
        Mockito.`when`(moviesRepository.upcomingMovies(1))
            .thenReturn(Response.success(popularMovieResponse))

        // Then
        val response = apiInterface.upcomingMoviesApi()

        // Assertion
        val expected = Resource.success(popularMovieResponse).data
        val actual = response.body()
        assertThat(actual).isEqualTo(expected)
    }


    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

}