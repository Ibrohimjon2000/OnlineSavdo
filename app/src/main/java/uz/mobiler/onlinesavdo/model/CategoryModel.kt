package uz.mobiler.onlinesavdo.model

data class CategoryModel(
    val id: Int,
    val title: String,
    val icon: String,
    val parent_id: String,
    val created_at: String,
    val updated_at: String,
    var is_checked:Boolean=false
)