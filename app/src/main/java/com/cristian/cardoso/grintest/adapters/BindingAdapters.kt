package com.cristian.cardoso.grintest.adapters

import android.databinding.BindingAdapter
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.ImageView

@BindingAdapter("android:setLinearLayout")
fun setLinearLayout(table: RecyclerView, setLinearLayout: Boolean) {
    table.layoutManager = LinearLayoutManager(table.context)
}

@BindingAdapter("android:setAdapter")
fun setAdapter(table: RecyclerView, adapter : RecyclerView.Adapter<RecyclerView.ViewHolder>){
    table.adapter = adapter
}

@BindingAdapter("android:src")
fun src(view: ImageView, imageResource : Int?) {
    imageResource?.let {
        view.setImageResource(imageResource)
    }
}

