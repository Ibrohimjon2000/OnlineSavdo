package uz.mobiler.onlinesavdo.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import uz.mobiler.onlinesavdo.R
import uz.mobiler.onlinesavdo.databinding.ItemCardBinding
import uz.mobiler.onlinesavdo.model.ProductModel
import uz.mobiler.onlinesavdo.utils.Constants

class CartAdapter(val items: List<ProductModel>) : RecyclerView.Adapter<CartAdapter.Vh>() {

    inner class Vh(val itemCardBinding: ItemCardBinding) :
        RecyclerView.ViewHolder(itemCardBinding.root) {
        fun onBind(item: ProductModel) {
            itemCardBinding.apply {
                tvName.text = item.name
                tvPrice.text = item.price
                count.text = item.cartCount.toString()
                Glide.with(itemView.context)
                    .load(Constants.HOST_IMAGE + item.image)
                    .apply(
                        RequestOptions().placeholder(R.drawable.plase_img)
                            .centerCrop()
                    )
                    .into(imgProduct)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }
}