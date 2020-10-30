/*
 * AppFragment.kt
 * Copyright 2020 Axl Yody <axlyod@gmail.com>
 *
 */

package id.axlyody.tapdaqmediationreport

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import id.axlyody.tapdaqmediationreport.service.Http

open class AppFragment : Fragment() {
    lateinit var prefs: SharedPreferences
    lateinit var api: Http
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.apply {
            prefs = getSharedPreferences(packageName, Context.MODE_PRIVATE)
            api = Http(this, prefs)
        }
    }
}