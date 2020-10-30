/*
 * FragmentApps.kt
 * Copyright 2020 Axl Yody <axlyod@gmail.com>
 *
 */

package id.axlyody.tapdaqmediationreport.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import id.axlyody.tapdaqmediationreport.AppFragment
import id.axlyody.tapdaqmediationreport.R
import id.axlyody.tapdaqmediationreport.adapter.AppsViewHolder
import id.axlyody.tapdaqmediationreport.model.Apps
import id.axlyody.tapdaqmediationreport.utils.gridAutoLayout
import kotlinx.android.synthetic.main.fragment_apps.*
import kotlinx.android.synthetic.main.fragment_apps.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import smartadapter.SmartRecyclerAdapter
import timber.log.Timber


class FragmentApps : AppFragment() {
    private var data: List<Apps>? = null
    private var request: Call<List<Apps>>? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_apps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        data?.let {
            retrieveData(it)
        } ?: run {
            getData()
        }
    }


    override fun onPause() {
        super.onPause()
        if (data == null) {
            request?.cancel()
        }
    }

    private fun getData() {
        requireView().apply {
            request = api.apps().also { call ->
                call.enqueue(object : Callback<List<Apps>> {
                    override fun onResponse(
                        call: Call<List<Apps>>,
                        response: Response<List<Apps>>
                    ) {
                        data = response.body()
                        data?.let {
                            retrieveData(it)
                            Timber.e("Loaded")
                        }
                    }

                    override fun onFailure(call: Call<List<Apps>>, t: Throwable) {
                        Timber.e("Error: %s", t.message)
                    }

                })
            }

        }
    }


    private fun retrieveData(data: List<Apps>) {
        rv_apps.apply {
            SmartRecyclerAdapter.items(data).map(Apps::class, AppsViewHolder::class)
                .setLayoutManager(gridAutoLayout(rv_apps, requireContext()))
                .into<SmartRecyclerAdapter>(this)
            visibility = View.VISIBLE
        }
        shimmer.visibility = View.GONE
    }


}