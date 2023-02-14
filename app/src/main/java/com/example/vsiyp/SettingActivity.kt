package com.example.vsiyp

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SettingActivity : AppCompatActivity() {
    private var mBack: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        initView()
        initEvent()
    }

    private fun initEvent() {
        mBack!!.setOnClickListener { finish() }
    }

    private fun initView() {
        mBack = findViewById(R.id.back)
        val mVersion = findViewById<TextView>(R.id.version)
        mVersion.text = versionName
    }

    /**
     * get App versionName
     *
     * @return version name
     */
    private val versionName: String
        get() {
            val packageManager = this.packageManager
            try {
                val packageInfo = packageManager.getPackageInfo(this.packageName, 0)
                return packageInfo.versionName
            } catch (e: PackageManager.NameNotFoundException) {
                Log.e("SettingActivity", "Failed to get package version: " + e.message)
            }
            return DEFAULT_VERSION
        }

    companion object {
        const val DEFAULT_VERSION = "1.1.0.301"
    }
}