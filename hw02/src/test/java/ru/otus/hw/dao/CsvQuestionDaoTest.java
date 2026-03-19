package ru.otus.hw.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.PropertySource;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.domain.Question;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@PropertySource("classpath:application.properties")
public class CsvQuestionDaoTest {



    private final AppProperties appProperties = mock();

    private final CsvQuestionDao questionDao = new CsvQuestionDao(appProperties);

    private static final String TEST_QUESTIONS_1 = "Is there life on Mars?";
    private static final String TEST_QUESTIONS_2 = "How should resources be loaded form jar in Java?";

    @Test
    void findAll_Test() {
        when(appProperties.getTestFileName()).thenReturn("questions.csv");

        List<Question> list = questionDao.findAll();
        Assertions.assertEquals(
                list.stream().map(Question::text).toList(),
                List.of(TEST_QUESTIONS_1, TEST_QUESTIONS_2)
        );
    }
}
