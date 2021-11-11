package de.tpuica.fixtures.model;

/**
 * Schedule (game plan).
 */
public class Schedule {

    private Round firstRound;

    private Round secondRound;

    /**
     * Default constructor.
     */
    public Schedule () {
        super ();
    }

    /**
     * Constructor.
     *
     * @param firstRound the first round
     * @param secondRound the second round
     */
    public Schedule ( Round firstRound, Round secondRound ) {
        this.firstRound = firstRound;
        this.secondRound = secondRound;
    }

    public Round getFirstRound () {
        return firstRound;
    }

    public void setFirstRound ( Round firstRound ) {
        this.firstRound = firstRound;
    }

    public Round getSecondRound () {
        return secondRound;
    }

    public void setSecondRound ( Round secondRound ) {
        this.secondRound = secondRound;
    }

}
