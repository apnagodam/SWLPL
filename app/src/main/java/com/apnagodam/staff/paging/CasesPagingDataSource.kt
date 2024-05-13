package com.apnagodam.staff.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.apnagodam.staff.Network.ApiService
import com.apnagodam.staff.db.SharedPreferencesRepository
import com.apnagodam.staff.module.AllCaseIDResponse

class CasesPagingDataSource(private val apiService: ApiService) :
    PagingSource<Int, AllCaseIDResponse.Datum>() {

    companion object {
        private const val STARTING_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, AllCaseIDResponse.Datum>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AllCaseIDResponse.Datum> {
        val page = params.key ?: STARTING_PAGE_INDEX
        return try {
            val response = apiService.getAllCasesPagination("15", page, "1", "")
            val allCasesList = arrayListOf<AllCaseIDResponse.Datum>()
            SharedPreferencesRepository.getDataManagerInstance().user.let { userDetails ->
                response.getaCase().let {
                    if (it!!.data != null) {
                        it.data.let { data ->
                            if (data != null) {
                                for (i in data.indices) {

                                    if ((data[i].cctvReport == null
                                                || data[i].ivrReport == null
                                                || data[i].secondQualityReport == null
                                                || data[i].sendToLab == null
                                                || data[i].firstKantaParchi == null
                                                || data[i].secondKantaParchi == null
                                                || data[i].labourBook == null
                                                || data[i].truckbook == null
                                                || data[i].gatepassReport == null)
                                    ) {
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


            }
            val list = allCasesList.distinctBy { it.caseId }
            LoadResult.Page(
                data = list.reversed().toList(),
                prevKey = if (page == STARTING_PAGE_INDEX) null else page.minus(1),
                nextKey = if (response.getaCase()!!.data!!.isEmpty() || page > 5) null else page.plus(
                    1
                )
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }
}