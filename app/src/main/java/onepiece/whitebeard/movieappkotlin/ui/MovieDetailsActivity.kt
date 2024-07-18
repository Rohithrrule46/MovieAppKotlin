package onepiece.whitebeard.movieappkotlin.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import onepiece.whitebeard.movieappkotlin.constants.Constant
import onepiece.whitebeard.movieappkotlin.databinding.ActivityMovieDetailsBinding
import onepiece.whitebeard.movieappkotlin.model.responses.PopularMovies

class MovieDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovieDetailsBinding
    private var movieObj: PopularMovies? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getIntents()

        if(movieObj!=null){
            binding.tvDate.text = movieObj?.release_date
            binding.tvMovie.text = movieObj?.title
            binding.tvDes.text = movieObj?.overview
            binding.tvStatus.text = movieObj?.vote_average.toString()

            val backImage = Constant.IMAGE_BASE_URL + movieObj?.backdrop_path
            val frontImage = Constant.IMAGE_BASE_URL + movieObj?.poster_path

            Glide.with(this).load(backImage).into(binding.imageview)
            Glide.with(this).load(frontImage).into(binding.imageFirst)
        }

    }
    private fun getIntents() {
        val intent = intent.extras
        movieObj = intent?.getSerializable("MOVIE_DETAIL") as PopularMovies?
    }

}