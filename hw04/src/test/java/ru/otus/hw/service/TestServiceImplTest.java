package ru.otus.hw.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Student;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TestServiceImplTest {
    @MockitoBean
    private LocalizedIOService localizedIOService;
    @MockitoBean
    private QuestionDao questionDao;

    @Autowired
    private TestServiceImpl testService;

    @Test
    void executeTest_Test() {
        doNothing().when(localizedIOService).printLine(anyString());
        doNothing().when(localizedIOService).printFormattedLine(anyString());
        when(questionDao.findAll()).thenReturn(new ArrayList<>());
        when(localizedIOService.readIntForRange(anyInt(), anyInt(), anyString())).thenReturn(1);

        Assertions.assertDoesNotThrow(() -> testService.executeTestFor(new Student("Ivan", "Ivanov")));
    }
}
