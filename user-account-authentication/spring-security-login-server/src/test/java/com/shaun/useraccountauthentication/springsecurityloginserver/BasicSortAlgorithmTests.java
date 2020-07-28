package com.shaun.useraccountauthentication.springsecurityloginserver;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;

@ActiveProfiles("test")
@SpringBootTest
class BasicSortAlgorithmTests {

    int[] generalInsertSort(int[] arr, int gap) {
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
    }

    @Test
    void InsertSort() {

        int[] sourceArray = {2, 9, 4, 2, 2, 5, 1, 1, 2, 1, 9, 8, 4, 4, 9};
        int[] resultArray = {1, 1, 1, 2, 2, 2, 2, 4, 4, 4, 5, 8, 9, 9, 9};

        int[] arr = Arrays.copyOf(sourceArray, sourceArray.length);

        arr = generalInsertSort(arr, 1);

        assert (Arrays.equals(arr, resultArray));
    }

    @Test
    void ShellSort() {

        int[] sourceArray = {2, 9, 4, 2, 2, 5, 1, 1, 2, 1, 9, 8, 4, 4, 9};
        int[] resultArray = {1, 1, 1, 2, 2, 2, 2, 4, 4, 4, 5, 8, 9, 9, 9};

        int[] arr = Arrays.copyOf(sourceArray, sourceArray.length);

        int gap = 1;
        while (gap < arr.length) {
            gap = gap * 3 + 1;
        }

        while (gap > 0) {
            arr = generalInsertSort(arr, gap);
            gap = (int) Math.floor(gap / 3);
        }

        assert (Arrays.equals(arr, resultArray));
    }

    @Test
    void ShellSortII() {

        int[] sourceArray = {2, 9, 4, 2, 2, 5, 1, 1, 2, 1, 9, 8, 4, 4, 9};
        int[] resultArray = {1, 1, 1, 2, 2, 2, 2, 4, 4, 4, 5, 8, 9, 9, 9};

        int[] incrementSequence = {40, 13, 4, 1};

        int[] arr = Arrays.copyOf(sourceArray, sourceArray.length);

        for (int i = 0; i < incrementSequence.length; i++) {
            int gap = incrementSequence[i];
            arr = generalInsertSort(arr, gap);
        }

        assert (Arrays.equals(arr, resultArray));
    }

    @Test
    void BubbleSort() {

        int[] sourceArray = {2, 9, 4, 2, 2, 5, 1, 1, 2, 1, 9, 8, 4, 4, 9};
        int[] resultArray = {1, 1, 1, 2, 2, 2, 2, 4, 4, 4, 5, 8, 9, 9, 9};

        int[] arr = Arrays.copyOf(sourceArray, sourceArray.length);

        for (int i = 1; i < arr.length; i++) {
            boolean flag = true;
            for (int j = 0; j < arr.length - i; j++) {
                if (arr[j] > arr[j + 1]) {
                    int tmp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = tmp;

                    flag = false;
                }
            }
            if (flag) {
                break;
            }
        }
        assert (Arrays.equals(arr, resultArray));
    }

    @Test
    void SelectionSort() {

        int[] sourceArray = {2, 9, 4, 2, 2, 5, 1, 1, 2, 1, 9, 8, 4, 4, 9};
        int[] resultArray = {1, 1, 1, 2, 2, 2, 2, 4, 4, 4, 5, 8, 9, 9, 9};

        int[] arr = Arrays.copyOf(sourceArray, sourceArray.length);

        for (int i = 0; i < arr.length - 1; i++) {
            int min = i;

            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j] < arr[min]) {
                    min = j;
                }
            }

            if (i != min) {
                int tmp = arr[i];
                arr[i] = arr[min];
                arr[min] = tmp;
            }
        }
        assert (Arrays.equals(arr, resultArray));
    }

}
