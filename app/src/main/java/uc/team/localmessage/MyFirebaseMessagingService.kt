package uc.team.localmessage

import android.os.Build
import android.telephony.SmsManager
import android.telephony.SubscriptionManager
import android.util.Log
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uc.team.localmessage.managers.StatisticsManager
import uc.team.localmessage.model.DeviceModel
import uc.team.localmessage.model.RegisterDevice
import uc.team.localmessage.service.ApiClient


class MyFirebaseMessagingService : FirebaseMessagingService() {

    private lateinit var statistics: StatisticsManager

    override fun onCreate() {
        super.onCreate()
        statistics = StatisticsManager(applicationContext)

    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        sendTokenToServer(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        if (message.data.isNotEmpty()){
            sendSms(message)
        }
    }

    private fun sendSms(message: RemoteMessage) {
        val content = message.data
        val toPhoneNumber = content["to_phone_number"]
        val text = content["text"]

//        Log.d("tok", SmsManager.getDefaultSmsSubscriptionId().toString())

        try{
            val defSubId = statistics.getPreferredSlot()

            val subId = if (defSubId != SubscriptionManager.INVALID_SUBSCRIPTION_ID) defSubId else SmsManager.getDefaultSmsSubscriptionId()

            val smsManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                SmsManager.getSmsManagerForSubscriptionId(subId)
            } else {
                SmsManager.getDefault()
            }

            Log.d("tok", "$defSubId $subId")

            smsManager.sendTextMessage(toPhoneNumber,null,text,null,null)
            statistics.incrementBoth()
        } catch (e: Exception){
            Log.d("tok", e.toString())
        }

    }


    private fun sendTokenToServer(token: String){
        val deviceName = "${Build.MANUFACTURER}-${Build.BRAND}-${Build.MODEL}"
        val device = RegisterDevice(deviceName, token)
        val call = ApiClient().getApiService(applicationContext).registerDevice(device)

        call.enqueue(object : Callback<DeviceModel> {
            override fun onResponse(call: Call<DeviceModel>, response: Response<DeviceModel>) {
                Log.d("tok", response.body().toString())
                if (response.isSuccessful){
                    statistics.run{
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
}