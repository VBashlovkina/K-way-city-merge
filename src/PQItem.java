public class PQItem implements Comparable<PQItem> {
	
	String value;
	int arrayIndex; //the index of the array containing this element
	
	public PQItem(String val, int arrayIndex){
		this.value = val;
		this.arrayIndex = arrayIndex;
	}
	/**
	 * compares two PQItems
	 */
	public int compareTo(PQItem pq) {
		return this.value.compareTo(pq.value);
	}
	
	public String toString(){
		return this.value;
	}

}
