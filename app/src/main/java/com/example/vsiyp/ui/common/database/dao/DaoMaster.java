package com.example.vsiyp.ui.common.database.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.vsiyp.ui.common.database.bean.CloudMaterialsBeanDao;
import com.huawei.hms.videoeditor.sdk.util.KeepOriginal;
import com.huawei.hms.videoeditor.sdk.util.SmartLog;

import org.greenrobot.greendao.AbstractDaoMaster;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseOpenHelper;
import org.greenrobot.greendao.database.StandardDatabase;
import org.greenrobot.greendao.identityscope.IdentityScopeType;

@KeepOriginal
public class DaoMaster extends AbstractDaoMaster {
    public static final int SCHEMA_DB_VERSION = 1;

    /**
     * Creates underlying database table using DAOs.
     */
    public static void createAllTables(Database db, boolean ifNotExists) {
        CloudMaterialsBeanDao.createTable(db, ifNotExists);
    }

    /**
     * Drops underlying database table using DAOs.
     */
    public static void dropAllTables(Database db, boolean ifExists) {
        CloudMaterialsBeanDao.dropTable(db, ifExists);
    }

    /**
     * WARNING: Drops all table on Upgrade! Use only during development.
     */
    public static DaoSession newDevSession(Context context, String dbName) {
        Database database = new DevOpenHelper(context, dbName).getWritableDb();
        DaoMaster dDaoMaster = new DaoMaster(database);
        return dDaoMaster.newSession();
    }

    public DaoMaster(SQLiteDatabase sqLiteDatabase) {
        this(new StandardDatabase(sqLiteDatabase));
    }

    public DaoMaster(Database database) {
        super(database, SCHEMA_DB_VERSION);
        registerDaoClass(CloudMaterialsBeanDao.class);
    }

    public DaoSession newSession() {
        return new DaoSession(db, IdentityScopeType.Session, daoConfigMap);
    }

    public DaoSession newSession(IdentityScopeType type) {
        return new DaoSession(db, type, daoConfigMap);
    }

    public static abstract class OpenHelper extends DatabaseOpenHelper {
        public OpenHelper(Context context, String dbName) {
            super(context, dbName, SCHEMA_DB_VERSION);
        }

        public OpenHelper(Context context, String dbName, SQLiteDatabase.CursorFactory cursorFactory) {
            super(context, dbName, cursorFactory, SCHEMA_DB_VERSION);
        }

        @Override
        public void onCreate(Database database) {
            SmartLog.i("DaoMaster", "Creating tables for schema version " + SCHEMA_DB_VERSION);
            createAllTables(database, false);
        }
    }

    public static class DevOpenHelper extends OpenHelper {
        public DevOpenHelper(Context con, String dbName) {
            super(con, dbName);
        }

        public DevOpenHelper(Context con, String dbName, SQLiteDatabase.CursorFactory cursorFactory) {
            super(con, dbName, cursorFactory);
        }

        @Override
        public void onUpgrade(Database database, int oldVersion, int newVersion) {
            SmartLog.d("greenDAO",
                    "Upgrading schema from version " + oldVersion + " to " + newVersion + " by dropping all tables");
            dropAllTables(database, true);
            onCreate(database);
        }
    }

}
