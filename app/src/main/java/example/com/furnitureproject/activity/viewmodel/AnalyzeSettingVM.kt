package example.com.furnitureproject.activity.viewmodel

import android.arch.lifecycle.MutableLiveData
import example.com.furnitureproject.activity.AnalyzeSetting
import example.com.furnitureproject.base.BaseViewModel

class AnalyzeSettingVM: BaseViewModel() {
    val typeList: MutableList<String> = mutableListOf(AnalyzeSetting.profitAnalyze, AnalyzeSetting.primeCostAnalyze, AnalyzeSetting.otherCostAnalyze)
    val selectType = MutableLiveData<String>()

    init {
        selectType.value = typeList[0]
    }
}