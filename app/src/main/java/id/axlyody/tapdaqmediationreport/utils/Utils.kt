package id.axlyody.tapdaqmediationreport.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.TypedValue
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun String.getMonthFromDate(): String {
    val before = SimpleDateFormat("y-m-d", Locale.ENGLISH).parse(this)
    return SimpleDateFormat("m", Locale("ID")).format(before!!)
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

fun Activity.start(clazz: Class<*>) {
    startActivity(Intent(this, clazz))
}