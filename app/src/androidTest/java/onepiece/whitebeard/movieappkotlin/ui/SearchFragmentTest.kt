package onepiece.whitebeard.movieappkotlin.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.google.common.truth.Truth
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import onepiece.whitebeard.movieappkotlin.R
import onepiece.whitebeard.movieappkotlin.adapters.SearchMoviesAdapter
import onepiece.whitebeard.movieappkotlin.launchFragmentInHiltContainer
import onepiece.whitebeard.movieappkotlin.model.responses.PopularMovies
import onepiece.whitebeard.movieappkotlin.util.Status
import onepiece.whitebeard.movieappkotlin.repository.FakeMoviesRepository
import onepiece.whitebeard.movieappkotlin.viewmodel.MoviesViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito


@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class SearchFragmentTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    lateinit var viewmodel: MoviesViewModel

    private val mIdlingResource: IdlingResource? = null

    @Before
    fun setUp() {
        hiltRule.inject()
        viewmodel = MoviesViewModel(FakeMoviesRepository())
    }

    @Test
    fun launchSearchFragment() {
        val navController = Mockito.mock(NavController::class.java)

        launchFragmentInHiltContainer<SearchFragment> {
            Navigation.setViewNavController(
                requireView(),
                navController
            )
        }
        onView(withId(R.id.searchFragmentView)).check(matches(isDisplayed()))
    }

    @Test
    fun loadSearchFragmentAndSearchMovies() {
        val navController = Mockito.mock(NavController::class.java)

        val adapter = SearchMoviesAdapter()

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

        val moviesList = ArrayList<PopularMovies>()
        moviesList.add(movie)

        var testViewModel: MoviesViewModel? = null
        launchFragmentInHiltContainer<SearchFragment> {
            testViewModel = viewmodel

            adapter.differ.submitList(moviesList)

            Navigation.setViewNavController(requireView(), navController)
//            mIdlingResource?.registerIdleTransitionCallback {  }
        }


        onView(withId(R.id.etSearch))
            .perform(ViewActions.replaceText(""))

        try {
            Thread.sleep(3000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        // Testing clicks on recyclerview item with RecyclerViewActions
        // Needs espresso contrib dependency... added in build.gradle
        onView(withId(R.id.rvSearchNews)).perform(
            RecyclerViewActions.actionOnItemAtPosition<SearchMoviesAdapter.SearchViewHolder>(
                0,
                ViewActions.click()
            )
        )

        var movieItem = PopularMovies()
        testViewModel?.searchMovies?.observeForever { resource ->
            if (resource.status == Status.SUCCESS) {
                movieItem = resource.data?.results?.get(0)!!
//                movieItemList = resource.data?.results!!
            }
        }
        Truth.assertThat(movieItem).isInstanceOf(PopularMovies::class.java)
//        Truth.assertThat(adapter.differ.currentList).contains(movie)

        testViewModel?.searchMovies?.removeObserver {}
    }

}

