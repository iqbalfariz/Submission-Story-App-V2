package com.izo.submissionstoryapp.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.izo.submissionstoryapp.data.local.database.StoryDatabase
import com.izo.submissionstoryapp.data.remote.ApiService

@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator(
    private val database: StoryDatabase,
    private val apiService: ApiService,
    private val auth: String
) : RemoteMediator<Int, ListStoryItem>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ListStoryItem>
    ): MediatorResult {
        val page = INITIAL_PAGE_INDEX
        return try {
            val responseData = apiService.getStoriesPaging(auth, page, state.config.pageSize, 1).listStory
            val endOfPaginationReached = responseData.isEmpty()
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.storyDao().deleteAll()
                }
                database.storyDao().insertQuote(responseData)
            }
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: Exception) {
            MediatorResult.Error(exception)
        }
    }

}