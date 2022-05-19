package onepiece.whitebeard.movieappkotlin.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import onepiece.whitebeard.movieappkotlin.getOrAwaitValue
import onepiece.whitebeard.movieappkotlin.model.responses.PopularMovies
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named


@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class MoviesDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var db: MoviesDatabase
    lateinit var dao: MoviesDao

    @Before
    fun setUp() {
        hiltRule.inject()
        dao = db.getMoviesDao()
    }

    @After
    fun tearDown() {
        db.close()
    }


    @Test
    fun insertMovieIntoDatabase() = runTest {

        val genre: List<Int> = listOf(10, 20)
        val movie = PopularMovies(
            false,
            "backdropImage",
            genre,
            1,
            "en-Us",
            "Movie Title",
            "Overview String",
            10.8,
            "URL",
            "date string",
            "title",
            false,
            5.6,
            100
        )

        dao.insertOrUpdateMovies(movie)
        val allMovies = dao.getAllPopularMoviesFromDatabase().getOrAwaitValue()

        assertThat(allMovies).contains(movie)


    }

    @Test
    fun deleteMovieFromDatabase() = runTest {

        val genre: List<Int> = listOf(10, 20)
        val movie = PopularMovies(
            false,
            "backdropImage",
            genre,
            1,
            "en-Us",
            "Movie Title",
            "Overview String",
            10.8,
            "URL",
            "date string",
            "title",
            false,
            5.6,
            100
        )

        dao.insertOrUpdateMovies(movie)
        dao.deleteMovieFromDatabase(movie)
        val allMovies = dao.getAllPopularMoviesFromDatabase().getOrAwaitValue()

        assertThat(allMovies).doesNotContain(movie)


    }

}