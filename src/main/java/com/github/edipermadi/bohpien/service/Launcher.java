package com.github.edipermadi.bohpien.service;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;

/**
 * Bohpien Service Launcher
 *
 * @author Edi Permadi
 */
public final class Launcher {
    public static void main(final String[] args) {
        /* register provider */
        Security.addProvider(new BouncyCastleProvider());

        /* start service */
        BohpienService.create(8080).start();
    }
}
