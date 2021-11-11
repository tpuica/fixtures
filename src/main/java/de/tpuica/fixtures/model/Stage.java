package de.tpuica.fixtures.model;

import java.util.ArrayList;
import java.util.List;


/**
 * Stage (Match day). A list of matches where each team plays once.
 */
public class Stage {

    private int id;

    private List<Match> matches;

    /**
     * Default constructor.
     */
    public Stage () {
        super ();
    }

    /**
     * Constructor.
     *
     * @param id the Id of the stage
     */
    public Stage ( int id ) {
        this.id = id;
    }

    public int getId () {
        return id;
    }

    public void setId ( int id ) {
        this.id = id;
    }

    public List<Match> getMatches () {
        return matches;
    }

    public void setMatches ( List<Match> matches ) {
        this.matches = matches;
    }

    /**
     * Adds a match to the current stage.
     *
     * @param match the match to add, not null
     */
    public void addMatch ( Match match ) {
        if ( matches == null ) {
            matches = new ArrayList<> ();
        }
        matches.add ( match );
    }

}
