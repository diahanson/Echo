package com.example.echo.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Index

@Entity(
    tableName = "reflections",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("userId")]
)
data class Reflection(
    @PrimaryKey(autoGenerate = true)
    val reflectionId: Int = 0,
    val userId: Int,
    val emotionTag: String,
    val reflectionText: String,
    val timestamp: Long
)
