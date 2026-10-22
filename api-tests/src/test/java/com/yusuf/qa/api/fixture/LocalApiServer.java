package com.yusuf.qa.api.fixture;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public final class LocalApiServer implements AutoCloseable {
    private final HttpServer server;

    public LocalApiServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/products", exchange -> {
            try (exchange) {
                int status;
                String body;
                String path = exchange.getRequestURI().getPath();
                if (!path.equals("/products") && !path.equals("/products/1")) {
                    status = 404;
                    body = "{\"error\":\"NOT_FOUND\"}";
                } else if (!exchange.getRequestMethod().equals("GET")) {
                    status = 405;
                    exchange.getResponseHeaders().set("Allow", "GET");
                    body = "{\"error\":\"METHOD_NOT_ALLOWED\"}";
                } else if (path.equals("/products/1")) {
                    status = 200;
                    body = """
                            {"id":1,"name":"Kablosuz Mouse","price":499.90}
                            """;
                } else {
                    status = 200;
                    body = """
                            {"products":[{"id":1,"name":"Kablosuz Mouse","price":499.90}],"total":1}
                            """;
                }
                byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
                exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
                exchange.sendResponseHeaders(status, bytes.length);
                exchange.getResponseBody().write(bytes);
            }
        });
        server.start();
    }

    public int port() {
        return server.getAddress().getPort();
    }

    @Override
    public void close() {
        server.stop(0);
    }
}
