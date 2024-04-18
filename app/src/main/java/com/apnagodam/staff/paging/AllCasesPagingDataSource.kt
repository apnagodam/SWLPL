package com.apnagodam.staff.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.apnagodam.staff.Network.ApiService
import com.apnagodam.staff.db.SharedPreferencesRepository
import com.apnagodam.staff.module.AllCaseIDResponse
import javax.inject.Inject

class AllCasesPagingDataSource (private val apiService: ApiService,var searchQuery :String ="") :PagingSource<Int, AllCaseIDResponse.Datum>(){
    override fun getRefreshKey(state: PagingState<Int, AllCaseIDResponse.Datum>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AllCaseIDResponse.Datum> {
        val page = params.key ?: AllCasesPagingDataSource.STARTING_PAGE_INDEX
        return try {
            val response = apiService.getAllCasesPagination("15", page, "1", searchQuery)
            val allCasesList = arrayListOf<AllCaseIDResponse.Datum>()
            SharedPreferencesRepository.getDataManagerInstance().user.let { userDetails ->
                response.getaCase().let {
                    if (it!!.data != null) {
                        it.data.let { data ->
                            if (data != null) {
                                for (i in data.indices) {

                                    if (userDetails.terminal != null) {
                                        if (data[i].terminalId.toString() == userDetails.terminal.toString()) {
                                            allCasesList.add(data[i])
                                        }
                                    } else {
                                        allCasesList.add(data[i])

                                    }
                                }
                            }

                        }
                    }

                }


            }
            LoadResult.Page(
                data = allCasesList,
                prevKey = if (page == AllCasesPagingDataSource.STARTING_PAGE_INDEX) null else page.minus(1),
                nextKey = if (response.getaCase()!!.data!!.isEmpty()) null else page.plus(
                    1
                )
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }
    companion object {
        private const val STARTING_PAGE_INDEX = 1
    }
}