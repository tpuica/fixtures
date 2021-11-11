package de.tpuica.fixtures.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * League. A collection of Teams.
 */
public class League {

    @JsonProperty ( "league" )
    private String name;

    private String country;

    private List<Team> teams = new ArrayList<> ();

    public String getName () {
        return name;
    }

    public void setName ( String name ) {
        this.name = name;
    }

    public String getCountry () {
        return country;
    }

    public void setCountry ( String country ) {
        this.country = country;
    }

    public List<Team> getTeams () {
        return teams;
    }

    public void setTeams ( List<Team> teams ) {
        this.teams = teams;
    }

    @Override
    public String toString () {
        StringBuilder sb = new StringBuilder ( name );
        sb.append ( " : " );
        if ( teams != null ) {
            sb.append ( teams.stream ().map ( Team::toString ).collect ( Collectors.joining ( ", " ) ) );
        }
        return sb.toString ();
    }

}
