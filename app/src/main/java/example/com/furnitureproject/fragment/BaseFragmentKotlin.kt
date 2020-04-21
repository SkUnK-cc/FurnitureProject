package example.com.furnitureproject.fragment

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import example.com.furnitureproject.R
import example.com.furnitureproject.eventbus.bean.MessageEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

abstract class BaseFragmentKotlin: Fragment() {

    private lateinit var rootView: View
    var context: FragmentActivity? = null
    var mIsVisible = false
    private var mRootView: View? = null
    private var mProgressDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = activity
        EventBus.getDefault().register(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(getLayoutId(),container,false)
        findView(rootView)
        initData()
        initView(rootView)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        initData()
//        initView(rootView)
    }

    public open fun findView(view: View) {
        initDialog()
    }

    fun initDialog() {
        mProgressDialog = Dialog(context, R.style.progress_dialog)
        mProgressDialog?.setContentView(R.layout.dialog_progress)
        mProgressDialog?.setCancelable(true)
        mProgressDialog?.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        //        TextView msg = (TextView) mProgressDialog.findViewById(R.id.id_tv_loadingmsg);
        //        msg.setText("卖力加载中");

    }

    protected fun showProgressDialog() {
        if (mProgressDialog != null && !mProgressDialog!!.isShowing()) {
            mProgressDialog!!.show()
        }
    }

    protected fun dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog!!.isShowing()) {
            mProgressDialog!!.dismiss()
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        //System.out.println("Infor_Fragment----setUserVisibleHint");
        if (userVisibleHint) {
            mIsVisible = true
//            loadData()
            //            initData();
            //            initView(mRootView);
        } else {
            mIsVisible = false
            //可以做一些销毁的操作。
        }
    }

//    protected fun loadData() {}

    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun eventNotify(e: MessageEvent){

    }

    abstract fun initView(rootView: View)

    abstract fun initData()

    abstract fun getLayoutId(): Int
}