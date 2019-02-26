package com.patrushev.my_cache_engine;

import java.lang.ref.SoftReference;
import java.util.*;
import java.util.function.Function;

public class CacheEngineMyImpl<K, V> implements CacheEngine<K, V> {
    //запас времени для подстраховки алгоритмов удаления устаревших элементов, привязанных ко времени
    private static final int TIME_THRESHOLD_MS = 5;

    //размер кэша
    private final int maxElements;
    //максимальное время, прошедшее с момента внесения элемента в кэш
    private final long lifeTimeMs;
    //максимальное время, прошедшее с момента последнего обращения к элементу извне
    private final long idleTimeMs;
    //определяет, удалять ли устаревшие элементы с течением времени работы кэша
    private final boolean isEternal;

    //внутреннее хранилище кэша, хранит элементы в порядке их внесения в кэш
    private final Map<K, MyElement<K, SoftReference<V>>> elements = new LinkedHashMap<>();
    //объект таймера для поддержки работы алгоритмов удаления
    private final Timer timer = new Timer();

    //счетчик успешных запросов элементов из кэша (элемент найден)
    private int hit = 0;
    //счетчик неудачных запросов элементов из кэша (элемент не найден)
    private int miss = 0;

    CacheEngineMyImpl(int maxElements, long lifeTimeMs, long idleTimeMs, boolean isEternal) {
        this.maxElements = maxElements;
        this.lifeTimeMs = lifeTimeMs > 0 ? lifeTimeMs : 0;
        this.idleTimeMs = idleTimeMs > 0 ? idleTimeMs : 0;
        this.isEternal = lifeTimeMs == 0 && idleTimeMs == 0 || isEternal;
    }

    //внесение нового элемента в кэш
    public void put(K key, V value) {
        //если в качестве значения передается null - выбрасывается исключение
        if (value == null) throw new IllegalArgumentException("it's forbidden to put null into the cache");
        //оборачиваем новый элемент
        MyElement<K, SoftReference<V>> element = new MyElement<>(key, new SoftReference<>(value));

        //чистка кэша от удаленных сборщиком мусора значений
        Iterator it = elements.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            if (pair.getValue() == null) {
                it.remove();
            }
        }

        //проверка наличия свободного места в кэше
        if (elements.size() == maxElements) {
            //удаляется самый старый элемент кэша
            K firstKey = elements.keySet().iterator().next();
            elements.remove(firstKey);
        }

        //новый элемент вносится в кэш
        elements.put(key, element);

        //если кэш настроен на автоматическое удаление устаревших элементов
        if (!isEternal) {
            //если удаление производится по общему времени хранения элемента
            if (lifeTimeMs != 0) {
                //создаем задачу по удалению добавленного элемента, по истечении времени lifeTimeMs после создания
                //момент времени, после которого элемент должен быть удален, определяется передаваемой Function
                TimerTask lifeTimerTask = getTimerTask(key, lifeElement -> lifeElement.getCreationTime() + lifeTimeMs);
                //планируем запуск созданной задачи через время lifeTimeMs
                timer.schedule(lifeTimerTask, lifeTimeMs);
            }

            //если удаление производится по последнему времени обращения к элементу
            if (idleTimeMs != 0) {
                //создаем задачу по удалению добавленного элемента, по истечении времени idleTimeMs после последнего обращения к элементу
                //момент времени, после которого элемент должен быть удален, определяется передаваемой Function
                TimerTask idleTimerTask = getTimerTask(key, idleElement -> idleElement.getLastAccessTime() + idleTimeMs);
                //планируем периодический запуск созданной задачи через время idleTimeMs
                timer.schedule(idleTimerTask, idleTimeMs, idleTimeMs);
            }
        }
    }

    //получаем элемент из кэша
    public V get(K key) {
        //получаем обёртку элемента из внутренней Map
        MyElement<K, SoftReference<V>> element = elements.get(key);
        if (element != null) {
            //проверяем, что сборщик мусора не удалил обёрнутый элемент
            if (element.getValue().get() == null) {
                miss++;
                elements.remove(key);
                return null;
            }
            //увеличиваем счетчик успешных запросов элементов из кэша
            hit++;
            //обновляем время последнего доступа к элементу
            element.setAccessed();
            return element.getValue().get();
        } else {
            //увеличиваем счетчик неудачных запросов элементов из кэша
            miss++;
            return null;
        }
    }

    //возвращает кол-во удачных запросов
    public int getHitCount() {
        return hit;
    }

    //возвращает кол-во неудачных запросов
    public int getMissCount() {
        return miss;
    }

    //отмена всех запланированных задач
    @Override
    public void dispose() {
        timer.cancel();
    }

    //создание задачи, которая потом передается таймеру
    private TimerTask getTimerTask(final K key, Function<MyElement<K, SoftReference<V>>, Long> timeFunction) {
        return new TimerTask() {
            @Override
            public void run() {
                //получаем нужный элемент по ключу
                MyElement<K, SoftReference<V>> element = elements.get(key);
                //если элемент null или если текущее время больше чем расчетное время удаления элемента
                if (element == null || isT1BeforeT2(timeFunction.apply(element), System.currentTimeMillis())) {
                    //тогда удаляем этот элемент
                    elements.remove(key);
                    //и отменяем таймертаск
                    this.cancel();
                }
            }
        };
    }

    //сравнивает переданные моменты времени с учетом запаса
    private boolean isT1BeforeT2(long t1, long t2) {
        return t1 < t2 + TIME_THRESHOLD_MS;
    }
}
