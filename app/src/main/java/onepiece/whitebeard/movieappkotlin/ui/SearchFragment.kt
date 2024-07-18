package onepiece.whitebeard.movieappkotlin.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import onepiece.whitebeard.movieappkotlin.R
import onepiece.whitebeard.movieappkotlin.adapters.SearchMoviesAdapter
import onepiece.whitebeard.movieappkotlin.constants.Constant
import onepiece.whitebeard.movieappkotlin.databinding.FragmentSearchBinding
import onepiece.whitebeard.movieappkotlin.model.responses.PopularMovies
import onepiece.whitebeard.movieappkotlin.util.Status
import onepiece.whitebeard.movieappkotlin.viewmodel.MoviesViewModel

class SearchFragment : Fragment(R.layout.fragment_search) {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var movieAdapter: SearchMoviesAdapter

    val moviesViewModel: MoviesViewModel by lazy {
        ViewModelProvider(requireActivity())[MoviesViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()

        /*Searching By Edittext and Getting Movies From API*/
        var job: Job? = null
        binding.etSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(1000L)
                editable?.let {
                    if (editable.toString().isNotEmpty()) {
                        moviesViewModel.searchForMovies(editable.toString())
                    }else{
                        moviesViewModel.searchForMovies("")
                    }
                }
            }
        }

        observeSearchLiveData()
        // Adapter Click and Go to Articles Fragment
        movieAdapter.setOnItemClickListener { movie ->
            val intent = Intent(context, MovieDetailsActivity::class.java)
            intent.putExtra("MOVIE_DETAIL", movie)
            startActivity(intent)

        }
    }

    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.GONE
        isLoading = false
    }

    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    private fun observeSearchLiveData() {
        moviesViewModel.searchLivedata.observe(viewLifecycleOwner) { result ->
            result.getContentIfNotHandled()?.let { response ->
                when (response.status) {
                    Status.LOADING -> {
                        showProgressBar()
                    }

                    Status.SUCCESS -> {
                        val moviesList: List<PopularMovies> = response.data!!.results!!
                        moviesList.forEach { popularMovies ->
                            moviesViewModel.insertOrUpdateMovies(popularMovies)
                        }
                        movieAdapter.differ.submitList(moviesList as ArrayList<PopularMovies>)

                        val totalPages = response.data.total_results / Constant.QUERY_PAGE_SIZE + 2
                        isLastPage = moviesViewModel.searchMoviesPage == totalPages
                        if (isLastPage) {
                            binding.rvSearchNews.setPadding(0, 0, 0, 0)
                        }

                        hideProgressBar()
                    }

                    Status.ERROR -> {
                        Snackbar.make(
                            binding.root,
                            response.message ?: "An unknown error occured.",
                            Snackbar.LENGTH_LONG
                        ).show()
                        hideProgressBar()

                    }
                }
            }


        }
    }

    //FOR PAGINATION
    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    private val onScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }


        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as GridLayoutManager
            val firstItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemsCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstItemPosition + visibleItemsCount >= totalItemCount
            val isNotAtBeginning = firstItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= Constant.QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage &&
                    isAtLastItem &&
                    isNotAtBeginning &&
                    isTotalMoreThanVisible &&
                    isScrolling

            if (shouldPaginate) {
                moviesViewModel.searchForMovies(binding.etSearch.text.toString())
                isScrolling = false
            }


        }
    }

    private fun setUpRecyclerView() {
        movieAdapter = SearchMoviesAdapter()
        binding.rvSearchNews.apply {
            adapter = movieAdapter
            layoutManager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
            addOnScrollListener(this@SearchFragment.onScrollListener)
        }
    }

}