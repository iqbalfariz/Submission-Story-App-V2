package com.izo.submissionstoryapp.view.ui.maps

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.izo.submissionstoryapp.data.ListStoryItem
import com.izo.submissionstoryapp.data.StoryRepository
import com.izo.submissionstoryapp.view.utils.DataDummy
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import com.izo.submissionstoryapp.data.Result
import com.izo.submissionstoryapp.view.main.getOrAwaitValue
import com.izo.submissionstoryapp.view.maps.MapsViewModel
import org.junit.Assert
import org.mockito.Mockito
import org.mockito.Mockito.`when`

@RunWith(MockitoJUnitRunner::class)
class GetStoriesLocTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var mapsViewModel: MapsViewModel
    private val dummyStory = DataDummy.generateDummyStoriesResponse()

    @Before
    fun setUp() {
        mapsViewModel = MapsViewModel(storyRepository)
    }

    @Test
    fun `when Get StoriesResponse Should Not Null and Return Succes`() {
        val expectedResult = MutableLiveData<Result<List<ListStoryItem>>>()
        expectedResult.value = Result.Success(dummyStory)
        val auth = "token"
        val loc = 1
        `when`(mapsViewModel.getStories(auth, loc)).thenReturn(expectedResult)
        val actualResult = mapsViewModel.getStories(auth, loc).getOrAwaitValue()
        Mockito.verify(storyRepository).getStories(auth, loc)
        Assert.assertNotNull(actualResult)
        Assert.assertTrue(actualResult is Result.Success<List<ListStoryItem>>)
        Assert.assertEquals(dummyStory[0].name, (actualResult as Result.Success).data[0].name)
    }

    @Test
    fun `when Network Error Should Return Error`() {
        val expectedResult = MutableLiveData<Result<List<ListStoryItem>>>()
        expectedResult.value = Result.Error("Error")
        val auth = "token"
        val loc = 1
        `when`(mapsViewModel.getStories(auth, loc)).thenReturn(expectedResult)
        val actualResult = mapsViewModel.getStories(auth, loc).getOrAwaitValue()
        Mockito.verify(storyRepository).getStories(auth, loc)
        Assert.assertNotNull(actualResult)
        Assert.assertTrue(actualResult is Result.Error)
    }

}