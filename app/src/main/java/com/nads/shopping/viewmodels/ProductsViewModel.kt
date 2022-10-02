package com.nads.shopping.viewmodels

import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.hadilq.liveevent.LiveEvent
import com.nads.epicureapp.ui.homepage.model.BasePagingSource
import com.nads.shopping.BR
import com.nads.shopping.R
import com.nads.shopping.api.ApiFactory
import com.nads.shopping.datamodels.*
import com.nads.shopping.listeners.ItemSelectListener
import com.nads.shopping.repositories.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import me.tatarka.bindingcollectionadapter2.ItemBinding
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass

class ProductsViewModel : BaseViewModel() {
    val addCartQuantity = MutableLiveData<Int>(1)
    val loading = MutableLiveData<Boolean>()
    val productListTitle = MutableLiveData<String>()
    val totalProducts = MutableLiveData<String>("0")
    val searchKeyword = MutableLiveData<String>("")

    val productsId = MutableLiveData<String>()
    val productList = ObservableArrayList<Product>()



    val productList2 = ObservableArrayList<Product>()
    val homeBestSellers = ObservableArrayList<Product>()
    val similarProducts = ObservableArrayList<Product>()
    val homeNewCollections = ObservableArrayList<Product>()
    var lastSelectedCategory = 0
    val banners = ObservableArrayList<String>()

    val selectedOptions = mutableListOf<String>()

    val productDetailsResponse = MutableLiveData<ProductDetail>()

    val sizeList = ObservableArrayList<ProductSize>()
    var lastSelectedSize = 2

    private val productSelect = LiveEvent<Product>()
    val productSelectEvent = productSelect

    private val sizeSelect = LiveEvent<OptionKey>()
    val sizeSelectEvent = sizeSelect

    val cartCount = MutableLiveData(0)

    val repository = ProductRepository(ApiFactory.brandattyAPI)

    private val productSelectListener = object : ItemSelectListener<Product> {

        override fun onItemSelected(item: Product) {
            productSelect.value = item
        }
    }






    private val optionSelectListener = object : ItemSelectListener<OptionKey> {

        override fun onItemSelected(item: OptionKey) {

            val list = productDetailsResponse.value?.options?.filter { it.id == item.optionId }

            if (!list.isNullOrEmpty()) {
                val position = productDetailsResponse.value?.options?.indexOf(list[0])

                productDetailsResponse.value?.options?.get(
                    position ?: 0
                )?.keys?.forEachIndexed { index, key ->
                    key.isSelected = false
                    productDetailsResponse.value?.options?.get(position ?: 0)?.keys?.set(index, key)
                }

                val keyPosition =
                    productDetailsResponse.value?.options?.get(position ?: 0)?.keys?.indexOf(item)!!
                item.isSelected = true
                productDetailsResponse.value?.options?.get(position ?: 0)?.keys?.set(
                    keyPosition,
                    item
                )

                selectedOptions.clear()
                productDetailsResponse.value?.options?.forEachIndexed { index, option ->
                    productDetailsResponse.value?.options?.get(index)?.keys?.forEach { key ->
                        if (key.isSelected)
                            selectedOptions.add(key.id)
                    }
                }


                addCartQuantity.value = 1
                sizeSelect.value = item
            }
        }
    }

    init {
//        getProductSize()
//        getBestSellers()
//        getNewCollections()
    }

    val productOptionsBinding: OnItemBindClass<Option> = OnItemBindClass<Option>().map(
        Option::class.java
    ) { itemBinding, position, item ->
        itemBinding.set(BR.option, R.layout.options_item)

        itemBinding.bindExtra(BR.viewModel, this)
    }

    val productOptionsKeyBinding: OnItemBindClass<OptionKey> = OnItemBindClass<OptionKey>().map(
        OptionKey::class.java
    ) { itemBinding, position, item ->
        itemBinding.set(BR.optionKey, R.layout.product_size_item)

        itemBinding.bindExtra(BR.listener, optionSelectListener)
    }

    val bannerItemBinding =
        ItemBinding.of<String>(BR.banner, R.layout.product_details_banner_item)

    val homeBestSellersBinding: OnItemBindClass<Product> = OnItemBindClass<Product>().map(
        Product::class.java
    ) { itemBinding, position, item ->
        itemBinding.set(BR.product, R.layout.product_item)

        itemBinding.bindExtra(BR.listener, productSelectListener)
    }
    val listproductsBinding: OnItemBindClass<Product> = OnItemBindClass<Product>().map(
        Product::class.java
    ) { itemBinding, position, item ->
        itemBinding.set(BR.product, R.layout.product_item)

        itemBinding.bindExtra(BR.listener, productSelectListener)
    }
    val similarProductsBinding: OnItemBindClass<Product> = OnItemBindClass<Product>().map(
        Product::class.java
    ) { itemBinding, position, item ->
        /* if (item.isSelected)
             itemBinding.set(BR.topupItem, R.layout.topup_item_selected)
         else*/ itemBinding.set(BR.product, R.layout.similar_product_item)

        itemBinding.bindExtra(BR.listener, productSelectListener)
    }

    fun getProducts(
        categoryId: String = "",
        subCategoryId: String = "",
        listType: ProductListType = ProductListType.NORMAL,
        brandId: String = "",
        priceFilter: String = "",
    ) {
        mainScope.launch {
            loading.postValue(true)
            val response = repository.getProducts(
                categoryId = categoryId,
                subCategoryId = subCategoryId,
                keywords = searchKeyword.value.toString(),
                brandId = brandId,
                priceFilter = priceFilter
            )

            loading.postValue(false)

            response?.let {
                totalProducts.value = it.total.toString()
            }

            response?.products?.let {

                when (listType) {
                    ProductListType.NORMAL -> {
                        productList.clear()
                        productList.addAll(it)
                    }
                    ProductListType.SIMILAR_PRODUCTS -> {
                        similarProducts.clear()
                        similarProducts.addAll(it)
                    }
                }


            }
        }
    }







    fun getProducts2(
        categoryId: String = "",
        subCategoryId: String = "",
        listType: ProductListType = ProductListType.NORMAL,
        brandId: String = "",
        priceFilter: String = "",
    ) {
        mainScope.launch {
            loading.postValue(true)
            val response = repository.getProducts(
                categoryId = categoryId,
                subCategoryId = subCategoryId,
                keywords = searchKeyword.value.toString(),
                brandId = brandId,
                priceFilter = priceFilter
            )

            loading.postValue(false)

            response?.let {
                totalProducts.value = it.total.toString()
            }

            response?.products?.let {

                when (listType) {
                    ProductListType.NORMAL -> {
                          productList2.clear()
                        productList2.addAll(it)
                    }
                    ProductListType.SIMILAR_PRODUCTS -> {
                        similarProducts.clear()
                        similarProducts.addAll(it)
                    }
                }


            }
        }

    }

    fun getPaginSource( categoryId: String = "",
                        subCategoryId: String = "",
                        listType: ProductListType = ProductListType.NORMAL,
                        brandId: String = "",
                        priceFilter: String = "",): Flow<PagingData<Product>> {
        return Pager(
            config = PagingConfig(
                pageSize = 2,
                enablePlaceholders = false,
                prefetchDistance = 20
            ),
            pagingSourceFactory = { BasePagingSource(repository,
                categoryId=categoryId,
                subCategoryId=subCategoryId,
                priceFilter=priceFilter,
                brandId=brandId) }
        ).flow.cachedIn(viewModelScope)
    }
    fun getNewCollectionPaginSource(): Flow<PagingData<Product>> {
        return Pager(
            config = PagingConfig(
                pageSize = 2,
                enablePlaceholders = false,
                prefetchDistance = 20
            ),
            pagingSourceFactory = { BasePagingSource(repository,newCollection = 1,keywords = "",brandId = "") }
        ).flow.cachedIn(viewModelScope)
    }
    fun getBestPaginSource(): Flow<PagingData<Product>> {
        return Pager(
            config = PagingConfig(
                pageSize = 2,
                enablePlaceholders = false,
                prefetchDistance = 20
            ),
            pagingSourceFactory = { BasePagingSource(repository,bestSellers = 1,keywords = "",brandId = "") }
        ).flow.cachedIn(viewModelScope)
    }

    fun getNewCollections(page: Int = 1) {
        mainScope.launch {
            loading.postValue(true)
            val response = repository.getNewCollections(page)

            loading.postValue(false)

            response?.let {
                totalProducts.value = it.total.toString()
            }
            response?.products?.let {
                homeNewCollections.clear()
                homeNewCollections.addAll(it)

                productList.clear()
                productList.addAll(it)
            }
        }
    }

    fun getBrandProducts(page: Int = 1, brandId: String? = null) {
        mainScope.launch {
            loading.postValue(true)
            val response = repository.getBrandProducts(page, brandId)

            loading.postValue(false)

            response?.let {
                totalProducts.value = it.total.toString()
            }
            response?.products?.let {
                homeNewCollections.clear()
                homeNewCollections.addAll(it)

                productList.clear()
                productList.addAll(it)
            }
        }
    }

    fun getBestSellers(page: Int = 1) {
        mainScope.launch {
            loading.postValue(true)
            val response = repository.getBestSellers(page)

            loading.postValue(false)

            response?.let {
                totalProducts.value = it.total.toString()
            }

            response?.products?.let {
                homeBestSellers.clear()
                homeBestSellers.addAll(it)

                productList.clear()
                productList.addAll(it)
            }
        }
    }

    fun getProductDetails(productId: String) {
        mainScope.launch {
            loading.postValue(true)
            val response = repository.getProductDetails(productId)

            loading.postValue(false)

            response?.data?.let {
              productDetailsResponse.value = it

                //  make initial option selection
                selectedOptions.clear()
                //Deleted this line for testing
               // selectedOptions.addAll(it.selectedOptions.split("-"))


                selectedOptions.forEachIndexed { index, optionId ->
                    val list =
                        productDetailsResponse.value?.options?.get(index)?.keys?.filter { key -> key.id == optionId }
                    val selectedPosition =
                        productDetailsResponse.value?.options?.get(index)?.keys?.indexOf(list?.get(0))
                            ?: 0

                    val key = productDetailsResponse.value?.options?.get(index)?.keys?.get(
                        selectedPosition
                    )
                    key?.isSelected = true
                    productDetailsResponse.value?.options?.get(index)?.keys?.set(
                        selectedPosition,
                        key
                    )
                }
//
                getProducts(it.categoryId, it.subCategoryId, ProductListType.SIMILAR_PRODUCTS)
            }
        }
    }

    fun getInventory(productId: String, options: String) {
        mainScope.launch {
            loading.postValue(true)
            val response = repository.getInventory(productId, options)

            loading.postValue(false)

            response?.inventory?.let {
                val productDetails = productDetailsResponse.value

                productDetails?.stock = it.stock
                productDetails?.price = it.price
                productDetails?.previousPrice = it.previousPrice
                productDetails?.inventoryId = it.inventoryId

                productDetails?.let { details ->
                    productDetailsResponse.value = details
                }

            }
        }
    }

    fun plusQuantity() {
        if (addCartQuantity.value ?: 1 < productDetailsResponse.value?.stock ?: 0)
            addCartQuantity.value = addCartQuantity.value?.plus(1)
    }

    fun minusQuantity() {
        if (addCartQuantity.value ?: 1 > 1)
            addCartQuantity.value = addCartQuantity.value?.minus(1)
    }

}