package com.yusuf.qa.support;

import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.TestStepFinished;
import java.io.PrintWriter;
import java.io.StringWriter;

/** Receives the real step exception before the scenario's After hook runs. */
public final class FailureCapture implements ConcurrentEventListener {
    public static final ThreadLocal<String> ERROR = new ThreadLocal<>();
    @Override public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestStepFinished.class, event -> {
            Throwable failure = event.getResult().getError();
            if (failure != null && ERROR.get() == null) ERROR.set(stackTrace(failure));
        });
    }
    public static String stackTrace(Throwable failure) {
        StringWriter text = new StringWriter();
        failure.printStackTrace(new PrintWriter(text));
        return text.toString();
    }
}
