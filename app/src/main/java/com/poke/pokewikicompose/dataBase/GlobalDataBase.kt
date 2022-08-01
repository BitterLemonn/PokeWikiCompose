package com.poke.pokewikicompose.dataBase

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.poke.pokewikicompose.dataBase.db.AppDataBase

class GlobalDataBase : Application() {
    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        lateinit var database: AppDataBase
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        database = AppDataBase.getInstance(context)
    }

}