package ee.alekal.bowlingscore.config;

import ee.alekal.bowlingscore.controller.BowlingGameController;
import ee.alekal.bowlingscore.controller.BowlingManagementController;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({BowlingManagementController.class,
        BowlingGameController.class})
public @interface EnableBowlingControllers {
}
