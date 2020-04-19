package example.com.furnitureproject.db

import example.com.furnitureproject.db.bean.DetailTypeBean
import example.com.furnitureproject.db.gen.DaoSession
import example.com.furnitureproject.db.gen.DetailTypeBeanDao
import org.greenrobot.greendao.AbstractDao
import java.util.*

object DbDetailTypeManager: DbManager<DetailTypeBean,Long>() {

    private var mDaoSession: DaoSession? = null

    fun setDaoSession(daoSession: DaoSession){
        this.mDaoSession = daoSession
    }

    override fun getAbstractDao(): AbstractDao<DetailTypeBean, Long> {
        if(mDaoSession ==null)throw Exception("DaoSession should call init before!")
        return mDaoSession!!.detailTypeBeanDao
    }

    /**
     * 获取所有商品类
     */
    fun getDetailTypeList(accountType: String): List<DetailTypeBean>{
        var builder = getAbstractDao().queryBuilder()
        if(accountType != DetailTypeBean.TYPE_ALL){
            builder.where(DetailTypeBeanDao.Properties.Type.eq(accountType))
        }
        builder.orderAsc(DetailTypeBeanDao.Properties.Time)
        return builder.list()
    }

    fun getDetailTypeList(accountType: String, start: Date, end: Date): List<DetailTypeBean>{
        var builder = getAbstractDao().queryBuilder()
                .where(DetailTypeBeanDao.Properties.Time.between(start,end))
        if(accountType != DetailTypeBean.TYPE_ALL){
            builder.where(DetailTypeBeanDao.Properties.Type.eq(accountType))
        }
        builder.orderAsc(DetailTypeBeanDao.Properties.Time)
        return builder.list()
    }


}