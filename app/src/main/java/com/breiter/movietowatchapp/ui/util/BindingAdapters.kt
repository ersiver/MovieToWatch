package com.breiter.movietowatchapp.ui.util

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.breiter.movietowatchapp.R
import com.breiter.movietowatchapp.data.domain.Movie
import com.breiter.movietowatchapp.data.util.Constants
import com.breiter.movietowatchapp.ui.screen.saved.SavedMovieAdapter
import com.breiter.movietowatchapp.ui.screen.saved.SavedMovieSwipeCallback
import com.breiter.movietowatchapp.ui.screen.search.NetworkStatus
import com.breiter.movietowatchapp.ui.screen.search.SearchMovieAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.util.*
import com.breiter.movietowatchapp.ui.screen.saved.SavedMovieSwipeCallback.SwipeListener
import com.breiter.movietowatchapp.ui.screen.saved.SavedMovieSwipeCallback.SwipeButton


/**
 * Binding adapter used to to display images from URL using Glide.
 */
@BindingAdapter("imageUrl")
fun ImageView.bindImage(posterPath: String?) {
    if (posterPath == null)
        setImageResource(R.drawable.ic_logo)
    else {
        val imgUri = (Constants.IMAGE_URL + posterPath).toUri().buildUpon().scheme("https").build()
        Glide.with(context)
            .load(imgUri)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image)
            )
            .into(this)
    }
}

/**
 * Binding adapter used to extract a year from the full release date and display in [TextView]
 */
@BindingAdapter("dateFormatter")
fun TextView.setDateFormatted(date: String?) {
    text = if (date != null) {
        if (date.length > 4)
            date.substring(0, 4) else date
    } else
        date
}

/**
 * Binding adapter used to submit list of saved movies to the [SavedMovieAdapter].
 */
@BindingAdapter("savedMovieListData")
fun RecyclerView.bindRecyclerView(data: List<Movie>?) {
    val adapter = adapter as SavedMovieAdapter
    adapter.submitList(data)
}

/**
 * Binding adapter used to submit list of searched movies to the  [SearchMovieAdapter]
 * once data is available. If [EditText] is cleared the empty list is submitted.
 */
@BindingAdapter("searchedMovieListData", "clearData")
fun RecyclerView.bindSearchMovieRecyclerView(data: List<Movie>?, clearData: Boolean) {
    val adapter = this.adapter as SearchMovieAdapter
    if (clearData)
        adapter.submitList(emptyList())
    else
        adapter.submitList(data)
}

/**
 * Binding adapter used to show the img indicating an error
 * once data is not available and hide it otherwise.
 */
@BindingAdapter("visibility")
fun ImageView.bindErrorImage(status: NetworkStatus?) {
    when (status) {
        NetworkStatus.DONE -> {
            visibility = View.GONE
        }
        NetworkStatus.ERROR -> {
            visibility = View.VISIBLE
        }
    }
}

/**
 * Binding adapter used to show [RecyclerView] once data
 * is available and hide it on error.
 */
@BindingAdapter("visibility")
fun RecyclerView.setVisibility(status: NetworkStatus?) {
    when (status) {
        NetworkStatus.DONE -> {
            visibility = View.VISIBLE
        }
        NetworkStatus.ERROR -> {
            visibility = View.GONE
        }
    }
}

/**
 * Binding adapter used to clear user input from [EditText]
 */
@BindingAdapter("clearText")
fun EditText.clearEditText(isClicked: Boolean) {
    if (isClicked) text.clear()
}

/**
 * Binding adapter used to to display genres from the list of String.
 * If there is more then one genre, then the come precedes the next word.
 */
@BindingAdapter("genreFormatted")
fun TextView.setGenreFormatted(genreList: List<String>?) {
    val builder = StringBuilder("")

    if (genreList != null) {
        for (genre in genreList) {
            builder.append(genre.toLowerCase(Locale.ROOT))

            if (genreList.indexOf(genre) == genreList.lastIndex)
                break

            builder.append(", ")
        }
    }
    text = builder
}

/**
 * Binding adapter used to load an icon to the [ImageView] enabling
 * saving a movie and a different icon, if movie is already saved.
 */
@BindingAdapter("src")
fun ImageView.bindFavouriteIcon(isSaved: Boolean) {
    if (isSaved)
        setImageResource(R.drawable.ic_favorite)
    else
        setImageResource(R.drawable.ic_add_favourite)
}

/**
 * Binding adapter used to showing keyboard for search fragment.
 */
@BindingAdapter("showSoftInput")
fun View.showKeyboard(isTyping: Boolean) {
    val inputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    if (isTyping)
        inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

/**
 * Binding adapter used to implement a swipe menu.
 * Each button should have been assigned to a different listener.
 * Listener functionality is overridden in the SavedFragment.
 */
@BindingAdapter("onSwipe")
fun RecyclerView.setItemSwiped(listener: SwipeListener) {
    val swipeToDeleteButton = SwipeButton(
        context,
        R.drawable.ic_delete,
        Color.RED,
        listener
    )

    ItemTouchHelper(object : SavedMovieSwipeCallback(context, this) {
        override fun addSwipeButton(swipeButtons: MutableList<SwipeButton>) {
            swipeButtons.add(swipeToDeleteButton)
        }
    }).attachToRecyclerView(this)
}