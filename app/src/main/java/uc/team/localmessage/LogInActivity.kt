package uc.team.localmessage

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessaging
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uc.team.localmessage.databinding.ActivityLoginBinding
import uc.team.localmessage.managers.TokenManager
import uc.team.localmessage.model.Code
import uc.team.localmessage.model.Constants
import uc.team.localmessage.model.TokenModel
import uc.team.localmessage.service.ApiClient

class LogInActivity : AppCompatActivity() {
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }

    private lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)



        tokenManager = TokenManager(applicationContext)
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.SEND_SMS),
            PERMISSION_REQUEST_SEND_SMS)


        binding.btnLogin.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED
            ){
                val builder = CustomTabsIntent.Builder()
                val customTabsIntent = builder.build()
                customTabsIntent.launchUrl(
                    this, Uri.parse(Constants.AUTH_URL)
                )
            } else Toast.makeText(this, "We cant work with you, unless give SMS permission !", Toast.LENGTH_SHORT).show()
        }

        var token: String? = null;
        token = tokenManager.getToken()


        if (token != null) {
            startActivity(Intent(this, MainActivity2::class.java))
            finish()
        }
    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val code = intent?.data?.getQueryParameter("code")
        if (code != null) {
            saveAccessToken(code)
        }
        startActivity(Intent(this, MainActivity2::class.java))

        FirebaseMessaging.getInstance().deleteToken().addOnSuccessListener {
            FirebaseMessaging.getInstance().token
        }

        finish()
    }

    private fun saveAccessToken(code: String) {
        val coder = Code(code = code)
        val call = ApiClient().getApiService(applicationContext).getToken(
            code = coder.code,
            scope = coder.scope,
            clientId = coder.client_id,
            grantType = coder.grant_type
        )

        call.enqueue(object : Callback<TokenModel> {
            override fun onResponse(call: Call<TokenModel>, response: Response<TokenModel>) {
                if (response.isSuccessful) {
                    val tokenModel = response.body() ?: return
                    val accessToken = tokenModel.access_token
                    tokenManager.saveToken(accessToken)
                }
            }

            override fun onFailure(call: Call<TokenModel>, t: Throwable) {
                Log.d("tok", t.toString())
            }
        })
    }

    companion object {
        const val PERMISSION_REQUEST_SEND_SMS = 123
    }


}