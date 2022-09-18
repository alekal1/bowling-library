package ee.alekal.bowlingscore.dto.api;

import lombok.Data;

@Data
public class ErrorResponse {
    String classifier;
    String message;

    public ErrorResponse(String classifier, String message) {
        this.classifier = classifier;
        this.message = message;
    }
}
