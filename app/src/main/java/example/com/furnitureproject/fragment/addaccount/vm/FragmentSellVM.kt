package example.com.furnitureproject.fragment.addaccount.vm

import example.com.furnitureproject.R
import example.com.furnitureproject.base.BaseViewModel
import example.com.furnitureproject.db.DbHelper
import example.com.furnitureproject.db.bean.AccountBean
import example.com.furnitureproject.db.bean.DetailTypeBean
import example.com.furnitureproject.eventbus.bean.EventAddSellTrans
import example.com.furnitureproject.eventbus.bean.EventUpdateSell
import org.greenrobot.eventbus.EventBus

class FragmentSellVM: BaseViewModel() {
    var selectGoods: DetailTypeBean? = null

    var accountBean: AccountBean = AccountBean()

    fun doSave(){

    }

    fun initAccountBean(accb: AccountBean){
        this.accountBean = accb
    }

    fun updateStock(count: String){
        selectGoods?.stockCount = selectGoods?.stockCount!! - count.toInt()
        DbHelper.getDetailTypeManager().update(selectGoods!!)
    }

    fun saveTrans(count: String, price: String, note: String, time: Long){
        accountBean.typeId = selectGoods?.id
        accountBean.name = selectGoods?.name
        accountBean.primeCost = selectGoods?.primeCost!!
        if(count.isNotEmpty()){
            accountBean.count = count.toLong()
        }else{
            accountBean.count = 0
        }
        accountBean.note = note
        if(price.isNotEmpty()){
            accountBean.price = price.toFloat()
        }else{
            accountBean.price = 0f
        }
        accountBean.type = AccountBean.TYPE_INCOME_SELL
        accountBean.picRes = R.drawable.ic_income
        accountBean.time = time
        val id = DbHelper.getAccountManager().insertAccount(accountBean)
        EventBus.getDefault().post(EventAddSellTrans())
    }

    fun updateTrans(count: String, price: String, note: String, time: Long){
        accountBean.typeId = selectGoods?.id
        accountBean.name = selectGoods?.name
        accountBean.primeCost = selectGoods?.primeCost!!
        if(count.isNotEmpty()){
            accountBean.count = count.toLong()
        }else{
            accountBean.count = 0
        }
        accountBean.note = note
        if(price.isNotEmpty()){
            accountBean.price = price.toFloat()
        }else{
            accountBean.price = 0f
        }
        accountBean.type = AccountBean.TYPE_INCOME_SELL
        accountBean.picRes = R.drawable.ic_income
        accountBean.time = time
        val id = DbHelper.getAccountManager().update(accountBean)
        EventBus.getDefault().post(EventUpdateSell())
    }
}