package com.nads.shopping.viewmodels

import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import com.hadilq.liveevent.LiveEvent
import com.nads.shopping.BR
import com.nads.shopping.R
import com.nads.shopping.api.ApiFactory
import com.nads.shopping.datamodels.Holidays
import com.nads.shopping.datamodels.TimeSlotModel
import com.nads.shopping.listeners.ItemSelectListener
import com.nads.shopping.repositories.BusRepository
import kotlinx.coroutines.launch
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass

class DressBusViewModel:BaseViewModel()  {
    val loading = MutableLiveData<Boolean>()
        val timeSlots=ObservableArrayList<TimeSlotModel>()
    val timeslotsselected = LiveEvent<TimeSlotModel>()
    var lastSelectedCategory = 0
    val makevisible =LiveEvent<Boolean>()
    val holidayslot=MutableLiveData<List<Holidays>>()
    val holidaysslot2 = LiveEvent<Boolean>()
    var locationlistened = LiveEvent<String>()
    var bookingsuccess = LiveEvent<String>()

    val repository = BusRepository(ApiFactory.brandattyAPI)


    private val timeslotselectlistener = object : ItemSelectListener<TimeSlotModel> {

        override fun onItemSelected(item: TimeSlotModel) {

            val lastCategory = timeSlots[lastSelectedCategory]
            lastCategory.isSelected = false
            timeSlots[lastSelectedCategory] = lastCategory

            val position = timeSlots.indexOf(item)
            item.isSelected = true
            timeSlots[position] = item

            lastSelectedCategory = position

            timeslotsselected.value = item

        }
    }

    val timeSlotBinding: OnItemBindClass<TimeSlotModel> = OnItemBindClass<TimeSlotModel>().map(
        TimeSlotModel::class.java
    ) { itemBinding, position, item ->
        itemBinding.set(BR.timesloter, R.layout.timeslot)

        itemBinding.bindExtra(BR.listener, timeslotselectlistener)
    }

    fun getTimeSloter(type:String,date:String){
        mainScope.launch {
            loading.postValue(true)
            val response = repository.getTimeSlots(type,date)

            loading.postValue(false)

            response?.let {
                it.data?.let {timeslotList->
                    timeSlots.addAll(timeslotList)
                    makevisible.value = true
                }
            }
        }


    }

    fun getHolidays(type:Int){
        mainScope.launch {
            loading.postValue(true)
            val response = repository.getHolidaysSlot(type)
            loading.postValue(false)
            response?.let {
                it.data?.let {
                    holidayslot.value = it

                }

            }
        }


    }


    fun getBooking(type:String,name:String,
                   phone:String,building:String,street:String,long:String,zone:String,
                   land_mark:String,date:String,time:String,map_address:String
                   ,lat:String,category_id:String){
        mainScope.launch {
            loading.postValue(true)
            val response = repository.bookbus(type,name,
                phone,building,street,long,zone,land_mark,date,time,map_address
            ,lat,category_id)
//            response.toString().let { Log.e("Nadeemh", it) }
            loading.postValue(false)
            response?.let {
                it.let { Log.e("nadeem",it.toString()) }
                bookingsuccess.value = it.message
            }
        }


    }




}