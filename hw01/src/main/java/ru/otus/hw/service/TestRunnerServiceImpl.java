package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TestRunnerServiceImpl implements TestRunnerService {

    private final TestService testService;

    private final IOService iOService;

    @Override
    public void run() {
        try {
            testService.executeTest();
        } catch (Exception e) {
            iOService.printLine("Error execute test");
        }
    }
}
