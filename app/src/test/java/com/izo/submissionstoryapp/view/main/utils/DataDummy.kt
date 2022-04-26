package com.izo.submissionstoryapp.view.main.utils

import com.izo.submissionstoryapp.data.ListStoryItem

object DataDummy {

//    fun getDummyToken(): String {
//
//    }

    fun generateDummyStoriesResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
//                i.toString(),
//                "id $i",
//                " $i",
                i.toString(),
                "name $i",
                "description $i",
                "photo $i",
                "createdAt $i",
                i.toDouble(),
                i.toDouble()
            )
            items.add(story)
        }
        return items
    }
}