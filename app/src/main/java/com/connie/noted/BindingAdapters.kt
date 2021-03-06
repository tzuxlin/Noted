@file:Suppress("NAME_SHADOWING")

package com.connie.noted

import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Glide.with
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.connie.noted.add2board.Add2boardAdapter
import com.connie.noted.board.item.BoardItemAdapter
import com.connie.noted.boardpage.BoardNotesAdapter
import com.connie.noted.data.Board
import com.connie.noted.data.Note
import com.connie.noted.explore.ExplorePopularAdapter
import com.connie.noted.explore.ExploreRecommendAdapter
import com.connie.noted.note.NoteAdapter


@BindingAdapter("imageNoRoundUrl")
fun bindNoRoundImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUrl = imgUrl.toUri().buildUpon().scheme("https").build()
       with(imgView.context)
            .load(imgUrl)
            .apply(
                RequestOptions()
                    .transform(MultiTransformation(FitCenter()))
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_placeholder)
            )
            .into(imgView)
    }

}



@BindingAdapter("imageNoteItemUrl")
fun bindNoteItemImage(imgView: ImageView, imgUrl: String?) {

    imgUrl?.let{

        val imgUrl = it.toUri().buildUpon().scheme("https").build()
        with(imgView.context)
            .load(imgUrl)
            .apply(
                RequestOptions()
                    .transform(MultiTransformation(FitCenter()))
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_placeholder)
            )
            .into(imgView)
}

}



@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {

        with(imgView.context)
            .load(imgUrl)
            .apply(
                RequestOptions()
                    .transform(MultiTransformation(FitCenter(), RoundedCorners(10)))
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
                        .placeholder(R.drawable.ic_placeholder)
                        .error(R.drawable.ic_placeholder)
                )
                .into(imgView)
        }
    }
}

@BindingAdapter("listNote")
fun bindNoteRecyclerView(recyclerView: RecyclerView, data: List<Note>?) {
    data?.let {

        recyclerView.adapter?.apply {
            when (this) {
                is NoteAdapter -> {
                    submitList(it)
                }
                is BoardNotesAdapter -> {
                    submitList(it)
                }
                is Add2boardAdapter -> {
                    submitList(it)
                }
            }
        }
    }
}


@BindingAdapter("listBoard")
fun bindBoardRecyclerView(recyclerView: RecyclerView, data: List<Board>?) {
    if (data != null) {
        recyclerView.adapter?.let { adapter ->

            val adapter = when (adapter) {
                is BoardItemAdapter -> adapter
                is ExplorePopularAdapter -> adapter
                is ExploreRecommendAdapter -> adapter
                else -> null
            }

            adapter?.submitList(data)
        }
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
    size?.let {
        textView.text = "$it 個筆記"
    }
}

@BindingAdapter("savedCount")
fun bindBoardNotesSavedCount(textView: TextView, size: Int?) {
    size?.let {
        textView.text = "$it 個收藏"
    }
}


@BindingAdapter("boardMore")
fun bindBoardMoreNotesCount(textView: TextView, size: Int?) {
    size?.let {
        textView.text = "${it - 4}+"
    }
}

@BindingAdapter("createdBy")
fun bindBoardCreatedBy(textView: TextView, name: String?) {
    name?.let {

        textView.text = it

    }
}


