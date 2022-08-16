package uz.mobiler.onlinesavdo.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import uz.mobiler.onlinesavdo.R
import uz.mobiler.onlinesavdo.databinding.FragmentProductDetailBinding
import uz.mobiler.onlinesavdo.library.SmoothBottomBar
import uz.mobiler.onlinesavdo.model.CartModel
import uz.mobiler.onlinesavdo.model.ProductModel
import uz.mobiler.onlinesavdo.utils.Constants
import uz.mobiler.onlinesavdo.utils.PrefUtils

private const val ARG_PARAM1 = Constants.EXTRA_DATA
private const val ARG_PARAM2 = "param2"

class ProductDetailFragment : Fragment() {
    private var param1: ProductModel? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getSerializable(ARG_PARAM1) as ProductModel?
            param2 = it.getString(ARG_PARAM2)
        }
    }
    private lateinit var binding: FragmentProductDetailBinding
    private lateinit var item:ProductModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentProductDetailBinding.inflate(inflater,container,false)
        binding.apply {
            if (param1!=null){
                item= param1 as ProductModel
            }
            (requireActivity() as AppCompatActivity).findViewById<SmoothBottomBar>(R.id.bottomBar).visibility =
                View.GONE

            (requireActivity() as AppCompatActivity).findViewById<Toolbar>(R.id.toolbar1).visibility =
                View.GONE

            back.setOnClickListener {
                Navigation.findNavController(binding.root).popBackStack()
            }

            favorite.setOnClickListener {
                PrefUtils.setFavorites(item)

                if (PrefUtils.checkFavorite(item)){
                    iconFavorite.setImageResource(R.drawable.ic_favorite)
                }else{
                    iconFavorite.setImageResource(R.drawable.ic_favorite_border)
                }
            }

            tvTitle.text=param1?.name
            title.text=param1?.name
            tvPrice.text=param1?.price
            Glide.with(requireContext())
                .load(Constants.HOST_IMAGE + param1?.image)
                .apply(
                    RequestOptions().placeholder(R.drawable.plase_img)
                        .centerCrop()
                )
                .into(img)
            if (PrefUtils.checkFavorite(item)){
                iconFavorite.setImageResource(R.drawable.ic_favorite)
            }else{
                iconFavorite.setImageResource(R.drawable.ic_favorite_border)
            }

            if (PrefUtils.getCartCount(item)>0){
                addCard.visibility=View.GONE
            }else{
                addCard.visibility=View.VISIBLE
            }

            addCard.setOnClickListener {
//                val productModel=ProductModel(item.id,item.name,item.price,item.image,item.category_id,item.created_at,item.updated_at,1)
                item.cartCount=1
                PrefUtils.setCart(item)
                Navigation.findNavController(root).popBackStack()
            }
        }
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: ProductModel, param2: String) =
            ProductDetailFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}