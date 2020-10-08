package id.axlyody.tapdaqmediationreport.activity

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.hadiidbouk.charts.BarData
import id.axlyody.tapdaqmediationreport.AppActivity
import id.axlyody.tapdaqmediationreport.R
import id.axlyody.tapdaqmediationreport.adapter.NetworksViewHolder
import id.axlyody.tapdaqmediationreport.model.Mediation
import id.axlyody.tapdaqmediationreport.model.Networks
import id.axlyody.tapdaqmediationreport.service.Http
import id.axlyody.tapdaqmediationreport.utils.getDate
import id.axlyody.tapdaqmediationreport.utils.getDayFromDate
import id.axlyody.tapdaqmediationreport.utils.getResColor
import id.axlyody.tapdaqmediationreport.utils.start
import kotlinx.android.synthetic.main.activity_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import smartadapter.SmartRecyclerAdapter
import timber.log.Timber
import java.util.*

class Home : AppActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        refresh_data.apply {
            setProgressBackgroundColorSchemeColor(getResColor(this@Home, R.attr.colorPrimary))
            setColorSchemeColors(Color.WHITE)
            setOnRefreshListener {
                getData()
            }
        }
        getData()

        bt_logout.setOnClickListener {
            prefs.edit().remove("api_token").apply()
            start(Main::class.java)
            finish()
        }

        tx_token.text = String.format("token: ***%s", prefs.getString("api_token", "")?.takeLast(4))

    }

    private fun getData() {
        // fetching data start here
        Http(this, prefs).mediation(
            JsonObject().apply {
                addProperty("start_time", getDate(add = Calendar.MONTH, value = -1))
                addProperty("end_time", getDate(add = Calendar.DATE, value = +1))
                add("group_by", JsonArray().apply {
                    add("app")
                    add("date")
                    add("ad_network")
                })
            }
        ).enqueue(object : Callback<List<Mediation>> {
            override fun onResponse(
                call: Call<List<Mediation>>,
                response: Response<List<Mediation>>
            ) {
                response.body()?.let { res ->
                    tv_revenue_month.text =
                        String.format("$%d", res.map { it.revenue / 1000 }.sum())
                    tv_today_today.text =
                        String.format("$%d", res.sortedBy { it.group.date }.last().revenue / 1000)

                    tv_revenue_week.text =
                        String.format(
                            "$%d",
                            res.sortedByDescending { it.group.date }.take(7)
                                .sumBy { it.revenue / 1000 })
                    cp_daily_revenue.apply {
                        setDataList(
                            res.sortedBy { it.group.date }.takeLast(7).map { item ->
                                BarData(
                                    item.group.date.getDayFromDate(),
                                    (item.revenue / 1000).toFloat(),
                                    String.format("$%d", item.revenue / 1000)
                                )
                            } as ArrayList<BarData>
                        )
                        setMaxValue(
                            (res.maxOf { it.revenue } / 1000).toFloat()
                        )
                        build()
                    }


                    SmartRecyclerAdapter.items(
                        res.groupBy { it.group.ad_network }.mapValues {
                            it.value.sumBy { res -> res.revenue / 1000 }
                        }.map {
                            Networks(
                                name = it.key,
                                revenue = (it.value)
                            )
                        }
                    )
                        .map(Networks::class, NetworksViewHolder::class)
                        .into<SmartRecyclerAdapter>(rv_networks)
                }

                refresh_data.isRefreshing = false
                Timber.d("Data fetched")
            }

            override fun onFailure(call: Call<List<Mediation>>, t: Throwable) {
                Toast.makeText(this@Home, t.message, Toast.LENGTH_SHORT).show()
                Timber.e("Error: %s", t.message)
            }


        })
    }
}