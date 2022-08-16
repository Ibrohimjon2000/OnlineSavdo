package uz.mobiler.onlinesavdo.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import uz.mobiler.onlinesavdo.MainViewModel
import uz.mobiler.onlinesavdo.R
import uz.mobiler.onlinesavdo.adapters.CartAdapter
import uz.mobiler.onlinesavdo.databinding.FragmentCartBinding
import uz.mobiler.onlinesavdo.library.SmoothBottomBar
import uz.mobiler.onlinesavdo.model.ProductModel
import uz.mobiler.onlinesavdo.utils.Constants
import uz.mobiler.onlinesavdo.utils.PrefUtils
import java.io.Serializable

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val TAG = "CartFragment"

class CartFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentCartBinding
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        binding = FragmentCartBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        viewModel.error.observe(requireActivity()) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        viewModel.progress.observe(requireActivity()) {
            binding.swipe.isRefreshing = it
        }
        viewModel.productsData.observe(requireActivity()) {
//            val cartList = PrefUtils.getCartList()
//            for (i in it.indices) {
//                it[i].cartCount= cartList[i].count
//            }
            if (it.isNotEmpty()) {
                binding.lottie.visibility = View.INVISIBLE
            } else {
                binding.lottie.visibility = View.VISIBLE
            }
            binding.rvProducts.adapter = CartAdapter(it)

            binding.makeOrder.isClickable = it.isNotEmpty()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        binding.apply {
            (requireActivity() as AppCompatActivity).findViewById<SmoothBottomBar>(R.id.bottomBar).visibility =
                View.VISIBLE

            swipe.setOnRefreshListener {
                loadData()
            }
            loadData()

            makeOrder.setOnClickListener {
                val bundle=Bundle()
                bundle.putSerializable(Constants.EXTRA_DATA,(viewModel.productsData.value?: emptyList())as Serializable)
                Navigation.findNavController(root).navigate(R.id.makeOrderFragment,bundle)
            }
        }
        return binding.root
    }

    fun loadData() {
        viewModel.getProductsByIds(PrefUtils.getCartList().map { it.product_id })
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CartFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}