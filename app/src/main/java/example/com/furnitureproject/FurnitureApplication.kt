package example.com.furnitureproject

import android.app.Application
import example.com.furnitureproject.db.DbHelper

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
    }

    override fun onTerminate() {
        super.onTerminate()
        DbHelper.close()
    }
}