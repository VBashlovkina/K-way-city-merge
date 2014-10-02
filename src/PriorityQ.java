/*
 * References:
 * How to use abstract classes:
 * http://docs.oracle.com/javase/tutorial/java/IandI/abstract.html
 * How to use compareTo:
 * http://www.tutorialspoint.com/java/java_string_compareto.htm
 */

import java.util.Arrays;

public class PriorityQ extends PQ {

	public PriorityQ(int k) {
		super(k);
	}

	/**
	 * gets the parent of the element at index
	 * 
	 * @param index
	 * @return the index of the parent of the element at index
	 */
	static private int parent(int index) {
		return (index - 1) / 2;
	}

	/**
	 * gets left child of the element at index
	 * 
	 * @param index
	 * @return the index of the left child of the element at index
	 */
	static private int left(int index) {
		return 2 * index + 1;
	}

	/**
	 * gets the right child of the element at index
	 * 
	 * @param index
	 * @return the index of the right child of the element at index
	 */
	static private int right(int index) {
		return 2 * index + 2;
	}

	/**
	 * This is for testing: determine whether this has the heap property
	 * @return true or false
	 */
	public boolean isHeap() {
		for (int i = 0; i < this.currentSize; i++) {
			if (this.numChildren(i) > 0) {
				if (this.heapArray[i].compareTo(heapArray[left(i)]) > 0) {
					return false;
				}
			}
			if (this.numChildren(i) > 1) {
				if (this.heapArray[i].compareTo(heapArray[right(i)]) > 0) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public void insert(PQItem item) {
		this.heapArray[this.currentSize] = item;
		this.swapUp();
		this.currentSize++;
	}

	/**
	 * swaps the elements at the indicies i1 & i2
	 * 
	 * @param i1
	 * @param i2
	 */
	private void swap(int i1, int i2) {
		PQItem tmp = this.heapArray[i1];
		this.heapArray[i1] = this.heapArray[i2];
		this.heapArray[i2] = tmp;
	}

	/**
	 * gets the number of children of the element at index
	 * 
	 * @param index
	 * @return the number of children (0, 1, or 2)
	 */
	private int numChildren(int index) {
		int num = 0;
		if (left(index) < this.currentSize) {
			num++;
		}// if
		if (right(index) < this.currentSize) {
			num++;
		}// if
		return num;
	}// numChildren

	/**
	 * "swaps up" elements at the bottom of the heap to restore the heap
	 * property
	 */
	private void swapUp() {
		int index = this.currentSize; // index of the element you want to swap
		// up
		PQItem val = this.heapArray[this.currentSize];
		while (index >= 1) {
			if (val.compareTo(this.heapArray[parent(index)]) < 0) {
				this.swap(index, parent(index));
				index = parent(index);
			} else {
				break;
			}// if/else
		}// while
	}// swapUp

	/**
	 * "swaps down" elements at the top of the heap to restore the heap property
	 */
	private void swapDown() {
		int index = 0;
		int smallerChildIndex = 0;
		while (numChildren(index) != 0) {
			if (numChildren(index) == 1
					|| this.heapArray[left(index)]
							.compareTo(this.heapArray[right(index)]) <= 0) {
				smallerChildIndex = left(index);
			}// if
			else {
				smallerChildIndex = right(index);
			}

			if (this.heapArray[smallerChildIndex]
					.compareTo(this.heapArray[index]) < 0) {
				swap(index, smallerChildIndex);
				index = smallerChildIndex;
			}// if -left/index
			else {
				break;
			}// else
		}// while
	}

	@Override
	public PQItem remove() {
		PQItem top = this.heapArray[0];
		this.heapArray[0] = this.heapArray[this.currentSize - 1];
		this.heapArray[currentSize - 1] = null;
		this.currentSize--;
		this.swapDown();
		return top;
	}

	public String toString() {
		return Arrays.toString(this.heapArray);
	}

	/**
	 * merges the k sorted arrays in input into one sorted array
	 * 
	 * @param input
	 *            an array of sorted arrays
	 * @precondition input contains sorted arrays
	 * @precondition the arrayIndexes of PQItems in each of the k arrays should
	 *               correspond to the index of the array containing it
	 * @post none
	 * @return output, an array containing all the items in input in one flat,
	 *         sorted array
	 */
	static public PQItem[] kWayMerge(PQItem[][] input) {
		int k = input.length;
		PriorityQ pq = new PriorityQ(k);
		int currentOutput = 0; // keep track of our place in output
		// index of the array where most recently removed item came from
		int arrayFrom;
		int totalVals = 0; // calculate total number of inputs
		int[] currents = new int[k]; // keep track of the current element in
		// each input array
		for (int j = 0; j < k; j++) {
			totalVals += input[j].length;
		}
		PQItem[] output = new PQItem[totalVals];

		for (int i = 0; i < k; i++) { // while there are items left in input
			pq.insert(input[i][currents[i]]); // put one element from each array
			// into the heap
		}
		Arrays.fill(currents, 1); // they all begin at the first element
		// current[i] points to the next element to be inserted into the heap

		while (!pq.isEmpty()) {
			// the first element in that heap is the smallest out of all of them
			// put that element in the output array
			output[currentOutput] = pq.remove();
			// get another element from the array the smallest item came from
			// and put that in the heap
			arrayFrom = output[currentOutput++].arrayIndex;

			// if there are elements left in the array the removed element was
			// from
			if (currents[arrayFrom] < input[arrayFrom].length) {
				// insert an element from that array and update pointer
				pq.insert(input[arrayFrom][currents[arrayFrom]++]);

			} else {// if that array is empty, pick another array to insert from
				for (int i = 0; i < k; i++) {
					if (currents[i] < input[i].length) {
						pq.insert(input[i][currents[i]++]);
						break;
					}
				}
			}

		} // while

		return output;
	}

	public static void main(String[] args) {

		// Experiment with the heap
		String[] contents = {"Blue", "Red", "green" };
		PriorityQ pq = new PriorityQ(7);
		int i = 0;
		while (!pq.isFull()) {
			System.out.println("inserted: " + contents[i]);
			pq.insert(new PQItem(contents[i++], 3));
		}
		System.out.println(Arrays.toString(pq.heapArray));
		while (!pq.isEmpty()) {
			System.out.println("top: " + pq.top().toString());
			System.out.println("removed: " + pq.remove().toString());
		}

		// Experiment with K-way merge of a 2d array
		int rows = 9;
		int cols = 7;
		PQItem[][] test = new PQItem[rows][cols];
		for (int k = 0; k < rows; k++) {
			for (int j = 0; j < cols; j++) {
				test[k][j] = new PQItem(k+"", k);
			}
		}

		System.out.println(Arrays.toString(kWayMerge(test)));

	}
}
