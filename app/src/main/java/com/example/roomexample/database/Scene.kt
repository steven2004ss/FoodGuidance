package com.example.roomexample.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Scene(
    var name: String,
    var address: String,
    var phone:String,
    var description: String,
    var photoId: Int,
    var photoFile: String = "",  //for the external photo file
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
) {
//    @PrimaryKey(autoGenerate = true)
//    var id: Long = 0L
    //data fields declared here will not be observed in the livedata
    //these data fields need not be initiated when created
}
