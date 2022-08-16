package uz.mobiler.onlinesavdo.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import uz.mobiler.onlinesavdo.R
import uz.mobiler.onlinesavdo.databinding.FragmentFavoriteBinding
import uz.mobiler.onlinesavdo.databinding.FragmentHomeBinding
import uz.mobiler.onlinesavdo.databinding.ItemPtoductBinding
import uz.mobiler.onlinesavdo.model.CategoryModel
import uz.mobiler.onlinesavdo.model.ProductModel
import uz.mobiler.onlinesavdo.utils.Constants

class ProductByIdsAdapter(
    val binding: FragmentFavoriteBinding,
    val items: List<ProductModel>,
    val categoryList: List<CategoryModel>
) :
    RecyclerView.Adapter<ProductByIdsAdapter.Vh>() {

    inner class Vh(val itemPtoductBinding: ItemPtoductBinding) :
        RecyclerView.ViewHolder(itemPtoductBinding.root) {
        fun onBind(item: ProductModel) {
            itemPtoductBinding.apply {

                itemView.setOnClickListener {
                    val bundle=Bundle()
                    bundle.putSerializable(Constants.EXTRA_DATA,item)
                    Navigation.findNavController(binding.root).navigate(R.id.productDetailFragment,bundle)
                }

                Glide.with(itemView.context)
                    .load(Constants.HOST_IMAGE + item.image)
                    .apply(
                        RequestOptions().placeholder(R.drawable.plase_img)
                            .centerCrop()
                    )
                    .into(imgProduct)
                tvName.text = item.name
                tvPrice.text = item.price

                if(item.name.startsWith("Смартфон")){
                    tvCategoryName.text = "Telefonlar"
                }else if (item.name.startsWith("Планшет")){
                    tvCategoryName.text = "Planshetlar"
                }else if (item.name.startsWith("Смарт ")){
                    tvCategoryName.text = "Smart vatch va brasletlar"
                }else if (item.name.startsWith("Ноутбук")){
                    tvCategoryName.text = "Noutbooklar"
                }else if (item.name.startsWith("Монитор")){
                    tvCategoryName.text = "Monitorlar"
                }else if (item.name.startsWith("Клавиатура")){
                    tvCategoryName.text = "Klaviatura va sichqonchalar"
                }else if (item.name.startsWith("Мышь")){
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
        holder.onBind(item)
    }

    override fun getItemCount(): Int {
        return items.count()
    }
}