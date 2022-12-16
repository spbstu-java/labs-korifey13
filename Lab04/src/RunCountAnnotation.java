import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

// Аннотация с целочисленным параметром.
@Retention(RetentionPolicy.RUNTIME)
public @interface RunCountAnnotation {
    int value() default 1;
}
