package at.ac.tuwien.sepm.ws16.qse01.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * A simple service answering all your questions.
 *
 * @author Dominik Moser
 */
@ShoutingService
public class SimpleAnswerService implements AnswerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleAnswerService.class);

    @Override
    public String getTheAnswer() {
        LOGGER.info("Calculating the answer");
        return "42";
    }

}
