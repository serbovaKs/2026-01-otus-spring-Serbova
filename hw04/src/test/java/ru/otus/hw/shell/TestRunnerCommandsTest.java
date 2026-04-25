package ru.otus.hw.shell;

import ru.otus.hw.service.TestRunnerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.InputProvider;
import org.springframework.shell.ResultHandlerService;
import org.springframework.shell.Shell;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest()
public class TestRunnerCommandsTest {
    private static final String COMMAND_TEST = "test";
    private static final String COMMAND_RUN = "run";

    private InputProvider inputProvider;

    private ArgumentCaptor<Object> argumentCaptor;

    @MockitoBean
    private TestRunnerService testRunnerService;

    @MockitoSpyBean
    private ResultHandlerService resultHandlerService;

    @Autowired
    private Shell shell;

    @BeforeEach
    void setUp() {
        inputProvider = mock(InputProvider.class);
        argumentCaptor = ArgumentCaptor.forClass(Object.class);
    }

    @DisplayName("Успешное выполнение тестирования при вводе допустимых комманд")
    @Test
    void runTest() throws Exception {
        when(inputProvider.readInput())
                .thenReturn(() -> COMMAND_RUN)
                .thenReturn(() -> COMMAND_TEST)
                .thenReturn(null);

        shell.run(inputProvider);

        verify(resultHandlerService, times(2)).handle(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue());

        verify(testRunnerService, times(2)).run();
    }
}
