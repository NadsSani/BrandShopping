package com.nads.shopping.repositories

import com.nads.shopping.api.EndPoints
import com.nads.shopping.datamodels.BaseModel
import com.nads.shopping.datamodels.InventoryResponse
import com.nads.shopping.datamodels.ProductDetail
import com.nads.shopping.datamodels.ProductListResponse
import com.nads.shopping.utils.A_D

class ProductRepository(private val api: EndPoints) : BaseRepository() {

    suspend fun getProducts(
        categoryId: String = "",
        subCategoryId: String = "",
        childCategoryId: String = "",
        priceFilter: String = A_D,
        shopId: String = "",
        page: Int = 1,
        newCollection:Int? = null ,
        bestSellers:Int? = null,
        keywords: String="",
        brandId: String="",
    ): ProductListResponse? {

        return safeApiCall(
            call = {
                api.getProductListAsync(
                    categoryId,
                    subCategoryId,
                    childCategoryId,
                    priceFilter,
                    shopId,
                    page,
                    newCollection,
                    bestSellers,
                   keywords =  keywords,
                    brandId = brandId
                ).await()
            },
            errorMessage = "Error in loading products"
        )
    }

    suspend fun getNewCollections(page: Int=1): ProductListResponse? {

        return safeApiCall(
            call = {
                api.getProductListAsync(newCollection = 1, page = page).await()
            },
            errorMessage = "Error in loading products"
        )
    }

    suspend fun getBrandProducts(page: Int=1, brandId: String?=null): ProductListResponse? {

        return safeApiCall(
            call = {
                api.getProductListAsync(brandId = brandId, page = page).await()
            },
            errorMessage = "Error in loading products in brands"
        )
    }

    suspend fun getBestSellers(page: Int=1): ProductListResponse? {

        return safeApiCall(
            call = {
                api.getProductListAsync(bestSellers = 1, page = page).await()
            },
            errorMessage = "Error in loading products"
        )
    }

    suspend fun getProductDetails(productId: String): BaseModel<ProductDetail>? {

        return safeApiCall(
            call = {
                api.getProductDetailsAsync(productId).await()
            },
            errorMessage = "Error in loading products details"
        )
    }

    suspend fun getInventory(productId: String, options: String): InventoryResponse? {

        return safeApiCall(
            call = {
                api.getInventoryAsync(productId, options).await()
            },
            errorMessage = "Error in loading inventory details"
        )
    }


//    suspend fun getProductes(
//        categoryId: String = "",
//        subCategoryId: String = "",
//        childCategoryId: String = "",
//        priceFilter: String = A_D,
//        shopId: String = "",
//        page: Int = 1,
//        newCollection:Int? = null ,
//        bestSellers:Int? = null,
//        keywords: String="",
//        brandId: String="",
//    )
//
//
//           =  flow {
//               val result =  api.getProductListAsyncs(
//                    categoryId,
//                    subCategoryId,
//                    childCategoryId,
//                    priceFilter,
//                    shopId,
//                    page,
//                    newCollection,
//                    bestSellers,
//                    keywords =  keywords,
//                    brandId = brandId
//               )
//              emit(result)
//                 kotlinx.coroutines.delay(1000L)
//
//            }
//
//



}