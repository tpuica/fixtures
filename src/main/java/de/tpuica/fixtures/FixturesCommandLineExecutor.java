package de.tpuica.fixtures;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import de.tpuica.fixtures.service.PrintService;


/**
 * The application command line runner.
 */
@Profile ( { "!test" } )
@Component
public class FixturesCommandLineExecutor implements CommandLineRunner {

    @Autowired
    private PrintService printService;

    /*
     * (non-Javadoc)
     * @see org.springframework.boot.ApplicationRunner#run(org.springframework.boot.ApplicationArguments)
     */
    @Override
    public void run ( String... args ) throws Exception {
        printService.printFixtures ();
    }

}
