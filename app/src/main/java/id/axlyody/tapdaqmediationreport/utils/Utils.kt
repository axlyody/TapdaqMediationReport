/*
 * Utils.kt
 * Copyright 2020 Axl Yody <axlyod@gmail.com>
 *
 */

package id.axlyody.tapdaqmediationreport.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.TypedValue
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun Activity.start(clazz: Class<*>) {
    startActivity(Intent(this, clazz))
}

fun String.getDayFromDate(): String {
    val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val outputFormat: DateFormat = SimpleDateFormat("E dd", Locale.getDefault())
    try {
        val date = inputFormat.parse(this)!!
        return outputFormat.format(date)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return "error"
}

fun getDate(set: Int? = null, add: Int? = null, value: Int? = null): String {
    val date = Calendar.getInstance()
    val output = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    set?.let {
        date.set(it, value!!)
    }
    add?.let {
        date.add(it, value!!)
    }
    return output.format(date.time)
}

fun getResColor(context: Context, colorId: Int): Int {
    var color: Int
    TypedValue().apply {
        context.obtainStyledAttributes(data, intArrayOf(colorId))
            .apply {
                color = getColor(0, 0)
                recycle()
            }
    }
    return color
}

fun ImageView.load(url: Any) {
    try {
        Glide.with(this)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(this)
    } catch (e: Exception) {
    }
}

