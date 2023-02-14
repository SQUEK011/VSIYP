package com.example.vsiyp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.vsiyp.fragment.ClipFragment
import com.example.vsiyp.ui.common.BaseActivity
import com.example.vsiyp.ui.mediaeditor.menu.MenuConfig
import com.example.vsiyp.ui.mediaexport.utils.InfoStateUtil
import com.example.vsiyp.utils.SmartLog
import com.example.vsiyp.view.NoScrollViewPager
import com.huawei.hms.videoeditor.sdk.MediaApplication
import com.huawei.secure.android.common.intent.SafeIntent
import java.util.*

class MainActivity : BaseActivity() {
    private val mPermissions = arrayOf(
        Manifest.permission.MANAGE_EXTERNAL_STORAGE,
        Manifest.permission.INTERNET,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.READ_MEDIA_VIDEO,
        Manifest.permission.READ_MEDIA_IMAGES,
        Manifest.permission.READ_MEDIA_AUDIO
    )
    private var viewPager: NoScrollViewPager? = null
    private val mFragments: MutableList<Fragment> = ArrayList()
    private var mFragmentManager: FragmentManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        verifyStoragePermissions(this)
        statusBarColor = R.color.home_color_FF181818
        navigationBarColor = R.color.home_color_FF181818
        super.onCreate(savedInstanceState)
        InfoStateUtil.getInstance().checkInfoState(this)
        VideoEditorApplication.getInstance().context = this
        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_main)
        mFragmentManager = supportFragmentManager
        MenuConfig.getInstance().initMenuConfig(this)
        initView()
        initData()
        initEvent()
    }

    private fun initView() {
        viewPager = findViewById<View>(R.id.home_viewPager) as NoScrollViewPager
    }

    private fun initData() {
        //String apiKey = AGConnectInstance.getInstance().getOptions().getString("client/api_key");
        MediaApplication.getInstance()
            .setApiKey("DAEDAPIVpsDTzvZB856dCzx04h9YnVxNJnm27mm+NIFiDVwnItB2RkU/KPyzbeO+KpIzPNF4AX9a4ghyzh1vFzfhUAIhJCs1Q/NYJg==")
        val uuid = UUID.randomUUID()
        MediaApplication.getInstance().setLicenseId(uuid.toString())

        mFragments.add(ClipFragment())
        val mAdapter: FragmentPagerAdapter = object :
            FragmentPagerAdapter(mFragmentManager!!, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            override fun getItem(position: Int): Fragment {
                return mFragments[position]
            }

            override fun getCount(): Int {
                return mFragments.size
            }

            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                val fragment = super.instantiateItem(container, position) as Fragment
                if (!mFragmentManager!!.isDestroyed) {
                    mFragmentManager!!.beginTransaction().show(fragment).commitAllowingStateLoss()
                }
                return fragment
            }

            override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
                val fragment = mFragments[position]
                if (!mFragmentManager!!.isDestroyed) {
                    mFragmentManager!!.beginTransaction().hide(fragment).commitAllowingStateLoss()
                }
            }
        }
        viewPager!!.adapter = mAdapter
        viewPager!!.offscreenPageLimit = 4
        viewPager!!.currentItem = initIndex
    }

    override fun onPause() {
        super.onPause()
        initIndex = 0
    }

    private fun initEvent() {}
    override fun onBackPressed() {
        super.onBackPressed()
        initIndex = 0
    }

    private fun verifyStoragePermissions(activity: MainActivity) {
        val requestCode = 1
        try {
            for (i in mPermissions.indices) {
                val permisson = mPermissions[i]
                val permissionRead = ActivityCompat.checkSelfPermission(activity, permisson)
                if (permissionRead != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity, mPermissions, requestCode)
                }
            }
        } catch (e: Exception) {
            SmartLog.e("MainActivity", e.message)
        }
    }

    companion object {
        //private TabLayout tabLayout;
        private var initIndex = 0
    }
}