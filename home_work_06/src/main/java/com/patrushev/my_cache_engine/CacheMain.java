package com.patrushev.my_cache_engine;

public class CacheMain {

    public static void main(String[] args) throws InterruptedException {
        eternalCacheExample();
        lifeCacheExample();
        idleCacheExample();
    }

    private static void eternalCacheExample() {
        int size = 5;
        CacheEngine<Integer, String> cache = new CacheEngineMyImpl<>(size, 0, 0, true);
        for (int i = 0; i < 10; i++) {
            cache.put(i, "String: " + i);
        }
        for (int i = 0; i < 10; i++) {
            String element = cache.get(i);
            System.out.println("String for " + i + ": " + (element != null ? element : "null"));
        }
        System.out.println("Cache hits: " + cache.getHitCount());
        System.out.println("Cache misses: " + cache.getMissCount());
        System.out.println();

        cache.dispose();
    }

    private static void lifeCacheExample() throws InterruptedException {
        int size = 5;
        CacheEngine<Integer, String> cache = new CacheEngineMyImpl<>(size, 1000, 0, false);

        for (int i = 0; i < size; i++) {
            cache.put(i, "String: " + i);
        }

        for (int i = 0; i < size; i++) {
            String element = cache.get(i);
            System.out.println("String for " + i + ": " + (element != null ? element : "null"));
        }

        System.out.println("Cache hits: " + cache.getHitCount());
        System.out.println("Cache misses: " + cache.getMissCount());

        Thread.sleep(1000);

        for (int i = 0; i < size; i++) {
            String element = cache.get(i);
            System.out.println("String for " + i + ": " + (element != null ? element : "null"));
        }

        System.out.println("Cache hits: " + cache.getHitCount());
        System.out.println("Cache misses: " + cache.getMissCount());
        System.out.println();
        cache.dispose();
    }


    private static void idleCacheExample() throws InterruptedException {
        int size = 5;
        CacheEngine<Integer, String> cache = new CacheEngineMyImpl<>(size, 0, 1000, false);

        for (int i = 0; i < size; i++) {
            cache.put(i, "String: " + i);
        }

        for (int i = 0; i < size; i++) {
            String element = cache.get(i);
            System.out.println("String for " + i + ": " + (element != null ? element : "null"));
        }

        System.out.println("Cache hits: " + cache.getHitCount());
        System.out.println("Cache misses: " + cache.getMissCount());

        Thread.sleep(800);

        System.out.println();
        System.out.println(cache.get(1));
        System.out.println(cache.get(2));
        System.out.println();

        Thread.sleep(800);

        System.out.println();
        System.out.println(cache.get(1));
        System.out.println(cache.get(2));
        System.out.println();

        Thread.sleep(800);

        for (int i = 0; i < size; i++) {
            String element = cache.get(i);
            System.out.println("String for " + i + ": " + (element != null ? element : "null"));
        }

        System.out.println("Cache hits: " + cache.getHitCount());
        System.out.println("Cache misses: " + cache.getMissCount());

        cache.dispose();
    }
}
