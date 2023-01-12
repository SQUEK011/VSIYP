package com.example.vsiyp

import android.Manifest
import com.example.vsiyp.view.NoScrollViewPager
import com.google.android.material.tabs.TabLayout
import android.os.Bundle
import com.example.vsiyp.R
import com.example.vsiyp.ui.mediaexport.utils.InfoStateUtil
import com.example.vsiyp.VideoEditorApplication
import com.example.vsiyp.ui.mediaeditor.menu.MenuConfig
import com.huawei.hms.videoeditor.sdk.MediaApplication
import com.example.vsiyp.ui.template.module.TemplateHomeFragment
import com.example.vsiyp.MainActivity
import com.example.vsiyp.fragment.ClipFragment
import androidx.fragment.app.FragmentPagerAdapter
import android.view.ViewGroup
import android.view.LayoutInflater
import android.widget.TextView
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.vsiyp.ui.common.BaseActivity
import com.example.vsiyp.utils.SmartLog
import com.huawei.secure.android.common.intent.SafeIntent
import java.lang.Exception
import java.util.*

class MainActivity : BaseActivity() {
    private val mPermissions = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.INTERNET,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.CAMERA
    )
    private var viewPager: NoScrollViewPager? = null
    private val mFragments: MutableList<Fragment> = ArrayList()
    private var tabLayout: TabLayout? = null
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
        tabLayout = findViewById(R.id.home_tabLayout)
    }

    private fun initData() {
        MediaApplication.getInstance().setApiKey("DAEDAPIVpsDTzvZB856dCzx04h9YnVxNJnm27mm+NIFiDVwnItB2RkU/KPyzbeO+KpIzPNF4AX9a4ghyzh1vFzfhUAIhJCs1Q/NYJg==")
        val uuid = UUID.randomUUID()
        MediaApplication.getInstance().setLicenseId(uuid.toString())
        //val tabItemImage = intArrayOf(R.drawable.home_tab_clip_selector, R.drawable.home_tab_draft_selector)
        //val tabItemText = arrayOf(getString(R.string.first_menu_cut), getString(R.string.home_tab1))
        /*val templateFragment = TemplateHomeFragment()
        val bundle = Bundle()
        val safeIntent = SafeIntent(intent)
        val source = safeIntent.getStringExtra(SOURCE)
        bundle.putString(TemplateHomeFragment.SOURCE_KEY, source)
        templateFragment.arguments = bundle*/
        mFragments.add(ClipFragment())
        //mFragments.add(templateFragment)
        val mAdapter: FragmentPagerAdapter = object : FragmentPagerAdapter(mFragmentManager!!, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
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
        /*tabLayout!!.setupWithViewPager(viewPager)
        tabLayout!!.tabGravity = TabLayout.GRAVITY_FILL
        tabLayout!!.tabMode = TabLayout.MODE_FIXED
        /*for (i in mFragments.indices) {
            val view = LayoutInflater.from(this)
                .inflate(R.layout.activity_main_tab_item, null, false)
            val imageMainTabItem = view.findViewById<ImageView>(R.id.image_main_tab_item)
            val textMainTabItem = view.findViewById<TextView>(R.id.text_main_tab_item)
            imageMainTabItem.setImageResource(tabItemImage[i])
            textMainTabItem.text = tabItemText[i]
            val tab = tabLayout!!.getTabAt(i)
            if (tab != null) {
                tab.customView = view
            }
        }*/
        val tab = tabLayout!!.getTabAt(initIndex)
        tab?.select()
        tabLayout!!.visibility = View.VISIBLE*/
    }

    override fun onPause() {
        super.onPause()
        initIndex = 0
    }

    private fun initEvent() {}

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
        const val SOURCE = "source"
        private var initIndex = 0
    }
}