package de.tpuica.fixtures;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.StringUtils;

import de.tpuica.fixtures.model.League;
import de.tpuica.fixtures.model.Round;
import de.tpuica.fixtures.model.Schedule;
import de.tpuica.fixtures.model.Team;
import de.tpuica.fixtures.service.InitializationService;
import de.tpuica.fixtures.service.SchedulerService;


@ActiveProfiles ( "test" )
@SpringBootTest
class FixturesIntegrationTest {

    @Autowired
    private InitializationService initializationService;

    @Autowired
    private SchedulerService schedulerService;

    @Test
    void contextLoads () {
    }

    @Test
    void testInitializationService () {

        League testLeague = initializationService.getLeague ();

        // test league
        assertNotNull ( testLeague );
        assertTrue ( "League name cannot be null or empty", StringUtils.hasText ( testLeague.getName () ) );
        assertEquals ( "Test League", testLeague.getName () );
        assertTrue ( "League country cannot be null or empty", StringUtils.hasText ( testLeague.getCountry () ) );
        assertEquals ( "Test Country", testLeague.getCountry () );

        // test league's teams
        assertNotNull ( testLeague.getTeams () );
        assertEquals ( 6, testLeague.getTeams ().size () );

        // test league's first team
        Team testTeam = testLeague.getTeams ().get ( 0 );
        assertNotNull ( testTeam );
        assertTrue ( "Team name cannot be null or empty", StringUtils.hasText ( testTeam.getName () ) );
        assertEquals ( "Team A", testTeam.getName () );
        assertTrue ( "Team A establishment date cannot be null or empty", StringUtils.hasText ( testTeam.getEstablishmentDate () ) );
        assertEquals ( "1990", testTeam.getEstablishmentDate () );
    }

    @Test
    void testSchedule () {
        Schedule schedule = schedulerService.createSeasonSchedule ( initializationService.getLeague () );

        assertNotNull ( schedule );

        Round firstRound = schedule.getFirstRound ();
        Round returnRound = schedule.getSecondRound ();

        assertNotNull ( firstRound );
        assertEquals ( 5, firstRound.getStages ().size () );
        assertEquals ( 15, firstRound.getStages ().stream ().flatMap ( s -> s.getMatches ().stream () ).collect ( Collectors.toList () ).size () );

        assertNotNull ( returnRound );
        assertEquals ( 5, returnRound.getStages ().size () );
        assertEquals ( 15, returnRound.getStages ().stream ().flatMap ( s -> s.getMatches ().stream () ).collect ( Collectors.toList () ).size () );

        LocalDate firstRoundStartDate
            = firstRound.getStages ().stream ().flatMap ( s -> s.getMatches ().stream () ).map ( m -> m.getDate () )
                .min ( Comparator.comparing ( Function.identity () ) ).orElse ( null );
        assertEquals ( LocalDate.of ( 2021, 10, 2 ), firstRoundStartDate );

        LocalDate firstRoundEndDate
            = firstRound.getStages ().stream ().flatMap ( s -> s.getMatches ().stream () ).map ( m -> m.getDate () )
                .max ( Comparator.comparing ( Function.identity () ) ).orElse ( null );
        assertEquals ( LocalDate.of ( 2021, 10, 31 ), firstRoundEndDate );

        LocalDate returnRoundStartDate
            = returnRound.getStages ().stream ().flatMap ( s -> s.getMatches ().stream () ).map ( m -> m.getDate () )
                .min ( Comparator.comparing ( Function.identity () ) ).orElse ( null );
        assertEquals ( LocalDate.of ( 2021, 11, 27 ), returnRoundStartDate );

        LocalDate returnRoundEndDate
            = returnRound.getStages ().stream ().flatMap ( s -> s.getMatches ().stream () ).map ( m -> m.getDate () )
                .max ( Comparator.comparing ( Function.identity () ) ).orElse ( null );
        assertEquals ( LocalDate.of ( 2021, 12, 26 ), returnRoundEndDate );
    }

}
