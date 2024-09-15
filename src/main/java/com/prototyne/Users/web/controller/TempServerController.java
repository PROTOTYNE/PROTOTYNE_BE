package com.prototyne.Users.web.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@RestController
public class TempServerController {

    @Value("${server.env:default_env}")
    private String env;
    @Value("${server.port:default_port}")
    private String serverPort;
    @Value("${server.serverAddress:default_address}")
    private String serverAddress;
    @Value("${serverName:default_name}")
    private String serverName;

    @GetMapping("/server-chk")
    public ResponseEntity<?> serverCheck(){
        Map<String, String> responseData = new TreeMap<>();
        responseData.put("serverName", serverName);
        responseData.put("serverAddress", serverAddress);
        responseData.put("serverPort", serverPort);
        responseData.put("env", env);
        responseData.put("name", "ci/cd test code");

        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/env")
    public ResponseEntity<?> getEnv(){
        Map<String, String> responseData = new HashMap<>();
        responseData.put("env", env);
        return ResponseEntity.ok(env);
    }
}
