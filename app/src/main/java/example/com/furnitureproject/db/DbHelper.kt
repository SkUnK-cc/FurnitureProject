package example.com.furnitureproject.db
import android.annotation.SuppressLint
import android.content.Context
import example.com.furnitureproject.db.bean.DetailTypeBean
import example.com.furnitureproject.db.gen.AccountBeanDao
import example.com.furnitureproject.db.gen.DaoMaster
import example.com.furnitureproject.db.gen.DaoSession
import example.com.furnitureproject.utils.TimeUtil
import java.util.*

object DbHelper {
    private val DB_NAME = "furniture.db"
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
        if(mDaoSession==null)throw Exception("DaoSession should call findView before!")
        if(accountManager==null){
            accountManager = DbAccountManager
            accountManager?.setDaoSession(mDaoSession!!)
        }
        return accountManager!!
    }

    fun getDetailTypeManager(): DbDetailTypeManager{
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

    fun initDefaultData(){
        val item1 = DetailTypeBean()
        item1.name = "广告费"
        item1.type = DetailTypeBean.TYPE_PAY_OTHER
        item1.time = Date(System.currentTimeMillis())
        getDetailTypeManager().insert(item1)

        val item2 = DetailTypeBean()
        item2.name = "运营费"
        item2.type = DetailTypeBean.TYPE_PAY_OTHER
        item2.time = Date(System.currentTimeMillis())
        getDetailTypeManager().insert(item2)

        val item3 = DetailTypeBean()
        item3.name = "人工费"
        item3.type = DetailTypeBean.TYPE_PAY_OTHER
        item3.time = Date(System.currentTimeMillis())
        getDetailTypeManager().insert(item3)

        val item4 = DetailTypeBean()
        item4.name = "办公用品"
        item4.type = DetailTypeBean.TYPE_PAY_OTHER
        item4.time = Date(System.currentTimeMillis())
        getDetailTypeManager().insert(item4)

        val item5 = DetailTypeBean()
        item5.name = "员工工资"
        item5.type = DetailTypeBean.TYPE_PAY_OTHER
        item5.time = Date(System.currentTimeMillis())
        getDetailTypeManager().insert(item5)
    }
}

