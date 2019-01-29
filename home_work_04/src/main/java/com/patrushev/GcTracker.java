package com.patrushev;

import com.sun.management.GarbageCollectionNotificationInfo;

import javax.management.Notification;
import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;
import java.lang.management.GarbageCollectorMXBean;
import java.util.ArrayList;
import java.util.List;


public class GcTracker {
    private static long youngGCcount;
    private static long youngGCDurationCount;
    private static long tempYoungGCCount;
    private static long oldGCcount;
    private static long oldGCDurationCount;
    private static long tempOldGCCount;
    private static int minuteNumber = 1;
    private static List<GarbageCollectorMXBean> gcbeans = java.lang.management.ManagementFactory.getGarbageCollectorMXBeans();

    static {
        for (GarbageCollectorMXBean gcbean : gcbeans) {
            System.out.println("GC name:" + gcbean.getName());

            //делаем бин сборщика мусора источником оповещений о событиях
            NotificationEmitter emitter = (NotificationEmitter) gcbean;
            //создаем слушателя оповещений о событиях
            NotificationListener listener = new NotificationListener() {

                //определяем метод, в котором будет происходить обработка полученных оповещений о событиях
                @Override
                public void handleNotification(Notification notification, Object handback) {
                    //проверяем, что оповещение является оповещением о сборке мусора
                    if (notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
                        //получаем всю информацию о сборке мусора из объекта оповещения
                        GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());
                        //получаем длительность сборки мусора
                        if (info.getGcName().contains("Young")) {
                            System.out.println("young сборка №" + info.getGcInfo().getId());
//                            tempYoungGCCount = info.getGcInfo().getId();
                            tempYoungGCCount++;
                            youngGCcount++;
                            youngGCDurationCount += info.getGcInfo().getDuration();
                        }
                        if (info.getGcName().contains("Old")) {
                            System.out.println("old сборка №" + info.getGcInfo().getId());
//                            tempOldGCCount = info.getGcInfo().getId();
                            tempOldGCCount++;
                            oldGCcount++;
                            oldGCDurationCount += info.getGcInfo().getDuration();
                        }
                    }
                }
            };

            //добавляем новосозданный слушатель в список источника
            emitter.addNotificationListener(listener, null, null);
        }
    }

    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] args) {
        long startTime1 = System.nanoTime();
        long startTime = System.nanoTime();
        try {
            List<Object> garbage = new ArrayList<>();
            int count = 10_000;
            while (true) {
                if ((System.nanoTime() - startTime) / 1000_000_000 >= 60) {
                    System.err.println("Приложение проработало " + (System.nanoTime() - startTime1) / 1000_000_000 + " секунд");
                    System.err.println("Статистика минуты №" + minuteNumber + ":");
//                    long temp1 = youngGCcount - tempYoungGCCount;
                    System.err.println("Количество сборок young - " + tempYoungGCCount);
//                    tempYoungGCCount += youngGCcount;
                    tempYoungGCCount = 0;
//                    tempYoungGCCount = temp1;
                    System.err.println("Затраченное время на сборки young - " + youngGCDurationCount + " миллисекунд");
//                    long temp2 = oldGCcount - tempOldGCCount;
                    System.err.println("Количество сборок old - " + tempOldGCCount);
//                    tempOldGCCount += oldGCcount;
                    tempOldGCCount = 0;
                    System.err.println("Затраченное время на сборки old - " + oldGCDurationCount + " миллисекунд\n");
                    youngGCDurationCount = oldGCDurationCount = 0;
                    minuteNumber++;
                    startTime = System.nanoTime();
                }
                for (int i = 0; i < count; i++) {
                    garbage.add(new Object());
                }

                for (int i = 0; i < count / 2; i++) {
                    garbage.remove(garbage.size() - 1);
                }
                Thread.sleep(1);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println("Приложение проработало " + (System.nanoTime() - startTime1) / 1000_000_000 + " секунд");
            System.out.println("Количество сборок young - " + youngGCcount);
            System.out.println("Количество сборок old - " + oldGCcount);
        }
    }
}
