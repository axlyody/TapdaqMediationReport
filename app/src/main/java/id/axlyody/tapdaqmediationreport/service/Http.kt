package id.axlyody.tapdaqmediationreport.service

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import id.axlyody.tapdaqmediationreport.R
import id.axlyody.tapdaqmediationreport.model.Apps
import id.axlyody.tapdaqmediationreport.model.Mediation
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import id.axlyody.tapdaqmediationreport.`interface`.request.Apps as appsRequest
import id.axlyody.tapdaqmediationreport.`interface`.request.Mediation as mediationRequest

class Http(private var context: Context, private var prefs: SharedPreferences) {
    private fun retrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(context.getString(R.string.api_url))
            .client(okhttp())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler((Schedulers.io())))
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().setLenient().create()
                )
            )
            .build()
    }

    private fun okhttp(): OkHttpClient {
        return OkHttpClient()
            .newBuilder()
            .addInterceptor { chain ->
                chain.proceed(
                    chain.request()
                        .newBuilder()
                        .apply {
                            addHeader(
                                "Authorization",
                                "Bearer ${prefs.getString("api_token", "0")}"
                            )
                        }
                        .build()
                ).apply {
                    // action
                }
            }
            .build()
    }

    fun apps(): Call<List<Apps>> {
        return retrofit().create(appsRequest::class.java).result()
    }

    fun mediation(jsonObject: JsonObject): Call<List<Mediation>> {
        return retrofit().create(mediationRequest::class.java).result(jsonObject)
    }

}