package com.example.myapplication.ui

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.example.myapplication.R
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.BlurTransformation

@BindingAdapter("bind_image_url_blur")
fun bindBlurImageWithPicasso(imageView: ImageView, url: String?) {
    when (url) {
        null -> Unit
        "" -> imageView.setBackgroundResource(R.drawable.side_nav_bar)
        else -> Picasso.get().load(url).error(R.drawable.side_nav_bar)
            .transform(BlurTransformation(imageView.context, 15, 1)).into(imageView)
    }
}

@BindingAdapter("bind_image_url")
fun bindImageWithPicasso(imageView: ImageView, url: String?) {
    when (url) {
        null -> Unit
        "" -> imageView.setBackgroundResource(R.drawable.ic_baseline_account_circle)
        else -> Picasso.get().load(url).error(R.drawable.ic_baseline_account_circle).into(imageView)
    }
}