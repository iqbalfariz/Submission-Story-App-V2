package com.izo.submissionstoryapp.view.utils

import com.izo.submissionstoryapp.data.ListStoryItem

object DataDummy {

    // Data dummy ketika user login

//    fun generateDummyUserLogin(): UserModel {
//        data = preferencesDataStore()
//    }

    // Data dummy response api get story
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