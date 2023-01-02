package com.example.vsiyp.ui.common.database.util;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.vsiyp.ui.common.database.bean.CloudMaterialsBeanDao;
import com.example.vsiyp.ui.common.database.dao.DaoMaster;
import com.huawei.hms.videoeditor.sdk.util.KeepOriginal;

import org.greenrobot.greendao.database.Database;

@KeepOriginal
public class MyOpenHelper extends DaoMaster.OpenHelper {
    public MyOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        DBMigrationHelper.getInstance().migrate(db, CloudMaterialsBeanDao.class);
    }
}
