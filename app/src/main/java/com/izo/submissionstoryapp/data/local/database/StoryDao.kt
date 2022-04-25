package com.izo.submissionstoryapp.data.local.database


import androidx.paging.PagingSource
import androidx.room.*
import com.izo.submissionstoryapp.data.ListStoryItem

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuote(quote: List<ListStoryItem>)

    @Query("SELECT * FROM story")
    fun getAllStory(): PagingSource<Int, ListStoryItem>

    @Query("DELETE FROM story")
    suspend fun deleteAll()
}