package com.example.vsiyp.ui.common.database.dao;

import com.example.vsiyp.ui.common.database.bean.CloudMaterialDaoBean;
import com.example.vsiyp.ui.common.database.bean.CloudMaterialsBeanDao;
import com.huawei.hms.videoeditor.sdk.util.KeepOriginal;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import java.util.Map;

@KeepOriginal
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig cloudMaterialsBeanDaoConfig;

    private final CloudMaterialsBeanDao cloudMaterialsBeanDao;

    public DaoSession(Database db, IdentityScopeType type,
                      Map<Class<? extends AbstractDao<?, ?>>, DaoConfig> daoConfigMap) {
        super(db);

        cloudMaterialsBeanDaoConfig = daoConfigMap.get(CloudMaterialsBeanDao.class).clone();
        cloudMaterialsBeanDaoConfig.initIdentityScope(type);

        cloudMaterialsBeanDao = new CloudMaterialsBeanDao(cloudMaterialsBeanDaoConfig, this);

        registerDao(CloudMaterialDaoBean.class, cloudMaterialsBeanDao);
    }

    public void clear() {
        cloudMaterialsBeanDaoConfig.clearIdentityScope();
    }

    public CloudMaterialsBeanDao getCloudMaterialsBeanDao() {
        return cloudMaterialsBeanDao;
    }
}
