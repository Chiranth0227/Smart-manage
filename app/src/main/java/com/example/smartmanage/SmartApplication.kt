package com.example.smartmanage

import android.app.Application
import com.example.smartmanage.data.AppDatabase

class SmartApplication : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
}
