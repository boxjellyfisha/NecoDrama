package com.kellyhong.necodrama.db

import android.content.Context

/**
 * Contain the data of student's info and permission.
 * (Normal user)
 */
class AppPreference(context: Context): BasePreference(context) {
    companion object {
        /* User Preferences */
        private const val PREFS = "app_prefs"

        val LAST_UPDATE = MyPair("lastUpdate", Long::class.java)
    }

    override fun getEditorName() = PREFS
}
