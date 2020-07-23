package com.connie.noted.add2board

import android.graphics.Rect
import android.util.Log
import android.view.View
import androidx.databinding.InverseMethod
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.connie.noted.NotedApplication
import com.connie.noted.R
import com.connie.noted.data.Board
import com.connie.noted.data.Note
import com.connie.noted.data.source.NotedRepository
import kotlinx.coroutines.*

/**
 * Created by Wayne Chen in Jul. 2019.
 *
 * The [ViewModel] that is attached to the [Add2cartDialog].
 */
class Add2boardViewModel(
    private val notedRepository: NotedRepository
) : ViewModel() {

    // Detail has product data from arguments
    val board = MutableLiveData<Board>().apply {
        value = Board()
    }

    // Handle the error for checkout
    private val _invalidInput = MutableLiveData<Int>()

    val invalidInput: LiveData<Int>
        get() = _invalidInput


    var liveNotes = MutableLiveData<List<Note>>()




    // Handle navigation to Added Success
    private val _navigateToAddedSuccess = MutableLiveData<Board>()

    val navigateToAddedSuccess: LiveData<Board>
        get() = _navigateToAddedSuccess

    // Handle navigation to Added Fail
    private val _navigateToAddedFail = MutableLiveData<Board>()

    val navigateToAddedFail: LiveData<Board>
        get() = _navigateToAddedFail

    // Handle leave add2cart
    private val _leave = MutableLiveData<Boolean>()

    val leave: LiveData<Boolean>
        get() = _leave

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)




    /**
     * When the [ViewModel] is finished, we cancel our coroutine [viewModelJob], which tells the
     * Retrofit service to stop.
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


    /**
     * track [StylishRepository.getUserProfile]: -> [DefaultStylishRepository] : [StylishRepository] -> [StylishLocalDataSource] : [StylishDataSource]
     */
    fun uploadBoard() {
//        product.value?.let {
//            coroutineScope.launch {
//                selectedVariant.value?.apply {
//                    it.selectedVariant = this
//                    it.amount = amount.value
//                    if (stylishRepository.isProductInCart(
//                            it.id,
//                            it.selectedVariant.colorCode,
//                            it.selectedVariant.size
//                        )
//                    ) {
//
//                        _navigateToAddedFail.value = it
//                    } else {
//                        stylishRepository.insertProductInCart(it)
//                        _navigateToAddedSuccess.value = it
//                    }
//                }
//            }
//        }
    }

    fun onAddedSuccessNavigated() {
        _navigateToAddedSuccess.value = null
    }

    fun onAddedFailNavigated() {
        _navigateToAddedFail.value = null
    }


    fun convertLongToString(value: Long): String {
        return value.toString()
    }

    fun leave() {
        _leave.value = true
    }

    fun onLeaveCompleted() {
        _leave.value = null
    }

    fun nothing() {}




}
