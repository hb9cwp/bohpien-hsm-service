package com.github.edipermadi.bohpien.service.grpc.types;

import com.github.edipermadi.bohpien.api.Cipher;

import java.util.UUID;

/**
 * Cipher session for encryption and decryption functions
 *
 * @author Edi Permadi
 */
public class CipherSession {
    private final javax.crypto.Cipher provider;
    private final Cipher.CipherAlgorithm algorithm;
    private final Cipher.CipherMode mode;
    private final Cipher.CipherPadding padding;
    private final UUID keyId;

    public CipherSession(final javax.crypto.Cipher provider, final Cipher.CipherAlgorithm algorithm, final Cipher.CipherMode mode,
                         final Cipher.CipherPadding padding, final UUID keyId) {
        this.provider = provider;
        this.algorithm = algorithm;
        this.mode = mode;
        this.padding = padding;
        this.keyId = keyId;
    }

    public javax.crypto.Cipher getProvider() {
        return provider;
    }

    public Cipher.CipherAlgorithm getAlgorithm() {
        return algorithm;
    }

    public Cipher.CipherMode getMode() {
        return mode;
    }

    public Cipher.CipherPadding getPadding() {
        return padding;
    }

    public UUID getKeyId() {
        return keyId;
    }
}
