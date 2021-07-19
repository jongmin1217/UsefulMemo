package com.usefulmemo.memo.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "memo")
data class Memo(
    @PrimaryKey(autoGenerate = true)
    val id : Long,
    val text : String,
    val regDate : Long,
    val active : Boolean,
    val folderId : Long
)