package com.intellifusion.test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class leetcode {

    public static void main(String[] args) {
        long startOne = System.currentTimeMillis();
        System.out.println(fib(45));
        System.out.println("暴力递归：" + (System.currentTimeMillis() - startOne)+"ms");

        long startTwo = System.currentTimeMillis();
        System.out.println(fibNew(45,new HashMap<Integer, Integer>()));
        System.out.println("算法优化后：" + (System.currentTimeMillis() - startTwo)+"ms");
    }

    static int fib(int N) {
        if (N == 1 || N == 2) return 1;
        return fib(N - 1) + fib(N - 2);
    }

    static int fibNew(int N, HashMap<Integer, Integer> record) {
        if (N < 1) return 0;
        return helper(record, N);
    }

    static int helper(HashMap<Integer, Integer> record, int n) {
        if (n == 1 || n == 2) return 1;
        // 已经计算过
        if (record.get(n) != null) return record.get(n);
        record.put(n, helper(record, n - 1) + helper(record, n - 2));
        return record.get(n);
    }

}
