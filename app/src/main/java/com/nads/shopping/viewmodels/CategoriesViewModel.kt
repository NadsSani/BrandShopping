package com.nads.shopping.viewmodels

import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import com.hadilq.liveevent.LiveEvent
import com.nads.shopping.BR
import com.nads.shopping.R
import com.nads.shopping.api.ApiFactory
import com.nads.shopping.datamodels.Category
import com.nads.shopping.datamodels.SubCategory
import com.nads.shopping.listeners.ItemSelectListener
import com.nads.shopping.repositories.CategoryRepository
import kotlinx.coroutines.launch
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass

class CategoriesViewModel : BaseViewModel() {
    val loading = MutableLiveData<Boolean>()
    val subCategories = ObservableArrayList<SubCategory>()
    val categories = ObservableArrayList<Category>()
    var lastSelectedCategory = 0
    var lastSelectedSubCategory = 0
    //to dressbus and toys bus
   val dressbus= LiveEvent<Boolean>()
    val allisactive= LiveEvent<Boolean>()
  val toysbus= LiveEvent<Boolean>()
    val dress = LiveEvent<Boolean>()
    val toys =  LiveEvent<Boolean>()
    val selectedall= LiveEvent<Boolean>()
    var selectedCat = 1
    //
    private val homeCategorySelect = LiveEvent<Category>()
    val homeCategorySelectEvent = homeCategorySelect

    private val categorySelect = LiveEvent<Category>()
    val categorySelectEvent = categorySelect

    private val subCategorySelect = LiveEvent<SubCategory>()
    val subCategorySelectEvent = subCategorySelect

    val repository = CategoryRepository(ApiFactory.brandattyAPI)

    private val homeCategorySelectListener = object : ItemSelectListener<Category> {

        override fun onItemSelected(item: Category) {

//            val name = categories[position].name
            Log.e("gettheid",item.id.toString())
            when(item.id){
                "85" -> dressbusselected(item)
                "86" -> toysbusselected(item)
                "0" -> allisselected(item)
                "4" ->  dressselected(item)
                "9" -> toysselected(item)
            }



        }
    }

    fun dressbusselected(item:Category){
        subCategories.clear()
        dressbus.value = true

    }
    fun toysbusselected(item:Category){
        subCategories.clear()
        toysbus.value = true

    }
    fun allisselected(item: Category){

    buttonselectfinder(item)
        allisactive.value = true
    }
   fun dressselected(item: Category){
buttonselectfinder(item)
       dress.value = true
       subCategories.clear()
       getSubCategories()
   }

    fun toysselected(item: Category){
       buttonselectfinder(item)
         toys.value = true
        subCategories.clear()
        getSubCategories()
    }

    fun buttonselectfinder(item: Category){
        val lastCategory = categories[lastSelectedCategory]
        lastCategory.isSelected = false
        categories[lastSelectedCategory] = lastCategory

        val position = categories.indexOf(item)
        item.isSelected = true
        categories[position] = item

        lastSelectedCategory = position

        homeCategorySelect.value = item
    }


    private val categorySelectListener = object : ItemSelectListener<Category> {

        override fun onItemSelected(item: Category) {
//            val lastCategory = homeCategories[lastSelectedCategory]
//            lastCategory.isSelected = false
//            homeCategories[lastSelectedCategory] = lastCategory

            val position = categories.indexOf(item)
            item.isSelected = !item.isSelected
            categories[position] = item

            lastSelectedCategory = position
            categorySelect.value = item
        }
    }

    private val subCategorySelectListener = object : ItemSelectListener<SubCategory> {

        override fun onItemSelected(item: SubCategory) {
            val lastCategory = subCategories[lastSelectedSubCategory]
            lastCategory.isSelected = false
            subCategories[lastSelectedSubCategory] = lastCategory

            val position = subCategories.indexOf(item)
            item.isSelected = true
            subCategories[position] = item

            lastSelectedSubCategory = position

            subCategorySelect.value = item
        }
    }


    init {
//        getCategories()
    }


    val homeCategoryBinding: OnItemBindClass<Category> = OnItemBindClass<Category>().map(
        Category::class.java
    ) { itemBinding, position, item ->
        itemBinding.set(BR.category, R.layout.home_category_item)

        itemBinding.bindExtra(BR.listener, homeCategorySelectListener)
    }


    val categoryBinding: OnItemBindClass<Category> = OnItemBindClass<Category>().map(
        Category::class.java
    ) { itemBinding, position, item ->
        itemBinding.set(BR.category, R.layout.category_item)
        itemBinding.bindExtra(BR.categoryViewModel, this)

        itemBinding.bindExtra(BR.listener, categorySelectListener)
    }

    val subCategoryBinding: OnItemBindClass<SubCategory> = OnItemBindClass<SubCategory>().map(
        SubCategory::class.java
    ) { itemBinding, position, item ->
        itemBinding.set(BR.subCategory, R.layout.sub_category_item)

        itemBinding.bindExtra(BR.listener, subCategorySelectListener)
    }

    val homeSubCategoryBinding: OnItemBindClass<SubCategory> = OnItemBindClass<SubCategory>().map(
        SubCategory::class.java
    ) { itemBinding, position, item ->
        itemBinding.set(BR.subCategory, R.layout.sub_category_item_home)

        itemBinding.bindExtra(BR.listener, subCategorySelectListener)
    }

    fun getCategories(selectDefault: Boolean = false) {

        mainScope.launch {
            loading.postValue(true)
            val response = repository.getCategories()

            loading.postValue(false)

            response?.let {
                it.data?.let { categoryList ->
                    categories.add( Category("0","كل","All"
                        ,"","",0,subCategories,false))
                    categories.addAll(categoryList)
                    categories.add(Category("85","حافلة اللباس","Dress Bus"
                        ,"","",0,subCategories,false))
                    categories.add(Category("86","لعب حافلة","Toys Bus"
                        ,"", "",0,subCategories,false))
                    homeCategorySelectListener.onItemSelected(categories[selectedCat])

//                    if (categoryList.isNotEmpty() && selectDefault)
//                       homeCategorySelectListener.onItemSelected(categoryList[0])

                }
            }
        }
    }

    fun getSubCategories() {

        mainScope.launch {
            loading.postValue(true)
            val response = repository.getSubCategories(homeCategorySelect.value?.id.toString())

            loading.postValue(false)

            response?.let {
                subCategories.clear()
                if (categories[lastSelectedCategory].name == "All"){
                    selectedall.value = true
                }
                if (homeCategorySelect.value?.id.toString() != "0"){
                    subCategories.add(SubCategory("","كل","All","","",
                    ))
                }
                subCategories.addAll(it.data!!)
                subCategorySelectEvent.value = subCategories.get(0)
                subCategorySelectListener.onItemSelected(subCategories[0])
            }
        }
    }


}