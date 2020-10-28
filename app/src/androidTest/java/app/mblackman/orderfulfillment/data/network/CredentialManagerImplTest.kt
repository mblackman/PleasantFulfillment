package app.mblackman.orderfulfillment.data.network

import androidx.test.core.app.ApplicationProvider
import app.mblackman.orderfulfillment.sharedTest.TestCredentials
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

    @Test
    fun setAndClearCredential() {
        val credential = TestCredentials("Test", "Tset")

        credentialManager.storeCredential(credential, CredentialSource.None)
        credentialManager.clearCredential(credential::class, CredentialSource.None)
        val actual = credentialManager.getCredential<TestCredentials>(CredentialSource.None)

        assertThat(actual).isNull()
    }

    @Test
    fun updateCredential() {
        val originalCreds = TestCredentials("Test", "Tset")
        val updatedCreds = TestCredentials("Updated", "Detadpu")

        credentialManager.storeCredential(originalCreds, CredentialSource.None)
        credentialManager.storeCredential(updatedCreds, CredentialSource.None)
        val actual = credentialManager.getCredential<TestCredentials>(CredentialSource.None)

        assertThat(actual).isEqualTo(updatedCreds)
    }
}