package com.izo.submissionstoryapp.view.ui.addstory

import android.media.Image
import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.core.content.res.ResourcesCompat
import androidx.core.net.toFile
import androidx.lifecycle.MutableLiveData
import com.izo.submissionstoryapp.R
import com.izo.submissionstoryapp.data.RegisterResponse
import com.izo.submissionstoryapp.data.StoryRepository
import com.izo.submissionstoryapp.view.utils.DataDummy
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import com.izo.submissionstoryapp.data.Result
import com.izo.submissionstoryapp.view.addstory.AddStoryViewModel
import com.izo.submissionstoryapp.view.addstory.uriToFile
import com.izo.submissionstoryapp.view.main.getOrAwaitValue
import org.junit.Assert
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import java.io.File

@RunWith(MockitoJUnitRunner::class)
class UploadImageTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var addStoryViewModel: AddStoryViewModel
    private val dummyAddStory = DataDummy.generateDummyAddStory()

    @Before
    fun setUp() {
        addStoryViewModel = AddStoryViewModel(storyRepository)
    }

    @Test
    fun `when Get Add Story Response Should Not Null and Return Succes`() {
        val expectedResult = MutableLiveData<Result<RegisterResponse>>()
        expectedResult.value = Result.Success(dummyAddStory)
        val auth = "token"
        val text = "text"
        val image = R.drawable.image_welcome.toString()
        val file = File(image)
        val latitude = -6.3651794F
        val longitude = 106.8880891F
        `when`(addStoryViewModel.uploadStory(auth, text, file, latitude, longitude)).thenReturn(expectedResult)
        val actualResult = addStoryViewModel.uploadStory(auth, text, file, latitude, longitude).getOrAwaitValue()
        Mockito.verify(storyRepository).uploadStory(auth, text, file, latitude, longitude)
        Assert.assertNotNull(actualResult)
        Assert.assertTrue(actualResult is Result.Success<RegisterResponse>)
        Assert.assertEquals(dummyAddStory.message, (actualResult as Result.Success).data.message)
    }

    @Test
    fun `when Network Error Should Return Error`() {
        val expectedResult = MutableLiveData<Result<RegisterResponse>>()
        expectedResult.value = Result.Error("Error")
        val auth = "token"
        val text = "text"
        val image = R.drawable.image_welcome.toString()
        val file = File(image)
        val latitude = -6.3651794F
        val longitude = 106.8880891F
        `when`(addStoryViewModel.uploadStory(auth, text, file, latitude, longitude)).thenReturn(expectedResult)
        val actualResult = addStoryViewModel.uploadStory(auth, text, file, latitude, longitude).getOrAwaitValue()
        Mockito.verify(storyRepository).uploadStory(auth, text, file, latitude, longitude)
        Assert.assertNotNull(actualResult)
        Assert.assertTrue(actualResult is Result.Error)
    }

}