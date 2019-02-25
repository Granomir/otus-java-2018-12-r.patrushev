package ru.otus.l061.cache;

/**
 * Created by tully.
 */
public class CacheMain {

    public static void main(String[] args) throws InterruptedException {
//        new CacheMain().eternalCacheExample();
        new CacheMain().lifeCacheExample();
    }

    private void eternalCacheExample() {
        //определяем размер кэша
        int size = 5;
        //создаем объект кэша с размером 5 и неограниченным по времени хранением элементов
        CacheEngine<Integer, String> cache = new CacheEngineImpl<>(size, 0, 0, true);
        //кладем в кэш 10 элементов - элемент состоит из ключа и значения (значение и есть непосредственно то, что кладем в кэш)
        //т.е. тут кладется не значение по ключу, как в Map, а сами создаем аналог Entry из Map и уже его кладем в кэш
        for (int i = 0; i < 10; i++) {
            cache.put(new MyElement<>(i, "String: " + i));
        }
        //получаем элементы из кэша и выводим на экран
        for (int i = 0; i < 10; i++) {
            //получаем элемент, если он есть в кеше, если нет то получаем null
            MyElement<Integer, String> element = cache.get(i);
            //выводим элемент на экран, лиюо если элемента в кеше не было, то выводим null
            System.out.println("String for " + i + ": " + (element != null ? element.getValue() : "null"));
        }
        //выводим кол-во полученных из кэша элементов
        System.out.println("Cache hits: " + cache.getHitCount());
        //выводим кол-во неполученных из кэша элементов
        System.out.println("Cache misses: " + cache.getMissCount());

        cache.dispose();
    }

    private void lifeCacheExample() throws InterruptedException {
        int size = 5;
        CacheEngine<Integer, String> cache = new CacheEngineImpl<>(size, 1000, 0, false);

        for (int i = 0; i < size; i++) {
            cache.put(new MyElement<>(i, "String: " + i));
        }

        for (int i = 0; i < size; i++) {
            MyElement<Integer, String> element = cache.get(i);
            System.out.println("String for " + i + ": " + (element != null ? element.getValue() : "null"));
        }

        System.out.println("Cache hits: " + cache.getHitCount());
        System.out.println("Cache misses: " + cache.getMissCount());

        Thread.sleep(1000);

        for (int i = 0; i < size; i++) {
            MyElement<Integer, String> element = cache.get(i);
            System.out.println("String for " + i + ": " + (element != null ? element.getValue() : "null"));
        }

        System.out.println("Cache hits: " + cache.getHitCount());
        System.out.println("Cache misses: " + cache.getMissCount());

        cache.dispose();
    }

}
