package ru.otus.hw.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.domain.Question;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class CsvQuestionDaoTest {

    private final AppProperties appProperties = mock();

    private final CsvQuestionDao questionDao = new CsvQuestionDao(appProperties);

    String TEST_QUESTIONS_1 = "Is there life on Mars?";
    String TEST_QUESTIONS_2 = "How should resources be loaded form jar in Java?";

    @Test
    void findAll_Test() {
        when(appProperties.getTestFileName()).thenReturn("questions.csv");

        List<Question> list = questionDao.findAll();
        Assertions.assertEquals(
                list.stream().map(Question::text).toList().subList(0,2),
                List.of(TEST_QUESTIONS_1, TEST_QUESTIONS_2)
        );
    }
}
