package edu.drexel.se320;

// Hamcrest
import static java.util.Arrays.sort;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

// Jqwik
import net.jqwik.api.*;
import net.jqwik.api.Tuple.Tuple2;
import net.jqwik.api.constraints.*;
import net.jqwik.api.statistics.Statistics;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.NoSuchElementException;


public class PropertyTests extends BinarySearchBase {

    public Integer[] generateArray(int length) {
        Arbitrary<Integer> betweenRange = Arbitraries.integers().between(1, 100);
        Integer[] array = new Integer[length];
        for (int i = 0; i < length; i++) {
            int num = betweenRange.sample();
            array[i] = num;
        }
        Arrays.sort(array);
        return array;
    }

    @Property
    public boolean checkSingleArrayElt(@ForAll int x) {
        Integer[] arr = {x};
        return find(arr, x) == 0;
    }

    @Property
    public void checkEmptyArray(@ForAll int x) {
        Integer[] arr = {};
        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            binarySearchImplementation(arr, x);
        });
        assertEquals("Element not found in array", exception.getMessage());
    }

    @Property
    public void arrayDoesntContainValue(@ForAll @IntRange(min = 0, max = 100) int x, @ForAll @IntRange(min = 0, max = 100) int target) {
        Integer[] arr = generateArray(x);
        Assume.that(Arrays.binarySearch(arr, target) < 0);

        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            binarySearchImplementation(arr, target);
        });
        assertEquals("Element not found in array", exception.getMessage());
    }

    @Property
    public void arrayContainsValue(@ForAll @IntRange(min = 0, max = 100) int x, @ForAll @IntRange(min = 0, max = 100) int target) {
        Integer[] arr = generateArray(x);
        Assume.that(Arrays.binarySearch(arr, target) >= 0);
        assertInstanceOf(Integer.class, binarySearchImplementation(arr, target));
    }

    @Property
    public void arrayContainsMultipleInstancesOfTarget(
            @ForAll @IntRange(min = 2, max = 100) int x
    ) {
        Integer[] arr = generateArray(x);
        Integer targetWithDuplicates = null;
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i].equals(arr[i + 1])) {
                targetWithDuplicates = arr[i];
                break;
            }
        }
        Assume.that(targetWithDuplicates != null);
        int index = binarySearchImplementation(arr, targetWithDuplicates);

        assertTrue(index >= 0 && index < arr.length);
        assertEquals(targetWithDuplicates, arr[index]);
    }


    @Property
    public void targetIsAboveMidpointInArray(@ForAll @IntRange(min = 20, max = 100) int x, @ForAll @IntRange(min = 50, max = 100) int target) {
        Integer[] arr = generateArray(x);
        int mid = arr.length / 2;
        Assume.that(Arrays.binarySearch(arr, target) >= 0);
        Assume.that(arr[mid] < target);

        int actual = binarySearchImplementation(arr, target);
        assertTrue(actual > mid);
    }

    @Property
    public void targetIsBelowMidpointInArray(@ForAll @IntRange(min = 20, max = 100) int x, @ForAll @IntRange(min = 0, max = 30) int target) {
        Integer[] arr = generateArray(x);
        int mid = arr.length / 2;
        Assume.that(Arrays.binarySearch(arr, target) >= 0);
        Assume.that(arr[mid] > target);

        int actual = binarySearchImplementation(arr, target);
        assertTrue(actual < mid);
    }

    @Property
    public void targetGreaterThanMaxFails(@ForAll @IntRange(min = 1, max = 10) int x, @ForAll @IntRange(min = 101, max = 200) int target) {
        Integer[] arr = generateArray(x);
        Assume.that(arr[arr.length - 1] < target);

        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            binarySearchImplementation(arr, target);
        });
        assertEquals("Element not found in array", exception.getMessage());
    }

    @Property
    public void targetLessThanMinFails(@ForAll @IntRange(min = 1, max = 10) int x, @ForAll @IntRange(min = -10, max = -1) int target) {
        Integer[] arr = generateArray(x);
        Assume.that(arr[0] > target);

        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            binarySearchImplementation(arr, target);
        });
        assertEquals("Element not found in array", exception.getMessage());
    }

    @Property
    public void correctIndexIsReturned(@ForAll @IntRange(min = 1, max = 100) int x, @ForAll @IntRange(min = 0, max = 100) int target) {
        Integer[] arr = generateArray(x);
        Assume.that(Arrays.binarySearch(arr, target) >= 0);
        int index = binarySearchImplementation(arr, target);
        assertEquals(target, arr[index]);
    }

    @Property
    public void targetWorksAtFirstElement(@ForAll @IntRange(min = 1, max = 50) int x) {
        Integer[] arr = generateArray(x);
        Assume.that(arr.length > 0);
        int target = arr[0];
        int index = binarySearchImplementation(arr, target);

        assertTrue(index >= 0 && index < arr.length);
        assertEquals(target, arr[index]);
    }

    @Property
    public void targetWorksAtLastElement(@ForAll @IntRange(min = 1, max = 50) int x) {
        Integer[] arr = generateArray(x);
        Assume.that(arr.length > 0);
        int target = arr[arr.length - 1];
        int index = binarySearchImplementation(arr, target);

        assertTrue(index >= 0 && index < arr.length);
        assertEquals(target, arr[index]);
    }
}
