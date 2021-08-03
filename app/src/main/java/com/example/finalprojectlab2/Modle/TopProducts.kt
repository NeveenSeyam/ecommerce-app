package com.example.projectlab.Model.Mian_Home_page

import androidx.annotation.Keep
import java.io.Serializable

@Keep
data class TopProducts  (
    var id : String?,
    var Item_img: String?,
    var nameOfBrand: String?,
    var nameOfitem: String?,
    var Rating: Float,
    var realNum:  String?
   ): Serializable {
    constructor() : this("" , "", "", "", 0f, "")


}
