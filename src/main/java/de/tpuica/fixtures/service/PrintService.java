package de.tpuica.fixtures.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import de.tpuica.fixtures.model.League;
import de.tpuica.fixtures.model.Match;
import de.tpuica.fixtures.model.Schedule;
import de.tpuica.fixtures.model.Stage;


/**
 * Print Service.
 */
@Service
public class PrintService {

    private static final String EMPTY_STRING = "";

    private static final String DEFAULT_DATE_FORMAT = "EEEE, dd.MM.yyyy";

    private static final Logger LOGGER = LoggerFactory.getLogger ( PrintService.class );

    @Autowired
    private InitializationService initializationService;

    @Autowired
    private SchedulerService schedulerService;

    @Value ( "${application.ui.dateFormat}" )
    private String dateFormat;

    private DateTimeFormatter dateFormatter;

    @PostConstruct
    private void init () {
        try {
            dateFormatter = DateTimeFormatter.ofPattern ( dateFormat );
        } catch ( IllegalArgumentException e ) {
            LOGGER.warn ( "Invalid printing date format pattern provided ({}), using default", dateFormat );
            dateFormatter = DateTimeFormatter.ofPattern ( DEFAULT_DATE_FORMAT );
        }
    }

    /**
     * Prints the complete game plan of the season to the console.
     */
    public void printFixtures () {

        try {
            League league = initializationService.getLeague ();
            LOGGER.info ( league.toString () );
            LOGGER.info ( EMPTY_STRING );

            Schedule schedule = schedulerService.createSeasonSchedule ( league );
            schedule.getFirstRound ().getStages ().forEach ( this::printStage );

            LOGGER.info ( "SEASON BREAK" );
            LOGGER.info ( EMPTY_STRING );

            schedule.getSecondRound ().getStages ().forEach ( this::printStage );
        } catch ( Exception e ) {
            // LOGGER.error ( "Unexpected error occurred", e );
            LOGGER.error ( "Application exit: unexpected error occurred" );
        }
    }

    private void printStage ( Stage stage ) {
        LOGGER.info ( "STAGE " + stage.getId () );
        stage.getMatches ().stream ().collect ( Collectors.groupingBy ( Match::getDate, LinkedHashMap::new, Collectors.toList () ) )
            .forEach ( this::printStageMatches );
        LOGGER.info ( EMPTY_STRING );
    }

    private void printStageMatches ( LocalDate matchDate, List<Match> matches ) {
        LOGGER.info ( matchDate.format ( dateFormatter ) );
        matches.forEach ( match -> LOGGER.info ( match.toString () ) );
    }

}
