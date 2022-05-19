package onepiece.whitebeard.movieappkotlin.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import onepiece.whitebeard.movieappkotlin.constants.Constant
import onepiece.whitebeard.movieappkotlin.databinding.UpcomingItemBinding
import onepiece.whitebeard.movieappkotlin.model.responses.PopularMovies


class UpcomingMoviesAdapter() : RecyclerView.Adapter<UpcomingMoviesAdapter.UpcomingMovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpcomingMovieViewHolder {
        return UpcomingMovieViewHolder(
            UpcomingItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: UpcomingMovieViewHolder, position: Int) {
        val movie = differ.currentList[position]
        val imageUrl = Constant.ImageBaseUrl + movie.poster_path
        Glide.with(holder.itemView)
            .load(imageUrl)
            .into(holder.binding.imageFirst)

        val image2Url = Constant.ImageBaseUrl + movie.backdrop_path
        Glide.with(holder.itemView)
            .load(image2Url)
            .into(holder.binding.imageview)

        holder.binding.tvMovie.text = movie.title
        holder.binding.tvDate.text = movie?.release_date

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

    class UpcomingMovieViewHolder(var binding: UpcomingItemBinding) :
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