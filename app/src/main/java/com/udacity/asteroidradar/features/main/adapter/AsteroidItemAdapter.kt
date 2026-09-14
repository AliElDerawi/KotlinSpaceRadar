package com.udacity.asteroidradar.features.main.adapter

import androidx.recyclerview.widget.DiffUtil
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.domain.AsteroidModel
import com.udacity.asteroidradar.data.BaseRecyclerViewAdapter

class AsteroidItemAdapter(
    diffCallback: DiffUtil.ItemCallback<AsteroidModel>,
    callback: ((item: AsteroidModel, position: Int) -> Unit)? = null
) : BaseRecyclerViewAdapter<AsteroidModel>(diffCallback, callback) {
    override fun getLayoutRes(viewType: Int) = R.layout.item_asteroid
}