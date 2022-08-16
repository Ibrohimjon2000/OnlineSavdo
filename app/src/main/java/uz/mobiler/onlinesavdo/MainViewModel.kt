package uz.mobiler.onlinesavdo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.mobiler.onlinesavdo.api.repository.ShopRepository
import uz.mobiler.onlinesavdo.model.CategoryModel
import uz.mobiler.onlinesavdo.model.OfferModel
import uz.mobiler.onlinesavdo.model.ProductModel

 class MainViewModel : ViewModel() {
    val repository = ShopRepository()

    val error = MutableLiveData<String>()
     val progress=MutableLiveData<Boolean>()

    val offersData = MutableLiveData<List<OfferModel>>()
    val categoriesData = MutableLiveData<List<CategoryModel>>()
    val productsData = MutableLiveData<List<ProductModel>>()

    fun getOffers() {
        repository.getOffers(error,progress, offersData)
    }

    fun getCategories() {
        repository.getCategories(error, categoriesData)
    }

    fun getTopProducts() {
        repository.getTopProducts(error, productsData)
    }

    fun getCategoryProducts(id:Int) {
        repository.getCategoryProducts(id,error, productsData)
    }

    fun getProductsByIds(ids:List<Int>) {
        repository.getProductsByIds(ids,error,progress ,productsData)
    }
}