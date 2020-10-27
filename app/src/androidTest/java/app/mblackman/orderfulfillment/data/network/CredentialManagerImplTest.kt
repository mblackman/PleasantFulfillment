package app.mblackman.orderfulfillment.data.network

import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class CredentialManagerImplTest {
    private lateinit var credentialManager: CredentialManagerImpl

    @Before
    fun before() {
        credentialManager = CredentialManagerImpl(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun setAndGetCredential() {
        val expectedResult = TestCredentials("Test", "Tset")

        credentialManager.storeCredential(expectedResult, CredentialSource.None)
        val actual = credentialManager.getCredential<TestCredentials>(CredentialSource.None)

        assertThat(actual).isEqualTo(expectedResult)
    }

    data class TestCredentials(
        val name: String,
        val password: String
    ) : Credential
}