package com.team.ui_xml.extension

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.team.core.R as coreR

fun ImageView.loadImage(url: String?) {
    Glide
        .with(this.context)
        .load(url)
        .placeholder(coreR.drawable.img_mascort)
        .fallback(coreR.drawable.img_mascort)
        .into(this)
}

fun ImageView.loadCircleImage(url: String?) {
    Glide
        .with(this.context)
        .load(url)
        .apply(RequestOptions.circleCropTransform())
        .placeholder(coreR.drawable.img_mascort)
        .fallback(coreR.drawable.img_mascort)
        .into(this)
}
