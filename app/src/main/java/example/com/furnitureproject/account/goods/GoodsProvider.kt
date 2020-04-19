package example.com.furnitureproject.account.goods

import example.com.furnitureproject.db.bean.DetailTypeBean

object GoodsProvider {

    val goods = mutableListOf<DetailTypeBean>()

    fun getAllGoods(): List<DetailTypeBean>{
        return goods
    }
}