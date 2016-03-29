package android.net;

import io.github.francoiscampbell.StubClassNotShadowedError;

/**
 * Created by francois on 16-03-15.
 */
public abstract class NetworkTemplate {
    //repeat this string in all classes because some but not all may be shadowed by the system

    //These implementations don't matter, the actual ones executed will be the system's
    public static NetworkTemplate buildTemplateMobileAll(String subscriberId) {
        throw new StubClassNotShadowedError();
    }
}
