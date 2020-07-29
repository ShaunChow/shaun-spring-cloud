package com.shaun.useraccountauthentication.springsecurityloginserver;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.function.BiFunction;

@ActiveProfiles("test")
@SpringBootTest
public class SelectSortAlgorithmTests {

    int[] sourceArray = {2, 9, 4, 2, 2, 5, 1, 1, 2, 1, 9, 8, 4, 4, 9};
    int[] resultArray = {1, 1, 1, 2, 2, 2, 2, 4, 4, 4, 5, 8, 9, 9, 9};

    BiFunction<int[], BiFunction<int[], Integer, Integer>, int[]> selectionSortMax = (arr, extractFunction) -> {
        int len = arr.length;

        for (int i = len - 1; i > 0; i--) {
            extractFunction.apply(arr, i);
        }
        return arr;
    };
    BiFunction<int[], BiFunction<int[], Integer, Integer>, int[]> selectionSortMin = (arr, extractFunction) -> {
        int len = arr.length;

        for (int i = 0; i < len - 1; i++) {
            extractFunction.apply(arr, i);
        }
        return arr;
    };

    private void swap(int[] arr, int i, int j) {
        if (i == j)
            return;
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    /*
     * Heapsort
     */

    private void buildMaxHeap(int[] arr) {
        for (int i = (int) Math.floor(arr.length / 2); i >= 0; i--) {
            maxHeapify(arr, i, arr.length);
        }
    }

    private void maxHeapify(int[] arr, int i, int len) {
        int left = 2 * i + 1;
        int right = 2 * i + 2;
        int largest = i;

        if (left < len && arr[left] > arr[largest]) {
            largest = left;
        }

        if (right < len && arr[right] > arr[largest]) {
            largest = right;
        }

        if (largest != i) {
            swap(arr, i, largest);
            maxHeapify(arr, largest, len);
        }
    }

    @Test
    void HeapsortMax() {

        int[] arr = Arrays.copyOf(sourceArray, sourceArray.length);

        int len = arr.length;

        buildMaxHeap(arr);

        for (int i = len - 1; i > 0; i--) {
            swap(arr, 0, i);
            len--;
            maxHeapify(arr, 0, len);
        }
        assert (Arrays.equals(arr, resultArray));
    }

    @Test
    void HeapsortMaxII() {

        int[] arr = Arrays.copyOf(sourceArray, sourceArray.length);

        int len = arr.length;

        buildMaxHeap(arr);

        for (int i = len - 1; i > 0; i--) {
            heapsortExtractMax.apply(arr, i);
        }
        assert (Arrays.equals(arr, resultArray));
    }

    @Test
    void HeapsortMaxIII() {
        int[] arr = Arrays.copyOf(sourceArray, sourceArray.length);
        buildMaxHeap(arr);
        arr = selectionSortMax.apply(arr, heapsortExtractMax);
        assert (Arrays.equals(arr, resultArray));
    }

    BiFunction<int[], Integer, Integer> heapsortExtractMax = (arr, maxPosition) -> {
        maxHeapify(arr, 0, maxPosition);
        swap(arr, maxPosition, 0);
        return arr[maxPosition];
    };


    /*
     * Selection-Sort
     */

    @Test
    void SelectionMinSort() {
        int[] arr = Arrays.copyOf(sourceArray, sourceArray.length);
        arr = selectionSortMin.apply(arr, selectionSortExtractMin);
        assert (Arrays.equals(arr, resultArray));
    }

    @Test
    void SelectionMaxSort() {
        int[] arr = Arrays.copyOf(sourceArray, sourceArray.length);
        arr = selectionSortMax.apply(arr, selectionSortExtractMax);
        assert (Arrays.equals(arr, resultArray));
    }

    BiFunction<int[], Integer, Integer> selectionSortExtractMax = (arr, maxPosition) -> {
        int max = maxPosition;

        for (int j = maxPosition - 1; j >= 0; j--) {
            if (arr[j] > arr[max]) {
                max = j;
            }
        }

        swap(arr, maxPosition, max);
        return arr[maxPosition];
    };

    BiFunction<int[], Integer, Integer> selectionSortExtractMin = (arr, minPosition) -> {
        int min = minPosition;

        for (int j = minPosition + 1; j < arr.length; j++) {
            if (arr[j] < arr[min]) {
                min = j;
            }
        }

        swap(arr, minPosition, min);
        return arr[minPosition];
    };
}
