package onepiece.whitebeard.movieappkotlin.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.*
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import onepiece.whitebeard.movieappkotlin.MainCoroutineRule
import onepiece.whitebeard.movieappkotlin.api.ApiInterface
import onepiece.whitebeard.movieappkotlin.database.MoviesDao
import onepiece.whitebeard.movieappkotlin.model.responses.PopularMovieResponse
import onepiece.whitebeard.movieappkotlin.other.Resource
import onepiece.whitebeard.movieappkotlin.repository.MoviesRepositoryImpl
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.*
import org.mockito.Mockito.*
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class MoviesViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()


    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var testRepo: MoviesRepositoryImpl
    lateinit var mockWebServer: MockWebServer
    lateinit var apiInterface: ApiInterface
    lateinit var gson: Gson
    lateinit var dao: MoviesDao


    private lateinit var viewModel: MoviesViewModel

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

        testRepo = MoviesRepositoryImpl(dao, apiInterface)
        testRepo = mock(MoviesRepositoryImpl::class.java)
        viewModel = MoviesViewModel(testRepo)
//        viewModel.popularMoviesLiveData.observeForever(eventObserver)
//        viewModel.searchLivedata.observeForever(eventObserver)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
//        viewModel.searchLivedata.removeObserver(eventObserver)
//        viewModel.popularMoviesLiveData.removeObserver(eventObserver)
    }


    @Test
    fun `get popular movie api test, success state`() = runBlocking {

        testRepo = mock(MoviesRepositoryImpl::class.java)
        // Given : Writing the response type which we will be getting from repository method
        val popularMovieResponse = PopularMovieResponse("", 0, mutableListOf(), 0, 0)
        val mockResponse = MockResponse()
        mockWebServer.enqueue(mockResponse.setBody(gson.toJson(popularMovieResponse)))


        `when`(testRepo.getMoviesListFromApi())
            .thenReturn(Resource.success(popularMovieResponse))

        viewModel.getPopularMoviesFromApi()

        viewModel.popularMoviesLiveData.observeForever {
            it.getContentIfNotHandled()?.let { result ->
                assertThat(result).isEqualTo(Resource.success(popularMovieResponse))
            }

        }


    }

    @Test
    fun `search movie without search query, no movies to search`() = runTest {

        val popularMovieResponse = PopularMovieResponse("", 0, mutableListOf(), 0, 0)
        val mockResponse = MockResponse()
        mockWebServer.enqueue(mockResponse.setBody(gson.toJson(popularMovieResponse)))


        `when`(testRepo.searchForMovieFromRepo("", 1))
            .thenReturn(Resource.error("Please enter a movie name", null))

        viewModel.searchForMovies("")

        viewModel.searchLivedata.observeForever {
            it.getContentIfNotHandled()?.let { result ->
                assertThat(result).isEqualTo(Resource.error("Please enter a movie name", null))
            }
        }


    }

    @Test
    fun `search movie correctly, passes`() = runTest {

        val popularMovieResponse = PopularMovieResponse("", 0, mutableListOf(), 0, 0)
        val mockResponse = MockResponse()
        mockWebServer.enqueue(mockResponse.setBody(gson.toJson(popularMovieResponse)))

        `when`(testRepo.searchForMovieFromRepo("Spiderman", 1))
            .thenReturn(Resource.success(popularMovieResponse))

        viewModel.searchForMovies("Spiderman")

        viewModel.searchLivedata.observeForever {
            it.getContentIfNotHandled()?.let { result ->
                assertThat(result).isEqualTo(Resource.success(popularMovieResponse))
            }
        }


    }

    @Test
    fun `get upcoming movie api test, success state`() = runBlocking {

        testRepo = mock(MoviesRepositoryImpl::class.java)
        // Given : Writing the response type which we will be getting from repository method
        val popularMovieResponse = PopularMovieResponse("", 0, mutableListOf(), 0, 0)
        val mockResponse = MockResponse()
        mockWebServer.enqueue(mockResponse.setBody(gson.toJson(popularMovieResponse)))


        `when`(testRepo.upcomingMovies(1))
            .thenReturn(Response.success(popularMovieResponse))

        viewModel.getUpcomingMovies()

        viewModel.upcomingMoviesLivedata.observeForever { result ->
                assertThat(result).isEqualTo(Resource.success(popularMovieResponse))
            }

        }

}

