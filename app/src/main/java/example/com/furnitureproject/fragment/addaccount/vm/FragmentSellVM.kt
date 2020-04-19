package example.com.furnitureproject.fragment.addaccount.vm

import example.com.furnitureproject.R
import example.com.furnitureproject.base.BaseViewModel
import example.com.furnitureproject.db.DbHelper
import example.com.furnitureproject.db.bean.AccountBean
import example.com.furnitureproject.db.bean.DetailTypeBean
import example.com.furnitureproject.eventbus.bean.EventAddSellTrans
import org.greenrobot.eventbus.EventBus

class FragmentSellVM: BaseViewModel() {
    var selectGoods: DetailTypeBean? = null

    var accountBean: AccountBean = AccountBean()

    fun doSave(){

    }

    fun initBean(count: String, price: String, note: String, time: Long){
        accountBean.typeId = selectGoods?.id
        accountBean.name = selectGoods?.name
        accountBean.primeCost = selectGoods?.primeCost!!
        if(count.isNotEmpty()){
            accountBean.count = count.toFloat()
        }else{
            accountBean.count = 0f
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
        DbHelper.getAccountManager().insertAccount(accountBean)
        EventBus.getDefault().post(EventAddSellTrans())
    }
}