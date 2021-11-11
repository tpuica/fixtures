package de.tpuica.fixtures.service;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.tpuica.fixtures.model.League;


/**
 * Initialization service.
 */
@Service
public class InitializationService {

    private static final Logger LOGGER = LoggerFactory.getLogger ( InitializationService.class );

    @Autowired
    private ObjectMapper objectMapper;

    @Value ( "${application.resources.league}" )
    private Resource leagueResource;

    /**
     * Get the {@link League} from application resources.
     *
     * @return the league
     * @throws IllegalStateException if any error occurs during resource processing
     */
    public League getLeague () {
        try {
            return objectMapper.readValue ( leagueResource.getInputStream (), League.class );
        } catch ( FileNotFoundException e ) {
            LOGGER.error ( "Resource does not exist: {}", leagueResource );
            throw new IllegalStateException ( "Soccer teams json file cannot be found", e );
        } catch ( IOException e ) {
            LOGGER.error ( "An error occurred while reading league teams. Is json file valid?" );
            throw new IllegalStateException ( "Invalid soccer teams json file", e );
        }
    }

}
