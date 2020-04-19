package example.com.furnitureproject.db

import org.greenrobot.greendao.AbstractDao
import org.greenrobot.greendao.annotation.NotNull
import org.greenrobot.greendao.query.Query
import org.greenrobot.greendao.query.QueryBuilder


interface IDatabase<M,K>{
    fun insert(@NotNull m: M): Boolean

    fun insertOrReplace(@NotNull m: M): Boolean

    fun insertInTx(@NotNull list: List<M>): Boolean

    fun insertOrReplaceInTx(@NotNull list: List<M>): Boolean

    fun delete(@NotNull m: M): Boolean

    fun deleteByKey(@NotNull key: K): Boolean

    fun deleteInTx(@NotNull list: List<M>): Boolean

    fun deleteByKeyInTx(@NotNull vararg key: K): Boolean

    fun deleteAll(): Boolean

    fun update(@NotNull m: M): Boolean

    fun updateInTx(@NotNull vararg m: M): Boolean

    fun updateInTx(@NotNull list: List<M>): Boolean

    fun load(@NotNull key: K): M

    fun loadAll(): List<M>

    fun refresh(@NotNull m: M): Boolean

    fun runInTx(@NotNull runnable: Runnable)

    fun getAbstractDao(): AbstractDao<M,K>

    fun queryBuilder(): QueryBuilder<M>

    fun queryRaw(@NotNull where: String, @NotNull vararg selectionArg: String): List<M>

    fun queryRawCreate(@NotNull where: String, @NotNull vararg selectionArg: Any): Query<M>

    fun queryRawCreateListArgs(@NotNull where: String, @NotNull selectionArg: Collection<Any>): Query<M>
}