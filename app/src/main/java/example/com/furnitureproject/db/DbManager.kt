package example.com.furnitureproject.db

import org.greenrobot.greendao.AbstractDao
import org.greenrobot.greendao.query.Query
import org.greenrobot.greendao.query.QueryBuilder

abstract class DbManager<M,K>: IDatabase<M,K> {

    override fun insert(m: M): Boolean {
        getAbstractDao().insert(m)
        return true
    }

    override fun insertOrReplace(m: M): Boolean {
        getAbstractDao().insertOrReplace(m)
        return true
    }

    override fun insertInTx(list: List<M>): Boolean {
        getAbstractDao().insertInTx(list)
        return true
    }

    override fun insertOrReplaceInTx(list: List<M>): Boolean {
        getAbstractDao().insertOrReplaceInTx(list)
        return true
    }

    override fun delete(m: M): Boolean {
        getAbstractDao().delete(m)
        return true
    }

    override fun deleteByKey(key: K): Boolean {
        getAbstractDao().deleteByKey(key)
        return true
    }

    override fun deleteInTx(list: List<M>): Boolean {
        getAbstractDao().deleteInTx(list)
        return true
    }

    override fun deleteAll(): Boolean {
        getAbstractDao().deleteAll()
        return true
    }

    override fun deleteByKeyInTx(vararg key: K): Boolean {
        getAbstractDao().deleteByKeyInTx(*key)
        return true
    }

    override fun update(m: M): Boolean {
        getAbstractDao().update(m)
        return true
    }


    override fun updateInTx(list: List<M>): Boolean {
        getAbstractDao().updateInTx(list)
        return true
    }

    override fun updateInTx(vararg m: M): Boolean {
        getAbstractDao().updateInTx(*m)
        return true
    }

    override fun load(key: K): M {
        return getAbstractDao().load(key)
    }

    override fun loadAll(): List<M> {
        return getAbstractDao().loadAll()
    }

    override fun refresh(m: M): Boolean {
        getAbstractDao().refresh(m)
        return true
    }

    override fun runInTx(runnable: Runnable) {
        getAbstractDao().session.runInTx(runnable)
    }

    override fun queryBuilder(): QueryBuilder<M> {
        return getAbstractDao().queryBuilder()
    }

    override fun queryRaw(where: String, vararg selectionArg: String): List<M> {
        return getAbstractDao().queryRaw(where, *selectionArg)
    }

    override fun queryRawCreate(where: String, vararg selectionArg: Any): Query<M> {
        return getAbstractDao().queryRawCreate(where,selectionArg)
    }

    override fun queryRawCreateListArgs(where: String, selectionArg: Collection<Any>): Query<M> {
        return getAbstractDao().queryRawCreateListArgs(where, selectionArg)
    }

    abstract override fun getAbstractDao(): AbstractDao<M, K>
}