package com.cermati.test.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.cermati.test.R


/**
 * Created by siapaSAYA on 7/18/2020
 */


@BindingAdapter("sourceOriginal")
fun ImageView.sourceOriginal(path: String?) {
    path?.also {
        val original =  it
        Glide.with(this)
            .load(original)
            .placeholder(R.drawable.ic_sharp_account_box_24)
            .skipMemoryCache(true)
            .into(this)
    }
}