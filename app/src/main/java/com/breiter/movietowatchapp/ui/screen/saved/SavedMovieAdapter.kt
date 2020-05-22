package com.breiter.movietowatchapp.ui.screen.saved

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.breiter.movietowatchapp.data.domain.Movie
import com.breiter.movietowatchapp.databinding.ListItemMovieBinding


class SavedMovieAdapter(private val listener: OnClickListener) :
    ListAdapter<Movie, SavedMovieAdapter.ViewHolder>(DiffCallback()) {
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

    fun getMovieAt(position: Int): Movie {
        return getItem(position)
    }

    class ViewHolder(private val binding: ListItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemMovieBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

        fun bind(movieItem: Movie) {
            binding.movie = movieItem
            binding.executePendingBindings()
        }
    }

    class OnClickListener(val listener: (movie: Movie) -> Unit) {
        fun onClick(movie: Movie) = listener(movie)
    }
}

class DiffCallback : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.id == newItem.id
    }
}
