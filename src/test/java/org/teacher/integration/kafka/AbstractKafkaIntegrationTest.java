package org.teacher.integration.kafka;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.awaitility.core.ThrowingRunnable;

import java.util.concurrent.TimeUnit;

public abstract class AbstractKafkaIntegrationTest {

    protected static final int TIMEOUT_SECONDS = 5;

    @BeforeEach
    void setUp() throws Exception {
        // Можно добавить общую инициализацию
    }

    @AfterEach
    void tearDown() throws Exception {
        // Очистка после тестов при необходимости
    }

    /**
     * Awaitility helper для проверки условий.
     */
    protected void waitUntil(ThrowingRunnable  assertion) {
        Awaitility.await()
                .atMost(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .untilAsserted(assertion);
    }
}
