package uc.team.localmessage.model

data class Code(
    var code: String,
    val scope: String="mobile",
    val client_id: String=Constants.CLIENT_ID,
    val grant_type: String ="authorization_code"
)
