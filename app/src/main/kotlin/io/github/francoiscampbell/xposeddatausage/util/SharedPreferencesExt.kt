package io.github.francoiscampbell.xposeddatausage.util

import android.content.SharedPreferences

/**
 * Created by francois on 16-03-17.
 */

fun SharedPreferences.getRaw(key: String): String {
    return when {
        isBoolean(key) -> getBoolean(key, false).toString()
        isFloat(key) -> getFloat(key, 0f).toString()
        isLong(key) -> getLong(key, 0L).toString()
        isInt(key) -> getInt(key, 0).toString()
        isString(key) -> getString(key, "").toString()
        isStringSet(key) -> getStringSet(key, setOf()).toString()
        else -> "null"
    }
}

fun SharedPreferences.isBoolean(key: String): Boolean {
    return try {
        getBoolean(key, false)
        true
    } catch (e: ClassCastException) {
        false
    }
}

fun SharedPreferences.isFloat(key: String): Boolean {
    return try {
        getFloat(key, 0f)
        true
    } catch (e: ClassCastException) {
        false
    }
}

fun SharedPreferences.isLong(key: String): Boolean {
    return try {
        getLong(key, 0L)
        true
    } catch (e: ClassCastException) {
        false
    }
}

fun SharedPreferences.isInt(key: String): Boolean {
    return try {
        getInt(key, 0)
        true
    } catch (e: ClassCastException) {
        false
    }
}

fun SharedPreferences.isString(key: String): Boolean {
    return try {
        getString(key, "")
        true
    } catch (e: ClassCastException) {
        false
    }
}

fun SharedPreferences.isStringSet(key: String): Boolean {
    return try {
        getStringSet(key, setOf())
        true
    } catch (e: ClassCastException) {
        false
    }
}