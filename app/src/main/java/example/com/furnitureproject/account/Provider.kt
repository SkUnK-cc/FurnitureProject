package example.com.furnitureproject.account

import example.com.furnitureproject.adapter.account.InfoItem

interface Provider {

    fun getItem(): ArrayList<InfoItem>
}