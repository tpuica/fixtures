package de.tpuica.fixtures.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import de.tpuica.fixtures.model.League;
import de.tpuica.fixtures.model.Match;
import de.tpuica.fixtures.model.Round;
import de.tpuica.fixtures.model.Stage;
import de.tpuica.fixtures.model.Team;
import de.tpuica.fixtures.service.SchedulerService;


@TestInstance ( TestInstance.Lifecycle.PER_CLASS )
class SchedulerServiceTest {

    private final static String TEAM_A = "A";

    private final static String TEAM_B = "B";

    private final static String TEAM_C = "C";

    private final static String TEAM_D = "D";

    private final static String TEAM_E = "E";

    private final SchedulerService schedulerService = new SchedulerService ();

    private League testLeague;

    @BeforeAll
    void setup () {
        testLeague = new League ();
        testLeague.setName ( "Dummy League" );
        testLeague.setCountry ( "Dummy Country" );

        List<Team> teams = new ArrayList<> ();

        Team team = new Team ( TEAM_A );
        teams.add ( team );

        team = new Team ( TEAM_B );
        teams.add ( team );

        team = new Team ( TEAM_C );
        teams.add ( team );

        team = new Team ( TEAM_D );
        teams.add ( team );

        team = new Team ( TEAM_E );
        teams.add ( team );
        testLeague.setTeams ( teams );
    }

    @Test
    void testGetFirstRound () {
        Round round = schedulerService.getFirstRound ( testLeague.getTeams () );
        checkRound ( round );
    }

    @Test
    void testGetReturnRound () {
        Round round = schedulerService.getFirstRound ( testLeague.getTeams () );
        Round returnRound = schedulerService.getReturnRound ( round );
        checkRound ( returnRound );

        // test team switch between 2 rounds
        List<Stage> firstRoundStages = round.getStages ();
        List<Stage> returnRoundStages = returnRound.getStages ();
        for ( int i = 0; i < firstRoundStages.size (); i++ ) {
            List<Match> frMatches = firstRoundStages.get ( i ).getMatches ();
            List<Match> rrMatches = returnRoundStages.get ( i ).getMatches ();
            for ( int j = 0; j < frMatches.size (); j++ ) {
                assertEquals ( frMatches.get ( j ).getHomeTeam (), rrMatches.get ( j ).getAwayTeam () );
            }

        }
    }

    private void checkRound ( Round round ) {
        // test league of 5 Teams has 5 stages, one team always gets Bye
        assertEquals ( 5, round.getStages ().size () );

        // each stage has 2 matches
        Object [] validStages = round.getStages ().stream ().filter ( s -> s.getMatches ().size () == 2 ).toArray ();
        assertEquals ( 5, validStages.length );

        // there should be a total of 10 matches
        List<Match> totalMatches = round.getStages ().stream ().flatMap ( s -> s.getMatches ().stream () ).collect ( Collectors.toList () );
        assertEquals ( 10, totalMatches.size () );

        Object [] teamAMatches = totalMatches.stream ()
            .filter ( m -> TEAM_A.equals ( m.getHomeTeam ().getName () ) || TEAM_A.equals ( m.getAwayTeam ().getName () ) ).toArray ();
        Object [] teamBMatches = totalMatches.stream ()
            .filter ( m -> TEAM_B.equals ( m.getHomeTeam ().getName () ) || TEAM_B.equals ( m.getAwayTeam ().getName () ) ).toArray ();
        Object [] teamCMatches = totalMatches.stream ()
            .filter ( m -> TEAM_C.equals ( m.getHomeTeam ().getName () ) || TEAM_C.equals ( m.getAwayTeam ().getName () ) ).toArray ();
        Object [] teamDMatches = totalMatches.stream ()
            .filter ( m -> TEAM_D.equals ( m.getHomeTeam ().getName () ) || TEAM_D.equals ( m.getAwayTeam ().getName () ) ).toArray ();
        Object [] teamEMatches = totalMatches.stream ()
            .filter ( m -> TEAM_E.equals ( m.getHomeTeam ().getName () ) || TEAM_E.equals ( m.getAwayTeam ().getName () ) ).toArray ();

        // each team plays 4 matches
        assertEquals ( 4, teamAMatches.length );
        assertEquals ( 4, teamBMatches.length );
        assertEquals ( 4, teamCMatches.length );
        assertEquals ( 4, teamDMatches.length );
        assertEquals ( 4, teamEMatches.length );
    }

    @Test
    void testNoLeague () {
        Exception ex = Assertions.assertThrows ( IllegalArgumentException.class, () -> {
            schedulerService.createSeasonSchedule ( null );
        } );
        assertEquals ( "League cannot be null", ex.getMessage () );
    }

    @Test
    void testEmptyLeague () {
        Exception ex = Assertions.assertThrows ( IllegalArgumentException.class, () -> {
            schedulerService.createSeasonSchedule ( new League () );
        } );
        assertEquals ( "League must have participating Teams", ex.getMessage () );
    }

    @Test
    void testNotEnoughTeamsInLeague () {
        Exception ex = Assertions.assertThrows ( IllegalArgumentException.class, () -> {
            Team team = new Team ();
            team.setName ( "aTeam" );
            League league = new League ();
            league.setTeams ( Arrays.asList ( team ) );
            schedulerService.createSeasonSchedule ( league );
        } );
        assertEquals ( "There must be at least two participating Teams", ex.getMessage () );
    }

}
