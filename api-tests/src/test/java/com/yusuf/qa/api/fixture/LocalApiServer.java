package com.yusuf.qa.api.fixture;

import com.sun.net.httpserver.HttpServer;
import groovy.json.JsonOutput;
import groovy.json.JsonSlurper;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public final class LocalApiServer implements AutoCloseable {
    private final HttpServer server;
    private final Map<Integer, Map<String, Object>> products = new LinkedHashMap<>();
    private int nextId = 2;

    public LocalApiServer() throws IOException {
        products.put(1, Map.of("id", 1, "name", "Kablosuz Mouse", "price", new BigDecimal("499.90")));
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/products", exchange -> {
            try (exchange) {
                int status;
                Object body;
                String path = exchange.getRequestURI().getPath();
                String method = exchange.getRequestMethod();
                boolean collection = path.equals("/products");
                Integer id = null;
                if (path.matches("/products/[0-9]+")) {
                    try {
                        id = Integer.valueOf(path.substring("/products/".length()));
                    } catch (NumberFormatException ignored) {
                        id = null;
                    }
                }
                if (!collection && (id == null || !products.containsKey(id))) {
                    status = 404;
                    body = Map.of("error", "NOT_FOUND");
                } else if (collection && method.equals("POST")) {
                    try {
                        Object parsed = new JsonSlurper().parseText(new String(
                                exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8));
                        if (!(parsed instanceof Map<?, ?> input)
                                || !(input.get("name") instanceof String name) || name.isBlank()
                                || !(input.get("price") instanceof Number price)
                                || new BigDecimal(price.toString()).signum() <= 0) {
                            throw new IllegalArgumentException("Invalid product");
                        }
                        int createdId = nextId++;
                        Map<String, Object> product = Map.of("id", createdId, "name", name,
                                "price", new BigDecimal(price.toString()));
                        products.put(createdId, product);
                        exchange.getResponseHeaders().set("Location", "/products/" + createdId);
                        status = 201;
                        body = product;
                    } catch (IllegalArgumentException | groovy.json.JsonException invalidBody) {
                        status = 400;
                        body = Map.of("error", "INVALID_PRODUCT");
                    }
                } else if (!method.equals("GET")) {
                    status = 405;
                    exchange.getResponseHeaders().set("Allow", collection ? "GET, POST" : "GET");
                    body = Map.of("error", "METHOD_NOT_ALLOWED");
                } else {
                    status = 200;
                    body = collection ? Map.of("products", products.values(), "total", products.size())
                            : products.get(id);
                }
                byte[] bytes = JsonOutput.toJson(body).getBytes(StandardCharsets.UTF_8);
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
