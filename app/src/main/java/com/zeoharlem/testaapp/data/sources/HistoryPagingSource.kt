package com.zeoharlem.testaapp.data.sources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.zeoharlem.testaapp.models.Tests
import java.io.IOException

class HistoryPagingSource  :
    PagingSource<Int, Tests>() {

    override fun getRefreshKey(state: PagingState<Int, Tests>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Tests> {
        return try {
            val page = params.key ?: 1

            val responses = emptyList<Tests>()

            LoadResult.Page(
                data = responses,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (responses.isEmpty()) null else page.plus(1)
            )
        } catch (e: IOException) {
            println("errorIOxception: ${e.message}")
            LoadResult.Error(e)
        } catch (e: Exception) {
            println("errorException: ${e.message}")
            LoadResult.Error(e)
        }
    }

}