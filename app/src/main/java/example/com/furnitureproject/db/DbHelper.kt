package example.com.furnitureproject.db
import android.annotation.SuppressLint
import android.content.Context
import example.com.furnitureproject.db.gen.AccountBeanDao
import example.com.furnitureproject.db.gen.DaoMaster
import example.com.furnitureproject.db.gen.DaoSession
import example.com.furnitureproject.utils.TimeUtil
import java.util.*

object DbHelper {
    private val DB_NAME = "furniture.db"
//    private var accountManager: DbManager<AccountBean,Long>? = null
//    private var detailTypeBean: DbManager<DetailTypeBean,Long>? = null
    private var accountManager: DbAccountManager? = null
    private var detailTypeManager: DbDetailTypeManager? = null
    @SuppressLint("StaticFieldLeak")
    private var mHelper: DaoMaster.DevOpenHelper? = null
    private var mDaoMaster: DaoMaster? = null
    private var mDaoSession: DaoSession? = null

    fun init(context: Context){
        mHelper = DaoMaster.DevOpenHelper(context, DB_NAME,null)
        mDaoMaster = DaoMaster(mHelper?.writableDatabase)
        mDaoSession = mDaoMaster?.newSession()
    }

    fun getAccountManager(): DbAccountManager{
//        if(accountManager==null){
//            accountManager = object: DbManager<AccountBean,Long>(){
//                override fun getAbstractDao(): AbstractDao<AccountBean, Long> {
//                    if(mDaoSession==null)throw Exception("DaoSession should call findView before!")
//                    return mDaoSession!!.accountBeanDao
//                }
//            }
//        }
//        return accountManager!!
        if(mDaoSession==null)throw Exception("DaoSession should call findView before!")
        if(accountManager==null){
            accountManager = DbAccountManager
            accountManager?.setDaoSession(mDaoSession!!)
        }
        return accountManager!!
    }

    fun getDetailTypeManager(): DbDetailTypeManager{
//        if(detailTypeBean==null){
//            detailTypeBean = object: DbManager<DetailTypeBean ,Long>(){
//                override fun getAbstractDao(): AbstractDao<DetailTypeBean, Long> {
//                    if(mDaoSession==null)throw Exception("DaoSession should call findView before!")
//                    return mDaoSession!!.detailTypeBeanDao
//                }
//            }
//        }
//        return detailTypeBean!!
        if(mDaoSession==null)throw Exception("DaoSession should call findView before!")
        if(detailTypeManager==null){
            detailTypeManager = DbDetailTypeManager
            detailTypeManager?.setDaoSession(mDaoSession!!)
        }
        return detailTypeManager!!
    }

    fun clear() {
        if (mDaoSession != null) {
            mDaoSession!!.clear()
            mDaoSession = null
        }
    }

    fun close() {
        clear()
        if (mHelper != null) {
            mHelper!!.close()
            mHelper = null
        }
    }



//    fun getAccountList(accountType: String, detailType: String, startTime: Date, endTime: Date): List<AccountBean> {
//
//        val builder = getAccountManager().queryBuilder()
//                .where(AccountBeanDao.Properties.Time.between(startTime, endTime),
//                        AccountBeanDao.Properties.Type.eq(accountType))
//        if (detailType != Extra.DETAIL_TYPE_DEFAULT)
//            builder.where(AccountBeanDao.Properties.Name.eq(detailType))
//        builder.orderAsc(AccountBeanDao.Properties.Time)
//        return builder.list()
//    }

    fun getMinDate(): Date? {
        val accountList = getAccountManager().queryBuilder()
                .orderAsc(AccountBeanDao.Properties.Time)
                .offset(0)
                .limit(1)
                .list()
        if (accountList != null && accountList!!.size > 0) {
            val time = accountList!!.get(0).getTime()
            val date = Date(time)
            val calendar = Calendar.getInstance()
            calendar.time = date
            val year = calendar.get(Calendar.YEAR)
            return if (year == Calendar.getInstance().get(Calendar.YEAR))
                date
            else
                TimeUtil.getBeginDayOfYear(Date())
        } else
            return null
    }

    fun getMaxDate(): Date? {
        val accountList = getAccountManager().queryBuilder()
                .orderDesc(AccountBeanDao.Properties.Time)
                .offset(0)
                .limit(1)
                .list()
        if (accountList != null && accountList!!.size > 0) {
            val time = accountList!!.get(0).getTime()
            val date = Date(time)
            val calendar = Calendar.getInstance()
            calendar.time = date
            val year = calendar.get(Calendar.YEAR)
            return if (year == Calendar.getInstance().get(Calendar.YEAR))
                date
            else
                null
        } else
            return null

    }
}

