package uz.mobiler.onlinesavdo.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import uz.mobiler.onlinesavdo.R
import uz.mobiler.onlinesavdo.databinding.ItemPtoductBinding
import uz.mobiler.onlinesavdo.model.ProductModel
import uz.mobiler.onlinesavdo.utils.Constants

class ProductSearchAdapter(
    var items: List<ProductModel>,
    val listener: OnItemClickListener
) :
    RecyclerView.Adapter<ProductSearchAdapter.Vh>() {

    inner class Vh(val itemPtoductBinding: ItemPtoductBinding) :
        RecyclerView.ViewHolder(itemPtoductBinding.root) {
        fun onBind(item: ProductModel, position: Int) {
            itemPtoductBinding.apply {

                itemView.setOnClickListener {
                    listener.onItemClickListener(item, position)
                }

                Glide.with(itemView.context)
                    .load(Constants.HOST_IMAGE + item.image)
                    .apply(
                        RequestOptions().placeholder(R.drawable.plase_img)
                            .centerCrop()
                    )
                    .into(imgProduct)
                tvName.text = item.name
                tvPrice.text = item.price + " so'm"

                if (item.name.startsWith("Смартфон")) {
                    tvCategoryName.text = "Telefonlar"
                } else if (item.name.startsWith("Планшет")) {
                    tvCategoryName.text = "Planshetlar"
                } else if (item.name.startsWith("Смарт ")) {
                    tvCategoryName.text = "Smart vatch va brasletlar"
                } else if (item.name.startsWith("Ноутбук")) {
                    tvCategoryName.text = "Noutbooklar"
                } else if (item.name.startsWith("Монитор")) {
                    tvCategoryName.text = "Monitorlar"
                } else if (item.name.startsWith("Клавиатура")) {
                    tvCategoryName.text = "Klaviatura va sichqonchalar"
                } else if (item.name.startsWith("Мышь")) {
                    tvCategoryName.text = "Klaviatura va sichqonchalar"
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemPtoductBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        val item = items[position]
        holder.onBind(item, position)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    fun filterList(filteredList: ArrayList<ProductModel>) {
        items = filteredList
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClickListener(item: ProductModel, position: Int)
    }
}