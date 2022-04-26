package com.izo.submissionstoryapp.view.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.izo.submissionstoryapp.data.LoginResponse
import com.izo.submissionstoryapp.data.StoryRepository
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
import com.izo.submissionstoryapp.view.login.LoginViewModel
import org.junit.Assert
import org.mockito.Mockito
import org.mockito.Mockito.`when`

@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var loginViewModel: LoginViewModel
    private val dummyLogin = DataDummy.generateDummyLoginResponse()

    @Before
    fun setUp() {
        loginViewModel = LoginViewModel(storyRepository)
    }

    @Test
    fun `when Get LoginResponse Should Not Null and Return Succes`() {
        val expectedResult = MutableLiveData<Result<LoginResponse>>()
        expectedResult.value = Result.Success(dummyLogin)
        val email = "email"
        val password = "password"
        `when`(loginViewModel.postDataLogin(email, password)).thenReturn(expectedResult)
        val actualResult = loginViewModel.postDataLogin(email, password).getOrAwaitValue()
        Mockito.verify(storyRepository).postDataLogin(email, password)
        Assert.assertNotNull(actualResult)
        Assert.assertTrue(actualResult is Result.Success<LoginResponse>)
        Assert.assertEquals(dummyLogin.error, (actualResult as Result.Success).data.error)
    }

    @Test
    fun `when Network Error Should Return Error`() {
        val expectedResult = MutableLiveData<Result<LoginResponse>>()
        expectedResult.value = Result.Error("Error")
        val email = "email"
        val password = "password"
        `when`(loginViewModel.postDataLogin(email, password)).thenReturn(expectedResult)
        val actualResult = loginViewModel.postDataLogin(email, password).getOrAwaitValue()
        Mockito.verify(storyRepository).postDataLogin(email, password)
        Assert.assertNotNull(actualResult)
        Assert.assertTrue(actualResult is Result.Error)
    }
}