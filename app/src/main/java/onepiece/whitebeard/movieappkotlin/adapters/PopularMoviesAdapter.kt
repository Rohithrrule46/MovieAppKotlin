
package onepiece.whitebeard.movieappkotlin.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import onepiece.whitebeard.movieappkotlin.constants.Constant
import onepiece.whitebeard.movieappkotlin.databinding.PopularMovieItemBinding
import onepiece.whitebeard.movieappkotlin.model.responses.PopularMovies


class PopularMoviesAdapter() : RecyclerView.Adapter<PopularMoviesAdapter.PopularMealViewHolder>() {

    lateinit var onItemClick: ((PopularMovies) -> Unit)
    private var moviesList = ArrayList<PopularMovies>()


    fun setMoviesAdapter(movies: ArrayList<PopularMovies>) {
        this.moviesList = movies
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMealViewHolder {
        return PopularMealViewHolder(
            PopularMovieItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PopularMealViewHolder, position: Int) {
        val imageUrl = Constant.ImageBaseUrl + moviesList.get(position).poster_path
        Glide.with(holder.itemView)
            .load(imageUrl)
            .into(holder.binding.imgCategoryMeal)

        holder.binding.movieNameTv.text = moviesList.get(position).original_title

        holder.itemView.setOnClickListener {
            onItemClick.invoke(moviesList[position])
        }
    }

    override fun getItemCount(): Int {
        return moviesList.size
    }

    class PopularMealViewHolder(var binding: PopularMovieItemBinding) :
        RecyclerView.ViewHolder(binding.root)


}