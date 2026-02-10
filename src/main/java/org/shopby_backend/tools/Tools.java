package org.shopby_backend.tools;

import org.springframework.stereotype.Component;

@Component
public class Tools {
    public static long getDurationMs(long start) {
        return (System.nanoTime() - start) / 1_000_000;
    }
}
