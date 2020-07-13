package com.connie.noted.board.item

import androidx.paging.PageKeyedDataSource
import com.connie.noted.board.BoardTypeFilter
import com.connie.noted.data.Board

class PagingDataSource(val type: BoardTypeFilter): PageKeyedDataSource<String, List<Board>>() {


    override fun loadInitial(
        params: LoadInitialParams<String>,
        callback: LoadInitialCallback<String, List<Board>>
    ) {
        TODO("Not yet implemented")
    }

    override fun loadAfter(
        params: LoadParams<String>,
        callback: LoadCallback<String, List<Board>>
    ) {
        TODO("Not yet implemented")
    }

    override fun loadBefore(
        params: LoadParams<String>,
        callback: LoadCallback<String, List<Board>>
    ) {
        TODO("Not yet implemented")
    }
}