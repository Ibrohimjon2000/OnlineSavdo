package uz.mobiler.onlinesavdo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.Navigation
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import uz.mobiler.onlinesavdo.R
import uz.mobiler.onlinesavdo.databinding.FragmentMakeOrderBinding
import uz.mobiler.onlinesavdo.library.SmoothBottomBar
import uz.mobiler.onlinesavdo.model.AddressModel
import uz.mobiler.onlinesavdo.model.ProductModel
import uz.mobiler.onlinesavdo.utils.Constants
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
    private lateinit var items:List<ProductModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentMakeOrderBinding.inflate(inflater,container,false)
        binding.apply {
            if (param1!=null) {
                items = param1 as List<ProductModel>
            }
            (requireActivity() as AppCompatActivity).findViewById<SmoothBottomBar>(R.id.bottomBar).visibility =
                View.GONE
            (requireActivity() as AppCompatActivity).findViewById<Toolbar>(R.id.toolbar1).visibility =
                View.GONE

            tvPrice.text = items.sumByDouble { it.cartCount*(it.price.replace(" ","").toDoubleOrNull()!!)}.toString()

            setFragmentResultListener(MapsFragment.REQUEST_KEY) { key, bundle ->
                val address = bundle.getSerializable(MapsFragment.BUNDLE_KEY) as AddressModel
                binding.address.setText("${address.latitude}, ${address.longitude}")
            }

            back.setOnClickListener {
                Navigation.findNavController(root).popBackStack()
            }

            address.setOnClickListener {
               Navigation.findNavController(root).navigate(R.id.mapsFragment)
            }

            addCard.setOnClickListener {

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