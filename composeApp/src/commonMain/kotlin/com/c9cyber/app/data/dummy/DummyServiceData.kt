package com.c9cyber.app.data.dummy

import com.c9cyber.app.domain.model.ServiceItem
import com.c9cyber.app.domain.model.ServiceType
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.food_placeholder

object DummyServiceData {
    fun getDummyServiceItems(): List<ServiceItem> {
        return listOf(
            ServiceItem("1", "Sting Dâu", ServiceType.Drink, 12000, true),
            ServiceItem("2", "Coca Cola", ServiceType.Drink, 10000, true),
            ServiceItem("3", "Red Bull", ServiceType.Drink, 15000, true),
            ServiceItem("4", "Mì Tôm Trứng", ServiceType.Food, 25000, true),
            ServiceItem("5", "Mì Cay 7 Cấp", ServiceType.Food, 45000, true),
            ServiceItem("6", "Cơm Rang Dưa Bò", ServiceType.Food, 40000, true),
            ServiceItem("7", "Bánh Mì Pate", ServiceType.Food, 20000, true),
            ServiceItem("8", "Trà Đào Cam Sả", ServiceType.Drink, 30000, true),
            ServiceItem("9", "Cafe Sữa Đá", ServiceType.Drink, 25000, true),
            ServiceItem("10", "Pepsi", ServiceType.Drink, 10000, false), // Hết hàng
            ServiceItem("11", "Bún Bò Huế", ServiceType.Food, 50000, true),
            ServiceItem("12", "Phở Bò", ServiceType.Food, 55000, true),
        )
    }
}

