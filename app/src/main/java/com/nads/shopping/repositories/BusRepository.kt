package com.nads.shopping.repositories

import com.nads.shopping.api.EndPoints
import com.nads.shopping.datamodels.*

class BusRepository(private val api: EndPoints) : BaseRepository() {
    suspend fun getTimeSlots(type:String,date:String): BaseModel<List<TimeSlotModel>>? {
        return safeApiCall(
            call = { api.getTimeSlotsAsync("token",type,date).await() },
            errorMessage = "Error in getting time list"
        )
    }
    suspend fun getHolidaysSlot(type:Int): BaseModel<List<Holidays>>? {
        return safeApiCall(
            call = { api.getbusholidaysAsync(type).await() },
            errorMessage = "Error in getting holiday list"
        )
    }
    suspend fun bookbus(type:String,name:String,phone:String,
                        building:String,street:String,long:String,zone:String,land_mark:String,date:String,
                        time:String,map_address:String,lat:String,category_id:String): BasicModel? {
        return safeApiCall(
            call = { api.book_busAsync("token",type,name,phone,building,street,long,zone,land_mark,date,time,map_address,lat,category_id).await() },
            errorMessage = "Error in getting holiday list"
        )
    }

}