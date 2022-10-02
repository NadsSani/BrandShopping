package com.nads.shopping.viewmodels

import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import com.nads.shopping.BR
import com.nads.shopping.R
import com.nads.shopping.api.ApiFactory
import com.nads.shopping.datamodels.Brand
import com.nads.shopping.datamodels.Category
import com.nads.shopping.datamodels.FilterType
import com.nads.shopping.datamodels.SelectedFilter
import com.nads.shopping.listeners.ItemSelectListener
import com.nads.shopping.repositories.BrandRepository
import com.nads.shopping.repositories.CategoryRepository
import kotlinx.coroutines.launch
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass

class FilterViewModel : BaseViewModel() {
    val brandList = ObservableArrayList<Brand>()
    val categories = ObservableArrayList<Category>()
    val selectedFilters = ObservableArrayList<SelectedFilter>()

    val loading = MutableLiveData<Boolean>()
    val isPriceLH = MutableLiveData<Boolean>(true)

    private val brandRepository = BrandRepository(ApiFactory.brandattyAPI)
    private val categoryRepository = CategoryRepository(ApiFactory.brandattyAPI)

    private val filterBrandSelectListener = object : ItemSelectListener<Brand> {

        override fun onItemSelected(item: Brand) {

            val position = brandList.indexOf(item)
            brandList[position].isSelected = !brandList[position].isSelected

            if (brandList[position].isSelected)
                selectedFilters.add(SelectedFilter(item.id, item.name, FilterType.BRANDS))
            else {
                val filterPosition = selectedFilters.find { it.id == item.id }
                selectedFilters.remove(filterPosition)
            }
        }
    }

    private val filterCategorySelectListener = object : ItemSelectListener<Category> {

        override fun onItemSelected(item: Category) {

            val position = categories.indexOf(item)
            categories[position].isSelected = !categories[position].isSelected

            if (categories[position].isSelected)
                selectedFilters.add(SelectedFilter(item.id, item.name, FilterType.CATEGORY))
            else {
                val filterPosition = selectedFilters.find { it.id == item.id }
                selectedFilters.remove(filterPosition)
            }
        }
    }

    val filterBrandItemBinding: OnItemBindClass<Brand> = OnItemBindClass<Brand>().map(
        Brand::class.java
    ) { itemBinding, position, item ->
        itemBinding.set(BR.brand, R.layout.brand_filter_item)

        itemBinding.bindExtra(BR.listener, filterBrandSelectListener)
    }

    val selectedFilterBinding: OnItemBindClass<SelectedFilter> =
        OnItemBindClass<SelectedFilter>().map(
            SelectedFilter::class.java
        ) { itemBinding, position, item ->
            itemBinding.set(BR.filter, R.layout.selected_filter_item)

//        itemBinding.bindExtra(BR.listener, homeCategorySelectListener)
        }

    val filterCategoryBinding: OnItemBindClass<Category> = OnItemBindClass<Category>().map(
        Category::class.java
    ) { itemBinding, position, item ->
        itemBinding.set(BR.category, R.layout.category_filter_item)

        itemBinding.bindExtra(BR.listener, filterCategorySelectListener)
    }

    fun getBrands() {
        mainScope.launch {
            loading.postValue(true)
            val response =
                brandRepository.getBrands()

            loading.postValue(false)

            response?.data?.let {
                brandList.clear()
                brandList.addAll(it)

                selectedFilters.forEachIndexed { index, filter ->
                    when (filter.type) {
                        FilterType.BRANDS -> {
                            val selectedFilter =
                                brandList.find { brand -> brand.id == filter.id }
                            val selectedFilterPosition = brandList.indexOf(selectedFilter)
                            brandList[selectedFilterPosition].isSelected = true
                        }
                    }
                }
            }
        }
    }

    fun getCategories() {

        mainScope.launch {
            loading.postValue(true)
            val response = categoryRepository.getCategories()

            loading.postValue(false)

            response?.let {
                it.data?.let { categoryList ->
                    categories.addAll(categoryList)


                    selectedFilters.forEachIndexed { index, filter ->
                        when (filter.type) {
                            FilterType.CATEGORY -> {
                                val selectedFilter =
                                    categories.find { category -> category.id == filter.id }
                                val selectedFilterPosition = categories.indexOf(selectedFilter)
                                categories[selectedFilterPosition].isSelected = true
                            }
                        }
                    }

                }
            }
        }
    }
}