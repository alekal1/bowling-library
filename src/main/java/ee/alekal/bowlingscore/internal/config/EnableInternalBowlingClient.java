package ee.alekal.bowlingscore.internal.config;

import ee.alekal.bowlingscore.controller.BowlingGameController;
import ee.alekal.bowlingscore.controller.BowlingManagementController;
import ee.alekal.bowlingscore.internal.handler.ApiExceptionHandler;
import ee.alekal.bowlingscore.internal.service.InternalBowlingGameService;
import ee.alekal.bowlingscore.internal.service.InternalBowlingManagementService;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({BowlingManagementController.class,
        BowlingGameController.class,
        InternalBowlingManagementService.class,
        InternalBowlingGameService.class,
        ApiExceptionHandler.class})
public @interface EnableInternalBowlingClient {
}
