package examples.util;

public final class ExceptionUtils {
    private ExceptionUtils() {

    }

    public static RuntimeException launderThrowable(Throwable throwable) {
        if (throwable instanceof RuntimeException) {
            return (RuntimeException) throwable;
        }
        if (throwable instanceof Error) {
            throw (Error) throwable;
        }
        throw new IllegalStateException("Not unchecked", throwable);
    }
}
