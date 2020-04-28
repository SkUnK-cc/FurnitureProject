package example.com.furnitureproject.fragment.chart.vm

import android.annotation.SuppressLint
import android.arch.lifecycle.MutableLiveData
import example.com.furnitureproject.activity.AnalyzeSetting
import example.com.furnitureproject.base.BaseViewModel
import example.com.furnitureproject.db.DbHelper
import example.com.furnitureproject.db.bean.AccountBean
import example.com.furnitureproject.eventbus.bean.EventAnalyzeSettingChange
import example.com.furnitureproject.utils.ColorUtils
import example.com.furnitureproject.utils.ToastUtil
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import lecho.lib.hellocharts.model.SliceValue

class AnalyzeFragmentVM: BaseViewModel() {
    var type = MutableLiveData<String>()
    var startTime: Long = 0L
    var endTime: Long = 0L

    private var pieData: MutableLiveData<ArrayList<SliceValue>> = MutableLiveData()
    private var rvData: MutableLiveData<List<PieItem>> = MutableLiveData()

    init {
        type.value = AnalyzeSetting.profitAnalyze
        startTime = DbHelper.getMinDate()?.time!!
        endTime = DbHelper.getMaxDate()?.time!!
        initData()
    }

    fun getPieData(): MutableLiveData<ArrayList<SliceValue>>{
        return pieData
    }

    fun getRvData(): MutableLiveData<List<PieItem>>{
        return rvData
    }

    private fun initData() {
        if(type.value == AnalyzeSetting.profitAnalyze){
            getProfitData()
        }


    }

    @SuppressLint("NewApi")
    fun getProfitData(){
        Observable.create( ObservableOnSubscribe<List<PieItem>>{
            val rawData = DbHelper.getAccountManager().getAccountList(AccountBean.TYPE_INCOME_SELL,startTime,endTime)
            if(rawData.isEmpty()){
                ToastUtil.showShort("数据为空")
                return@ObservableOnSubscribe
            }
            val map: MutableMap<String,PieItem> = mutableMapOf()
            ColorUtils.resetIndex()
            for(entry in rawData){
                val name = entry.name
                var pieItem: PieItem? = null
                if(map.containsKey(name))
                    pieItem = map[name]
                else
                    pieItem = PieItem(name,entry.note,ColorUtils.nextColor())
                pieItem!!.profit += (entry.price - entry.primeCost) * entry.count
                map[name] = pieItem!!
            }
            it.onNext(map.values.toMutableList())
//            val values = ArrayList<SliceValue>()
//            ColorUtils.resetIndex()
//            for(name in map.keys){
//                values.add(SliceValue(map[name]!!.profit,ColorUtils.nextColor()).setLabel(name))
//            }
//            it.onNext(values)
        }).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe{
            rvData.value = it
            val values = ArrayList<SliceValue>()
            for(item in it){
                values.add(SliceValue(item.profit,item.color).setLabel(item.name))
            }
            pieData.value = values

        }
    }

    fun updateSetting(event: EventAnalyzeSettingChange){
        type.value = event.type
        startTime = event.start
        endTime = event.end
    }

    class PieItem(val name: String, val note: String, val color: Int){
        var profit: Float = 0F

    }
}