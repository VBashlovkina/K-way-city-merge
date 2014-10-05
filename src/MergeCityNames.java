import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Arrays;

public class MergeCityNames {

    /*
     * http://www.cs.grinnell.edu/~walker/courses/207.fa14/readings/reading-input
     * .shtml for reading input
     * http://docs.oracle.com/javase/7/docs/api/java/io/BufferedReader.html
     * return type of readLine()
     * http://stackoverflow.com/questions/14443662/printwriter-add-text-to-file
     * use PrintWriter to write to file
     * http://docs.oracle.com/javase/7/docs/api/
     * java/io/FileReader.html#FileReader(java.lang.String) for FileReader
     * exceptions
     */

    /**
     * Merge sorted textual data from multiple files into a single
     * alphabetically sorted text file
     * 
     * @param inputFileNames
     *            the names of the text files to be merged in the project
     *            directory
     * @param outputFileName
     *            the name of the file where the merged data will be written
     * @pre the input files exist in the project directory and are readable
     * @pre in the input files the items appear one per line
     * @pre there is at least one line in each input files
     * @post a text file with merged data will appear in the project directory
     * @throws Exception
     *             TODO: fix formatting if input files cannot be read; if the
     *             output file cannot be written (is a directory name); if the
     *             input files do not contain at least one line
     */
    public static void mergeCities(String[] inputFileNames,
	    String outputFileName) throws Exception {
	int len = inputFileNames.length;// number of files to merge

	// Initialize input streams and readers to each of the input files
	FileReader[] istreams = new FileReader[len];
	BufferedReader[] eyes = new BufferedReader[len];
	for (int i = 0; i < len; i++) {
	    istreams[i] = new FileReader(inputFileNames[i]);
	    eyes[i] = new BufferedReader(istreams[i]);
	}

	// Initialize the output writer
	PrintWriter pen = new PrintWriter(new FileWriter(outputFileName, false));

	// Make a heap sized for at most one element from each file at a time
	PriorityQ pq = new PriorityQ(len);

	// Initialize helper variables
	PQItem toInsert; // PQItem that will be inserted next
	PQItem removed; // the item that was most recently removed
	int fromWhichFile = 0;// index of file from which that item was removed

	// A boolean flag indicating the first iteration of the while loop
	boolean firstIteration = true;

	while (firstIteration || !pq.isEmpty()) {

	    // Pick a new item from one of the files
	    // Start at the file where the most recently removed item came from
	    for (int i = fromWhichFile; i < len + fromWhichFile; i++) {
		// try reading a new line
		try {
		    // If there are more lines, read in a new item
		    toInsert = new PQItem(eyes[i % len].readLine(), i % len);
		    // Insert that item in the heap
		    pq.insert(toInsert);

		    if (!firstIteration)
			// Unless it's the first iteration, we don't need to
			// insert any more items so we leave the loop
			break;
		}// try
		 // If no more lines, close the input stream and readers
		catch (Exception e) {
		    istreams[i % len].close();
		    eyes[i % len].close();
		}// catch
	    }// for
	    firstIteration = false;
	    // Remove the top element which is the smallest
	    removed = pq.remove();
	    // Record its file index
	    fromWhichFile = removed.fileIndex;
	    // Print it to output file
	    pen.println(removed.value);
	} // while
	pen.close();
    }// mergeCities

    /**
     * Check whether the sum of the number of lines in input files is equal to
     * the length of the output file
     * 
     * @param inputsFiles
     * @param outputFile
     * @return true if output has the right length, false otherwise
     * @throws Exception
     */
    public static boolean isRightLength(String[] inputFiles, String outputFile)
	    throws Exception {
	FileReader istream = null;
	BufferedReader eyes = null;
	int len = inputFiles.length;

	// Find the total length of input and output files
	int inputTotalLength = 0 - len;// overcount of null lines
	int outputLength = -1;// overcount of null lines
	String line;
	for (int i = 0; i < len + 1; i++) {
	    if (i < len)
		istream = new FileReader(inputFiles[i]);
	    else
		istream = new FileReader(outputFile);
	    eyes = new BufferedReader(istream);
	    line = "";
	    while (line != null) {
		try {
		    line = eyes.readLine();
		    if (i < len)
			inputTotalLength++;
		    else
			outputLength++;
		}// try
		catch (Exception e) {
		    break;// this never happens for some reason?
		}// catch
	    }// while
	}// for
	istream.close();
	eyes.close();
	return inputTotalLength == outputLength;

    }

    /**
     * Check whether the data in given file is sorted
     * 
     * @param filename
     * @return true if sorted, false otherwise
     * @throws Exception
     *             if file cannot be opened or is empty
     */
    public static boolean isSorted(String filename) throws Exception {
	FileReader istream = new FileReader(filename);
	BufferedReader eyes = new BufferedReader(istream);
	String line1 = eyes.readLine();
	String line2;
	boolean toReturn = true;
	while (true) {
	    try {
		line2 = eyes.readLine();
		if (line1.compareTo(line2) > 0) {
		    // if something is in the wrong order, print it
		    System.out.println("Not in order: " + line1 + ", " + line2);
		    toReturn = false;
		}
		// read next line
		line1 = line2;
	    }// try
	    catch (Exception e) {
		// if there are no more lines, we are done
		break;
	    }// catch
	}// while
	istream.close();
	eyes.close();
	return toReturn;
    }// isSorted

    /**
     * Merge text files with sorted city names from different states into one
     * alphabetically sorted file The possible states are: 0 - Iowa, 1 -
     * Illinois, 2 - Kansas, 3 - Minnesota, 4 - Missouri, 5 - Nebraska, 6 -
     * North Dakota, 7 - South Dakota, 8 - Wisconsin.
     * 
     * @param args
     *            To specify which states to use, enter the corresponding
     *            numbers separated by spaces as command-line arguments. If no
     *            arguments are entered, all possible states are used. ,
     * @pre the text files with city name data exist in the project directory
     *      and are readable
     * @pre args contain from 0 to 9 integers ranging from 0 to 8.
     * @throws Exception
     *             if preconditions aren't met
     */
    public static void main(String[] args) throws Exception {
	// Initialize the possible filenames
	String[] possibleStates = { "iowa", "illinois", "kansas", "minnesota",
		"missouri", "nebraska", "north-dakota", "south-dakota",
		"wisconsin" };
	int len = possibleStates.length;
	for (int i = 0; i < len; i++)
	    possibleStates[i] = possibleStates[i] + ".cities";

	// If there were arguments,
	String[] requestedStates = possibleStates;
	int argLen = args.length;
	if (argLen > len)
	    throw new Exception("The expected number of arguments "
		    + "is less than or equal to " + len + ", was " + argLen);
	if (args.length > 0) {
	    requestedStates = new String[args.length];
	    for (int i = 0; i < args.length; i++) {
		requestedStates[i] = possibleStates[new Integer(args[i])];
	    }
	}

	System.out.println("Merging the following files:\n"
		+ Arrays.toString(requestedStates));	
	mergeCities(requestedStates, "merged.cities");
	System.out.println("The merged file \"merged.cities\" " +
		"is now in the project directory.");

	System.out
		.println("Does the merged file contatin the right number of lines? "
			+ isRightLength(requestedStates, "merged.cities"));
	// System.out.println(isSorted("merged.cities"));// false
	// System.out.println(isSorted("illinois.cities"));// false
    }// main
}// class MergeCityNames
