package net.programmer.igoodie.streamspawn.exception;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExceptionContainer extends Exception {

    protected List<Exception> exceptions = new ArrayList<>();

    public List<Exception> getExceptions() {
        return Collections.unmodifiableList(exceptions);
    }

    public boolean isEmpty() {
        return exceptions.isEmpty();
    }

    public void reset() {
        exceptions.clear();
    }

    public <T> T tryRunning(ExceptionRunner<T> runner) {
        try {
            return runner.execute();
        } catch (Exception e) {
            this.exceptions.add(e);
            return null;
        }
    }

    @FunctionalInterface
    public interface ExceptionRunner<T> {
        T execute() throws Exception;
    }

}
