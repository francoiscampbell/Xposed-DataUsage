package io.github.francoiscampbell;

/**
 * Created by francois on 16-03-29.
 */
public class StubClassNotShadowedError extends IllegalAccessError {
    private static final String DEFAULT_ERROR_MSG = "These implementations are supposed to be replaced by the system's. Do they exist in this version of Android?";

    public StubClassNotShadowedError() {
        super(DEFAULT_ERROR_MSG);
    }
}
