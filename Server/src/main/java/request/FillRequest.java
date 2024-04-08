package request;

/**
 * The fill request.
 */
public class FillRequest {
    /**
     * Parameter that has the username that needs to have data generated for.
     */
    String username;
    /**
     * Parameter that sets the number of generations of ancestors to be generated.
     */
    int generations;

    public String getUsername() {
        return username;
    }

    public int getNumOfGenerations() {
        return generations;
    }

    public FillRequest(String username, int generations) {
        this.username = username;
        this.generations = generations;
    }
}
