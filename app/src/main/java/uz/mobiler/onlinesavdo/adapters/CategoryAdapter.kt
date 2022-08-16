package uz.mobiler.onlinesavdo.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import uz.mobiler.onlinesavdo.R
import uz.mobiler.onlinesavdo.databinding.FragmentHomeBinding
import uz.mobiler.onlinesavdo.databinding.ItemCategoryBinding
import uz.mobiler.onlinesavdo.model.CategoryModel

interface CategoryAdapterCallback{
    fun onClickItem(item: CategoryModel)
}

class CategoryAdapter( val items: List<CategoryModel>,val callback: CategoryAdapterCallback) :
    RecyclerView.Adapter<CategoryAdapter.Vh>() {

    inner class Vh(val itemCategoryBinding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(itemCategoryBinding.root) {
        fun onBind(item: CategoryModel) {
            itemCategoryBinding.apply {
                itemView.setOnClickListener {
                    items.forEach {
                        it.is_checked = false
                    }
                    item.is_checked = true
                    callback.onClickItem(item)
                    notifyDataSetChanged()
                }
                tvTitle.text = item.title
                if (item.is_checked) {
                    cardView.setCardBackgroundColor(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.colorPrimary
                        )
                    )
                    tvTitle.setTextColor(Color.WHITE)
                } else {
                    cardView.setCardBackgroundColor(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.white
                        )
                    )
                    tvTitle.setTextColor(Color.BLACK)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        val item = items[position]
        holder.onBind(item)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}