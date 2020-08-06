package com.connie.noted.util

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.TypedValue
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.connie.noted.NotedApplication
import com.connie.noted.R
import com.connie.noted.board.item.BoardItemAdapter
import com.connie.noted.login.UserManager
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

/**
 * Updated by Wayne Chen in Mar. 2019.
 */
object Util {

    /**
     * Determine and monitor the connectivity status
     *
     * https://developer.android.com/training/monitoring-device-state/connectivity-monitoring
     */
    fun isInternetConnected(): Boolean {
        val cm = NotedApplication.instance
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }

    fun getString(resourceId: Int): String {
        return NotedApplication.instance.getString(resourceId)
    }

    fun getColor(resourceId: Int): Int {
        return NotedApplication.instance.getColor(resourceId)
    }

    fun replaceBr(string: String): String {
        return string.replace("brbr", "\n")
    }

    fun getWindowWidth(): Int {
        return NotedApplication.instance.resources.displayMetrics.widthPixels
    }

    fun checkIfSaved(savedBy: List<String?>): Boolean {

        Logger.d("checkIfSaved: ${savedBy.contains((UserManager.userEmail) ?: "")}, currentUser = ${UserManager.userEmail}, savedBy = $savedBy")

        return savedBy.contains((UserManager.userEmail) ?: "")
    }

    fun setUpThinTags(
        tagList: MutableList<String?>,
        chipGroup: ChipGroup
    ): MutableLiveData<MutableList<String>> {

        val liveTag = MutableLiveData<MutableList<String>>().apply {
            value = mutableListOf()
        }

        for (index in tagList.indices) {
            val tagName = tagList[index]
            val chip = Chip(chipGroup.context, null, R.attr.customChipThin)
            val paddingDp = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                10f,
                NotedApplication.instance.resources.displayMetrics
            ).toInt()
            chip.setPadding(paddingDp, paddingDp, paddingDp, paddingDp)
            chip.text = tagName


            chip.setTextColor(Color.WHITE)

            val states = arrayOf(intArrayOf(-android.R.attr.state_checked))

            val chipColors = intArrayOf(Color.parseColor("#657153"))
            val chipColorsStateList = ColorStateList(states, chipColors)

            chip.chipBackgroundColor = chipColorsStateList
            chip.closeIconTint = ColorStateList(states, intArrayOf(Color.WHITE))

            chip.setOnClickListener {
                Toast.makeText(NotedApplication.instance, "Click", Toast.LENGTH_SHORT).show()

                liveTag.value?.let { list ->

                    if (list.contains(chip.text as String)) {
                        list.remove(chip.text as String)
                        Logger.d(list.toString())
                    } else {
                        list.add(chip.text as String)
                        Logger.d(list.toString())
                    }


                }

            }


            chip.setOnLongClickListener {
                chip.isCloseIconEnabled = !chip.isCloseIconEnabled


                //Added click listener on close icon to remove tag from ChipGroup
                chip.setOnCloseIconClickListener {
                    tagList.remove(tagName)
                    chipGroup.removeView(chip)

                    Logger.d(tagList.toString())
                }

                true
            }



            chipGroup.addView(chip)


        }

        return liveTag
    }

    fun setUpThinTags(
        tagList: MutableList<String?>,
        chipGroup: ChipGroup,
        listener: BoardItemAdapter.Util.OnTagClickListener
    ) {

        chipGroup.removeAllViews()

        for (index in tagList.indices) {
            val tagName = tagList[index]
            val chip = Chip(chipGroup.context, null, R.attr.customChipThin)
            val paddingDp = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                10f,
                NotedApplication.instance.resources.displayMetrics
            ).toInt()
            chip.setPadding(paddingDp, paddingDp, paddingDp, paddingDp)
            chip.text = tagName


            chip.setTextColor(Color.WHITE)

            val states = arrayOf(intArrayOf(-android.R.attr.state_checked))

            val chipColors = intArrayOf(Color.parseColor("#657153"))
            val chipColorsStateList = ColorStateList(states, chipColors)

            chip.chipBackgroundColor = chipColorsStateList
            chip.closeIconTint = ColorStateList(states, intArrayOf(Color.WHITE))

            chip.setOnClickListener {
                Toast.makeText(NotedApplication.instance, "Click", Toast.LENGTH_SHORT).show()

                listener.onTagClick(chip.text.toString())

            }


            chip.setOnLongClickListener {
                chip.isCloseIconEnabled = !chip.isCloseIconEnabled


                //Added click listener on close icon to remove tag from ChipGroup
                chip.setOnCloseIconClickListener {
                    tagList.remove(tagName)
                    chipGroup.removeView(chip)

                    Logger.d(tagList.toString())
                }

                true
            }



            chipGroup.addView(chip)


        }

    }
}
