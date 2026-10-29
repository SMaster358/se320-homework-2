package edu.drexel.se320;

import java.util.Arrays;
import java.util.NoSuchElementException;

public class BinarySearch {

    // DO NOT MODIFY THIS SIGNATURE
    // This includes the protected modifier; the autograder currently relies
    // on a combination of overloading and visibility hacks to swap out your
    // code at runtime to test your test suite.
    protected static <T extends Comparable<T>> int binarySearchImplementation(T[] array, T elem) {
        int size;
        int middle;
        int result;
        try {
            size = array.length;
            middle = size / 2;
            result = array[middle].compareTo(elem);

        } catch (IndexOutOfBoundsException e1) {
            throw new NoSuchElementException("Element not found in array");
        }
        catch (Exception e2) {
            throw new IllegalArgumentException("Illegal argument given");
        }
        if (size > 1) {
            if (middle + 1 == size) {
                if (array[middle - 1].compareTo(array[middle]) > 0) {
                    throw new RuntimeException("Array is not sorted");
                }
            } else {
                if (array[middle].compareTo(array[middle + 1]) > 0) {
                    throw new RuntimeException("Array is not sorted");
                }
            }
        } else if (!(array[0].equals(elem))) {
            throw new NoSuchElementException("Element not found in array");
        }
        if (result == 0) {
            return middle;
        } else if (result < 0) {
            T[] newArray = Arrays.copyOfRange(array, middle, size);
            return middle + binarySearchImplementation(newArray, elem);
        } else {
            T[] newArray = Arrays.copyOfRange(array, 0, middle);
            return binarySearchImplementation(newArray, elem);
        }
    }

}
