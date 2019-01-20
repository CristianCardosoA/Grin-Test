package com.cristian.cardoso.grintest.adapters

import android.arch.lifecycle.MutableLiveData
import android.databinding.BindingAdapter
import android.graphics.drawable.ClipDrawable.HORIZONTAL
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import android.support.constraint.ConstraintLayout
import android.support.constraint.Guideline
import android.support.v4.widget.SwipeRefreshLayout


@BindingAdapter("android:setLinearLayout")
fun setLinearLayout(table: RecyclerView, setLinearLayout: Boolean) {
    table.layoutManager = LinearLayoutManager(table.context)
}

@BindingAdapter("android:setDivider")
fun setDivider(table: RecyclerView, setDivider: Boolean) {
    val itemDecor = DividerItemDecoration(table.context, HORIZONTAL)
    table.addItemDecoration(itemDecor)
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


@BindingAdapter("android:setRefreshListener", "android:isRefreshing", requireAll = true)
fun src(view: SwipeRefreshLayout, listener : SwipeRefreshLayout.OnRefreshListener, isRefreshing : MutableLiveData<Boolean>) {
    view.setOnRefreshListener(listener)
    view.isRefreshing = isRefreshing.value ?: false
}

@BindingAdapter("layout_constraintGuide_percent")
fun setLayout_constraintGuide_percent(guideline: Guideline, percent: Float) {
    val params = guideline.layoutParams as ConstraintLayout.LayoutParams
    params.guidePercent = percent
    guideline.layoutParams = params
}



