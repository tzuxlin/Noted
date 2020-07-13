package com.connie.noted.board.item

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.connie.noted.board.BoardTypeFilter
import com.connie.noted.data.Board

class PagingDataSourceFactory (val type: BoardTypeFilter): DataSource.Factory<String, List<Board>>() {

    val sourceLiveData = MutableLiveData<PagingDataSource>()


    override fun create(): DataSource<String, List<Board>> {
        val source = PagingDataSource(type)
        sourceLiveData.postValue(source)
        return source

    }
}