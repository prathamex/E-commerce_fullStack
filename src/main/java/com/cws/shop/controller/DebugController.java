package com.cws.shop.controller;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/debug")
public class DebugController {

    @GetMapping
    public ResponseEntity<?> echo(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        map.put("method", request.getMethod());
        map.put("requestURI", request.getRequestURI());

        Map<String, String> headers = new HashMap<>();
        Enumeration<String> names = request.getHeaderNames();
        if (names != null) {
            while (names.hasMoreElements()) {
                String name = names.nextElement();
                headers.put(name, request.getHeader(name));
            }
        }

        map.put("headers", headers);

        return ResponseEntity.ok(map);
    }
}
