package com.example.cache.controller;

import com.example.cache.service.CacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cache")
@RequiredArgsConstructor
public class CacheController {

    private final CacheService cacheService;

    @GetMapping("/{key}")
    public ResponseEntity<String> get(@PathVariable String key) {
        return ResponseEntity.ok(cacheService.get(key));
    }

    @PostMapping("/{key}")
    public ResponseEntity<Void> set(@PathVariable String key, @RequestBody String value) {
        cacheService.set(key, value);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{key}")
    public ResponseEntity<Void> delete(@PathVariable String key) {
        cacheService.delete(key);
        return ResponseEntity.ok().build();
    }
}
