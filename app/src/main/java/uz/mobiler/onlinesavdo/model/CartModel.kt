package uz.mobiler.onlinesavdo.model

import java.io.Serializable

data class CartModel(
    val product_id: Int,
    var count: Int
):Serializable