package com.usefulmemo.memo.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.usefulmemo.memo.utils.Util

@Entity(tableName = "memo")
data class Memo(
    @PrimaryKey(autoGenerate = true)
    var id : Long,
    var text : String,
    var regDate : Long,
    var active : Boolean,
    var folderId : Long
){
    fun textCheck() : Boolean{
        return text.isBlank()
    }

    fun getDate() : String{
        val memoDate = Util.formatDate(regDate)
        val nowDate = Util.getDate()

        return if(memoDate == nowDate){
            Util.formatTimeWithMarker(regDate)
        }else{
            memoDate
        }
    }

    fun getTextInfo() : List<String> {
        val list = ArrayList<String>()
        text.split("\n").apply {
            for(i in this.indices){
                if(this[i].isNotBlank()) list.add(this[i])
            }
        }
        return list
    }
}