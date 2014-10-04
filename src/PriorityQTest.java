import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

public class PriorityQTest {
    static PQItem[][][] arrays = { new PQItem[5][5],
            new PQItem[3][7], new PQItem[3][1], new PQItem[1][8] };
    static PQItem[][] empty = {};

    /**
     * Determine whether a given array is sorted
     * 
     * @param arr
     * @return true or false
     */
    public static boolean isSorted(PQItem[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i].compareTo(arr[i + 1]) > 0) {
                return false;
            }
        }
        return true;
    }

    @org.junit.Before
    public void Before() {
        // create some rectangular arrays
        for (int i = 0; i < arrays.length; i++) {
            for (int j = 0; j < arrays[i].length; j++) {
                for (int k = 0; k < arrays[i][j].length; k++) {
                    arrays[i][j][k] = new PQItem(i + j + k, j);
                }
            }
        }
    }

    @Test
    public void testHeap() {
        PriorityQ pq = new PriorityQ(8);

        for (int i = 0; i < arrays[3][0].length; i++) {
            pq.insert(arrays[3][0][i]);
            assertTrue(pq.isHeap()); // has heap property
            assertTrue(Arrays.asList(pq.heapArray).contains(
                    arrays[3][0][i]));
            // contains the item we just inserted
        }
        for (int i = 0; i < arrays[3][0].length; i++) {
            PQItem removed = pq.remove();
            assertTrue(pq.isHeap()); // has heap property
            // does not contain the item we just inserted
            assertFalse(Arrays.asList(pq.heapArray).contains(removed));
        }

        // Reverse the order of the array
        for (int i = arrays[3][0].length - 1; i >= 0; i--) {
            pq.insert(arrays[3][0][i]);
            assertTrue(pq.isHeap());
            assertTrue(Arrays.asList(pq.heapArray).contains(
                    arrays[3][0][i]));
        }
        for (int i = 0; i < arrays[3][0].length; i++) {
            PQItem removed = pq.remove();
            assertTrue(pq.isHeap());
            assertFalse(Arrays.asList(pq.heapArray).contains(removed));
        }
    }

    @Test
    public void testKMerge() {
        // check rectangular (same length) arrays
        for (int i = 0; i < arrays.length; i++) {
            assertTrue(isSorted(PriorityQ.kWayMerge(arrays[i])));
        }

        // mix and match to form non-rectangular arrays
        PQItem[][] mixMatch1 = { arrays[0][0], arrays[1][0],
                arrays[2][0], arrays[3][0] };
        // reassign arrayIndexes
        for (int i = 0; i < mixMatch1.length; i++) {
            for (int j = 0; j < mixMatch1[i].length; j++) {
                mixMatch1[i][j].fileIndex = i;
            }
        }
        PQItem[][] mixMatch2 = { arrays[0][4], arrays[1][2],
                arrays[2][1], arrays[3][0] };
        for (int i = 0; i < mixMatch2.length; i++) {
            for (int j = 0; j < mixMatch2[i].length; j++) {
                mixMatch2[i][j].fileIndex = i;
            }
        }
        // check the mix and match arrays
        assertTrue(isSorted(PriorityQ.kWayMerge(mixMatch1)));
        assertTrue(isSorted(PriorityQ.kWayMerge(mixMatch2)));
        // check the empty array
        assertTrue(isSorted(PriorityQ.kWayMerge(empty)));

    }

}
