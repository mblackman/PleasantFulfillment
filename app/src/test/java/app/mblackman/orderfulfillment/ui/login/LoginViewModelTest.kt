package app.mblackman.orderfulfillment.ui.login

import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.mblackman.orderfulfillment.BuildConfig
import app.mblackman.orderfulfillment.data.network.CredentialManager
import app.mblackman.orderfulfillment.data.network.CredentialSource
import app.mblackman.orderfulfillment.sharedTest.TestCredentials
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LoginViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var credentialManager: CredentialManager

    @RelaxedMockK
    lateinit var etsyRedirectLogin: RedirectLogin

    private lateinit var viewModel: LoginViewModel

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = LoginViewModel(TestCoroutineDispatcher(), credentialManager, etsyRedirectLogin)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun startEtsyLoginSuccess() {
        val etsyLoginUrl = "EtsyTestPage.Url"
        every { etsyRedirectLogin.getAuthorizationPage() } returns etsyLoginUrl

        viewModel.startEtsyLogin()

        val authorizationPage = viewModel.authorizationUrl
        val loginStatus = viewModel.loginStatus
        authorizationPage.observeForever {}
        loginStatus.observeForever {}

        assertThat(authorizationPage.value).isEqualTo(etsyLoginUrl)
        assertThat(loginStatus.value).isEqualTo(LoginStatus.GET_AUTHORIZATION_PAGE_SUCCESS)
    }

    @Test
    fun startEtsyLoginNoUrl() {
        every { etsyRedirectLogin.getAuthorizationPage() } returns null

        viewModel.startEtsyLogin()

        val authorizationPage = viewModel.authorizationUrl
        val loginStatus = viewModel.loginStatus
        authorizationPage.observeForever {}
        loginStatus.observeForever {}

        assertThat(authorizationPage.value).isEqualTo(null)
        assertThat(loginStatus.value).isEqualTo(LoginStatus.GET_AUTHORIZATION_PAGE_FAILED)
    }

    @Test
    fun handleEtsyLoginRedirectUri() {
        val oauthVerifier = "test_verifier"
        val testCredentials = TestCredentials("Test", "Test")
        val testUri = Uri.parse(BuildConfig.ETSY_REDIRECT_URL)
            .buildUpon()
            .appendQueryParameter("oauth_verifier", oauthVerifier)
            .build()

        every { etsyRedirectLogin.getAccessToken(oauthVerifier) } returns testCredentials

        viewModel.handleRedirectUri(testUri)

        verify { credentialManager.storeCredential(testCredentials, CredentialSource.Etsy) }
    }
}