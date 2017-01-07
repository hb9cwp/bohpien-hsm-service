package com.github.edipermadi.bohpien.test;

import com.github.edipermadi.bohpien.api.BohpienServiceGrpc;
import com.github.edipermadi.bohpien.api.Digest;
import com.github.edipermadi.bohpien.api.Uuid;
import com.github.edipermadi.bohpien.service.BohpienService;
import com.google.protobuf.ByteString;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import org.apache.commons.codec.binary.Hex;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Digest API test cases
 *
 * @author Edi Permadi
 */
public class DigestTest {
    private BohpienService service;
    private BohpienServiceGrpc.BohpienServiceBlockingStub client;
    private Uuid.UUID session;

    @BeforeClass
    public void beforeClass() {
        final int port = 8080;
        service = BohpienService.create(port);
        service.start();

        final Channel channel = ManagedChannelBuilder.forAddress("127.0.0.1", port)
                .usePlaintext(true).build();
        client = BohpienServiceGrpc.newBlockingStub(channel);
    }

    @AfterClass
    public void afterClass() {
        service.stop();
    }

    @Test
    public void testDigestInit() {
        final Digest.DigestInitRequest request = Digest.DigestInitRequest.newBuilder().
                setMechanism(Digest.DigestMechanism.SHA1)
                .build();
        final Digest.DigestInitResponse response = client.digestInit(request);
        final Digest.DigestErrorCode errorCode = response.getErrorCode();
        Assert.assertEquals(errorCode, Digest.DigestErrorCode.DIGEST_ERROR_NONE);
        session = response.getSessionId();
    }

    @Test(dependsOnMethods = {"testDigestInit"})
    public void testDigestUpdate() {
        final Digest.DigestUpdateRequest request = Digest.DigestUpdateRequest.newBuilder()
                .setSessionId(session)
                .setData(ByteString.copyFromUtf8("hello world")).build();

        final Digest.DigestUpdateResponse response = client.digestUpdate(request);
        final Digest.DigestErrorCode errorCode = response.getErrorCode();
        Assert.assertEquals(errorCode, Digest.DigestErrorCode.DIGEST_ERROR_NONE);
    }

    @Test(dependsOnMethods = {"testDigestUpdate"})
    public void testDigestFinal() {
        //

        final Digest.DigestFinalRequest request = Digest.DigestFinalRequest.newBuilder()
                .setSessionId(session)
                .build();

        final Digest.DigestFinalResponse response = client.digestFinal(request);
        final Digest.DigestErrorCode errorCode = response.getErrorCode();
        Assert.assertEquals(errorCode, Digest.DigestErrorCode.DIGEST_ERROR_NONE);

        final String actual = Hex.encodeHexString(response.getDigest().toByteArray());
        final String reference = "2aae6c35c94fcfb415dbe95f408b9ce91ee846ed";
        Assert.assertEquals(actual, reference);

        Reporter.log("hash : " + actual, true);
    }
}
