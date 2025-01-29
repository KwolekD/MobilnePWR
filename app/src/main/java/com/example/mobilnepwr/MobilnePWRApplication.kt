package com.example.mobilnepwr

import android.app.Application
import com.example.mobilnepwr.data.AppContainer
import com.example.mobilnepwr.data.AppDataContainer

class MobilnePWRApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }

    fun setTestContainer(testContainer: AppContainer) {
        container = testContainer
    }
}
