package com.nads.epicureapp.ui.homepage.model

import androidx.paging.PagingSource
import com.nads.shopping.datamodels.Product
import com.nads.shopping.repositories.ProductRepository

class BasePagingSource(
     val repository: ProductRepository,
     val categoryId: String="",
     val subCategoryId: String="",
     val childCategoryId: String="",
     val priceFilter: String="",
     val shopId: String="",
     val page: Int = 1,
     val newCollection: Int?=null,
     val bestSellers: Int?=null,
     val keywords: String="",
     val brandId: String?=null,
    ) : PagingSource<Int, Product>() {


    override fun invalidate() {
        super.invalidate()
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        try {
            // Start refresh at page 1 if undefined.
            val nextPageNumber = params.key ?: 1

            val respons = repository.getProducts(categoryId = categoryId,page = nextPageNumber,newCollection=newCollection,
                bestSellers = bestSellers,
                subCategoryId =subCategoryId,childCategoryId = childCategoryId,
                priceFilter = priceFilter,shopId = shopId,keywords = keywords,brandId = brandId.orEmpty())

            val responseData = mutableListOf<Product>()
            val data = respons?.products
            if (data != null) {
                responseData.addAll(data)
            }
            return LoadResult.Page(
                data = responseData,
                prevKey = null, // Only paging forward.
                nextKey = nextPageNumber.plus(1)
            )
        } catch (e: Exception) {
            return LoadResult.Page(
                data = emptyList(),
                prevKey = null, // Only paging forward.
                nextKey = null
            )
        }
    }


}