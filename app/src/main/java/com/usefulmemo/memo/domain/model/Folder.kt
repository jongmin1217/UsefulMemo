package com.usefulmemo.memo.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "folder")
data class Folder(
    @PrimaryKey(autoGenerate = true)
    val id : Long,
    val name : String
)