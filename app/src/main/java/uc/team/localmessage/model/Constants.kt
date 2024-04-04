package uc.team.localmessage.model

object Constants {

    const val BASE_URL = "https://sms.qodirov.uz"
    const val CLIENT_ID = "r8YHex4faIcGN9eKthsHytFCkgTS1bdtcF58in2m"
    private const val REDIRECT_URL = "auth://callback"
    const val AUTH_URL = "$BASE_URL/login/authorize/?response_type=code&client_id=$CLIENT_ID&redirect_uri=$REDIRECT_URL"
}