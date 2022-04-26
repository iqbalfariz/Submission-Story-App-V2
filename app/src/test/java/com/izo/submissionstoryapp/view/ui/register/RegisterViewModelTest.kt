package com.izo.submissionstoryapp.view.ui.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.izo.submissionstoryapp.data.RegisterResponse
import com.izo.submissionstoryapp.data.StoryRepository
import com.izo.submissionstoryapp.view.register.RegisterViewModel
import com.izo.submissionstoryapp.view.utils.DataDummy
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoJUnitRunner
import com.izo.submissionstoryapp.data.Result
import com.izo.submissionstoryapp.view.UnitTest.getOrAwaitValue
import org.junit.Assert
import org.mockito.Mockito
import org.mockito.Mockito.`when`

@RunWith(MockitoJUnitRunner::class)
class RegisterViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var registerViewModel: RegisterViewModel
    private val dummyRegister = DataDummy.generateDummyRegisterResponse()

    @Before
    fun setUp() {
        registerViewModel = RegisterViewModel(storyRepository)
    }

    @Test
    fun `when Get RegisterResponse Should Not Null and Return Succes`() {
        val expectedResult = MutableLiveData<Result<RegisterResponse>>()
        expectedResult.value = Result.Success(dummyRegister)
        val name = "name"
        val email = "email"
        val password = "password"
        `when`(registerViewModel.postDataRegis(name, email, password)).thenReturn(expectedResult)
        val actualResult = registerViewModel.postDataRegis(name, email, password).getOrAwaitValue()
        Mockito.verify(storyRepository).postDataRegis(name, email, password)
        Assert.assertNotNull(actualResult)
        Assert.assertTrue(actualResult is Result.Success<RegisterResponse>)
        Assert.assertEquals(dummyRegister.error, (actualResult as Result.Success).data.error)
    }

    @Test
    fun `when Network Error Should Return Error`() {
        val expectedResult = MutableLiveData<Result<RegisterResponse>>()
        expectedResult.value = Result.Error("Error")
        val name = "name"
        val email = "email"
        val password = "password"
        `when`(registerViewModel.postDataRegis(name, email, password)).thenReturn(expectedResult)
        val actualResult = registerViewModel.postDataRegis(name, email, password).getOrAwaitValue()
        Mockito.verify(storyRepository).postDataRegis(name, email, password)
        Assert.assertNotNull(actualResult)
        Assert.assertTrue(actualResult is Result.Error)
    }

}