package com.team.todoktodok.presentation.core.ext

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.team.todoktodok.R

fun ImageView.loadImage(url: String?) {
    Glide
        .with(this.context)
        .load(url)
        .placeholder(R.drawable.img_mascort)
        .fallback(R.drawable.img_mascort)
        .into(this)
}

fun ImageView.loadCircleImage(url: String?) {
    Glide
        .with(this.context)
        .load(url)
        .apply(RequestOptions.circleCropTransform())
        .placeholder(R.drawable.img_mascort)
        .fallback(R.drawable.img_mascort)
        .into(this)
}
