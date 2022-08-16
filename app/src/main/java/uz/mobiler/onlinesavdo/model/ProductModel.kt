package uz.mobiler.onlinesavdo.model

import java.io.Serializable

data class ProductModel(
    val id: Int,
    val name: String,
    val price: String,
    val image: String,
    val category_id: String,
    val created_at: String,
    val updated_at: String,
    var cartCount:Int
):Serializable