package example.com.furnitureproject.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import android.widget.FrameLayout
import example.com.furnitureproject.R
import example.com.furnitureproject.custom.viewpager.NoScrollViewPager
import example.com.furnitureproject.db.bean.AccountBean
import example.com.furnitureproject.eventbus.bean.EventUpdateSell
import example.com.furnitureproject.fragment.addaccount.*
import example.com.furnitureproject.utils.ToastUtil
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class AccountEditActivity : BaseActivity(), View.OnClickListener {
    companion object {
        const val PARAM_ACCOUNT = "paramAccount"
        const val TYPE_INCOME_SELL = "商品收入"
        const val TYPE_PAY_STOCK = "进货"
        const val TYPE_PAY_OTHER = "其他支出"
    }
    var type: String = ""

    var fragment: Fragment? = null
    var accountBean: AccountBean? = null
    private var viewPager: NoScrollViewPager? = null
    private var ll_title_contract: FrameLayout? = null
    private var ll_title_return: FrameLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_edit)
        accountBean = intent.getParcelableExtra(PARAM_ACCOUNT)
        type = accountBean?.type!!
        if(type.isNullOrEmpty()){
            ToastUtil.showShort("类型错误,无法编辑！")
            finish()
        }
        initView()
    }
    private fun initView() {
        viewPager = findViewById(R.id.vp_fragment)
        ll_title_contract = findViewById(R.id.ll_title_contract)
        ll_title_contract?.setOnClickListener(this)
        ll_title_return = findViewById(R.id.ll_title_return)
        ll_title_return?.setOnClickListener(this)
        fragment = getFragmentByType(type)
        if(fragment == null){
            ToastUtil.showShort("类型错误,无法编辑！")
            finish()
            return
        }

        val fragmentAdapter = AccountFragmentAdapter(supportFragmentManager)
        val fragmentList = listOf(fragment!!)
        fragmentAdapter.setData(fragmentList)
        viewPager?.setScrollEnable(false)
        viewPager?.adapter = fragmentAdapter

//        val transaction = fragmentManager.beginTransaction()
//        transaction.replace(R.id.fragment_container,fragment).commit()
        ll_title_contract?.setOnClickListener(this)
        ll_title_return?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.ll_title_contract -> {
                (fragment as BaseAddTransFragment).updateTrans()
            }
            R.id.ll_title_return -> {
                finish()
            }
        }
    }

    private fun getFragmentByType(type: String): Fragment? {
        val frag: Fragment? = when(type){
            AccountBean.TYPE_INCOME_SELL -> FragmentSell()
            AccountBean.TYPE_PAY_STOCK -> FragmentStock()
            AccountBean.TYPE_PAY_OTHER -> FragmentOther()
            else -> null
        }
        val bundle = Bundle()
        bundle.putParcelable(BaseAddTransFragment.PARAM_ACCOUNT,accountBean)
        frag?.arguments = bundle
        return frag
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onSellAdd(event: EventUpdateSell){
        finish()
    }




}
