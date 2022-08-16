package uz.mobiler.onlinesavdo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import uz.mobiler.onlinesavdo.MainViewModel
import uz.mobiler.onlinesavdo.R
import uz.mobiler.onlinesavdo.adapters.ProductByIdsAdapter
import uz.mobiler.onlinesavdo.databinding.FragmentFavoriteBinding
import uz.mobiler.onlinesavdo.library.SmoothBottomBar
import uz.mobiler.onlinesavdo.model.CategoryModel
import uz.mobiler.onlinesavdo.utils.PrefUtils

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class FavoriteFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var viewModel: MainViewModel
    var list: List<CategoryModel> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        binding = FragmentFavoriteBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        viewModel.categoriesData.observe(requireActivity()) {
            list = it
        }

        viewModel.productsData.observe(requireActivity()) {
            if (it.isNotEmpty()) {
                binding.lottie.visibility = View.INVISIBLE
            } else {
                binding.lottie.visibility = View.VISIBLE
            }
            binding.rvProducts.adapter=ProductByIdsAdapter(binding,it,list)
        }

        viewModel.error.observe(requireActivity()) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        viewModel.progress.observe(requireActivity()){
            binding.swipe.isRefreshing=it
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        binding.apply {
            (requireActivity() as AppCompatActivity).findViewById<SmoothBottomBar>(R.id.bottomBar).visibility =
                View.VISIBLE

            swipe.setOnRefreshListener {
                loadData()
            }
            loadData()
        }
        return binding.root
    }
    fun loadData(){
        viewModel.getProductsByIds(PrefUtils.getFavoriteList())
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FavoriteFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}