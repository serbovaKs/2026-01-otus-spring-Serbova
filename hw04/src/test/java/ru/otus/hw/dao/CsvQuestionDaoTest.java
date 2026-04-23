package ru.otus.hw.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.domain.Question;
import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest
public class CsvQuestionDaoTest {

    @MockitoBean
    private AppProperties appProperties;

    @Autowired
    private CsvQuestionDao questionDao;

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
