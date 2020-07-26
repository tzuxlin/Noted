@file:Suppress("NAME_SHADOWING")

package com.connie.noted

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Glide.with
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.connie.noted.board.item.BoardItemAdapter
import com.connie.noted.boardpage.BoardNotesAdapter
import com.connie.noted.data.Board
import com.connie.noted.data.Note
import com.connie.noted.explore.ExplorePopularAdapter
import com.connie.noted.login.UserManager
import com.connie.noted.note.NoteAdapter
import com.connie.noted.util.Util.getWindowWidth


@BindingAdapter("imageNoRoundUrl")
fun bindNoRoundImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUrl = imgUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
            .load(imgUrl)
            .apply(
                RequestOptions()
                    .transform(MultiTransformation(FitCenter()))
//                    .fitCenter()
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_placeholder)
            )
            .into(imgView)
    }

}


//@BindingAdapter("imageNoRoundUrl")
//fun bindImageNoRound(imgView: ImageView, imgUrl: String?) {
//    imgUrl?.let {
//
//        var height = 400
//        val imgUrl = imgUrl.toUri().buildUpon().scheme("https").build()
//        GlideApp.with(imgView.context)
//            .load(imgUrl)
//            .listener(object : RequestListener<Drawable> {
//                override fun onLoadFailed(
//                    e: GlideException?,
//                    model: Any?,
//                    target: Target<Drawable>?,
//                    isFirstResource: Boolean
//                ): Boolean {
//
//
//                    return true
//                }
//
//                override fun onResourceReady(
//                    resource: Drawable?,
//                    model: Any?,
//                    target: Target<Drawable>?,
//                    dataSource: DataSource?,
//                    isFirstResource: Boolean
//                ): Boolean {
//
//                    resource?.intrinsicHeight?.let{
//                        if (it < height) {
//                            height = it
//                        }
//                    }
//
//                    return true
//                }
//            })
//            .apply(
//                RequestOptions()
//                    .transform(MultiTransformation(FitCenter()))
//                    .override(getWindowWidth(), height)
////                    .fitCenter()
//                    .placeholder(R.drawable.ic_placeholder)
//                    .error(R.drawable.ic_placeholder)
//            )
//            .into(imgView)
//
//    }
//
//}


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
    data?.let {

        recyclerView.adapter?.apply {
            when (this) {
                is NoteAdapter -> {
                    submitList(it)
                }
                is BoardNotesAdapter -> {
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

        textView.text = "由 $it 新增"

    }
}


