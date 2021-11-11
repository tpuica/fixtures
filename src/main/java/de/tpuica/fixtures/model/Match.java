package de.tpuica.fixtures.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


/**
 * Match.
 */
public class Match {

    private Team homeTeam;

    private Team awayTeam;

    private LocalDate date;

    private int stageId;

    /**
     * Default constructor.
     */
    public Match () {
        super ();
    }

    /**
     * Constructor.
     *
     * @param stageId the stage ID
     * @param homeTeam the home team
     * @param awayTeam the away team
     */
    public Match ( int stageId, Team homeTeam, Team awayTeam ) {
        this.stageId = stageId;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
    }

    public Team getHomeTeam () {
        return homeTeam;
    }

    public void setHomeTeam ( Team homeTeam ) {
        this.homeTeam = homeTeam;
    }

    public Team getAwayTeam () {
        return awayTeam;
    }

    public void setAwayTeam ( Team awayTeam ) {
        this.awayTeam = awayTeam;
    }

    public LocalDate getDate () {
        return date;
    }

    public void setDate ( LocalDate date ) {
        this.date = date;
    }

    public int getStageId () {
        return stageId;
    }

    public void setStageId ( int stageId ) {
        this.stageId = stageId;
    }

    @Override
    public String toString () {
        return getHomeTeam () + " - " + getAwayTeam ();
    }

    /**
     * String representation of the scheduled match for the given date formatter.
     *
     * @param dateFormatter the date formatter
     * @return the match as string
     */
    public String toString ( DateTimeFormatter dateFormatter ) {
        return toString ( date.format ( dateFormatter ) );
    }

    private String toString ( String formattedDate ) {
        return formattedDate + " " + toString ();
    }

}
