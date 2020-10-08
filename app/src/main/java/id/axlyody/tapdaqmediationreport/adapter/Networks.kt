package id.axlyody.tapdaqmediationreport.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import id.axlyody.tapdaqmediationreport.R
import id.axlyody.tapdaqmediationreport.model.Networks
import kotlinx.android.synthetic.main.adapter_network.view.*
import smartadapter.viewholder.SmartViewHolder

class NetworksViewHolder(parentView: ViewGroup) : SmartViewHolder<Networks>(
    LayoutInflater.from(parentView.context)
        .inflate(R.layout.adapter_network, parentView, false)
) {
    override fun bind(item: Networks) {
        itemView.apply {
            tv_name.text = String.format("• \t %s", item.name)
            tv_revenue.text = String.format("$%d", item.revenue)
        }
    }

}