package com.poke.pokewikicompose.dataBase

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy

class GlobalDataBase : Application() {
    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        lateinit var database: AppDataBase
    }

    override fun onCreate() {
        super.onCreate()
        // 初始化更好的Logger
        val strategy = PrettyFormatStrategy.newBuilder()
            .tag("Logger")
            .build()
        Logger.addLogAdapter(AndroidLogAdapter(strategy))
        context = applicationContext
        database = AppDataBase.getInstance(context)
    }

}