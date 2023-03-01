package com.example.vsiyp.ui.mediapick.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vsiyp.R
import com.example.vsiyp.ui.common.BaseActivity
import com.example.vsiyp.ui.common.bean.Constant
import com.example.vsiyp.ui.common.bean.MediaData
import com.example.vsiyp.ui.common.listener.OnClickRepeatedListener
import com.example.vsiyp.ui.common.utils.SizeUtils
import com.example.vsiyp.ui.common.view.decoration.GridItemDividerDecoration
import com.example.vsiyp.ui.mediapick.adapter.PicturePickAdapter
import com.example.vsiyp.ui.mediapick.viewmodel.PickPictureViewModel

class PicturePickActivity : BaseActivity() {

    private var mCloseIcon: ImageView? = null
    private var mRecyclerView: RecyclerView? = null
    private var mMediaAdapter: PicturePickAdapter? = null
    private var mMediaViewModel: PickPictureViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picture_pick)

        initView()
        initObject()
        initData()
        initEvent()
    }

    private fun initView() {
        mCloseIcon = findViewById(R.id.iv_close)
        mRecyclerView = findViewById(R.id.choice_recyclerview)
    }

    private fun initObject() {
        mMediaViewModel = ViewModelProvider(this, factory).get<PickPictureViewModel>(
            PickPictureViewModel::class.java
        )
        mRecyclerView!!.setHasFixedSize(true)
        val itemAnimator = DefaultItemAnimator()
        itemAnimator.supportsChangeAnimations = false
        mRecyclerView!!.itemAnimator = itemAnimator
        mMediaAdapter = PicturePickAdapter(this)
        mRecyclerView!!.layoutManager = GridLayoutManager(this, 3)
        if (mRecyclerView!!.itemDecorationCount == 0) {
            mRecyclerView!!.addItemDecoration(
                GridItemDividerDecoration(
                    SizeUtils.dp2Px(this, 14.5f),
                    SizeUtils.dp2Px(this, 14.5f), ContextCompat.getColor(this, R.color.black)
                )
            )
        }
        mRecyclerView!!.adapter = mMediaAdapter
    }

    private fun initData() {
        mMediaViewModel!!.pageData.observe(
            this
        ) { pagedList: PagedList<MediaData?> ->
            if (pagedList.size > 0) {
                mMediaAdapter!!.submitList(pagedList)
            }
        }
    }

    private fun initEvent() {
        mCloseIcon!!.setOnClickListener(OnClickRepeatedListener { v: View? -> finish() })
        mMediaAdapter!!.setOnItemClickListener { position: Int ->
            val mediaDataList = mMediaAdapter!!.currentList
            if (mediaDataList != null && mediaDataList.size > position) {
                val mediaData = mediaDataList[position]
                val intent = Intent()
                if (mediaData != null && mediaData.path != null) {
                    intent.putExtra(
                        Constant.EXTRA_SELECT_RESULT,
                        mediaData.path
                    )
                    setResult(Constant.RESULT_CODE, intent)
                    finish()
                }
            }
        }
    }
}