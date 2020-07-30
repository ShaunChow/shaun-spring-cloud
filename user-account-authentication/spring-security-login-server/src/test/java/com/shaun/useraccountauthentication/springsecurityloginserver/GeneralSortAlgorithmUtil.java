package com.shaun.useraccountauthentication.springsecurityloginserver;

import java.util.function.BiFunction;

public class GeneralSortAlgorithmUtil {

    public static BiFunction<int[], Integer, int[]> InsertSort = (arr, gap) -> {
        for (int i = gap; i < arr.length; i++) {
            int tmp = arr[i];
            int j = i - gap;
            while (j >= 0 && tmp < arr[j]) {
                arr[j + gap] = arr[j];
                j -= gap;
            }
            if ((j + gap) != i) {
                arr[j + gap] = tmp;
            }
        }
        return arr;
    };


}
