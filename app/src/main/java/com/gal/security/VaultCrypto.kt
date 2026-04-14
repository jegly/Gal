package com.gal.security

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.CipherInputStream
import javax.crypto.CipherOutputStream
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject
import javax.inject.Singleton

/**
 * AES-256-GCM vault encryption using AndroidKeyStore.
 *
 * File format: [12-byte IV][ciphertext + 16-byte GCM auth tag]
 *
 * Key properties:
 *  - Hardware-backed where available (Pixel 9 Pro Fold: Titan M2 chip)
 *  - Requires strong biometric for every use (no time window)
 *  - Invalidated if new biometrics enrolled
 *  - 256-bit key, GCM mode, no padding
 */
@Singleton
class VaultCrypto @Inject constructor() {

    companion object {
        private const val KEY_ALIAS = "gal_vault_v1"
        private const val KEYSTORE = "AndroidKeyStore"
        private const val TRANSFORM = "AES/GCM/NoPadding"
        private const val IV_LENGTH = 12
        private const val TAG_BITS = 128
        private const val CHUNK = 4 * 1024 * 1024 // 4 MB streaming
    }

    fun getOrCreateKey(): SecretKey {
        val ks = KeyStore.getInstance(KEYSTORE).apply { load(null) }
        ks.getKey(KEY_ALIAS, null)?.let { return it as SecretKey }
        return KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, KEYSTORE).apply {
            init(
                KeyGenParameterSpec.Builder(
                    KEY_ALIAS,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .setKeySize(256)
                    .setUserAuthenticationRequired(true)
                    .setUserAuthenticationParameters(0, KeyProperties.AUTH_BIOMETRIC_STRONG)
                    .setInvalidatedByBiometricEnrollment(true)
                    .build()
            )
        }.generateKey()
    }

    fun buildEncryptCipher(key: SecretKey): Cipher =
        Cipher.getInstance(TRANSFORM).apply { init(Cipher.ENCRYPT_MODE, key) }

    fun buildDecryptCipher(key: SecretKey, encryptedFile: File): Pair<Cipher, InputStream> {
        val stream = encryptedFile.inputStream()
        val iv = ByteArray(IV_LENGTH).also { stream.read(it) }
        val cipher = Cipher.getInstance(TRANSFORM).apply {
            init(Cipher.DECRYPT_MODE, key, GCMParameterSpec(TAG_BITS, iv))
        }
        return Pair(cipher, stream)
    }

    /** Encrypt [input] to [output]. Prepends IV. Handles any file size. */
    fun encrypt(cipher: Cipher, input: InputStream, output: OutputStream) {
        output.write(cipher.iv)
        CipherOutputStream(output, cipher).use { cos ->
            val buf = ByteArray(CHUNK)
            var n: Int
            while (input.read(buf).also { n = it } != -1) cos.write(buf, 0, n)
        }
    }

    /** Decrypt [input] (IV already consumed by buildDecryptCipher) to [output]. */
    fun decrypt(cipher: Cipher, input: InputStream, output: OutputStream) {
        CipherInputStream(input, cipher).use { cis ->
            val buf = ByteArray(CHUNK)
            var n: Int
            while (cis.read(buf).also { n = it } != -1) output.write(buf, 0, n)
        }
    }
}
