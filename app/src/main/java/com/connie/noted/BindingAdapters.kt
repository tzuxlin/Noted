@file:Suppress("NAME_SHADOWING")

package com.connie.noted

import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.connie.noted.board.item.BoardItemAdapter
import com.connie.noted.data.Board
import com.connie.noted.data.Note
import com.connie.noted.note.NoteAdapter


@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
            val imgUrl = imgUrl.toUri().buildUpon().scheme("https").build()
            Glide.with(imgView.context)
                .load(imgUrl)
                .apply(
                    RequestOptions()
                        .transform(MultiTransformation(FitCenter(), RoundedCorners(10)))
//                    .fitCenter()
                        .placeholder(R.drawable.ic_placeholder)
                        .error(R.drawable.ic_placeholder)
                )
                .into(imgView)
        }

}


@BindingAdapter("imageCollageUrl")
fun bindImageCollage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        if (it.length > 2) {
            val imgUrl = imgUrl.toUri().buildUpon().scheme("https").build()
            Glide.with(imgView.context)
                .load(imgUrl)
                .apply(
                    RequestOptions()
                        .transform(MultiTransformation(CenterCrop(), RoundedCorners(10)))
//                    .fitCenter()
                        .placeholder(R.drawable.ic_placeholder)
                        .error(R.drawable.ic_placeholder)
                )
                .into(imgView)
        }
    }
}

@BindingAdapter("listNote")
fun bindNoteRecyclerView(recyclerView: RecyclerView, data: List<Note>?) {
    if (data != null) {
        val adapter = recyclerView.adapter as NoteAdapter
        adapter.submitList(data)
    }
}

@BindingAdapter("listBoard")
fun bindBoardRecyclerView(recyclerView: RecyclerView, data: List<Board>?) {
    if (data != null) {
        val adapter = recyclerView.adapter as BoardItemAdapter
        adapter.submitList(data)
    }
}


@BindingAdapter("profileImageUrl")
fun bindProfileImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUrl = imgUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
            .load(imgUrl)
            .apply(
                RequestOptions()
                    .circleCrop()
            )
            .into(imgView)
    }
}

@BindingAdapter("notesCount")
fun bindBoardNotesCount(textView: TextView, size: Int?) {
    size?.let{
        textView.text = "$it notes"
    }
}


@BindingAdapter("boardMore")
fun bindBoardMoreNotesCount(textView: TextView, size: Int?) {
    size?.let{
        textView.text = "${it-4}+"
    }
}