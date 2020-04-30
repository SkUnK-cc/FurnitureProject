package example.com.furnitureproject.db

import example.com.furnitureproject.db.bean.AccountBean
import example.com.furnitureproject.db.gen.AccountBeanDao
import example.com.furnitureproject.db.gen.DaoSession
import org.greenrobot.greendao.AbstractDao
import java.util.*

object DbAccountManager: DbManager<AccountBean,Long>() {

    private var mDaoSession: DaoSession? = null

    fun setDaoSession(session: DaoSession){
        this.mDaoSession = session
    }

    override fun getAbstractDao(): AbstractDao<AccountBean, Long> {
        if(mDaoSession ==null)throw Exception("DaoSession should call findView before!")
        return mDaoSession!!.accountBeanDao
    }

    /**
     *
     * @param accountType 1:支出 ， 2收入
     * @param detailType 具体类型（餐饮等）
     * @param startTime
     * @param endTime
     * @return
     */
    fun getAccountList(accountType: String, detailType: String, startTime: Date, endTime: Date): List<AccountBean> {

        val builder = getAbstractDao().queryBuilder()
                .where(AccountBeanDao.Properties.Time.between(startTime.time, endTime.time))
        if(accountType != AccountBean.TYPE_ALL){
            if (accountType == AccountBean.TYPE_INCOME_SELL) {
                builder.where(AccountBeanDao.Properties.Type.eq(accountType))
            } else if (accountType == AccountBean.TYPE_PAYOUT) {
                builder.where(AccountBeanDao.Properties.Type.notEq(AccountBean.TYPE_INCOME_SELL))
            } else {
                builder.where(AccountBeanDao.Properties.Type.eq(accountType))
            }
//            builder.where(AccountBeanDao.Properties.Type.eq(accountType))
        }
        if (detailType != AccountBean.NAME_ALL)
            builder.where(AccountBeanDao.Properties.Name.eq(detailType))
        builder.orderAsc(AccountBeanDao.Properties.Time)
        //List<AccountModel> accountList  .list();
        return builder.list()
    }

    fun getAccountList(): MutableList<AccountBean>? {
        val builder = getAbstractDao().queryBuilder()
        builder.orderAsc(AccountBeanDao.Properties.Time)
        return builder.list()
    }

    fun getAccountList(accountType: String, start: Date, end: Date): List<AccountBean>{
        val builder = getAbstractDao().queryBuilder()
                .where(AccountBeanDao.Properties.Time.between(start.time,end.time))
        if(accountType != AccountBean.TYPE_ALL){
            if (accountType == AccountBean.TYPE_INCOME_SELL) {
                builder.where(AccountBeanDao.Properties.Type.eq(accountType))
            } else if (accountType == AccountBean.TYPE_PAYOUT) {
                builder.where(AccountBeanDao.Properties.Type.notEq(AccountBean.TYPE_INCOME_SELL))
            } else {
                builder.where(AccountBeanDao.Properties.Type.eq(accountType))
            }
//            builder.where(AccountBeanDao.Properties.Type.eq(accountType))
        }
        builder.orderAsc(AccountBeanDao.Properties.Time)
        return builder.list()
    }

    fun getAccountList(accountType: String, start: Long, end: Long): List<AccountBean>{
        val builder = getAbstractDao().queryBuilder()
                .where(AccountBeanDao.Properties.Time.between(start,end))
        if(accountType != AccountBean.TYPE_ALL){
            if (accountType == AccountBean.TYPE_INCOME_SELL) {
                builder.where(AccountBeanDao.Properties.Type.eq(accountType))
            } else if (accountType == AccountBean.TYPE_PAYOUT) {
                builder.where(AccountBeanDao.Properties.Type.notEq(AccountBean.TYPE_INCOME_SELL))
            } else {
                builder.where(AccountBeanDao.Properties.Type.eq(accountType))
            }
//            builder.where(AccountBeanDao.Properties.Type.eq(accountType))
        }
        builder.orderAsc(AccountBeanDao.Properties.Time)
        return builder.list()
    }

    fun insertAccount(account: AccountBean): Long {
        return getAbstractDao().insert(account)
    }

    fun deleteAccount(account: AccountBean){
        getAbstractDao().delete(account)
    }
}