package com.patrushev.my_orm;

public class Main2 {
    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder();
        sb.append("12345");
        sb.delete(sb.length() - 2, sb.length());
        System.out.println(sb);
    }
}
