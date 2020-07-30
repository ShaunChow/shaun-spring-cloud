package com.shaun.useraccountauthentication.springsecurityloginserver;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class BucketSortAlgorithmTests {

    int[] sourceArray = {2, 9, 4, 2, 2, 5, 1, 1, 2, 1, 9, 8, 4, 4, 9};
    int[] resultArray = {1, 1, 1, 2, 2, 2, 2, 4, 4, 4, 5, 8, 9, 9, 9};

    private int getMaxValue(int[] arr) {
        int maxValue = arr[0];
        for (int value : arr) {
            if (maxValue < value) {
                maxValue = value;
            }
        }
        return maxValue;
    }

    private int[] arrAppend(int[] arr, int value) {
        arr = Arrays.copyOf(arr, arr.length + 1);
        arr[arr.length - 1] = value;
        return arr;
    }


    /*
     * Bucket-Sort
     */

    private int[] bucketSort(int[] arr, int bucketSize) {
        if (arr.length == 0) {
            return arr;
        }

        int minValue = arr[0];
        int maxValue = arr[0];
        for (int value : arr) {
            if (value < minValue) {
                minValue = value;
            } else if (value > maxValue) {
                maxValue = value;
            }
        }

        int bucketCount = (int) Math.floor((maxValue - minValue) / bucketSize) + 1;
        int[][] buckets = new int[bucketCount][0];

        for (int i = 0; i < arr.length; i++) {
            int index = (int) Math.floor((arr[i] - minValue) / bucketSize);
            buckets[index] = arrAppend(buckets[index], arr[i]);
        }

        int arrIndex = 0;
        for (int[] bucket : buckets) {
            if (bucket.length <= 0) {
                continue;
            }
            bucket = GeneralSortAlgorithmUtil.InsertSort.apply(bucket, 1);
            for (int value : bucket) {
                arr[arrIndex++] = value;
            }
        }
        return arr;
    }

    @Test
    void BucketSort() {

        int[] arr = Arrays.copyOf(sourceArray, sourceArray.length);

        int bucketSize = 5;
        arr = bucketSort(arr, bucketSize);

        assert (Arrays.equals(arr, resultArray));
    }


    /*
     * Counting-Sort
     */

    private int[] countingSort(int[] arr, int maxValue) {
        int bucketLen = maxValue + 1;
        int[] bucket = new int[bucketLen];

        for (int value : arr) {
            bucket[value]++;
        }

        int sortedIndex = 0;
        for (int j = 0; j < bucketLen; j++) {
            while (bucket[j] > 0) {
                arr[sortedIndex++] = j;
                bucket[j]--;
            }
        }
        return arr;
    }

    @Test
    void CountingSort() {

        int[] arr = Arrays.copyOf(sourceArray, sourceArray.length);

        int maxValue = getMaxValue(arr);
        arr = countingSort(arr, maxValue);

        assert (Arrays.equals(arr, resultArray));
    }


    /*
     * Radix-Sort
     */

    private int getMaxDigit(int[] arr) {
        int maxValue = getMaxValue(arr);
        return getNumLenght(maxValue);
    }

    protected int getNumLenght(long num) {
        if (num == 0) {
            return 1;
        }
        int lenght = 0;
        for (long temp = num; temp != 0; temp /= 10) {
            lenght++;
        }
        return lenght;
    }

    private int[] radixSort(int[] arr, int maxDigit) {
        int mod = 10;
        int dev = 1;

        for (int i = 0; i < maxDigit; i++, dev *= 10, mod *= 10) {
            // 考虑负数的情况，这里扩展一倍队列数，其中 [0-9]对应负数，[10-19]对应正数 (bucket + 10)
            int[][] counter = new int[mod * 2][0];

            for (int j = 0; j < arr.length; j++) {
                int bucket = ((arr[j] % mod) / dev) + mod;
                counter[bucket] = arrAppend(counter[bucket], arr[j]);
            }

            int pos = 0;
            for (int[] bucket : counter) {
                for (int value : bucket) {
                    arr[pos++] = value;
                }
            }
        }

        return arr;
    }

    @Test
    void RadixSort() {

        int[] arr = Arrays.copyOf(sourceArray, sourceArray.length);

        int maxDigit = getMaxDigit(arr);
        arr = radixSort(arr, maxDigit);

        assert (Arrays.equals(arr, resultArray));
    }
}
