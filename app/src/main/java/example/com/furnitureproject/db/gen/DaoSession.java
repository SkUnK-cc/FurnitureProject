package example.com.furnitureproject.db.gen;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import example.com.furnitureproject.db.bean.AccountBean;
import example.com.furnitureproject.db.bean.DetailTypeBean;

import example.com.furnitureproject.db.gen.AccountBeanDao;
import example.com.furnitureproject.db.gen.DetailTypeBeanDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig accountBeanDaoConfig;
    private final DaoConfig detailTypeBeanDaoConfig;

    private final AccountBeanDao accountBeanDao;
    private final DetailTypeBeanDao detailTypeBeanDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        accountBeanDaoConfig = daoConfigMap.get(AccountBeanDao.class).clone();
        accountBeanDaoConfig.initIdentityScope(type);

        detailTypeBeanDaoConfig = daoConfigMap.get(DetailTypeBeanDao.class).clone();
        detailTypeBeanDaoConfig.initIdentityScope(type);

        accountBeanDao = new AccountBeanDao(accountBeanDaoConfig, this);
        detailTypeBeanDao = new DetailTypeBeanDao(detailTypeBeanDaoConfig, this);

        registerDao(AccountBean.class, accountBeanDao);
        registerDao(DetailTypeBean.class, detailTypeBeanDao);
    }
    
    public void clear() {
        accountBeanDaoConfig.clearIdentityScope();
        detailTypeBeanDaoConfig.clearIdentityScope();
    }

    public AccountBeanDao getAccountBeanDao() {
        return accountBeanDao;
    }

    public DetailTypeBeanDao getDetailTypeBeanDao() {
        return detailTypeBeanDao;
    }

}
