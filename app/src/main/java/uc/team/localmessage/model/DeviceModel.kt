package uc.team.localmessage.model

import com.google.gson.annotations.SerializedName

data class DeviceModel(
    val id: String,
    val preferred: Boolean,
    @SerializedName("device_name")
    val deviceName: String,
    @SerializedName("device_id")
    val deviceId: String,
    @SerializedName("preferred_slot")
    val preferredSlot: Int,
    @SerializedName("created_time")
    val createdTime: String
)
