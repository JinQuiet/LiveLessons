package search;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

/**
 * This class demonstrates the use of the Java 7 fork-join framework
 * to search for phrases in the works of Shakespeare.  There's
 * commented-out code that shows how to use Java 8 streams to
 * implement this solution even more concisely.
 */
public class SearchWithForkJoinTask
       extends RecursiveTask<List<List<SearchResults>>> {
    /**
     * The list of strings to search.
     */
    private List<? extends CharSequence> mInputList;

    /**
     * The list of phrases to find.
     */
    private List<String> mPhrasesToFind;

    /**
     * Indicates whether to run PhraseMatchTask concurrently.
     */
    private boolean mParallel;

    /**
     * Construtor initializes the fields.
     */
    public SearchWithForkJoinTask(List<? extends CharSequence> inputList,
                                  List<String> phrasesToFind,
                                  boolean parallel) {
        mInputList = inputList;
        mPhrasesToFind = phrasesToFind;
        mParallel = parallel;
    }

    /**
     * Searches for phrases to find in the input list.
     */
    @Override
    protected List<List<SearchResults>> compute() {
        // Create a list of RecursiveTasks.
        List<RecursiveTask<List<SearchResults>>> forks =
            new LinkedList<>();

        // Loop through each input string in the list.
        for (CharSequence input : mInputList) {
            // Create a RecursiveTask that searches an input string
            // for a list of phrases.
            SearchForPhrasesTask task =
                new SearchForPhrasesTask(input,
                                         mPhrasesToFind,
                                         mParallel);

            // Add the new task to the list of tasks.
            forks.add(task);

            // Use the fork-join framework to create a list of
            // SearchResults that indicate which phrases are found in
            // the list of input strings.
            task.fork();
        }

        // Create a list to hold the results.
        List<List<SearchResults>> results =
                new LinkedList<>();

        // Iterate through the list of ReactiveTasks.
        for (RecursiveTask<List<SearchResults>> task : forks)
            // Join each task and add to the list of results.
            results.add(task.join());

        // Return the results.
        return results;

        /*
        // A more concise solution via Java 8 streams.
        return mInputList
            // Convert the input list into a stream.
            .stream()

            // Create and fork a new SearchForPhrasesTask for 
            // each input string.
            .map(inputString
                 -> new SearchForPhrasesTask(inputString).fork())

            // Collect the results into a list of lists of
            // SearchResults.
            .collect(toList())

            // Convert that list into another stream.
            .stream()

            // Join all the results into a stream of lists of
            // SearchResults.
            .map(ForkJoinTask::join)

            // Collect the results into the final list of lists of
            searchResults.
            .collect(toList());
        */
    }
}
