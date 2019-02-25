package java.com.patrushev.my_cache_engine;

import java.util.LinkedHashMap;

public class test {
    public static void main(String[] args) {
        LinkedHashMap<Integer, String> map = new LinkedHashMap<>();
        map.put(1, "1");
        map.put(2, "2");
        map.put(3, "3");
        map.put(4, "4");
        map.put(5, "5");
        map.put(6, "6");
        map.put(7, "7");
        map.put(8, "8");
        for (int i = 1; i <= map.size(); i++) {
            System.out.println(map.get(i));
        }
    }
}
