@file:Suppress("NAME_SHADOWING")

package com.connie.stylish

import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.connie.stylish.cart.CartAdapter
import com.connie.stylish.catalog.ProductGridAdapter
import com.connie.stylish.data.Product
import com.connie.stylish.home.HomeAdapter
import com.connie.stylish.home.HomeDataItem

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<HomeDataItem>?) {
    if (data != null) {

        val adapter = recyclerView.adapter as HomeAdapter
        adapter.submitList(data)
    }
}

@BindingAdapter("listProduct")
fun bindProductRecyclerView(recyclerView: RecyclerView, data: List<Product>?) {
    if (data != null) {
        val adapter = recyclerView.adapter as ProductGridAdapter
        adapter.submitList(data)
    }
}


@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUrl = imgUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
            .load(imgUrl)
            .apply(
                RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.ic_broken_image)
            )
            .into(imgView)
    }
}


@BindingAdapter("cartProduct")
fun bindCartProductRecyclerView(recyclerView: RecyclerView, data: LiveData<List<Product>>?) {
    if (data != null) {
        val adapter = recyclerView.adapter as CartAdapter
        adapter.submitList(data.value)
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
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.ic_broken_image)
            )
            .into(imgView)
    }
}


@BindingAdapter("priceToString")
fun TextView.convertPriceToString(price: Long) {
    price.let{
        text = "NT$$it"
    }}

@BindingAdapter("totalCount")
fun TextView.totalCount(productList: List<Product>) {
    productList.let{
        text = context.getString(R.string.binding_adapter_total_pre) + it.size + context.getString(R.string.binding_adapter_total_suf)
    }}
