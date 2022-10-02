package com.nads.shopping.viewmodels

import com.hadilq.liveevent.LiveEvent

class HomeNavigatorViewModel :BaseViewModel() {

     var type = LiveEvent<String>()
    var id = LiveEvent<String>()
    var title = LiveEvent<String>()




}