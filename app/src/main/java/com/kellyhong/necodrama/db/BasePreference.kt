package com.kellyhong.necodrama.db

import android.content.Context
import android.content.SharedPreferences

abstract class BasePreference(context: Context) {

    abstract fun getEditorName(): String

    protected var sharedPreferences: SharedPreferences? = null

    init {
        sharedPreferences = context.run {
            getSharedPreferences(getEditorName(), Context.MODE_PRIVATE)
        }
    }

    fun setter(isAsync: Boolean = false, dataSet: List<Pair<String, Any>>) {
        if (sharedPreferences != null) {
            val editor = sharedPreferences!!.edit()
            dataSet.forEach { data ->
                when (data.second) {
                    is String ->
                        editor.putString(data.first, data.second as String)
                    is Int ->
                        editor.putInt(data.first, data.second as Int)
                    is Long ->
                        editor.putLong(data.first, data.second as Long)
                    is Boolean ->
                        editor.putBoolean(data.first, data.second as Boolean)
                }
            }
            if (isAsync) editor.apply() else editor.commit()
        }
    }

    fun setter(isAsync: Boolean = false, vararg dataSet: Pair<String, Any>) {
        if (sharedPreferences != null) {
            val editor = sharedPreferences!!.edit()
            dataSet.forEach { data ->
                when (data.second) {
                    is String ->
                        editor.putString(data.first, data.second as String)
                    is Int ->
                        editor.putInt(data.first, data.second as Int)
                    is Long ->
                        editor.putLong(data.first, data.second as Long)
                    is Boolean ->
                        editor.putBoolean(data.first, data.second as Boolean)
                }
            }
            if (isAsync) editor.apply() else editor.commit()
        }
    }

    @Suppress("UNCHECKED_CAST")
    @Throws(NoClassDefFoundError::class)
    fun <T> getter(p: MyPair<T>): T {
        return checkPair(p)
    }

    private fun <T> checkPair(p: MyPair<T>): T {
        return when {
            p.returnType.isAssignableFrom(String::class.javaObjectType) ->
                if (sharedPreferences?.contains(p.key) == true)
                    sharedPreferences!!.getString(p.key, "") as T
                else
                    "" as T
            p.returnType.isAssignableFrom(Int::class.java) ->
                if (sharedPreferences?.contains(p.key) == true)
                    sharedPreferences!!.getInt(p.key, 0) as T
                else
                    0 as T
            p.returnType.isAssignableFrom(Long::class.java) ->
                if (sharedPreferences?.contains(p.key) == true)
                    sharedPreferences!!.getLong(p.key, 0) as T
                else
                    0 as T
            p.returnType.isAssignableFrom(Boolean::class.java) ->
                if (sharedPreferences?.contains(p.key) == true)
                    sharedPreferences!!.getBoolean(p.key, false) as T
                else
                    false as T
            else -> throw NoClassDefFoundError()
        }
    }

    fun clear() {
        sharedPreferences?.edit()?.clear()?.apply()
    }

    data class MyPair<T>(val key: String, val returnType: Class<T>)
}
