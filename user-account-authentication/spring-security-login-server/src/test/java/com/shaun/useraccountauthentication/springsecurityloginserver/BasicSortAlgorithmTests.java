package com.shaun.useraccountauthentication.springsecurityloginserver;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;

@ActiveProfiles("test")
@SpringBootTest
class BasicSortAlgorithmTests {

    int[] sourceArray = {2, 9, 4, 2, 2, 5, 1, 1, 2, 1, 9, 8, 4, 4, 9};
    int[] resultArray = {1, 1, 1, 2, 2, 2, 2, 4, 4, 4, 5, 8, 9, 9, 9};

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

        int[] arr = Arrays.copyOf(sourceArray, sourceArray.length);

        arr = generalInsertSort(arr, 1);

        assert (Arrays.equals(arr, resultArray));
    }

    @Test
    void ShellSort() {

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

        int[] incrementSequence = {40, 13, 4, 1};

        int[] arr = Arrays.copyOf(sourceArray, sourceArray.length);

        for (int i = 0; i < incrementSequence.length; i++) {
            int gap = incrementSequence[i];
            arr = generalInsertSort(arr, gap);
        }

        assert (Arrays.equals(arr, resultArray));
    }

    private void swap(int[] arr, int i, int j) {
        if (i == j)
            return;
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

}
