package com.udacity.asteroidradar.features.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.api.models.AsteroidModel
import com.udacity.asteroidradar.databinding.ItemAsteroidBinding

class AsteroidItemAdapter(val clickListener: AsteroidClickListener) :
    ListAdapter<AsteroidModel, AsteroidItemAdapter.ViewHolder>(AsteroidDiffCallback()) {


    public class AsteroidClickListener(val clickListener: (asteroidModel: AsteroidModel) -> Unit) {
        fun onClick(asteroidModel: AsteroidModel) = clickListener(asteroidModel)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(clickListener, item)
    }


    class ViewHolder(val mBinding: ItemAsteroidBinding) : RecyclerView.ViewHolder(mBinding.root) {

        fun bind(clickListener: AsteroidClickListener, item: AsteroidModel) {
            mBinding.asteroid = item
            mBinding.clickListener = clickListener
            mBinding.executePendingBindings()
        }


        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemAsteroidBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)

            }
        }


    }


}


class AsteroidDiffCallback : DiffUtil.ItemCallback<AsteroidModel>() {
    override fun areItemsTheSame(oldItem: AsteroidModel, newItem: AsteroidModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: AsteroidModel, newItem: AsteroidModel): Boolean {
        return oldItem == newItem
    }
}