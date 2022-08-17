package uz.mobiler.onlinesavdo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.Navigation
import uz.mobiler.onlinesavdo.R
import uz.mobiler.onlinesavdo.databinding.FragmentMakeOrderBinding
import uz.mobiler.onlinesavdo.library.SmoothBottomBar
import uz.mobiler.onlinesavdo.model.AddressModel
import uz.mobiler.onlinesavdo.model.ProductModel
import uz.mobiler.onlinesavdo.utils.Constants
import uz.mobiler.onlinesavdo.utils.PrefUtils
import java.io.Serializable

private const val ARG_PARAM1 = Constants.EXTRA_DATA
private const val ARG_PARAM2 = "param2"

class MakeOrderFragment : Fragment() {
    private var param1: List<ProductModel>? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getSerializable(ARG_PARAM1) as List<ProductModel>
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private lateinit var binding: FragmentMakeOrderBinding
    private lateinit var items: List<ProductModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMakeOrderBinding.inflate(inflater, container, false)
        binding.apply {
            if (param1 != null) {
                items = param1 as List<ProductModel>
            }
            (requireActivity() as AppCompatActivity).findViewById<SmoothBottomBar>(R.id.bottomBar).visibility =
                View.GONE

            var sum=0
            items.forEach {
                val price = it.price.replace(" ", "")
                val priceInt = price.toInt()
                sum+=it.cartCount*priceInt
            }
            tvPrice.text = "$sum so'm"

            setFragmentResultListener(MapsFragment.REQUEST_KEY) { key, bundle ->
                val address = bundle.getSerializable(MapsFragment.BUNDLE_KEY) as AddressModel
                binding.address.text = "${address.latitude}, ${address.longitude}"
            }

            back.setOnClickListener {
                Navigation.findNavController(root).popBackStack()
            }

            address.setOnClickListener {
                Navigation.findNavController(root).navigate(R.id.mapsFragment)
            }

            addCard.setOnClickListener {
                if (address.text.toString().isNotEmpty() && comment.text.toString().isNotEmpty()) {
                    items.forEach {
                        it.cartCount = 0
                        PrefUtils.setCart(it)
                    }
                    Navigation.findNavController(root).popBackStack()
                    Toast.makeText(requireContext(), "Buyurtma berildi", Toast.LENGTH_SHORT)
                        .show()
                }else{
                    Toast.makeText(requireContext(), "Ma'lumotlar to'liq kiritilmagan", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: List<ProductModel>, param2: String) =
            MakeOrderFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, param1 as Serializable)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}