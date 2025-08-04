package com.team.todoktodok.presentation.view.book

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.team.todoktodok.R

fun ImageView.loadImage(url: String?) {
    Glide
        .with(this.context)
        .load(url)
        .placeholder(R.drawable.img_mascort)
        .fallback(R.drawable.img_mascort)
        .into(this)
}
