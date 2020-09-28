package com.patrushev;

import com.sun.management.GarbageCollectionNotificationInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;
import java.lang.management.GarbageCollectorMXBean;
import java.util.ArrayList;
import java.util.List;

/*
 -XX:+UseG1GC
 -XX:+UseConcMarkSweepGC
 -XX:+UseSerialGC
 -XX:+UseParallelGC
*/

public class GcTracker {
    private static final Logger logger = LoggerFactory.getLogger("logger");

    private static final List<GCjobReport> reports = new ArrayList<>();

    private static long youngGCcount;
    private static long youngGCDurationCount;
    private static long tempYoungGCCount;
    private static long oldGCcount;
    private static long oldGCDurationCount;
    private static long tempOldGCCount;
    private static long totalGCDuration;
    private static int minuteNumber = 1;

    //настройка отслеживания работы GC
    private static void initiateMonitoring() {
        //получаем список сборщиков мусора и выводим их названия в консоль
        List<GarbageCollectorMXBean> gcBeans = java.lang.management.ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean gcBean : gcBeans) {
            System.out.println("GC name:" + gcBean.getName());

            //делаем бин сборщика мусора источником оповещений о событиях
            NotificationEmitter emitter = (NotificationEmitter) gcBean;
            //создаем слушателя оповещений о событиях
            //определяем метод, в котором будет происходить обработка полученных оповещений о событиях
            NotificationListener listener = (notification, handBack) -> {
                //проверяем, что оповещение является оповещением о сборке мусора
                if (notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
                    //получаем всю информацию о сборке мусора из объекта оповещения
                    GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());

                    if (info.getGcName().contains("Young") || info.getGcName().contains("ParNew") || info.getGcName().contains("Copy") || info.getGcName().equals("PS Scavenge")) {
                        logger.info("young сборка №" + info.getGcInfo().getId());
                        tempYoungGCCount++;
                        youngGCcount++;
                        youngGCDurationCount += info.getGcInfo().getDuration();
                        totalGCDuration += info.getGcInfo().getDuration();
                    }
                    if (info.getGcName().contains("Old") || info.getGcName().contains("ConcurrentMarkSweep") || info.getGcName().contains("MarkSweepCompact") || info.getGcName().equals("PS MarkSweep")) {
                        logger.info("old сборка №" + info.getGcInfo().getId());
                        tempOldGCCount++;
                        oldGCcount++;
                        oldGCDurationCount += info.getGcInfo().getDuration();
                        totalGCDuration += info.getGcInfo().getDuration();
                    }
                }
            };

            //добавляем новосозданный слушатель в список источника
            emitter.addNotificationListener(listener, null, null);
        }
    }

    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] args) {
        initiateMonitoring();
        long applicationStartTime = System.nanoTime();
        long intermediateTimestamp = System.nanoTime();
        try {
            List<Object> garbage = new ArrayList<>();
            int count = 10_000;
            while (true) {
                if ((System.nanoTime() - intermediateTimestamp) / 1000_000_000 >= 60) {
                    reports.add(new GCjobReport(minuteNumber, tempYoungGCCount, youngGCDurationCount, tempOldGCCount, oldGCDurationCount));
                    System.out.println("Приложение проработало " + (System.nanoTime() - applicationStartTime) / 1000_000_000 + " секунд");
                    tempYoungGCCount = tempOldGCCount = youngGCDurationCount = oldGCDurationCount = 0;
                    minuteNumber++;
                    intermediateTimestamp = System.nanoTime();
                }
                for (int i = 0; i < count; i++) {
                    garbage.add(new Object());
                }

                for (int i = 0; i < count / 2; i++) {
                    garbage.remove(garbage.size() - 1);
                }
                Thread.sleep(15);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {

            for (GCjobReport report : reports) {
                System.out.println("Статистика минуты №" + report.getMinuteNumber() + ":");
                System.out.println("Количество сборок young - " + report.getYoungGCcount());
                System.out.println("Затраченное время на сборки young - " + report.getYoungGCDurationCount() + " миллисекунд");
                System.out.println("Количество сборок old - " + report.getOldGCcount());
                System.out.println("Затраченное время на сборки old - " + report.getOldGCDurationCount() + " миллисекунд\n");
            }

            System.out.println("Статистика последней минуты работы приложения:");
            System.out.println("Количество сборок young - " + tempYoungGCCount);
            System.out.println("Затраченное время на сборки young - " + youngGCDurationCount + " миллисекунд");
            System.out.println("Количество сборок old - " + tempOldGCCount);
            System.out.println("Затраченное время на сборки old - " + oldGCDurationCount + " миллисекунд\n");

            System.out.println("Статистика полного времени работы приложения:");
            System.out.println("Приложение проработало " + (System.nanoTime() - applicationStartTime) / 1000_000_000 + " секунд");
            System.out.println("Количество сборок young - " + youngGCcount);
            System.out.println("Количество сборок old - " + oldGCcount);
            System.out.println("Время, затраченное на сборку мусора - " + totalGCDuration + " миллисекунд");
        }
    }
}
