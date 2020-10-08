package id.axlyody.tapdaqmediationreport.activity

import android.os.Bundle
import android.widget.Toast
import id.axlyody.tapdaqmediationreport.AppActivity
import id.axlyody.tapdaqmediationreport.R
import id.axlyody.tapdaqmediationreport.model.Apps
import id.axlyody.tapdaqmediationreport.service.Http
import id.axlyody.tapdaqmediationreport.utils.start
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class Main : AppActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (prefs.contains("api_token")) {
            start(Home::class.java)
            finish()
        }
        bt_save.setOnClickListener {
            tx_key.apply {
                when {
                    text.isEmpty() -> {
                        tb_key.setError(getString(R.string.main_error_field_blank), true)
                    }
                    text.length < 36 -> {
                        tb_key.setError(getString(R.string.main_error_token_must_36_chars), true)
                    }
                    else -> {
                        tb_key.removeError()
                        login(tx_key.text.toString())
                    }
                }
            }
        }

    }

    private fun login(token: String): Result<String> {
        prefs.edit().putString("api_token", token).apply()
        bt_save.apply {
            isEnabled = false
            text = getString(R.string.main_wait)
        }
        Http(this, prefs).apps().enqueue(object : Callback<List<Apps>> {
            override fun onResponse(call: Call<List<Apps>>, response: Response<List<Apps>>) {
                when (response.code()) {
                    200 -> {
                        start(Home::class.java)
                        finish()
                        Timber.d("Successfully login")
                        Result.Success("done")
                    }
                    else -> {
                        Toast.makeText(
                            this@Main,
                            getString(R.string.main_error_wrong_api_token),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        prefs.edit().remove("api_token").apply()
                        bt_save.apply {
                            isEnabled = true
                            text = getString(R.string.main_save)
                        }

                        Timber.e("Error: %s", response.message())
                        Result.Error(Exception("ERROR"))
                    }

                }

            }

            override fun onFailure(call: Call<List<Apps>>, t: Throwable) {
                Toast.makeText(this@Main, t.message, Toast.LENGTH_SHORT).show()
                prefs.edit().remove("api_token").apply()
                bt_save.apply {
                    isEnabled = true
                    text = getString(R.string.main_save)
                }
                Timber.e("Error: %s", t.message)
                Result.Error(Exception(t))
            }

        })

        return Result.Error(Exception("Error"))
    }

    sealed class Result<out R> {
        data class Success<out T>(val data: T) : Result<T>()
        data class Error(val exception: Exception) : Result<Nothing>()
    }

}

