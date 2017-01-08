package com.github.edipermadi.bohpien.service.grpc.types;

import com.github.edipermadi.bohpien.api.Digest;

/**
 * Digest Mechanism Enumeration
 *
 * @author Edi Permadi
 */
public enum DigestMechanismEnum {
    MD2(Digest.DigestMechanism.MD2, "MD2"),
    MD4(Digest.DigestMechanism.MD4, "MD4"),
    MD5(Digest.DigestMechanism.MD5, "MD5"),
    RIPEMD_128(Digest.DigestMechanism.RIPEMD_128, "RIPEMD128"),
    RIPEMD_160(Digest.DigestMechanism.RIPEMD_160, "RIPEMD160"),
    RIPEMD_256(Digest.DigestMechanism.RIPEMD_256, "RIPEMD256"),
    RIPEMD_320(Digest.DigestMechanism.RIPEMD_320, "RIPEMD320"),
    SHA1(Digest.DigestMechanism.SHA1, "SHA1"),
    SHA2_224(Digest.DigestMechanism.SHA2_224, "SHA-224"),
    SHA2_256(Digest.DigestMechanism.SHA2_256, "SHA-256"),
    SHA2_384(Digest.DigestMechanism.SHA2_384, "SHA-384"),
    SHA2_512(Digest.DigestMechanism.SHA2_512, "SHA-512"),
    SHA3_224(Digest.DigestMechanism.SHA3_224, "SHA3-224"),
    SHA3_256(Digest.DigestMechanism.SHA3_256, "SHA3-256"),
    SHA3_384(Digest.DigestMechanism.SHA3_384, "SHA3-384"),
    SHA3_512(Digest.DigestMechanism.SHA3_512, "SHA3-512"),
    KECCAK_224(Digest.DigestMechanism.KECCAK_224, "Keccak-224"),
    KECCAK_256(Digest.DigestMechanism.KECCAK_256, "Keccak-256"),
    KECCAK_384(Digest.DigestMechanism.KECCAK_384, "Keccak-384"),
    KECCAK_512(Digest.DigestMechanism.KECCAK_512, "Keccak-512"),
    SKEIN_256(Digest.DigestMechanism.SKEIN_256, "Skein-256-256"),
    SKEIN_512(Digest.DigestMechanism.SKEIN_512, "Skein-512-512"),
    SKEIN_1024(Digest.DigestMechanism.SKEIN_1024, "Skein-1024-1024"),
    GOST3411_256(Digest.DigestMechanism.GOST3411_256, "GOST3411"),
    GOST3411_2012_256(Digest.DigestMechanism.GOST3411_2012_256, "GOST3411-2012-256"),
    GOST3411_2012_512(Digest.DigestMechanism.GOST3411_2012_512, "GOST3411-2012-512"),
    SM3_256(Digest.DigestMechanism.SM3_256, "SM3"),
    TIGER_192(Digest.DigestMechanism.TIGER_192, "Tiger"),
    WHIRLPOOL_512(Digest.DigestMechanism.WHIRLPOOL_512, "Whirlpool");

    private final Digest.DigestMechanism mechanism;
    private final String algorithm;

    DigestMechanismEnum(final Digest.DigestMechanism mechanism, final String algorithm) {
        this.mechanism = mechanism;
        this.algorithm = algorithm;
    }

    public Digest.DigestMechanism getMechanism() {
        return mechanism;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public static DigestMechanismEnum fromMechanism(final Digest.DigestMechanism mechanism) {
        for (final DigestMechanismEnum value : values()) {
            if (value.getMechanism().equals(mechanism)) {
                return value;
            }
        }

        throw new IndexOutOfBoundsException(mechanism.toString());
    }
}
