package com.example.taskmanager.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

fun <T> String.convertJsonToModel(modelClass: Class<T>): T {
    val gson = GsonBuilder()
        .serializeNulls()
        .create()

    return gson.fromJson(this, modelClass)
}

inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, object: TypeToken<T>() {}.type)

fun Any.convertObjectToString(): String {

    val gson = GsonBuilder()
        .serializeNulls()
        .create()

    return gson.toJson(this)
}