package jyotti.apexing.apexing_android.data.local

import androidx.paging.PagingSource
import androidx.paging.PagingState
import jyotti.apexing.apexing_android.data.model.statistics.MatchModels

class MatchPagingSource(private val matchDao: MatchDao) : PagingSource<Int, MatchModels>() {

    companion object {
        const val START_PAGE_INDEX = 0
    }

    override fun getRefreshKey(state: PagingState<Int, MatchModels>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MatchModels> {
        val position = params.key ?: START_PAGE_INDEX
        val data = matchDao.getPage(index = position, loadSize = params.loadSize)

        return LoadResult.Page(
            data = data,
            prevKey = if (position == START_PAGE_INDEX) null else position - 1,
            nextKey = if (data.isNullOrEmpty()) null else position + 1
        )
    }
}