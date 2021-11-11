package de.tpuica.fixtures;

import java.io.PrintStream;

import org.springframework.boot.Banner;
import org.springframework.core.env.Environment;


/**
 * The Banner for this Application.
 */
public class FixturesBanner implements Banner {

    private static final String LS = System.getProperty ( "line.separator" );

    private static final String BANNER = "___________________________________________________________________________" + LS
        + "  ___   _         _                           " + LS
        + " | __| (_) __ __ | |_   _  _   _ _   ___   ___" + LS
        + " | _|  | | \\ \\ / |  _| | || | | '_| / -_) (_-<" + LS
        + " |_|   |_| /_\\_\\  \\__|  \\_,_| |_|   \\___| /__/" + LS
        + "___________________________________________________________________________" + LS;

    /*
     * (non-Javadoc)
     * @see org.springframework.boot.Banner#printBanner(org.springframework.core.env.Environment, java.lang.Class, java.io.PrintStream)
     */
    @Override
    public void printBanner ( Environment environment, Class<?> sourceClass, PrintStream out ) {
        out.println ( BANNER );
    }

}
