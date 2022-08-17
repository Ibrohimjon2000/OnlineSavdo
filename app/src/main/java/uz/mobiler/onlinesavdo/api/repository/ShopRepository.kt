package uz.mobiler.onlinesavdo.api.repository

import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import uz.mobiler.onlinesavdo.api.ApiClient
import uz.mobiler.onlinesavdo.api.ApiService
import uz.mobiler.onlinesavdo.model.BaseResponse
import uz.mobiler.onlinesavdo.model.CategoryModel
import uz.mobiler.onlinesavdo.model.OfferModel
import uz.mobiler.onlinesavdo.model.ProductModel
import uz.mobiler.onlinesavdo.model.request.GetProductsByIdRequest

class ShopRepository {
    val compositeDisposable = CompositeDisposable()

    fun getOffers(
        error: MutableLiveData<String>,
        process: MutableLiveData<Boolean>,
        success: MutableLiveData<List<OfferModel>>
    ) {
        process.value = true
        compositeDisposable.add(
            ApiClient.getRetrofit().create(ApiService::class.java).getOffers()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<BaseResponse<List<OfferModel>>>() {
                    override fun onNext(t: BaseResponse<List<OfferModel>>) {
                        process.value = false
                        if (t.success) {
                            success.value = t.data
                        } else {
                            error.value = t.message
                        }
                    }

                    override fun onError(e: Throwable) {
                        process.value = false
                        error.value = e.localizedMessage
                    }

                    override fun onComplete() {

                    }

                })
        )
    }

    fun getCategories(
        error: MutableLiveData<String>,
        success: MutableLiveData<List<CategoryModel>>
    ) {
        compositeDisposable.add(
            ApiClient.getRetrofit().create(ApiService::class.java).getCategories()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<BaseResponse<List<CategoryModel>>>() {
                    override fun onNext(t: BaseResponse<List<CategoryModel>>) {
                        if (t.success) {
                            success.value = t.data
                        } else {
                            error.value = t.message
                        }
                    }

                    override fun onError(e: Throwable) {
                        error.value = e.localizedMessage
                    }

                    override fun onComplete() {

                    }

                })
        )
    }

    fun getTopProducts(
        error: MutableLiveData<String>,
        success: MutableLiveData<List<ProductModel>>
    ) {
        compositeDisposable.add(
            ApiClient.getRetrofit().create(ApiService::class.java).getTopProducts()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<BaseResponse<List<ProductModel>>>() {
                    override fun onNext(t: BaseResponse<List<ProductModel>>) {
                        if (t.success) {
                            success.value = t.data
                        } else {
                            error.value = t.message
                        }
                    }

                    override fun onError(e: Throwable) {
                        error.value = e.localizedMessage
                    }

                    override fun onComplete() {

                    }
                })
        )
    }


    fun getTopRefreshProducts(
        error: MutableLiveData<String>,
        process: MutableLiveData<Boolean>,
        success: MutableLiveData<List<ProductModel>>
    ) {
        process.value = true
        compositeDisposable.add(
            ApiClient.getRetrofit().create(ApiService::class.java).getTopProducts()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<BaseResponse<List<ProductModel>>>() {
                    override fun onNext(t: BaseResponse<List<ProductModel>>) {
                        process.value = false
                        if (t.success) {
                            success.value = t.data
                        } else {
                            error.value = t.message
                        }
                    }

                    override fun onError(e: Throwable) {
                        process.value = false
                        error.value = e.localizedMessage
                    }

                    override fun onComplete() {

                    }
                })
        )
    }

    fun getCategoryProducts(
        id: Int,
        error: MutableLiveData<String>,
        success: MutableLiveData<List<ProductModel>>
    ) {
        compositeDisposable.add(
            ApiClient.getRetrofit().create(ApiService::class.java).getCategoryProducts(id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<BaseResponse<List<ProductModel>>>() {
                    override fun onNext(t: BaseResponse<List<ProductModel>>) {
                        if (t.success) {
                            success.value = t.data
                        } else {
                            error.value = t.message
                        }
                    }

                    override fun onError(e: Throwable) {
                        error.value = e.localizedMessage
                    }

                    override fun onComplete() {

                    }

                })
        )
    }

    fun getProductsByIds(
        ids: List<Int>,
        error: MutableLiveData<String>,
        process: MutableLiveData<Boolean>,
        success: MutableLiveData<List<ProductModel>>
    ) {
        process.value = true
        compositeDisposable.add(
            ApiClient.getRetrofit().create(ApiService::class.java).getProductsById(
                GetProductsByIdRequest(ids)
            )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<BaseResponse<List<ProductModel>>>() {
                    override fun onNext(t: BaseResponse<List<ProductModel>>) {
                        process.value = false
                        if (t.success) {
                            success.value = t.data
                        } else {
                            error.value = t.message
                        }
                    }

                    override fun onError(e: Throwable) {
                        process.value = false
                        error.value = e.localizedMessage
                    }

                    override fun onComplete() {

                    }

                })
        )
    }
}