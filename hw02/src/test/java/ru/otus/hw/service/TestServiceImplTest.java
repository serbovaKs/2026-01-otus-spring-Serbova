package ru.otus.hw.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Student;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


public class TestServiceImplTest {
    private final IOService ioService = mock();
    private final QuestionDao questionDao = mock();

    private final TestServiceImpl testService = new TestServiceImpl(ioService, questionDao);

    @Test
    void executeTest_Test() {
        doNothing().when(ioService).printLine(anyString());
        doNothing().when(ioService).printFormattedLine(anyString());
        when(questionDao.findAll()).thenReturn(new ArrayList<>());
        when(ioService.readIntForRange(anyInt(), anyInt(), anyString())).thenReturn(1);

        Assertions.assertDoesNotThrow(() -> testService.executeTestFor(new Student("Ivan", "Ivanov")));
    }
}
