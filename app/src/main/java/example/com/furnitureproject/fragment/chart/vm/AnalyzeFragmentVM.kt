package example.com.furnitureproject.fragment.chart.vm

import android.annotation.SuppressLint
import android.arch.lifecycle.MutableLiveData
import example.com.furnitureproject.activity.AnalyzeSetting
import example.com.furnitureproject.base.BaseViewModel
import example.com.furnitureproject.db.DbHelper
import example.com.furnitureproject.db.bean.AccountBean
import example.com.furnitureproject.eventbus.bean.EventAnalyzeSettingChange
import example.com.furnitureproject.utils.ColorUtils
import example.com.furnitureproject.utils.Logger
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

    fun refreshData() {
        when(type.value) {
            AnalyzeSetting.profitAnalyze -> getProfitData()
            AnalyzeSetting.primeCostAnalyze -> getPrimeCostData()
            AnalyzeSetting.otherCostAnalyze -> getOtherCostData()
        }
    }

    private fun getOtherCostData() {
        Observable.create( ObservableOnSubscribe<List<PieItem>> {
            val start = if(startTime == 0L){
                DbHelper.getMinDate()?.time
            } else {
                startTime
            }
            val end = if(endTime == 0L){
                DbHelper.getMaxDate()?.time
            } else {
                endTime
            }
            val rawData = DbHelper.getAccountManager().getAccountList(AccountBean.TYPE_PAY_OTHER,start!!,end!!)
            if(rawData.isEmpty()){
                it.onError(Exception("数据为空"))
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
                pieItem!!.count += entry.count
                pieItem!!.money += (entry.price * entry.count)
                map[name] = pieItem!!
            }
            it.onNext(map.values.toMutableList())
        }).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
            rvData.value = it
            val values = ArrayList<SliceValue>()
            for(item in it){
                values.add(SliceValue(item.money,item.color).setLabel(item.name))
            }
            pieData.value = values
        },{
            rvData.value = listOf<PieItem>()
            pieData.value = ArrayList<SliceValue>()
//            ToastUtil.showShort(it.message)
            Logger.e(it.message)
        })
    }

    private fun getPrimeCostData() {
        Observable.create( ObservableOnSubscribe<List<PieItem>> {
            val start = if(startTime == 0L){
                DbHelper.getMinDate()?.time
            } else {
                startTime
            }
            val end = if(endTime == 0L){
                DbHelper.getMaxDate()?.time
            } else {
                endTime
            }
            val rawData = DbHelper.getAccountManager().getAccountList(AccountBean.TYPE_PAY_STOCK,start!!,end!!)
            if(rawData.isEmpty()){
                it.onError(Exception("数据为空"))
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
                pieItem!!.count += entry.count
                pieItem!!.money += (entry.price * entry.count)
                map[name] = pieItem!!
            }
            it.onNext(map.values.toMutableList())
        }).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
            rvData.value = it
            val values = ArrayList<SliceValue>()
            for(item in it){
                values.add(SliceValue(item.money,item.color).setLabel(item.name))
            }
            pieData.value = values
        },{
            rvData.value = listOf<PieItem>()
            pieData.value = ArrayList<SliceValue>()
//            ToastUtil.showShort(it.message)
            Logger.e(it.message)
        })
    }


    @SuppressLint("NewApi")
    fun getProfitData(){
        Observable.create( ObservableOnSubscribe<List<PieItem>>{
            val start = if(startTime == 0L){
                DbHelper.getMinDate()?.time
            } else {
                startTime
            }

            val end = if(endTime == 0L){
                DbHelper.getMaxDate()?.time
            } else {
                endTime
            }
//            startTime = DbHelper.getMinDate()?.time!!
//            endTime = DbHelper.getMaxDate()?.time!!
            val rawData = DbHelper.getAccountManager().getAccountList(AccountBean.TYPE_INCOME_SELL,start!!,end!!)
            if(rawData.isEmpty()){
                it.onError(Exception("数据为空"))
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
                pieItem!!.count += entry.count
                pieItem!!.money += (entry.price - entry.primeCost) * entry.count
                map[name] = pieItem!!
            }
            it.onNext(map.values.toMutableList())
//            val values = ArrayList<SliceValue>()
//            ColorUtils.resetIndex()
//            for(name in map.keys){
//                values.add(SliceValue(map[name]!!.money,ColorUtils.nextColor()).setLabel(name))
//            }
//            it.onNext(values)
        }).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
            rvData.value = it
            val values = ArrayList<SliceValue>()
            for(item in it){
                values.add(SliceValue(item.money,item.color).setLabel(item.name))
            }
            pieData.value = values
        },{
            rvData.value = listOf<PieItem>()
            pieData.value = ArrayList<SliceValue>()
//            ToastUtil.showShort(it.message)
            Logger.e(it.message)
        })
    }

    fun updateSetting(event: EventAnalyzeSettingChange){
        type.value = event.type
        startTime = event.start
        endTime = event.end
        refreshData()
    }

    class PieItem(val name: String, val note: String, val color: Int){
        var money: Float = 0F
        var count: Float = 0F
    }
}