package com.example.vsiyp

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SettingActivity : AppCompatActivity() {
    private var mBack: TextView? = null
    private var mPrivacyBtn: ImageView? = null
    private var mAbtBtn: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        initView()
        initEvent()
    }

    private fun initEvent() {
        mBack!!.setOnClickListener { finish() }
        mPrivacyBtn!!.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://developer.huawei.com/consumer/en/doc/development/Media-Guides/sdk-data-security-0000001147828303")
                )
            )
        }

        mAbtBtn!!.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://developer.huawei.com/consumer/en/hms/huawei-video-editor/")
                )
            )

        }
    }

    private fun initView() {
        mBack = findViewById(R.id.back)
        val mVersion = findViewById<TextView>(R.id.version)
        mVersion.text = versionName

        mPrivacyBtn = findViewById(R.id.privacy_btn)
        mAbtBtn = findViewById(R.id.about_btn)
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
        const val DEFAULT_VERSION = "1.0"
    }
}