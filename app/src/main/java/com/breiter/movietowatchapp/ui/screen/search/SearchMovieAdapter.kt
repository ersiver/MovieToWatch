package com.breiter.movietowatchapp.ui.screen.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.breiter.movietowatchapp.data.domain.Movie
import com.breiter.movietowatchapp.databinding.ListItemMovieBinding


class SearchMovieAdapter(
    private val listener: OnClickListener
) : ListAdapter<Movie, SearchMovieAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movieItem = getItem(position)
        holder.itemView.setOnClickListener {
            listener.onClick(movieItem)
        }
        holder.bind(movieItem)
    }

    interface OnClickListener {
        fun onClick(movieItem: Movie)
    }

    //Compare objects
    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Movie> =
            object : DiffUtil.ItemCallback<Movie>() {
                override fun areItemsTheSame(oldItem: Movie, newItem: Movie) =
                    oldItem == newItem

                override fun areContentsTheSame(oldItem: Movie, newItem: Movie) =
                    oldItem.id == newItem.id
            }
    }

    /**
     * View Holder for a [Movie] RecyclerView list item.
     */
    class ViewHolder(private val binding: ListItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            movieItem: Movie
        ) {
            binding.run {
                movie = movieItem
                executePendingBindings()
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    ListItemMovieBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}
