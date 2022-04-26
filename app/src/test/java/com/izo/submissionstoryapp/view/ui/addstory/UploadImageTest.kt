package com.izo.submissionstoryapp.view.ui.addstory

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
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

//    @Test
//    fun `when Get Add Story Response Should Not Null and Return Succes`() {
//        val expectedResult = MutableLiveData<Result<RegisterResponse>>()
//        expectedResult.value = Result.Success(dummyAddStory)
//        val email = "email"
//        val password = "password"
//        val file: File
//        `when`(addStoryViewModel.postDataLogin(email, password)).thenReturn(expectedResult)
//        val actualResult = addStoryViewModel.postDataLogin(email, password).getOrAwaitValue()
//        Mockito.verify(storyRepository).postDataLogin(email, password)
//        Assert.assertNotNull(actualResult)
//        Assert.assertTrue(actualResult is Result.Success<RegisterResponse>)
//        Assert.assertEquals(dummyLogin.error, (actualResult as Result.Success).data.error)
//    }

//    @Test
//    fun `when Network Error Should Return Error`() {
//        val expectedResult = MutableLiveData<Result<RegisterResponse>>()
//        expectedResult.value = Result.Error("Error")
//        val email = "email"
//        val password = "password"
//        `when`(addStoryViewModel.postDataLogin(email, password)).thenReturn(expectedResult)
//        val actualResult = addStoryViewModel.postDataLogin(email, password).getOrAwaitValue()
//        Mockito.verify(storyRepository).postDataLogin(email, password)
//        Assert.assertNotNull(actualResult)
//        Assert.assertTrue(actualResult is Result.Error)
//    }
}