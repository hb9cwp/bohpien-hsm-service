package com.github.edipermadi.bohpien.service.grpc;

import com.github.edipermadi.bohpien.api.*;
import com.github.edipermadi.bohpien.service.grpc.types.DigestMechanismEnum;
import com.github.edipermadi.bohpien.service.grpc.types.DigestSession;
import com.google.protobuf.ByteString;
import io.grpc.stub.StreamObserver;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Bohpien GRPC server implementation
 *
 * @author Edi Permadi
 */
public final class BohpienServer extends BohpienServiceGrpc.BohpienServiceImplBase {

    private static final String JCE_PROVIDER = "BC";
    private final Map<UUID, DigestSession> digestSessions = new HashMap<>();

    @Override
    public void digestInit(final Digest.DigestInitRequest request,
                           final StreamObserver<Digest.DigestInitResponse> responseObserver) {
        try {
            final DigestMechanismEnum mechanism = DigestMechanismEnum.fromMechanism(request.getMechanism());
            final String algorithm = mechanism.getAlgorithm();
            final MessageDigest digest = MessageDigest.getInstance(algorithm, JCE_PROVIDER);
            final UUID session = UUID.randomUUID();

            /* register session */
            digestSessions.put(session, new DigestSession(digest, request.getMechanism()));

            /* set response  */
            final Uuid.UUID uuid = Uuid.UUID.newBuilder()
                    .setMsb(session.getMostSignificantBits())
                    .setLsb(session.getLeastSignificantBits())
                    .build();
            final Digest.DigestInitResponse response = Digest.DigestInitResponse.newBuilder()
                    .setErrorCode(Digest.DigestErrorCode.DIGEST_ERROR_NONE)
                    .setSessionId(uuid)
                    .build();
            responseObserver.onNext(response);
        } catch (final IndexOutOfBoundsException ex) {
            final Digest.DigestInitResponse response = Digest.DigestInitResponse.newBuilder()
                    .setErrorCode(Digest.DigestErrorCode.DIGEST_ERROR_UNKNOWN_MECHANISM)
                    .build();
            responseObserver.onNext(response);
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            final Digest.DigestInitResponse response = Digest.DigestInitResponse.newBuilder()
                    .setErrorCode(Digest.DigestErrorCode.DIGEST_ERROR_SYSTEM)
                    .build();
            responseObserver.onNext(response);
        } finally {
            responseObserver.onCompleted();
        }
    }

    @Override
    public void digestUpdate(final Digest.DigestUpdateRequest request,
                             final StreamObserver<Digest.DigestUpdateResponse> responseObserver) {
        try {
            final Uuid.UUID sessionId = request.getSessionId();
            final UUID uuid = new UUID(sessionId.getMsb(), sessionId.getLsb());
            final Digest.DigestUpdateResponse.Builder builder = Digest.DigestUpdateResponse.newBuilder();

            /* load session */
            final DigestSession session = digestSessions.get(uuid);
            if (session == null) {
                builder.setErrorCode(Digest.DigestErrorCode.DIGEST_ERROR_SESSION_NOT_FOUND);
            } else {
                /* append data */
                final byte[] data = request.getData().toByteArray();
                session.getProvider().update(data);

                builder.setErrorCode(Digest.DigestErrorCode.DIGEST_ERROR_NONE);
            }

            responseObserver.onNext(builder.build());
        } finally {
            responseObserver.onCompleted();
        }
    }

    @Override
    public void digestFinal(final Digest.DigestFinalRequest request,
                            final StreamObserver<Digest.DigestFinalResponse> responseObserver) {
        try {
            final Uuid.UUID sessionId = request.getSessionId();
            final UUID uuid = new UUID(sessionId.getMsb(), sessionId.getLsb());
            final Digest.DigestFinalResponse.Builder builder = Digest.DigestFinalResponse.newBuilder();

            /* load session */
            final DigestSession session = digestSessions.get(uuid);
            if (session == null) {
                builder.setErrorCode(Digest.DigestErrorCode.DIGEST_ERROR_SESSION_NOT_FOUND);
            } else {
                builder.setErrorCode(Digest.DigestErrorCode.DIGEST_ERROR_NONE)
                        .setDigest(ByteString.copyFrom(session.getProvider().digest()));
            }

            responseObserver.onNext(builder.build());
        } finally {
            responseObserver.onCompleted();
        }
    }

    @Override
    public void digestKey(final Digest.DigestKeyRequest request,
                          final StreamObserver<Digest.DigestKeyResponse> responseObserver) {
    }

    @Override
    public void digestData(final Digest.DigestDataRequest request,
                           final StreamObserver<Digest.DigestDataResponse> responseObserver) {
        try {
            final Digest.DigestMechanism mechanism = request.getMechanism();
            final String algorithm = DigestMechanismEnum.fromMechanism(mechanism).getAlgorithm();
            final MessageDigest digest = MessageDigest.getInstance(algorithm, JCE_PROVIDER);
            digest.update(request.getData().toByteArray());
            final byte[] hash = digest.digest();

            /* set response  */
            final Digest.DigestDataResponse response = Digest.DigestDataResponse.newBuilder()
                    .setErrorCode(Digest.DigestErrorCode.DIGEST_ERROR_NONE)
                    .setDigest(ByteString.copyFrom(hash))
                    .build();
            responseObserver.onNext(response);
        } catch (final IndexOutOfBoundsException ex) {
            final Digest.DigestDataResponse response = Digest.DigestDataResponse.newBuilder()
                    .setErrorCode(Digest.DigestErrorCode.DIGEST_ERROR_UNKNOWN_MECHANISM)
                    .build();
            responseObserver.onNext(response);
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            final Digest.DigestDataResponse response = Digest.DigestDataResponse.newBuilder()
                    .setErrorCode(Digest.DigestErrorCode.DIGEST_ERROR_SYSTEM)
                    .build();
            responseObserver.onNext(response);
        } finally {
            responseObserver.onCompleted();
        }
    }

    public void digestInfo(final Digest.DigestInfoRequest request,
                           final StreamObserver<Digest.DigestInfoResponse> responseObserver) {
        try {
            final Uuid.UUID sessionId = request.getSessionId();
            final UUID uuid = new UUID(sessionId.getMsb(), sessionId.getLsb());
            final Digest.DigestInfoResponse.Builder builder = Digest.DigestInfoResponse.newBuilder();

            /* load session */
            final DigestSession session = digestSessions.get(uuid);
            if (session == null) {
                builder.setErrorCode(Digest.DigestErrorCode.DIGEST_ERROR_SESSION_NOT_FOUND);
            } else {
                builder.setErrorCode(Digest.DigestErrorCode.DIGEST_ERROR_NONE)
                        .setMechanism(session.getMechanism());
            }

            responseObserver.onNext(builder.build());
        } finally {
            responseObserver.onCompleted();
        }
    }

    @Override
    public void encryptInit(final Encrypt.EncryptInitRequest request,
                            final StreamObserver<Encrypt.EncryptInitResponse> responseObserver) {

    }

    @Override
    public void encryptUpdate(final Encrypt.EncryptUpdateRequest request,
                              final StreamObserver<Encrypt.EncryptUpdateResponse> responseObserver) {
    }

    @Override
    public void encryptFinal(final Encrypt.EncryptFinalRequest request,
                             final StreamObserver<Encrypt.EncryptFinalResponse> responseObserver) {
    }

    @Override
    public void encrypt(final Encrypt.EncryptRequest request,
                        final StreamObserver<Encrypt.EncryptResponse> responseObserver) {
    }

    @Override
    public void encryptInfo(final Encrypt.EncryptInfoRequest request,
                            final StreamObserver<Encrypt.EncryptInfoResponse> responseObserver) {

    }

    @Override
    public void decryptInit(final Decrypt.DecryptInitRequest request,
                            final StreamObserver<Decrypt.DecryptInitResponse> responseObserver) {

    }

    @Override
    public void decrypt(final Decrypt.DecryptRequest request,
                        final StreamObserver<Decrypt.DecryptResponse> responseObserver) {

    }

    @Override
    public void decryptUpdate(final Decrypt.DecryptUpdateRequest request,
                              final StreamObserver<Decrypt.DecryptUpdateResponse> responseObserver) {

    }

    @Override
    public void decryptFinal(final Decrypt.DecryptFinalRequest request,
                             final StreamObserver<Decrypt.DecryptFinalResponse> responseObserver) {

    }

    @Override
    public void decryptInfo(final Decrypt.DecryptInfoRequest request,
                            final StreamObserver<Decrypt.DecryptInfoResponse> responseObserver) {
    }
}
