package com.example.vsiyp.ui.common.database;


import android.content.Context;

import com.example.vsiyp.ui.common.bean.CloudMaterialBean;
import com.example.vsiyp.ui.common.database.bean.CloudMaterialDaoBean;
import com.example.vsiyp.ui.common.database.bean.CloudMaterialsBeanDao;
import com.example.vsiyp.ui.common.utils.KeepOriginal;
import com.huawei.hms.videoeditor.sdk.materials.network.response.MaterialsCutContentType;
import com.huawei.hms.videoeditor.sdk.util.SmartLog;

import org.greenrobot.greendao.query.WhereCondition;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@KeepOriginal
public class CloudMaterialsDataManager {
    private static final String TAG = "CloudMaterialsDataManager";

    private WeakReference<Context> mContext;

    public CloudMaterialsDataManager(Context context) {
        mContext = new WeakReference<>(context);
    }

    public boolean updateCloudMaterialsBean(CloudMaterialBean cloudMaterialBean) {
        if (mContext == null || mContext.get() == null) {
            return false;
        }
        if (cloudMaterialBean == null) {
            return false;
        }

        CloudMaterialDaoBean cloudMaterialDaoBean = new CloudMaterialDaoBean();
        cloudMaterialDaoBean.setId(cloudMaterialBean.getId());
        cloudMaterialDaoBean.setPreviewUrl(cloudMaterialBean.getPreviewUrl());
        cloudMaterialDaoBean.setName(cloudMaterialBean.getName());
        cloudMaterialDaoBean.setLocalPath(cloudMaterialBean.getLocalPath());
        cloudMaterialDaoBean.setDuration(cloudMaterialBean.getDuration());
        cloudMaterialDaoBean.setType(cloudMaterialBean.getType());
        cloudMaterialDaoBean.setCategoryName(cloudMaterialBean.getCategoryName());
        cloudMaterialDaoBean.setLocalDrawableId(cloudMaterialBean.getLocalDrawableId());

        Long insert = DBManager.getInstance(mContext.get()).insertOrReplace(cloudMaterialDaoBean);
        return insert != -1;
    }

    public List<CloudMaterialBean> queryCloudMaterialsBeanByType(int type) throws ClassCastException {
        Field[] fields = MaterialsCutContentType.class.getDeclaredFields();
        List<Integer> types = new ArrayList<>();
        try {
            for (Field field : fields) {
                String name = field.getName();
                Object tmp = field.get(name);
                if (tmp instanceof Integer) {
                    types.add((Integer) tmp);
                }
            }
        } catch (IllegalAccessException e) {
            SmartLog.e(TAG, "inner error." + e.getMessage());
        }

        if (!types.contains(type)) {
            SmartLog.e(TAG, "queryMaterialsCutContentById fail because type is illegal");
            return new ArrayList<>();
        }

        if (mContext == null || mContext.get() == null) {
            return new ArrayList<>();
        }
        List<WhereCondition> whereConditionList = new ArrayList<>();
        whereConditionList.add(CloudMaterialsBeanDao.Properties.TYPE.eq(type));
        List<CloudMaterialDaoBean> beanListTemp =
                DBManager.getInstance(mContext.get()).queryByCondition(CloudMaterialDaoBean.class, whereConditionList);

        List<CloudMaterialBean> contents = new ArrayList<>();
        if (beanListTemp != null && beanListTemp.size() > 0) {
            for (CloudMaterialDaoBean bean : beanListTemp) {
                CloudMaterialBean content = new CloudMaterialBean();
                content.setId(bean.getId());
                content.setPreviewUrl(bean.getPreviewUrl());
                content.setName(bean.getName());
                content.setLocalPath(bean.getLocalPath());
                content.setDuration(bean.getDuration());
                content.setType(bean.getType());
                content.setCategoryName(bean.getCategoryName());
                content.setLocalDrawableId(bean.getLocalDrawableId());
                contents.add(content);
            }
        }
        return contents;
    }
}
