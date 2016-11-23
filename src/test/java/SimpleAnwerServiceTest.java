import at.ac.tuwien.sepm.ws16.qse01.service.AnswerService;
import at.ac.tuwien.sepm.ws16.qse01.service.SimpleAnswerService;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class SimpleAnwerServiceTest {

    private AnswerService answerService;

    @Before
    public void before() {
        answerService = new SimpleAnswerService();
    }

    @Test
    public void testIfTheSimpleAnswerServiceReturnsTheCorrectAnswer() {
        assertThat(answerService.getTheAnswer(), is("42"));
    }

}
