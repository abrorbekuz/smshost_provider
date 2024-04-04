package uc.team.localmessage.managers

import android.content.Context
import android.content.SharedPreferences
import java.util.Calendar

class StatisticsManager(context: Context) {

    companion object {
        private const val PREF_NAME = "Statistics"
        private const val KEY_DEVICE_ID = "device_id"
        private const val KEY_PREFERRED_SLOT_ID = "preferred_slot_id"
        private const val KEY_TOTAL_COUNT = "total_count"
        private const val KEY_TODAY_COUNT = "today_count"
        private const val KEY_LAST_SAVED_DATE = "last_saved_date"
    }

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun getDeviceId(): String? {
        return prefs.getString(KEY_DEVICE_ID, null)
    }

    fun setDeviceId(deviceId: String?) {
        prefs.edit().putString(KEY_DEVICE_ID, deviceId).apply()
    }

    fun getPreferredSlot(): Int {
        return prefs.getInt(KEY_PREFERRED_SLOT_ID, 0)
    }

    fun setPreferredSlot(deviceId: Int) {
        prefs.edit().putInt(KEY_PREFERRED_SLOT_ID, deviceId).apply()
    }

    fun getTotalCount(): Int {
        return prefs.getInt(KEY_TOTAL_COUNT, 0)
    }

    fun getTodayCount(): Int {
        val savedDate = prefs.getLong(KEY_LAST_SAVED_DATE, 0L)
        val today = Calendar.getInstance().timeInMillis

        return if (isSameDay(savedDate, today)) {
            prefs.getInt(KEY_TODAY_COUNT, 0)
        } else {
            0
        }
    }

    private fun incrementTotalCount() {
        val currentTotal = prefs.getInt(KEY_TOTAL_COUNT, 0)
        prefs.edit().putInt(KEY_TOTAL_COUNT, currentTotal + 1).apply()
    }

    private fun incrementTodayCount() {
        val savedDate = prefs.getLong(KEY_LAST_SAVED_DATE, 0L)
        val today = Calendar.getInstance().timeInMillis

        if (!isSameDay(savedDate, today)) {
            prefs.edit().remove(KEY_LAST_SAVED_DATE).apply()
            prefs.edit().remove(KEY_TODAY_COUNT).apply()
        }

        val currentTodayCount = prefs.getInt(KEY_TODAY_COUNT, 0)
        prefs.edit().putInt(KEY_TODAY_COUNT, currentTodayCount + 1)
            .putLong(KEY_LAST_SAVED_DATE, today)
            .apply()
    }

    private fun isSameDay(millis1: Long, millis2: Long): Boolean {
        val cal1 = Calendar.getInstance()
        cal1.timeInMillis = millis1

        val cal2 = Calendar.getInstance()
        cal2.timeInMillis = millis2

        return (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR))
    }

    fun incrementBoth() {
        incrementTotalCount()
        incrementTodayCount()
    }

    fun reset(){
        prefs.edit().clear().apply()
    }
}