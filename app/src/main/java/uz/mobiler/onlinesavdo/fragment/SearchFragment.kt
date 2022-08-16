package uz.mobiler.onlinesavdo.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import uz.mobiler.onlinesavdo.MainViewModel
import uz.mobiler.onlinesavdo.R
import uz.mobiler.onlinesavdo.adapters.ProductSearchAdapter
import uz.mobiler.onlinesavdo.databinding.FragmentSearchBinding
import uz.mobiler.onlinesavdo.library.SmoothBottomBar
import uz.mobiler.onlinesavdo.model.ProductModel

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SearchFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var productSearchAdapter: ProductSearchAdapter
    lateinit var viewModel: MainViewModel
    private var list: List<ProductModel> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
    }

    private lateinit var binding: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        binding.apply {
            (requireActivity() as AppCompatActivity).findViewById<SmoothBottomBar>(R.id.bottomBar).visibility =
                View.VISIBLE

            loadData()

            viewModel.error.observe(requireActivity()) {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }

            viewModel.productsData.observe(requireActivity()) {
                list = it
                if (it.isNotEmpty()) {
                    binding.lottie.visibility = View.INVISIBLE
                } else {
                    binding.lottie.visibility = View.VISIBLE
                }

                productSearchAdapter = ProductSearchAdapter(binding, it)

                binding.search.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun afterTextChanged(p0: Editable?) {
                        val text = p0.toString()
                        if (text.isNotEmpty()) {
                            binding.clearText.visibility = View.VISIBLE
                        } else {
                            binding.clearText.visibility = View.GONE
                        }
                        filter(text)
                    }
                })

                binding.clearText.setOnClickListener {
                    binding.search.setText("")
                }
                rv.adapter = productSearchAdapter
            }
        }
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun loadData() {
        viewModel.getTopProducts()
    }

    private fun filter(text: String) {
        val filteredList = ArrayList<ProductModel>()
        for (product in list) {
            if (product.name.lowercase().contains(text.lowercase())
                || (product.price).lowercase().contains(text.lowercase())
            ) {
                filteredList.add(product)
            }
        }
        productSearchAdapter.filterList(filteredList)
    }
}