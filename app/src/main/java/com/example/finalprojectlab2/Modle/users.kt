package com.example.finalprojectlab2.Modle

import androidx.annotation.Keep
import com.example.projectlab.Model.Mian_Home_page.TopProducts
import java.io.Serializable


@Keep
data class users  (
    var AboutME : String?,
    var Location: String?,
    var Skills: String?,
    var career: String?,
    var id : String?,
    var imgURL: String?,
    var itemIBuy  :List<TopProducts>?,
    var phone_number: String?,
    var usermail:  String?,
    var username:  String?
): Serializable{
    constructor() : this("" , "", "", "", "" , "", null , "", "" , "")


}
















/*
data class users {
    var AboutME : String?,
    var Location: String?,
    var Skills: String?,
    var career: String?,
    var id: Float,
    var imgURL:  String? ,
    var itemIBuy  :List<TopProducts>,
     var Phone_number : String?,
     var usermail : String?,
       var username :String?


}*/
