package de.tpuica.fixtures.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import de.tpuica.fixtures.model.League;
import de.tpuica.fixtures.model.Match;
import de.tpuica.fixtures.model.Round;
import de.tpuica.fixtures.model.Schedule;
import de.tpuica.fixtures.model.Stage;
import de.tpuica.fixtures.model.Team;


/**
 * Scheduler Service.
 */
@Service
public class SchedulerService {

    private static final Logger LOGGER = LoggerFactory.getLogger ( SchedulerService.class );

    @Value ( "${application.scheduling.seasonStartDate}" )
    private String seasonStart;

    @Value ( "${application.scheduling.seasonBreak}" )
    private byte seasonBreak;

    @Value ( "${application.scheduling.dateFormat}" )
    private String dateFormat;

    private DateTimeFormatter dateFormatter;

    /**
     * Creates game plan of the season for the given {@link League}.
     *
     * @param league the league, not null
     * @return the game plan
     * @throws IllegalArgumentException if league is null, has insufficient or no participating teams
     */
    public Schedule createSeasonSchedule ( League league ) {
        Assert.notNull ( league, "League cannot be null" );

        // some league / teams validation
        List<Team> leagueTeams = league.getTeams ();
        if ( leagueTeams.isEmpty () ) {
            LOGGER.error ( "League must have participating Teams" );
            throw new IllegalArgumentException ( "League must have participating Teams" );
        } else if ( leagueTeams.size () < 2 ) {
            LOGGER.error ( "There must be at least two participating Teams" );
            throw new IllegalArgumentException ( "There must be at least two participating Teams" );
        }

        // season start date / date format validation
        LocalDate startDate = null;
        try {
            startDate = LocalDate.parse ( seasonStart, getDateFormatter () );
        } catch ( DateTimeParseException e ) {
            LOGGER.error ( "Cannot parse season start date '" + seasonStart + "' with date format '" + dateFormat + "'. Check your configuration" );
            throw new IllegalStateException ( "Season start date does not match expected date format" );
        }

        final Round firstRound = getFirstRound ( leagueTeams );
        computeRoundSchedule ( firstRound, startDate );

        LocalDate firstRoundEndDate
            = firstRound.getStages ().stream ().flatMap ( stage -> stage.getMatches ().stream () ).map ( match -> match.getDate () )
                .max ( Comparator.comparing ( Function.identity () ) ).orElse ( null );
        LocalDate secondRoundStartDate = firstRoundEndDate.minusDays ( 1 ).plusWeeks ( seasonBreak );

        final Round returnRound = getReturnRound ( firstRound );
        computeRoundSchedule ( returnRound, secondRoundStartDate );

        return new Schedule ( firstRound, returnRound );
    }

    private DateTimeFormatter getDateFormatter () {
        if ( dateFormatter == null ) {
            try {
                dateFormatter = DateTimeFormatter.ofPattern ( dateFormat );
            } catch ( IllegalArgumentException e ) {
                LOGGER.error ( "Invalid scheduling date format pattern provided: {}. Check your configuration", dateFormat );
                throw new IllegalStateException ( "Invalid scheduling date format pattern provided" );
            }
        }

        return dateFormatter;
    }

    /**
     * Computes the match schedule for the given {@link Round} by setting the date of each {@link Match} for each {@link Stage}.
     * <p>
     * One {@link Stage} every week: half of the matches on Saturday, the other half on Sunday.
     * </p>
     *
     * @param round the Round, not null
     * @param startDate the start date of the round, not null
     * @return the scheduled Matches
     */
    private void computeRoundSchedule ( Round round, LocalDate startDate ) {
        LocalDate ld = startDate.with ( TemporalAdjusters.nextOrSame ( DayOfWeek.SATURDAY ) );

        for ( Stage stage: round.getStages () ) {

            final LocalDate matchDate = ld;
            List<Match> stageMatches = stage.getMatches ();

            // split stage matches, half on Saturday
            stageMatches.subList ( 0, stageMatches.size () / 2 ).forEach ( match -> match.setDate ( matchDate ) );
            //  the other half on Sunday
            stageMatches.subList ( stageMatches.size () / 2, stageMatches.size () ).forEach ( match -> match.setDate ( matchDate.plusDays ( 1 ) ) );

            if ( LOGGER.isDebugEnabled () ) {
                stageMatches
                    .forEach ( match -> LOGGER.debug ( "{} : {} - {}", match.getDate (), match.getHomeTeam ().getName (), match.getAwayTeam ().getName () ) );
            }

            ld = ld.with ( TemporalAdjusters.next ( DayOfWeek.SATURDAY ) );
        }
    }

    /**
     * Gets first {@link Round} (1st leg) for the given Teams.
     * <p>
     * Circle method applied. Split list of teams in the middle, reverse second half, pair Teams.<br>
     * First Team is fixed. Before next Stage, move last Team on the second position and shift the others to the right. Repeat split, move and shift <i>n-1</i>
     * times, where <i>n</i> is the number of teams. See sample below for first three stages for 6 participating Teams.
     * </p>
     *
     * <pre>
     * <b>Teams</b>
     * A  B  C  D  E  F
     *
     * <b>1st Stage</b>
     * A  B  C  D  E  F
     *
     * A  B  C
     * |  |  |
     * F  E  D
     *
     * <b>2nd Stage</b>
     * A  F  B  C  D  E
     *
     * A  F  B
     * |  |  |
     * E  D  C
     *
     * <b>3rd Stage</b>
     * A  E  F  B  C  D
     *
     * A  E  F
     * |  |  |
     * D  C  B
     * </pre>
     *
     * @param teams the participating Teams, not null
     * @return the initial Round
     */
    public Round getFirstRound ( List<Team> teams ) {
        Round round = new Round ();

        if ( teams.size () % 2 == 1 ) {
            // odd number of teams ->  add Bye team, since one must normally have a name, a nameless Team is used, should be enough for now
            teams.add ( new Team () );
        }

        for ( int i = 1; i < teams.size (); i++ ) {
            round.addStage ( createStage ( i, teams ) );
            teams.add ( 1, teams.get ( teams.size () - 1 ) );
            teams.remove ( teams.size () - 1 );
        }

        return round;
    }

    /**
     * Creates a {@link Stage}.
     *
     * @param stageId the Id of the stage, not null
     * @param teams the participating Teams, not null
     * @return the created Stage
     */
    private Stage createStage ( int stageId, List<Team> teams ) {
        Stage stage = new Stage ( stageId );

        // split list in the middle
        List<Team> h1 = teams.subList ( 0, teams.size () / 2 );
        List<Team> h2 = teams.subList ( teams.size () / 2, teams.size () );

        // create new list for second half, we need to reverse it; when done directly on h2, the original Team list is broken
        List<Team> h2rev = h2.stream ().collect ( Collectors.toList () );
        Collections.reverse ( h2rev );

        // pair Teams by index --> Match ( h1.(i) - h2rev.(i) )
        IntStream.range ( 0, h1.size () ).forEach ( i -> addMatch ( stage, h1.get ( i ), h2rev.get ( i ) ) );

        return stage;
    }

    /**
     * Creates a {@link Match} between 2 given teams and adds it to the provided {@link Stage}.
     *
     * @param stage the stage, not null
     * @param firstTeam the first participant team, not null
     * @param secondTeam the second participant team, not null
     */
    private void addMatch ( Stage stage, Team firstTeam, Team secondTeam ) {
        if ( firstTeam.getName () != null && secondTeam.getName () != null ) { // ignore teams getting Bye
            Match match;

            // switch sides after each stage
            if ( stage.getId () % 2 == 1 ) {
                match = new Match ( stage.getId (), firstTeam, secondTeam );
            } else {
                match = new Match ( stage.getId (), secondTeam, firstTeam );
            }

            if ( LOGGER.isDebugEnabled () ) {
                LOGGER.debug ( "{} : {} - {}", stage.getId (), match.getHomeTeam ().getName (), match.getAwayTeam ().getName () );
            }
            stage.addMatch ( match );
        }
    }

    /**
     * Gets return {@link Round} (2nd leg) for the given initial Round. Keep the same order of matches, only switch Home and Away Team.
     *
     * @param round the initial Round, not null
     *
     * @return the return Round
     */
    public Round getReturnRound ( Round round ) {
        Round returnRound = new Round ();

        for ( Stage stage: round.getStages () ) {
            Stage rStage = new Stage ( stage.getId () );
            for ( Match match: stage.getMatches () ) {
                rStage.addMatch ( new Match ( stage.getId (), match.getAwayTeam (), match.getHomeTeam () ) );
            }
            returnRound.addStage ( rStage );
        }

        return returnRound;
    }

}
