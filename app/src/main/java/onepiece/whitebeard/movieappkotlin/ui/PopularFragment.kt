package onepiece.whitebeard.movieappkotlin.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import onepiece.whitebeard.movieappkotlin.adapters.PopularMoviesAdapter
import onepiece.whitebeard.movieappkotlin.databinding.FragmentPopularBinding
import onepiece.whitebeard.movieappkotlin.model.responses.PopularMovies
import onepiece.whitebeard.movieappkotlin.other.Status
import onepiece.whitebeard.movieappkotlin.viewmodel.MoviesViewModel

class PopularFragment : Fragment() {

    private lateinit var binding: FragmentPopularBinding
    private val moviesViewModel: MoviesViewModel by lazy {
        ViewModelProvider(requireActivity())[MoviesViewModel::class.java]
    }

    private lateinit var movieAdapter: PopularMoviesAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movieAdapter = PopularMoviesAdapter()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPopularBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()
        moviesViewModel.getPopularMoviesFromApi()
        observeMovieLiveData()
        movieAdapterClick()

    }

    private fun setUpRecyclerView() {
        binding.popularMovieRv.apply {
            layoutManager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
            adapter = movieAdapter
        }
    }


    private fun observeMovieLiveData() {
        moviesViewModel.popularMoviesLiveData.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        val moviesList: List<PopularMovies> = result.data!!.results!!
                        moviesList.forEach { popularMovies ->
                            moviesViewModel.insertOrUpdateMovies(popularMovies)
                        }
                        movieAdapter.setMoviesAdapter(moviesList as ArrayList<PopularMovies>)
                        binding.progressBar.visibility = View.GONE
                    }
                    Status.ERROR -> {
                        Snackbar.make(
                            binding.root,
                            result.message ?: "An unknown error occurred.",
                            Snackbar.LENGTH_LONG
                        ).show()
                        binding.progressBar.visibility = View.GONE
                    }
                    Status.LOADING -> {
                        binding.progressBar.visibility = View.VISIBLE

                    }
                }

            }
        })
    }

    private fun movieAdapterClick(){
        movieAdapter.onItemClick = {
            val intent = Intent(context, MovieDetailsActivity::class.java)
            intent.putExtra("MOVIE_DETAIL", it)
            startActivity(intent)
        }
    }


}