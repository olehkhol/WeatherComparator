package ua.in.khol.oleh.touristweathercomparer.model;

/**
 * Common interface for all types of Repository helpers
 */
public interface Helper {

    /**
     * Setups a source of data
     *
     * @param source - int value, which represents a source of data
     */
    void setup(int source);
}
