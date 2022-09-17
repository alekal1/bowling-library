package ee.alekal.bowlingscore.internal.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class InternalUtils {

    public static Integer convertStringScore(String score) {
        return Integer.valueOf(score);
    }
}
