package ee.alekal.bowlingscore.internal.annotation;

import ee.alekal.bowlingscore.controller.BowlingController;
import ee.alekal.bowlingscore.internal.service.InternalBowlingService;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({BowlingController.class, InternalBowlingService.class})
public @interface EnableInternalBowlingClient {
}
