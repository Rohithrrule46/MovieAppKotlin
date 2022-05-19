package onepiece.whitebeard.movieappkotlin.api

import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import onepiece.whitebeard.movieappkotlin.model.responses.PopularMovieResponse
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.MockitoAnnotations
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitApiTest {

    lateinit var mockWebServer: MockWebServer
    lateinit var retrofitInstance: ApiInterface
    lateinit var gson: Gson

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        gson = Gson()
        mockWebServer = MockWebServer()
        retrofitInstance = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build().create(ApiInterface::class.java)
    }

    @Test
    fun `get all movies api test`() = runBlocking {
        val mockResponse = MockResponse()
        mockWebServer.enqueue(mockResponse.setBody("{}"))
        val response = retrofitInstance.getPopularMoviesApi()
        val request = mockWebServer.takeRequest()

        assertThat(request.path).isEqualTo("/movie/popular?&api_key=753a30ffa773c04498450eaf8c0d8e16")
        assertThat(response.body()).isEqualTo(PopularMovieResponse(null, 0, null, 0, 0))
    }

    @Test
    fun `get search movies api test`() = runBlocking {
        val mockResponse = MockResponse()
        mockWebServer.enqueue(mockResponse.setBody("{}"))
        val response = retrofitInstance.searchForMoviesApi(searchString = "spiderman")
        val request = mockWebServer.takeRequest()

        assertThat(request.path).isEqualTo(
            "/search/movie?&api_key=753a30ffa773c04498450eaf8c0d8e16&language=en-US&query=spiderman&page=1"
        )
        assertThat(response.body()).isEqualTo(PopularMovieResponse(null, 0, null, 0, 0))
    }

    @Test
    fun `get upcoming movies api test`() = runBlocking {
        val mockResponse = MockResponse()
        mockWebServer.enqueue(mockResponse.setBody("{}"))
        val response = retrofitInstance.upcomingMoviesApi()
        val request = mockWebServer.takeRequest()

        assertThat(request.path).isEqualTo(
            "/movie/upcoming?&api_key=753a30ffa773c04498450eaf8c0d8e16&language=en-US&page=1"
        )
        assertThat(response.body()).isEqualTo(PopularMovieResponse(null, 0, null, 0, 0))
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

}