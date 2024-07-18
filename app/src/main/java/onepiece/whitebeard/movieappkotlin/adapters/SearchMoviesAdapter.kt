package onepiece.whitebeard.movieappkotlin.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import onepiece.whitebeard.movieappkotlin.constants.Constant
import onepiece.whitebeard.movieappkotlin.databinding.SearchMovieItemBinding
import onepiece.whitebeard.movieappkotlin.model.responses.PopularMovies


class SearchMoviesAdapter() : RecyclerView.Adapter<SearchMoviesAdapter.SearchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(
            SearchMovieItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val movie = differ.currentList[position]
        val imageUrl = Constant.IMAGE_BASE_URL + movie.poster_path
        Glide.with(holder.itemView)
            .load(imageUrl)
            .into(holder.binding.imgCategoryMeal)

        holder.binding.movieNameTv.text = movie.original_title

        holder.itemView.setOnClickListener {
            // Checking if itemClickListener Not Null
            onItemClickListener?.let {
                it(movie)
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    class SearchViewHolder(var binding: SearchMovieItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    // DiffUtil replaces NotifyDataSetChanged,
    // Because it only notifies if data is changed making it faster.
    private val differCallback = object : DiffUtil.ItemCallback<PopularMovies>() {
        override fun areItemsTheSame(oldItem: PopularMovies, newItem: PopularMovies): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PopularMovies, newItem: PopularMovies): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, differCallback)

    // Return Nothing- Use -> Unit
    private var onItemClickListener: ((PopularMovies) -> Unit)? = null

    fun setOnItemClickListener(listener: (PopularMovies) -> Unit) {
        onItemClickListener = listener
    }
}