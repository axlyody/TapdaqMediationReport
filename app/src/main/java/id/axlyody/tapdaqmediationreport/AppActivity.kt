/*
 * AppActivity.kt
 * Copyright 2020 Axl Yody <axlyod@gmail.com>
 *
 */

package id.axlyody.tapdaqmediationreport

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import id.axlyody.tapdaqmediationreport.service.Http

open class AppActivity : AppCompatActivity() {
    lateinit var prefs: SharedPreferences
    lateinit var api: Http
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = this.getSharedPreferences(packageName, Context.MODE_PRIVATE)
        api = Http(this, prefs)
    }
}