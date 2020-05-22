package com.breiter.movietowatchapp.ui.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.breiter.movietowatchapp.R
import com.breiter.movietowatchapp.data.domain.Movie
import com.breiter.movietowatchapp.data.util.Constants
import com.breiter.movietowatchapp.ui.screen.saved.SavedMovieAdapter
import com.breiter.movietowatchapp.ui.screen.search.NetworkStatus
import com.breiter.movietowatchapp.ui.screen.search.SearchMovieAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.util.*


/**
 * Binding adapter used to to display images from URL using Glide.
 */
@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, posterPath: String?) {
    posterPath?.let {
        val imgUri = (Constants.IMAGE_URL + posterPath).toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
            .load(imgUri)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image)
            )
            .into(imgView)
    }
}

/**
 * Binding adapter used to extract a year from the full release date and display in [TextView]
 */
@BindingAdapter("dateFormatter")
fun TextView.setDateFormatted(date: String?) {
    text = if (date != null)
        if (date.length > 4) date.substring(0, 4) else date
    else
        date
}

/**
 * Binding adapter used to submit list of saved movies to the [SavedMovieAdapter].
 */
@BindingAdapter("savedMovieListData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Movie>?) {
    val adapter = recyclerView.adapter as SavedMovieAdapter
    adapter.submitList(data)
}

/**
 * Binding adapter used to submit list of searched movies to the  [SearchMovieAdapter]
 * once data is available. If [EditText] is cleared the empty list is submitted.
 */
@BindingAdapter("searchedMovieListData", "clearData")
fun bindSearchMovieRecyclerView(
    recyclerView: RecyclerView,
    data: List<Movie>?,
    clearData: Boolean
) {
    val adapter = recyclerView.adapter as SearchMovieAdapter
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
fun bindErrorImage(
    imageView: ImageView,
    status: NetworkStatus?
) {

    when (status) {
        NetworkStatus.DONE -> {
            imageView.visibility = View.GONE
        }
        NetworkStatus.ERROR -> {
            imageView.visibility = View.VISIBLE
        }
    }
}

/**
 * Binding adapter used to show [RecyclerView] once data
 * is available and hide it on error.
 */
@BindingAdapter("visibility")
fun bindRecyclerView(
    recyclerView: RecyclerView,
    status: NetworkStatus?
) {
    when (status) {
        NetworkStatus.DONE -> {
            recyclerView.visibility = View.VISIBLE
        }
        NetworkStatus.ERROR -> {
            recyclerView.visibility = View.GONE
        }
    }
}

/**
 * Binding adapter used to clear user input from [EditText]
 */
@BindingAdapter("clearText")
fun clearEditText(editText: EditText, isClicked: Boolean) {
    if (isClicked)
        editText.text.clear()
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
fun showKeyboard(view: View, isTyping: Boolean) {
    val inputMethodManager =
        view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    if (isTyping)
        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
}