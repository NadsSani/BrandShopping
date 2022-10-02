package com.nads.shopping.utils

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputLayout
import com.nads.shopping.R
import de.hdodenhof.circleimageview.CircleImageView

@BindingAdapter("android:visibility")
fun setVisibility(view: View, show: Boolean) {
    if (show)
        view.visibility = View.VISIBLE
    else view.visibility = View.GONE
}

@BindingAdapter("app:strokeWidth")
fun MaterialCardView.setStrokeWidth(width: Int) {
    strokeWidth = width.getPixels()
}

@BindingAdapter("android:fontFamily")
fun TextView.setFont(font:Int) {
    try {
        typeface = ResourcesCompat.getFont(context, font)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@BindingAdapter("android:src")
fun setImage(imageView: ImageView, image: String?) {
    image?.let {
        Glide.with(imageView.context)
            .load(it)
            .centerCrop()
            .into(imageView)
    }
}

@BindingAdapter("android:animate")
fun setChangeAnimation(recycler: RecyclerView, animate: Boolean) {
    (recycler.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = animate
}

@BindingAdapter("android:src", "android:placeholder")
fun setVisibility(
    imageView: CircleImageView,
    image: String?,
    placeholder: Int = R.drawable.avatar
) {
    image?.let {
        Glide.with(imageView.context)
            .load(it)
            .placeholder(placeholder)
            .centerCrop()
            .into(imageView)
    }
}

@BindingAdapter("android:src", "android:scale")
fun setGlideImage(imageView: ImageView, image: Int, scale: String = "center") {

    when (scale) {
        "center" -> Glide.with(imageView.context)
            .load(image)
            .fitCenter()
            .into(imageView)
        "crop" -> Glide.with(imageView.context)
            .load(image)
            .centerCrop()
            .into(imageView)
    }

}

@BindingAdapter("android:clearButtonTint")
fun BottomNavigationView.clearButtonTint(clear: String) {
    if (clear == "yes")
        this.itemIconTintList = null
}

@BindingAdapter("android:en", "android:ar")
fun setLanguageSpecificText(view: TextView, en: String? = "", ar: String? = "") {
    if (view.context.isEnglish())
        view.text = en
    else view.text = if (ar.isNullOrEmpty()) en else ar
}

@BindingAdapter("android:error")
fun setErrorResource(view: TextView, id: Int?) {
//    view.context.resources.configuration.locale
    try {
//        id?.let { setErrorResource(view, it) }
        if (id!=null)
            view.error = view.context.getString(id)
        else view.error = null
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@BindingAdapter("android:error")
fun setErrorResource(view: TextInputLayout, id: Int?) {
//    view.context.resources.configuration.locale
    try {
//        id?.let { setErrorResource(view, it) }
        if (id!=null)
            view.error = view.context.getString(id)
        else view.error = null
    } catch (e: Exception) {
        e.printStackTrace()
    }
}