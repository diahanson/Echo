package com.example.echo.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Index

@Entity(
    tableName = "wellness_entries",
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
data class WellnessEntry(
    @PrimaryKey(autoGenerate = true)
    val entryId: Int = 0,
    val userId: Int,
    val mood: String,
    val stressLevel: Int, // 1-10
    val activities: String, // comma separated
    val wellnessScore: Int,
    val timestamp: Long 
)
