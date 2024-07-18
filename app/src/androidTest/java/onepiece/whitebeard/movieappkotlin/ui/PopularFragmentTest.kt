package onepiece.whitebeard.movieappkotlin.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.MediumTest
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import onepiece.whitebeard.movieappkotlin.R
import onepiece.whitebeard.movieappkotlin.adapters.PopularMoviesAdapter
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
class PopularFragmentTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val mActivityTestRule: ActivityScenarioRule<MainActivity> = ActivityScenarioRule(MainActivity::class.java)

    lateinit var viewmodel: MoviesViewModel

    @Before
    fun setUp() {
        hiltRule.inject()
        viewmodel = MoviesViewModel(FakeMoviesRepository())
    }

    @Test
    fun launchPopularFragment() {
        val navController = Mockito.mock(NavController::class.java)

        launchFragmentInHiltContainer<PopularFragment>() {
            Navigation.setViewNavController(requireView(), navController)
        }
    }

    @Test
    fun clickMovieItem_NavigateToMovieDetailsActivity() {
        val navController = Mockito.mock(NavController::class.java)

        var testViewModel: MoviesViewModel? = null
        launchFragmentInHiltContainer<PopularFragment>() {
            testViewModel = viewmodel
            Navigation.setViewNavController(requireView(), navController)
            try {
                Thread.sleep(3000)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

        }

        // Testing clicks on recyclerview item with RecyclerViewActions
        // Needs espresso contrib dependency... added in build.gradle
        Espresso.onView(ViewMatchers.withId(R.id.popularMovieRv)).perform(
            RecyclerViewActions.actionOnItemAtPosition<PopularMoviesAdapter.PopularMealViewHolder>(
                0,
                ViewActions.click()
            )
        )
        var movieItem = PopularMovies()
        testViewModel?.movies?.observeForever{ resource ->
            if(resource.status == Status.SUCCESS){
                movieItem = resource.data?.get(0)!!
            }
        }
        assertThat(movieItem).isInstanceOf(PopularMovies::class.java)

        testViewModel?.movies?.removeObserver{}
    }


}