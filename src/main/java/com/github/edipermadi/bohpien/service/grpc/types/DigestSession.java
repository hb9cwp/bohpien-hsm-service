package com.github.edipermadi.bohpien.service.grpc.types;

import com.github.edipermadi.bohpien.api.Digest;

import java.security.MessageDigest;

/**
 * Digest session
 *
 * @author Edi Permadi
 */
public class DigestSession {
    private final MessageDigest provider;
    private final Digest.DigestMechanism mechanism;

    public DigestSession(final MessageDigest provider, final Digest.DigestMechanism mechanism) {
        this.provider = provider;
        this.mechanism = mechanism;
    }

    public MessageDigest getProvider() {
        return provider;
    }

    public Digest.DigestMechanism getMechanism() {
        return mechanism;
    }
}
