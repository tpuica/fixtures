package de.tpuica.fixtures.model;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Team.
 */
public class Team {

    private String name;

    @JsonProperty ( "founding_date" )
    private String establishmentDate;

    /**
     * Default constructor.
     */
    public Team () {
        super ();
    }

    /**
     * Constructor.
     *
     * @param name the name of the team
     */
    public Team ( String name ) {
        this.name = name;
    }

    public String getName () {
        return name;
    }

    public void setName ( String name ) {
        this.name = name;
    }

    public String getEstablishmentDate () {
        return establishmentDate;
    }

    public void setEstablishmentDate ( String establishmentDate ) {
        this.establishmentDate = establishmentDate;
    }

    @Override
    public String toString () {
        return name;
    }

    @Override
    public int hashCode () {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( establishmentDate == null ) ? 0 : establishmentDate.hashCode () );
        result = prime * result + ( ( name == null ) ? 0 : name.hashCode () );
        return result;
    }

    @Override
    public boolean equals ( Object obj ) {
        if ( this == obj )
            return true;
        if ( ! ( obj instanceof Team ) )
            return false;
        Team other = (Team) obj;
        if ( establishmentDate == null ) {
            if ( other.establishmentDate != null )
                return false;
        } else if ( !establishmentDate.equals ( other.establishmentDate ) )
            return false;
        if ( name == null ) {
            if ( other.name != null )
                return false;
        } else if ( !name.equals ( other.name ) )
            return false;
        return true;
    }

}
