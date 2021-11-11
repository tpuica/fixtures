package de.tpuica.fixtures.model;

import java.util.ArrayList;
import java.util.List;


/**
 * Round (e.g. 1st or 2nd leg of a tournament-championship).
 */
public class Round {

    private List<Stage> stages;

    public List<Stage> getStages () {
        return stages;
    }

    public void setStages ( List<Stage> stages ) {
        this.stages = stages;
    }

    /**
     * Adds a stage to the current round.
     *
     * @param stage the stage to add, not null
     */
    public void addStage ( Stage stage ) {
        if ( stages == null ) {
            stages = new ArrayList<> ();
        }
        stages.add ( stage );
    }

}
