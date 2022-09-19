package ee.alekal.bowlingscore.dto.api;

import lombok.Data;

@Data
public class ErrorResponse {
    private final String classifier;
    private final String message;
}
