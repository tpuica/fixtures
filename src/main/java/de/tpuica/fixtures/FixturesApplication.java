package de.tpuica.fixtures;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;


/**
 * The Fixtures Application.
 */
@SpringBootApplication
public class FixturesApplication {

    /**
     * The main.
     *
     * @param args external arguments
     */
    public static void main ( String [] args ) {
        new SpringApplicationBuilder ( FixturesApplication.class ).banner ( new FixturesBanner () ).bannerMode ( Banner.Mode.CONSOLE ).run ( args );
    }

}
