package com.wooze.mid_point

import android.app.Application
import android.util.Log
import com.wooze.mid_point.data.DataStoreManager

class MyApplication : Application() {
    lateinit var dataStoreManager: DataStoreManager

    override fun onCreate() {
        super.onCreate()
        Log.d("mpDebug", "m ike")
        dataStoreManager = DataStoreManager(this)
    }

}