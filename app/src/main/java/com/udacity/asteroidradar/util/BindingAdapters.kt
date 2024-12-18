package com.udacity.asteroidradar.util

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.api.AsteroidApiStatus
import com.udacity.asteroidradar.api.models.ImageOfTodayModel
import com.udacity.asteroidradar.data.BaseRecyclerViewAdapter
import kotlinx.coroutines.launch
import timber.log.Timber

@BindingAdapter("statusIcon")
fun ImageView.bindAsteroidStatusImage(isHazardous: Boolean) {
    if (isHazardous) {
        setImageResource(R.drawable.ic_status_potentially_hazardous)
        contentDescription = context.getString(R.string.text_description_potentially_hazardous_asteroid_image)
    } else {
        setImageResource(R.drawable.ic_status_normal)
        contentDescription = context.getString(R.string.text_description_not_hazardous_asteroid_image)
    }
}

@BindingAdapter("text")
fun TextView.setContext(text: String?) {
    this.text = text
    contentDescription = text
}

@BindingAdapter("listData", "currentScrolledPosition", "lifecycleOwner")
fun <T : Any> RecyclerView.bindRecyclerView(
    list: PagingData<T>?,
    currentScrolledPosition: Int, lifecycleOwner: LifecycleOwner
) {
    list?.let {
        Timber.d("bindRecyclerView:currentScrolledPosition $currentScrolledPosition")
        if (adapter == null) {
            this.adapter = adapter as? BaseRecyclerViewAdapter<T>
            setHasFixedSize(true)
        }
        // TODO-Comment-Issue: This is a workaround to scroll to the clicked position when user back to MainFragment, But it doesn't fix the issue
        lifecycleOwner.lifecycleScope.launch {
            (adapter as? BaseRecyclerViewAdapter<T>)?.submitData(lifecycleOwner.lifecycle, list)
        }
        if (currentScrolledPosition != 0) {
                smoothScrollToPosition(currentScrolledPosition)
//            (layoutManager as? LinearLayoutManager)?.scrollToPositionWithOffset(currentScrolledPosition,0)
        }
    }
}

@BindingAdapter("asteroidStatusImage")
fun ImageView.bindDetailsStatusImage(isHazardous: Boolean) {
    if (isHazardous) {
        setImageResource(R.drawable.asteroid_hazardous)
        contentDescription = context.getString(R.string.text_description_potentially_hazardous_asteroid_image)
    } else {
        setImageResource(R.drawable.asteroid_safe)
        contentDescription = context.getString(R.string.text_description_not_hazardous_asteroid_image)
    }
}

@BindingAdapter("astronomicalUnitText")
fun TextView.bindTextViewToAstronomicalUnit(number: Double) {
    text = String.format(context.getString(R.string.text_format_astronomical_unit), number)
    contentDescription = text
}

@BindingAdapter("kmUnitText")
fun TextView.bindTextViewToKmUnit(number: Double) {
    text = String.format(context.getString(R.string.text_format_km_unit), number)
    contentDescription = text
}

@BindingAdapter("velocityText")
fun TextView.bindTextViewToDisplayVelocity(number: Double) {
    text = String.format(context.getString(R.string.text_format_km_s_unit), number)
    contentDescription = text
}

@BindingAdapter("imageOfToday", "progressBar")
fun ImageView.setImageOfToday(imageOfTodayModel: ImageOfTodayModel?, progress: ProgressBar) {
    if (imageOfTodayModel != null && imageOfTodayModel.mediaType == Constants.MEDIA_TYPE_IMAGE) {
        contentDescription = String.format(
            context.getString(R.string.text_format_nasa_picture_of_day),
            imageOfTodayModel.title
        )
        progress.visibility = View.VISIBLE
        Picasso.get().load(imageOfTodayModel.url)
            .into(this, object : com.squareup.picasso.Callback {
                override fun onSuccess() {
                    progress.visibility = View.GONE
                }

                override fun onError(e: Exception?) {
                    progress.visibility = View.GONE
                }
            })
    } else {
        contentDescription =
            context.getString(R.string.text_empty_picture_of_today)
    }

}

@BindingAdapter("loadingStatus")
fun ProgressBar.setLoadingStatus(asteroidApiStatus: AsteroidApiStatus?) {
    if (asteroidApiStatus != null) {
        visibility = when (asteroidApiStatus) {
            AsteroidApiStatus.LOADING -> {
                View.VISIBLE
            }

            AsteroidApiStatus.DONE -> {
                View.GONE
            }

            else -> {
                View.GONE
            }
        }
    }
}

