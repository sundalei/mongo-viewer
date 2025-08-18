package com.example.exception;

import com.mongodb.MongoException;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

  /**
   * Handles exceptions related to MongoDB operations, such as connection errors or invalid query
   * syntax (which often throws a JSON parse exception).
   */
  @ExceptionHandler({MongoException.class, org.bson.json.JsonParseException.class})
  protected ResponseEntity<Object> handleMongoErrors(Exception ex) {
    ErrorResponse errorResponse =
        new ErrorResponse(
            LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), "MongoDB Error", ex.getMessage());
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  /** A general fallback handler for any other unexpected exceptions. */
  @ExceptionHandler(Exception.class)
  protected ResponseEntity<Object> handleGenericErrors(Exception ex) {
    ErrorResponse errorResponse =
        new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "An unexpected error occurred",
            ex.getMessage());
    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
