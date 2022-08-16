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
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import uz.mobiler.onlinesavdo.MainViewModel
import uz.mobiler.onlinesavdo.R
import uz.mobiler.onlinesavdo.adapters.CategoryAdapter
import uz.mobiler.onlinesavdo.adapters.CategoryAdapterCallback
import uz.mobiler.onlinesavdo.adapters.ProductAdapter
import uz.mobiler.onlinesavdo.databinding.FragmentHomeBinding
import uz.mobiler.onlinesavdo.library.SmoothBottomBar
import uz.mobiler.onlinesavdo.model.CategoryModel

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var productAdapter: ProductAdapter
    lateinit var viewModel: MainViewModel
    var list: List<CategoryModel> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.apply {
            (requireActivity() as AppCompatActivity).findViewById<SmoothBottomBar>(R.id.bottomBar).visibility =
                View.VISIBLE
            (requireActivity() as AppCompatActivity).findViewById<Toolbar>(R.id.toolbar1).visibility =
                View.VISIBLE

            swipe.setOnRefreshListener {
                loadData()
            }

            loadData()

            viewModel.error.observe(requireActivity()) {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }

            viewModel.progress.observe(requireActivity()) {
                swipe.isRefreshing = it
            }

            viewModel.offersData.observe(requireActivity()) {
                carouselView.setImageListener { position, imageView ->
                    Glide.with(imageView)
                        .load("http://osonsavdo.herokuapp.com/images/${it[position].image}")
                        .apply(
                            RequestOptions().placeholder(R.drawable.plase_img)
                                .centerCrop()
                        )
                        .into(imageView)
                }
                carouselView.pageCount = it.count()
            }

            viewModel.categoriesData.observe(requireActivity()) {
                list = it
                categoryAdapter = CategoryAdapter(it, object : CategoryAdapterCallback {
                    override fun onClickItem(item: CategoryModel) {
                        viewModel.getCategoryProducts(item.id)
                    }
                })
                rvCategory.adapter = categoryAdapter
            }

            viewModel.productsData.observe(requireActivity()) {
                productAdapter = ProductAdapter(binding,it, list)
                rvProduct.adapter = productAdapter
            }
        }
        return binding.root
    }

    fun loadData() {
        viewModel.getOffers()
        viewModel.getCategories()
        viewModel.getTopProducts()
    }
}