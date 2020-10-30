/*
 * FragmentOverview.kt
 * Copyright 2020 Axl Yody <axlyod@gmail.com>
 *
 */

package id.axlyody.tapdaqmediationreport.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.hadiidbouk.charts.BarData
import id.axlyody.tapdaqmediationreport.AppFragment
import id.axlyody.tapdaqmediationreport.R
import id.axlyody.tapdaqmediationreport.adapter.NetworksViewHolder
import id.axlyody.tapdaqmediationreport.model.Mediation
import id.axlyody.tapdaqmediationreport.model.Networks
import id.axlyody.tapdaqmediationreport.utils.getDate
import id.axlyody.tapdaqmediationreport.utils.getDayFromDate
import id.axlyody.tapdaqmediationreport.utils.getResColor
import kotlinx.android.synthetic.main.fragment_overview.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import smartadapter.SmartRecyclerAdapter
import timber.log.Timber
import java.util.*

class FragmentOverview : AppFragment() {
    private var data: List<Mediation>? = null
    private var request: Call<List<Mediation>>? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_overview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireView().apply {
            refresh_layout.apply {
                setProgressBackgroundColorSchemeColor(
                    getResColor(
                        requireContext(),
                        R.attr.colorPrimary
                    )
                )
                setColorSchemeColors(Color.WHITE)
                setOnRefreshListener {
                    getData()
                }
            }
        }

        data?.let {
            retrieveData(it)
        } ?: run {
            getData()
        }
    }

    override fun onPause() {
        super.onPause()
        request?.cancel()
    }


    private fun getData() {
        requireView().apply {
            request = api.mediation(
                JsonObject().apply {
                    addProperty("start_time", getDate(add = Calendar.MONTH, value = -1))
                    addProperty("end_time", getDate(add = Calendar.DATE, value = +1))
                    add("group_by", JsonArray().apply {
                        add("app")
                        add("date")
                        add("ad_network")
                    })
                }
            ).also { call ->
                call.enqueue(object : Callback<List<Mediation>> {
                    override fun onResponse(
                        call: Call<List<Mediation>>,
                        response: Response<List<Mediation>>
                    ) {

                        data = response.body()
                        data?.let {
                            retrieveData(it)
                        }

                        Timber.d("Data fetched")
                    }

                    override fun onFailure(call: Call<List<Mediation>>, t: Throwable) {
                        Timber.e("Error: %s", t.message)
                        refresh_layout.isRefreshing = false
                    }

                })
            }
        }
    }

    private fun retrieveData(data: List<Mediation>) {
        shimmer.visibility = View.GONE
        layout.visibility = View.VISIBLE
        refresh_layout.isRefreshing = false
        tv_revenue_month.text =
            String.format("$%d", data.map { it.revenue / 1000 }.sum())
        tv_today_today.text =
            String.format(
                "$%d",
                data.sortedBy { it.group.date }.last().revenue / 1000
            )
        tv_revenue_week.text =
            String.format(
                "$%d",
                data.sortedByDescending { it.group.date }.take(7)
                    .sumBy { it.revenue / 1000 })
        cp_daily_revenue.apply {
            setDataList(
                data.sortedBy { it.group.date }.takeLast(7).map { item ->
                    BarData(
                        item.group.date.getDayFromDate(),
                        (item.revenue / 1000).toFloat(),
                        String.format("$%d", item.revenue / 1000)
                    )
                } as ArrayList<BarData>
            )
            setMaxValue(
                (data.maxOf { it.revenue } / 1000).toFloat()
            )
            build()
        }


        SmartRecyclerAdapter.items(
            data.groupBy { it.group.ad_network }.mapValues {
                it.value.sumBy { res -> res.revenue / 1000 }
            }.map {
                Networks(
                    name = it.key,
                    revenue = (it.value)
                )
            }
        )
            .setLayoutManager(
                LinearLayoutManager(
                    requireContext(),
                    RecyclerView.HORIZONTAL,
                    false
                )
            )
            .map(Networks::class, NetworksViewHolder::class)
            .into<SmartRecyclerAdapter>(rv_networks)
    }
}