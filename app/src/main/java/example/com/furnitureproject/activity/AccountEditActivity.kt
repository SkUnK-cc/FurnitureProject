package example.com.furnitureproject.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import example.com.furnitureproject.R
import example.com.furnitureproject.db.bean.AccountBean
import example.com.furnitureproject.fragment.addaccount.BaseAddTransFragment
import example.com.furnitureproject.fragment.addaccount.FragmentOther
import example.com.furnitureproject.fragment.addaccount.FragmentSell
import example.com.furnitureproject.fragment.addaccount.FragmentStock
import example.com.furnitureproject.utils.ToastUtil
import kotlinx.android.synthetic.main.layout_toolbar.*

class AccountEditActivity : AppCompatActivity(), View.OnClickListener {
    companion object {
        const val PARAM_ACCOUNT = "paramAccount"
        const val TYPE_INCOME_SELL = "商品收入"
        const val TYPE_PAY_STOCK = "进货"
        const val TYPE_PAY_OTHER = "其他支出"
    }
    var type: String = ""

    var fragment: Fragment? = null
    var accountBean: AccountBean? = null
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
        fragment = getFragmentByType(type)
        if(fragment == null){
            ToastUtil.showShort("类型错误,无法编辑！")
            finish()
        }

        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fragment_container,fragment).commit()
        ll_title_contract.setOnClickListener(this)
        ll_title_return.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.ll_title_contract -> {
//                (fragment as BaseAddTransFragment)
            }
            R.id.ll_title_return -> {
                finish()
            }
        }
    }

    private fun getFragmentByType(type: String): Fragment? {
        val frag: Fragment? = when(type){
            TYPE_INCOME_SELL -> FragmentSell()
            TYPE_PAY_STOCK -> FragmentStock()
            TYPE_PAY_OTHER -> FragmentOther()
            else -> null
        }
        val bundle = Bundle()
        bundle.putParcelable(BaseAddTransFragment.PARAM_ACCOUNT,accountBean)
        frag?.arguments = bundle
        return frag
    }




}
