package example.com.furnitureproject.fragment.addaccount

import example.com.furnitureproject.fragment.BaseFragmentKotlin

abstract class BaseAddTransFragment: BaseFragmentKotlin() {
    companion object {
        const val PARAM_ACCOUNT = "paramAccount"
    }
    abstract fun saveTrans()
}