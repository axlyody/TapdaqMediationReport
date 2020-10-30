/*
 * License.kt
 * Copyright 2020 Axl Yody <axlyod@gmail.com>
 *
 */

package id.axlyody.tapdaqmediationreport.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import id.axlyody.tapdaqmediationreport.AppActivity
import id.axlyody.tapdaqmediationreport.R
import id.axlyody.tapdaqmediationreport.adapter.LicenseViewHolder
import id.axlyody.tapdaqmediationreport.model.License
import id.axlyody.tapdaqmediationreport.model.LicenseLibraries
import kotlinx.android.synthetic.main.activity_license.*
import smartadapter.SmartRecyclerAdapter

class License : AppActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_license)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }

        assets.open("licenses.json")
            .bufferedReader()
            .use {
                Gson().fromJson(it, License::class.java).apply {
                    rl_license.let { res ->
                        SmartRecyclerAdapter.items(libraries)
                            .map(LicenseLibraries::class, LicenseViewHolder::class)
                            .into<SmartRecyclerAdapter>(res)
                        res.addItemDecoration(
                            DividerItemDecoration(
                                res.context,
                                RecyclerView.VERTICAL
                            )
                        )
                    }
                }
            }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

}