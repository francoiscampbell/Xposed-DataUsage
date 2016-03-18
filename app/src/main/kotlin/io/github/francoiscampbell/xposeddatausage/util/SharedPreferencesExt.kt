package io.github.francoiscampbell.xposeddatausage.util

import android.content.SharedPreferences

/**
 * Created by francois on 16-03-17.
 */

fun SharedPreferences.get(key: String): Any? {
    return when {
        isBoolean(key) -> getBoolean(key, false)
        isFloat(key) -> getFloat(key, 0f)
        isLong(key) -> getLong(key, 0L)
        isInt(key) -> getInt(key, 0)
        isString(key) -> getString(key, "")
        isStringSet(key) -> getStringSet(key, setOf())
        else -> null
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