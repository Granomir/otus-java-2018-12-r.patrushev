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

    //внутреннее хранилище кэша, хранит элементы в порядке их внесения в кэш
    private final Map<K, MyElement> elements = new LinkedHashMap<>();
    //объект таймера для запуска задач по удалению устаревших элементов
    private final Timer timer = new Timer();

    //счетчик успешных запросов элементов из кэша (элемент найден)
    private int hit = 0;
    //счетчик неудачных запросов элементов из кэша (элемент не найден)
    private int miss = 0;

    CacheEngineMyImpl(int maxElements, long lifeTimeMs, long idleTimeMs) {
        if (maxElements <= 0) throw new IllegalArgumentException("capacity cannot be 0 or less");
        if (lifeTimeMs < 0 || idleTimeMs < 0) throw new IllegalArgumentException("time cannot be less than 0");
        this.maxElements = maxElements;
        this.lifeTimeMs = lifeTimeMs;
        this.idleTimeMs = idleTimeMs;
        //запуск таймеров для автоматического удаления устаревших элементов
        startNullsDeleterTimer();
        if (lifeTimeMs > 0) {
            startLifeTimer();
        }
        if (idleTimeMs > 0) {
            startIdleTimer();
        }
    }

    private void startNullsDeleterTimer() {
        //создаем задачу по периодическому удалению нулевых софтреференсов
        TimerTask nullsDeleter = new TimerTask() {
            @Override
            public void run() {
                elements.entrySet().removeIf(pair -> pair.getValue().getValue() == null);
            }
        };
        //запускаем таймер на эту задачу с периодичностью 10 сек
        timer.schedule(nullsDeleter, 10000, 10000);
    }

    private void startLifeTimer() {
        //создаем задачу по удалению добавленного элемента, по истечении времени lifeTimeMs после создания
        //момент времени, после которого элемент должен быть удален, определяется передаваемой Function
        TimerTask lifeTimerTask = getTimerTask(lifeElement -> lifeElement.getCreationTime() + lifeTimeMs);
        //планируем запуск созданной задачи через время lifeTimeMs
        timer.schedule(lifeTimerTask, lifeTimeMs, lifeTimeMs);
    }

    private void startIdleTimer() {
        //создаем задачу по удалению добавленного элемента, по истечении времени idleTimeMs после последнего обращения к элементу
        //момент времени, после которого элемент должен быть удален, определяется передаваемой Function
        TimerTask idleTimerTask = getTimerTask(idleElement -> idleElement.getLastAccessTime() + idleTimeMs);
        //планируем периодический запуск созданной задачи через время idleTimeMs
        timer.schedule(idleTimerTask, idleTimeMs, idleTimeMs);
    }

    /**
     * внесение нового элемента в кэш
     */
    public void put(K key, V value) {
        //если в качестве значения передается null - выбрасывается исключение
        if (value == null) throw new IllegalArgumentException("it's forbidden to put null into the cache");
        //оборачиваем новый элемент
        MyElement element = new MyElement(value);

        //проверка наличия свободного места в кэше
        if (elements.size() == maxElements) {
            //удаляется самый старый элемент кэша
            K firstKey = elements.keySet().iterator().next();
            elements.remove(firstKey);
        }

        //новый элемент вносится в кэш
        elements.put(key, element);
    }

    /**
     * получение элемента из кэша
     */
    public V get(K key) {
        //получаем обёртку элемента из внутренней Map
        MyElement element = elements.get(key);
        if (element != null) {
            //проверяем, что сборщик мусора не удалил обёрнутый элемент
            if (element.getValue() == null) {
                miss++;
                elements.remove(key);
                return null;
            }
            //увеличиваем счетчик успешных запросов элементов из кэша
            hit++;
            //обновляем время последнего доступа к элементу
            element.setAccessed();
            return element.getValue();
        } else {
            //увеличиваем счетчик неудачных запросов элементов из кэша
            miss++;
            return null;
        }
    }

    /**
     * возвращает кол-во удачных запросов
     */
    public int getHitCount() {
        return hit;
    }

    /**
     * возвращает кол-во неудачных запросов
     */
    public int getMissCount() {
        return miss;
    }

    /**
     * отмена всех запланированных задач
     */
    @Override
    public void dispose() {
        timer.cancel();
    }

    /**
     * создание задачи, которая потом передается таймеру
     */
    private TimerTask getTimerTask(Function<MyElement, Long> timeFunction) {
        return new TimerTask() {
            @Override
            public void run() {
                elements.entrySet().removeIf(pair -> isT1BeforeT2(timeFunction.apply(pair.getValue()), System.currentTimeMillis()));
            }
        };
    }

    /**
     * сравнивает переданные моменты времени с учетом запаса
     */
    private boolean isT1BeforeT2(long t1, long t2) {
        return t1 < t2 + TIME_THRESHOLD_MS;
    }

    /**
     * Оболочка для элемента кэша
     */
    public class MyElement {
        private final SoftReference<V> value;
        private final long creationTime;
        private long lastAccessTime;

        MyElement(V value) {
            this.value = new SoftReference<>(value);
            this.creationTime = System.currentTimeMillis();
            this.lastAccessTime = System.currentTimeMillis();
        }

        V getValue() {
            return value.get();
        }

        long getCreationTime() {
            return creationTime;
        }

        long getLastAccessTime() {
            return lastAccessTime;
        }

        void setAccessed() {
            lastAccessTime = System.currentTimeMillis();
        }
    }
}
