package name.kmeta;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

/**
 * Created with IntelliJ IDEA.
 * User: Aykut
 * Date: 11/09/13
 * Time: 22:22
 * To change this template use File | Settings | File Templates.
 */
public final class Utils {
    public static <T> Supplier<T> wrap(Callable<T> callable) throws RuntimeException {
        return () -> {
            try {
                return callable.call();
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}
