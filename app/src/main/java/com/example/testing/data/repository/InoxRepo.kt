package com.example.testing.data.repository

import android.content.Context
import com.example.testing.data.model.RootModel
import com.google.gson.Gson

class InoxRepo(private val  context: Context){

    fun getFoodDetails() : RootModel {
        val jsonString = context.assets.open("fnb.json")
            .bufferedReader()
            .use { it.readText() }

        return Gson().fromJson(jsonString, RootModel::class.java)
    }
}