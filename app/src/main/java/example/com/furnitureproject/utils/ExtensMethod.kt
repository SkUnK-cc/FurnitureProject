package example.com.furnitureproject.utils

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment

fun <T: ViewModel> Fragment.vm(clazz: Class<T>): T {
    val vm: ViewModel by lazy {
        ViewModelProviders.of(this).get(clazz)
    }
    return vm as T
}