/*
 * About.kt
 * Copyright 2020 Axl Yody <axlyod@gmail.com>
 *
 */

package id.axlyody.tapdaqmediationreport.activity

import android.os.Bundle
import android.view.MenuItem
import id.axlyody.tapdaqmediationreport.AppActivity
import id.axlyody.tapdaqmediationreport.R
import id.axlyody.tapdaqmediationreport.utils.start
import kotlinx.android.synthetic.main.activity_about.*

class About : AppActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }

        bt_license.setOnClickListener {
            start(License::class.java)
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }


}