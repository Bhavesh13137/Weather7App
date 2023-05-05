package com.weatherapp.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("glideImage")
fun glideImage(imageView: ImageView, imageUrl: String){
    Glide.with(imageView.context).load(imageUrl).into(imageView)
}