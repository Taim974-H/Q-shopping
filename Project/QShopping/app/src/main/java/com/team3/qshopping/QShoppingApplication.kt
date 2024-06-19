package com.team3.qshopping

import android.app.Application

class QShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Globals.provide(this)
    }
}