package com.example.vsiyp.ui.common.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.vsiyp.ui.common.database.dao.DaoMaster;
import com.example.vsiyp.ui.common.database.dao.DaoSession;
import com.example.vsiyp.ui.common.database.util.MyOpenHelper;
import com.huawei.hms.videoeditor.sdk.util.KeepOriginal;
import com.huawei.hms.videoeditor.sdk.util.SmartLog;

import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@KeepOriginal
public class DBManager {
    private static final String TAG = "BaseDBManager";

    private static final String DATABASE_NAME = "testui.db";

    private volatile DaoSession daoSession;

    private static WeakReference<Context> mContext;

    private static class BaseDBManagerHolder {
        private static final DBManager INSTANCE = new DBManager();
    }

    public static DBManager getInstance(Context context) {
        mContext = new WeakReference<>(context);
        return BaseDBManagerHolder.INSTANCE;
    }

    public DBManager() {
        initGreenDao();
    }

    private void initGreenDao() {
        Context context = mContext == null ? null : mContext.get();
        if (context != null) {
            MyOpenHelper databaseOpenHelper = new MyOpenHelper(context, DATABASE_NAME, null);
            SQLiteDatabase db = databaseOpenHelper.getWritableDatabase();
            DaoMaster daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
        }
    }

    public <T> List<T> queryAll(Class<T> bean) {
        if (daoSession == null) {
            return Collections.emptyList();
        }
        List<T> beanList = new ArrayList<>();
        try {
            beanList.addAll((List<T>) daoSession.getDao(bean).loadAll());
        } catch (Exception e) {
            SmartLog.d(TAG, "queryAll fail：" + e.getMessage());
        }
        return beanList;
    }

    public <T> List<T> queryByCondition(Class<T> bean, List<WhereCondition> whereConditions) {
        List<T> beanList = null;
        try {
            QueryBuilder<T> queryBuilder = daoSession.queryBuilder(bean);
            if (whereConditions != null) {
                for (WhereCondition whereCondition : whereConditions) {
                    queryBuilder.where(whereCondition);
                }
            }
            beanList = queryBuilder.build().list();
        } catch (Exception e) {
            SmartLog.e(TAG, "queryConditionAll fail：" + e.getMessage());
        }
        return beanList;
    }

    public <T> List<T> querySqlCondition(Class<T> bean, String sqlConditions) {
        List<T> beanList = null;
        try {
            WhereCondition.StringCondition stringCondition = new WhereCondition.StringCondition(sqlConditions);
            QueryBuilder<T> queryBuilder = daoSession.queryBuilder(bean);
            queryBuilder.where(stringCondition);

            beanList = queryBuilder.build().list();
        } catch (Exception e) {
            SmartLog.e(TAG, "queryConditionAll fail：" + e.getMessage());
        }
        return beanList;
    }

    public <T> Long insert(T bean) {
        try {
            return daoSession.insert(bean);
        } catch (Exception e) {
            SmartLog.e(TAG, "insertDbBean fail：" + e.getMessage());
        }
        return -1L;
    }

    public <T> void update(T bean) {
        try {
            daoSession.update(bean);
        } catch (Exception e) {
            SmartLog.e(TAG, "updateDbBean fail：" + e.getMessage());
        }
    }

    public <T> Long insertOrReplace(T bean) {
        try {
            return daoSession.insertOrReplace(bean);
        } catch (Exception e) {
            SmartLog.e(TAG, "insertOrReplaceDbBean fail：" + e.getMessage());
        }
        return -1L;
    }

    public <T> void delete(T bean) {
        try {
            daoSession.delete(bean);
        } catch (Exception e) {
            SmartLog.e(TAG, "deleteDbBean fail：" + e.getMessage());
        }
    }

    public <T> void deleteAll(T bean) {
        try {
            daoSession.deleteAll(bean.getClass());
        } catch (Exception e) {
            SmartLog.e(TAG, "deleteAllDbBean fail：" + e.getMessage());
        }
    }
}
