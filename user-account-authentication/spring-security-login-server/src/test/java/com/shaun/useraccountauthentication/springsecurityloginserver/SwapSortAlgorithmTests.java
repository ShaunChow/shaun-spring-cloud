package com.shaun.useraccountauthentication.springsecurityloginserver;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class SwapSortAlgorithmTests {

    int[] sourceArray = {2, 9, 4, 2, 2, 5, 1, 1, 2, 1, 9, 8, 4, 4, 9};
    int[] resultArray = {1, 1, 1, 2, 2, 2, 2, 4, 4, 4, 5, 8, 9, 9, 9};

    private void swap(int[] arr, int i, int j) {
        if (i == j)
            return;
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    private int partitionSingleSideLoop(int[] arr, int left, int right) {
        int pivot = left;
        int mark = left;
        for (int i = mark; i <= right; i++) {
            if (arr[i] < arr[pivot]) {
                mark++;
                swap(arr, i, mark);
            }
        }
        swap(arr, pivot, mark);
        return mark;
    }

    private int partitionDoubleSideLoop(int[] arr, int left, int right) {
        int pivot = left;
        while (left < right) {
            while (left < right && arr[right] > arr[pivot]) {
                right--;
            }
            while (left < right && arr[left] <= arr[pivot]) {
                left++;
            }
            if (right > left) {
                swap(arr, right, left);
            }
        }
        swap(arr, pivot, left);
        return left;
    }

    /*
     * Bubble-Sort
     */

    @Test
    void BubbleSort() {

        int[] arr = Arrays.copyOf(sourceArray, sourceArray.length);

        for (int i = 1; i < arr.length; i++) {
            boolean flag = true;
            for (int j = 0; j < arr.length - i; j++) {
                if (arr[j] > arr[j + 1]) {
                    swap(arr, j, j + 1);
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
    void BubbleSortII() {

        int[] arr = Arrays.copyOf(sourceArray, sourceArray.length);

        for (int i = 1; i < arr.length; i++) {
            for (int j = 0; j < arr.length - i; j++) {
                int partitionIndex = partitionSingleSideLoop(arr, j, arr.length - 1);
            }
        }
        assert (Arrays.equals(arr, resultArray));
    }

    /*
     * Quick-Sort
     */

    private int[] quickSort(int[] arr, int left, int right) {
        if (left < right) {
            int partitionIndex = partitionDoubleSideLoop(arr, left, right);
            quickSort(arr, left, partitionIndex - 1);
            quickSort(arr, partitionIndex + 1, right);
        }
        return arr;
    }

    @Test
    void QuickSort() {

        int[] arr = Arrays.copyOf(sourceArray, sourceArray.length);

        arr = quickSort(arr, 0, arr.length - 1);

        assert (Arrays.equals(arr, resultArray));
    }
}
