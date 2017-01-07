package com.github.edipermadi.bohpien.service;

import com.github.edipermadi.bohpien.service.grpc.BohpienServer;
import io.grpc.ServerBuilder;

import java.io.IOException;

/**
 * Bohpien Service Class
 *
 * @author Edi Permadi
 */
public final class BohpienService {
    private final int port;
    private Thread thread;

    private BohpienService(final int port) {
        this.port = port;
    }

    public void start() {
        thread = new Thread(() -> {
            try {
                ServerBuilder.forPort(port)
                        .addService(new BohpienServer())
                        .build().start().awaitTermination();
            } catch (final InterruptedException | IOException ex) {

            }
        });

        thread.start();
    }

    public void stop() {
        thread.interrupt();
        try {
            thread.join();
        } catch (final InterruptedException ex) {
        }
    }

    public static BohpienService create(final int port) {
        return new BohpienService(port);
    }
}
