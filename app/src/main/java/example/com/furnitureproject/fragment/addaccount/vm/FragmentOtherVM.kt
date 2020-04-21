package example.com.furnitureproject.fragment.addaccount.vm

import example.com.furnitureproject.R
import example.com.furnitureproject.base.BaseViewModel
import example.com.furnitureproject.db.DbHelper
import example.com.furnitureproject.db.bean.AccountBean
import example.com.furnitureproject.db.bean.DetailTypeBean
import example.com.furnitureproject.eventbus.bean.EventAddOtherTrans
import org.greenrobot.eventbus.EventBus
import java.util.*
import kotlin.math.roundToInt

class FragmentOtherVM: BaseViewModel() {
    var selectGoods: DetailTypeBean? = null

    var accountBean: AccountBean = AccountBean()

    fun doSave(){

    }

    fun saveTrans(count: String, note: String, time: Long){
        accountBean.typeId = selectGoods?.id
        accountBean.name = selectGoods?.name
        accountBean.primeCost = selectGoods?.primeCost!!
        if(count.isNotEmpty()){
            accountBean.count = count.toFloat()
        }else{
            accountBean.count = 0f
        }
        accountBean.note = note
//        if(price.isNotEmpty()){
//            accountBean.price = price.toFloat()
//        }else{
//            accountBean.price = 0f
//        }
        accountBean.type = AccountBean.TYPE_PAY_OTHER
        accountBean.picRes = R.drawable.ic_payout
        accountBean.time = time
        val id = DbHelper.getAccountManager().insertAccount(accountBean)
        EventBus.getDefault().post(EventAddOtherTrans())
    }

    fun updateStock(count: String, time: Long) {
        if(count.isNotEmpty()){
            selectGoods?.stockCount = selectGoods?.stockCount!! +count.toFloat().roundToInt()
        }
        selectGoods?.time = Date(time)
        DbHelper.getDetailTypeManager().update(selectGoods!!)
    }
}