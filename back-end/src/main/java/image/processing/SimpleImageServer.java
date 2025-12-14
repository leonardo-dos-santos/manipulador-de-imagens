package image.processing;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SimpleImageServer {
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/api/health", exchange -> {
            setCors(exchange);
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(204, -1);
                exchange.close();
                return;
            }
            byte[] out = "ok".getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "text/plain; charset=utf-8");
            exchange.sendResponseHeaders(200, out.length);
            exchange.getResponseBody().write(out);
            exchange.close();
        });
        server.createContext("/api/process/trim", new ProcessHandler("trim"));
        server.createContext("/api/process/negative", new ProcessHandler("negative"));
        server.createContext("/api/process/invert", new ProcessHandler("invert"));
        server.createContext("/api/process/stretch-h", new ProcessHandler("stretch-h"));
        server.createContext("/api/process/shrink-v", new ProcessHandler("shrink-v"));
        server.createContext("/api/process/color-filter", new ProcessHandler("color-filter"));
        server.createContext("/api/process/grayscale", new ProcessHandler("grayscale"));
        server.setExecutor(null);
        server.start();
        System.out.println("SimpleImageServer started on http://localhost:8080");
    }

    static class ProcessHandler implements HttpHandler {
        private final String op;
        ProcessHandler(String op) { this.op = op; }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            setCors(exchange);
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(204, -1);
                exchange.close();
                return;
            }
            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                setCors(exchange);
                exchange.getResponseHeaders().add("Allow", "POST, OPTIONS");
                exchange.sendResponseHeaders(405, -1);
                exchange.close();
                return;
            }
            Map<String, String> q = queryParams(exchange);
            int[][] input = readImage(exchange, q);
            if (input == null) {
                setCors(exchange);
                byte[] err = "invalid image".getBytes(StandardCharsets.UTF_8);
                exchange.getResponseHeaders().add("Content-Type", "text/plain; charset=utf-8");
                exchange.sendResponseHeaders(400, err.length);
                exchange.getResponseBody().write(err);
                exchange.close();
                return;
            }
            try {
                int[][] out;
                switch (op) {
                    case "trim": {
                        int pixelCount = parseInt(q.get("pixelCount")).orElse(10);
                        out = ImageProcessor.trimBorders(input, pixelCount);
                        break;
                    }
                    case "negative":
                        out = ImageProcessor.negativeColor(input);
                        break;
                    case "invert":
                        out = ImageProcessor.invertImage(input);
                        break;
                    case "stretch-h":
                        out = ImageProcessor.stretchHorizontally(input);
                        break;
                    case "shrink-v":
                        out = ImageProcessor.shrinkVertically(input);
                        break;
                    case "color-filter": {
                        int r = parseInt(q.get("red")).orElse(0);
                        int g = parseInt(q.get("green")).orElse(0);
                        int b = parseInt(q.get("blue")).orElse(0);
                        out = ImageProcessor.colorFilter(input, r, g, b);
                        break;
                    }
                    case "grayscale":
                        out = ImageProcessor.grayscale(input);
                        break;
                    default:
                        out = input;
                }
                byte[] bytes = ImageUtils.twoDToJpegBytes(out);
                exchange.getResponseHeaders().add("Content-Type", "image/jpeg");
                exchange.sendResponseHeaders(200, bytes.length);
                exchange.getResponseBody().write(bytes);
                exchange.close();
            } catch (Exception e) {
                setCors(exchange);
                byte[] err = "processing error".getBytes(StandardCharsets.UTF_8);
                exchange.getResponseHeaders().add("Content-Type", "text/plain; charset=utf-8");
                exchange.sendResponseHeaders(500, err.length);
                exchange.getResponseBody().write(err);
                exchange.close();
            }
        }

        private static Optional<Integer> parseInt(String s) {
            try {
                if (s == null) return Optional.empty();
                return Optional.of(Integer.parseInt(s));
            } catch (Exception e) {
                return Optional.empty();
            }
        }
    }

    private static int[][] readImage(HttpExchange exchange, Map<String, String> q) {
        try {
            String imageUrl = q.get("imageUrl");
            if (imageUrl != null && imageUrl.trim().length() > 0) {
                return ImageUtils.imgToTwoD(imageUrl);
            }
            try (InputStream is = exchange.getRequestBody()) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[8192];
                int n;
                while ((n = is.read(buffer)) > 0) {
                    baos.write(buffer, 0, n);
                }
                byte[] body = baos.toByteArray();
                if (body.length == 0) return null;
                BufferedImage img = ImageIO.read(new ByteArrayInputStream(body));
                return ImageUtils.imgToTwoD(img);
            }
        } catch (Exception e) {
            return null;
        }
    }

    private static Map<String, String> queryParams(HttpExchange exchange) {
        Map<String, String> map = new HashMap<>();
        String raw = exchange.getRequestURI().getRawQuery();
        if (raw == null || raw.trim().length() == 0) return map;
        for (String pair : raw.split("&")) {
            String[] kv = pair.split("=", 2);
            String k;
            String v;
            try {
                k = URLDecoder.decode(kv[0], "UTF-8");
            } catch (Exception e) {
                k = kv[0];
            }
            try {
                v = kv.length > 1 ? URLDecoder.decode(kv[1], "UTF-8") : "";
            } catch (Exception e) {
                v = kv.length > 1 ? kv[1] : "";
            }
            map.put(k, v);
        }
        return map;
    }

    private static void setCors(HttpExchange exchange) {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type, Accept");
    }
}
