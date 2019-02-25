package java.com.patrushev.my_cache_engine;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Function;

public class CacheEngineImpl<K, V> implements CacheEngine<K, V> {
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
    private final Map<K, MyElement<K, V>> elements = new LinkedHashMap<>();
    //объект таймера для поддержки работы алгоритмов вытеснения
    private final Timer timer = new Timer();

    //счетчик успешных запросов элементов из кэша (элемент найден)
    private int hit = 0;
    //счетчик неудачных запросов элементов из кэша (элемент не найден)
    private int miss = 0;

    //в конструкторе заадется размер кэша, lifetime (макс время жизни элемента), idletime (макс время, за которое не будет ни одного обращения к элементу)
    //или хранятся ли элементы бесконечно (isEternal)
    CacheEngineImpl(int maxElements, long lifeTimeMs, long idleTimeMs, boolean isEternal) {
        this.maxElements = maxElements;
        this.lifeTimeMs = lifeTimeMs > 0 ? lifeTimeMs : 0;
        this.idleTimeMs = idleTimeMs > 0 ? idleTimeMs : 0;
        this.isEternal = lifeTimeMs == 0 && idleTimeMs == 0 || isEternal;
    }

    //кладем новый элемент в кэш
    public void put(MyElement<K, V> element) {
        //проверяем заполнен ли кэш
        if (elements.size() == maxElements) {
            //если места уже нет, то получаем ключ первого элемента, хранящегося в кэше
            // (он же ялвляется первым элементом, который положили в кэш - т.е. самый старый)
            K firstKey = elements.keySet().iterator().next();
            //и удаляем элемент по этому ключу из кэша
            elements.remove(firstKey);
        }

        //затем кладем новый элемент в кэш по переданному (внутри объекта аналога Entry) ключу
        K key = element.getKey();
        elements.put(key, element);

        //проверяем как настроен наш кэш (бесконечное хранение или по времени - если по времени то еще выполняем некоторые действия))
        if (!isEternal) {
            //если по общему времени хранения
            if (lifeTimeMs != 0) {
                //создаем задачу, срабатывающую по таймеру - удалить только что добавленный элемент(???)
                //в создающий метод передаются ключ элемента и функция(???)
                TimerTask lifeTimerTask = getTimerTask(key, new Function<MyElement<K, V>, Long>() {
                    //производится действие над переданным элементом (в данном случае над нашим элементом)
                    //в данном случае результатом является Long
                    //само действие -
                    @Override
                    public Long apply(MyElement<K, V> lifeElement) {
                        //возвращает время создания элемента + время жизни элемента, т.е. возвращает время, когда элемент должен быть удален
                        return lifeElement.getCreationTime() + lifeTimeMs;
                    }
                });
                //запускаем таймер дял выполнения таймертаска через время равное lifeTimeMs
                timer.schedule(lifeTimerTask, lifeTimeMs);
                //ИТОГО - положили элемент в кэш и запустили таймер на время lifeTimeMs, по истечении которого запустится выполнение timerTask,
                //задача которого состоит в том, чтобы удалить этот элемент
            }


            if (idleTimeMs != 0) {
                TimerTask idleTimerTask = getTimerTask(key, new Function<MyElement<K, V>, Long>() {
                    @Override
                    public Long apply(MyElement<K, V> idleElement) {
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
    public MyElement<K, V> get(K key) {
        //получакем элемент из внутренней Map
        MyElement<K, V> element = elements.get(key);
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

    //создает объект задачи по таймеру (передаются ключ и функция, которую над произвести над элементом, который привязан к ключу)
    private TimerTask getTimerTask(final K key, Function<MyElement<K, V>, Long> timeFunction) {
        return new TimerTask() {
            @Override
            public void run() {
                //получаем нужный элемент по ключу
                MyElement<K, V> element = elements.get(key);
                //если элемент null или если текущее время больше чем рассчитанное время для удаления элемента
                if (element == null || isT1BeforeT2(timeFunction.apply(element), System.currentTimeMillis())) {
                    //тогда удаляем этот элемент
                    elements.remove(key);
                    //отменяет таймертаск(???)
                    this.cancel();
                }
            }
        };
    }

    //сравнивает время с учетом трешхолда(запаса???)
    private boolean isT1BeforeT2(long t1, long t2) {
        return t1 < t2 + TIME_THRESHOLD_MS;
    }
}
