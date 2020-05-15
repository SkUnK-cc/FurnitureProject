package example.com.furnitureproject

import android.app.Application
import example.com.furnitureproject.db.DbHelper
import example.com.furnitureproject.utils.Logger

class FurnitureApplication: Application() {

    companion object {
        var application: FurnitureApplication? = null

        fun getInstance(): Application?{
            return application
        }
    }

    override fun onCreate() {
        super.onCreate()
        application = this
        DbHelper.init(this)
        firstRun()
    }

    private fun firstRun() {
        val sharedPreferences = getSharedPreferences("FirstRun",0)
        val first_run = sharedPreferences.getBoolean("First",true)
        if (first_run){
            sharedPreferences.edit().putBoolean("First",false).commit()
            DbHelper.initDefaultData()
            Logger.debug("首次运行")
        }
        else {
            Logger.debug("非首次运行")
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        DbHelper.close()
    }
}