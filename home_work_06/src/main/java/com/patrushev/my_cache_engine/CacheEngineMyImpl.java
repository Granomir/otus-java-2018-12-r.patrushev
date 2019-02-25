package java.com.patrushev.my_cache_engine;

import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Function;

public class CacheEngineMyImpl<K, V> implements CacheEngine<K, V> {
    //запас времени для подстраховки алгоритмов вытеснения, привязанных ко времени
    private static final int TIME_THRESHOLD_MS = 5;

    //размер кэша
    private final int maxElements;
    //время, прошедшее с момента внесения элемента кэш
    private final long lifeTimeMs;
    //время, прошедшее с момента последнего обращения к элементу извне
    private final long idleTimeMs;
    //переключатель - либо элементы заносятся в кэш пока не будет OOM (true) - если lifeTimeMs и idleTimeMs равны 0,
    //либо будет применяться какой-то алгоритм вытеснения (lifeTime или idleTime)
    private final boolean isEternal;

    //внутреннее хранилище кэша, хранит элементы в порядке их внесения в кэш
    private final Map<K, MyElement<K, SoftReference<V>>> elements = new LinkedHashMap<>();
    //объект таймера для поддержки работы алгоритмов вытеснения
    private final Timer timer = new Timer();

    //счетчик успешных запросов элементов из кэша (элемент найден)
    private int hit = 0;
    //счетчик неудачных запросов элементов из кэша (элемент не найден)
    private int miss = 0;

    //в конструкторе заадется размер кэша, lifetime (макс время жизни элемента), idletime (макс время, за которое не будет ни одного обращения к элементу)
    //или хранятся ли элементы бесконечно (isEternal)
    CacheEngineMyImpl(int maxElements, long lifeTimeMs, long idleTimeMs, boolean isEternal) {
        this.maxElements = maxElements;
        this.lifeTimeMs = lifeTimeMs > 0 ? lifeTimeMs : 0;
        this.idleTimeMs = idleTimeMs > 0 ? idleTimeMs : 0;
        this.isEternal = lifeTimeMs == 0 && idleTimeMs == 0 || isEternal;
    }

    //внесение нового элемента в кэш
    public void put(K key, V value) {
        //оборачиваем новый элемент
        MyElement<K, SoftReference<V>> element = new MyElement<>(key, new SoftReference<>(value));

        //чистка кэша от удаленных сборщиком мусора значений
        /////////////////////////////////////////////////////

        //проверка наличия свободного места в кэше
        if (elements.size() == maxElements) {
            //если свободного места нет, то получаем ключ первого элемента, хранящегося в кэше
            //(он же является первым элементом, который положили в кэш - т.е. самый старый)
            K firstKey = elements.keySet().iterator().next();
            //и удаляем элемент по этому ключу из кэша
            elements.remove(firstKey);
        }

        //затем кладем новый элемент в кэш по переданному ключу
        elements.put(key, element);

        //если кэш настроен на работу по алгоритму вытеснения, то выполняем действия по настройке вытеснения
        if (!isEternal) {
            //если используется вытеснение по общему времени хранения элементов
            if (lifeTimeMs != 0) {
                //создаем задачу по удалению добавленного элемента, по истечении времени lifeTimeMs
                //момент времени, после которого элемент должен быть удален, определяется передаваемой Function
                TimerTask lifeTimerTask = getTimerTask(key, new Function<>() {
                    @Override
                    public Long apply(MyElement<K, SoftReference<V>> lifeElement) {
                        return lifeElement.getCreationTime() + lifeTimeMs;
                    }
                });
                //планируем запуск созданной задачи через время lifeTimeMs
                timer.schedule(lifeTimerTask, lifeTimeMs);
            }

            //если используется вытеснение по последнему времени обращения к элементу
            if (idleTimeMs != 0) {
                TimerTask idleTimerTask = getTimerTask(key, new Function<MyElement<K, SoftReference<V>>, Long>() {
                    @Override
                    public Long apply(MyElement<K, SoftReference<V>> idleElement) {
                        return idleElement.getLastAccessTime() + idleTimeMs;
                    }
                });
                timer.schedule(idleTimerTask, idleTimeMs, idleTimeMs);
                //ИТОГО - положили элемент в кэш и запустили таймер на время idleTimeMs, по истечении которого запустится выполнение timerTask
                //с периодичностью, равной idleTimeMs,
                //задача которого состоит в том, чтобы удалить этот элемент
            }
        }
    }

    //получаем элемент из кэша
    public MyElement<K, SoftReference<V>> get(K key) {
        //получакем элемент из внутренней Map
        MyElement<K, SoftReference<V>> element = elements.get(key);
        //если элемент не null
        if (element != null) {
            //увеличиваем счетчик успешных вытаскиваний элементов из кэша
            hit++;
            //обновляем время последнего доступа к элементу
            element.setAccessed();
        } else {
            //увеличиваем счетчик неудачных вытаскиваний элементов из кэша
            miss++;
        }
        //возвращаем элемент, даже если он равен null
        return element;
    }

    //возвращает кол-во удачных вытаскиваний
    public int getHitCount() {
        return hit;
    }

    //возвращает кол-во неудачных вытаскиваний
    public int getMissCount() {
        return miss;
    }

    //останавливает таймер, отменяя все запланированные задачи
    //в данном случае - задачи на удаление самых ненужных элементов
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
