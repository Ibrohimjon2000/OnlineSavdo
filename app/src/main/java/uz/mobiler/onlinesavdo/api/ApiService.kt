package uz.mobiler.onlinesavdo.api

import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import uz.mobiler.onlinesavdo.model.BaseResponse
import uz.mobiler.onlinesavdo.model.CategoryModel
import uz.mobiler.onlinesavdo.model.OfferModel
import uz.mobiler.onlinesavdo.model.ProductModel
import uz.mobiler.onlinesavdo.model.request.GetProductsByIdRequest

interface ApiService {
    @GET("get_offers")
    fun getOffers(): Observable<BaseResponse<List<OfferModel>>>

    @GET("get_categories")
    fun getCategories(): Observable<BaseResponse<List<CategoryModel>>>

    @GET("get_top_products")
    fun getTopProducts(): Observable<BaseResponse<List<ProductModel>>>

    @GET("get_products/{category_id}")
    fun getCategoryProducts(@Path("category_id") categoryId: Int): Observable<BaseResponse<List<ProductModel>>>


    @POST("get_products_by_ids")
    fun getProductsById(@Body request: GetProductsByIdRequest): Observable<BaseResponse<List<ProductModel>>>
}