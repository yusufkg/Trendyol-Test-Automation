package com.yusuf.qa.support;

import io.cucumber.junit.Cucumber;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import java.time.Instant;
import java.time.Duration;

/** Cucumber scenarios are logged by Hooks; ordinary JUnit tests are logged here. */
public final class JunitResultListener extends RunListener {
    private Instant started;
    private Throwable error;
    private boolean skipped;
    private boolean cucumber(Description description) {
        Class<?> type = description.getTestClass();
        // Cucumber feature/scenario descriptions have no Java test class; Hooks owns them.
        if (type == null) return true;
        RunWith runner = type == null ? null : type.getAnnotation(RunWith.class);
        return runner != null && runner.value().equals(Cucumber.class);
    }
    @Override public void testStarted(Description description) {
        started = Instant.now(); error = null; skipped = false;
        TestContext.RESULT_SCREENSHOT.remove();
    }
    @Override public void testFailure(Failure failure) { error = failure.getException(); }
    @Override public void testAssumptionFailure(Failure failure) { skipped = true; }
    @Override public void testIgnored(Description description) throws Exception {
        testStarted(description); skipped = true; testFinished(description);
    }
    @Override public void testFinished(Description description) throws Exception {
        if (cucumber(description)) return;
        Instant ended = Instant.now();
        String status = skipped ? "SKIPPED" : error == null ? "PASSED" : "FAILED";
        String detail = error == null ? null : FailureCapture.stackTrace(error);
        String screenshot = TestContext.RESULT_SCREENSHOT.get();
        TestContext.RESULT_SCREENSHOT.remove();
        RuntimeException sqlFailure = null;
        String sqlStatus = "FAILED";
        try { sqlStatus = SqlTestLogger.log(description.getDisplayName(), status, started, ended, detail, screenshot, "JUnit"); }
        catch (RuntimeException e) { sqlFailure = e; }
        if (sqlFailure != null) detail = (detail == null ? "" : detail + "\n") + FailureCapture.stackTrace(sqlFailure);
        HtmlDashboard.record(description.getDisplayName(), status, Duration.between(started, ended).toMillis(), screenshot, detail, sqlStatus);
        if (sqlFailure != null) {
            throw sqlFailure;
        }
    }
}
