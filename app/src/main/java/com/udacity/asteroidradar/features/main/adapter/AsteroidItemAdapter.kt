package com.udacity.asteroidradar.features.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.api.models.AsteroidModel
import com.udacity.asteroidradar.data.BaseRecyclerViewAdapter
import com.udacity.asteroidradar.databinding.ItemAsteroidBinding

class AsteroidItemAdapter(
    diffCallback: DiffUtil.ItemCallback<AsteroidModel>,
    callback: ((item: AsteroidModel) -> Unit)? = null
) : BaseRecyclerViewAdapter<AsteroidModel>(diffCallback, callback) {
    override fun getLayoutRes(viewType: Int) = R.layout.item_asteroid
}