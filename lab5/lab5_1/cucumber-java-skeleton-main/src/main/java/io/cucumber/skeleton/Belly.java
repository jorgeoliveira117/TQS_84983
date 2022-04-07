package io.cucumber.skeleton;
import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;
import org.slf4j.Logger;

public class Belly {
    private static final Logger logger = getLogger(lookup().lookupClass());

    public void eat(int cukes) {
        logger.info("Eating " + cukes + " cukes.");
    }
    public void wait(int hours){
        logger.info("Waiting " + hours + " hours.");
    }
    public void growl(){
        logger.info("Growling.");
    }
}
