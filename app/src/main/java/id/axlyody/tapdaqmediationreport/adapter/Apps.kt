/*
 * Apps.kt
 * Copyright 2020 Axl Yody <axlyod@gmail.com>
 *
 */

package id.axlyody.tapdaqmediationreport.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import id.axlyody.tapdaqmediationreport.R
import id.axlyody.tapdaqmediationreport.model.Apps
import id.axlyody.tapdaqmediationreport.utils.load
import kotlinx.android.synthetic.main.adapter_apps.view.*
import smartadapter.viewholder.SmartViewHolder

class AppsViewHolder(parentView: ViewGroup) : SmartViewHolder<Apps>(
    LayoutInflater.from(parentView.context).inflate(R.layout.adapter_apps, parentView, false)
) {
    override fun bind(item: Apps) {
        itemView.apply {
            tv_name.text = item.name
            tv_package_name.text = item._storeMeta.bundleId

            when (item.operatingSystem) {
                "ios" -> iv_os.load(R.drawable.ic_apple)
                "android" -> iv_os.load(R.drawable.ic_android)
            }
            item.sdk?.apply {
                tv_sdk.text = String.format("SDK %s", "$versionMajor.$versionMinor.$versionPatch")
            }

        }
    }

}