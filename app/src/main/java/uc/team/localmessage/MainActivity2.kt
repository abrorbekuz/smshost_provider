package uc.team.localmessage

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uc.team.localmessage.databinding.ActivityMain2Binding
import uc.team.localmessage.managers.StatisticsManager
import uc.team.localmessage.managers.TokenManager
import uc.team.localmessage.model.DeviceModel
import uc.team.localmessage.service.ApiClient

class MainActivity2 : AppCompatActivity() {

    private val binding by lazy { ActivityMain2Binding.inflate(layoutInflater) }
    private lateinit var tokenManager: TokenManager
    private lateinit var statistics: StatisticsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        tokenManager = TokenManager(applicationContext)
        statistics = StatisticsManager(applicationContext)

        binding.apply {
            deviceWrapper.setOnClickListener {
                statistics.getDeviceId()?.let { copyToClipboard(applicationContext) }
            }

            toolbar.apply {
                logout.setOnClickListener {
                    resetDashboard()
                    finish()
                }

                refresh.setOnClickListener{
                    refreshDashboard()
                }
            }
        }


        refreshDashboard()

        Log.d("tok", "pref " + statistics.getPreferredSlot().toString())
    }

    private fun resetDashboard(){
        tokenManager.resetToken()
        statistics.reset()
    }

    private fun prepareDashboard(){
        val deviceName = "${Build.MANUFACTURER}-${Build.BRAND}-${Build.MODEL}"
        val call = ApiClient().getApiService(applicationContext).getMe(deviceName)

        call.enqueue(object : Callback<DeviceModel> {
            override fun onResponse(call: Call<DeviceModel>, response: Response<DeviceModel>) {

                if (response.isSuccessful){
                    statistics.run {
                        setDeviceId(response.body()?.id)
                        response.body()?.preferredSlot?.let { setPreferredSlot(it) }
                    }

                    Toast.makeText(applicationContext, "Ok, to make sure press Refresh !", Toast.LENGTH_SHORT).show()
                }

                if (response.code() == 403){
                    Log.d("API", "unauthorized")
                }
            }

            override fun onFailure(call: Call<DeviceModel>, t: Throwable) {
                Toast.makeText(applicationContext, "Can't connect to server !", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun refreshDashboard(){
        if(statistics.getDeviceId() == null) prepareDashboard()
        binding.apply{
            deviceId.text = statistics.getDeviceId()
            total.text = statistics.getTotalCount().toString()
            today.text = statistics.getTodayCount().toString()
        }
    }
}

private fun Any?.copyToClipboard(context: Context) {
    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clipData = ClipData.newPlainText("label", this.toString())
    clipboardManager.setPrimaryClip(clipData)

    Toast.makeText(context, "Text copied to clipboard", Toast.LENGTH_SHORT).show()
}