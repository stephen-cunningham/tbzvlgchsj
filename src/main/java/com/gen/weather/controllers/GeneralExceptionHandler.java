package com.gen.weather.controllers;

import com.gen.weather.exceptions.NotFoundException;
import com.gen.weather.models.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Optional;

@RestControllerAdvice
public class GeneralExceptionHandler {
  public static final String BAD_REQUEST_CODE = "bad.request";
  public static final String NOT_FOUND_CODE = "not.found";
  private static final Logger LOGGER = LoggerFactory.getLogger(GeneralExceptionHandler.class);

  @ExceptionHandler({HttpMessageNotReadableException.class, DataIntegrityViolationException.class, ConversionFailedException.class, IllegalArgumentException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleBadRequests(Exception ex) {
    return new ErrorResponse(
            BAD_REQUEST_CODE, Optional.ofNullable(ex.getMessage()).orElse("The supplied input is invalid."));
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ErrorResponse handleResourceNotFound() {
    return new ErrorResponse(NOT_FOUND_CODE, "The resource was not found");
  }

  @ExceptionHandler
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ErrorResponse handleCustomNotFound(NotFoundException ex) {
    return new ErrorResponse(NOT_FOUND_CODE, ex.getMessage());
  }

  @ExceptionHandler
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorResponse handleUnexpectedErrors(Exception ex) {
    LOGGER.error("An unexpected error has occurred. See the following for details:", ex);

    return new ErrorResponse(
            "unexpected.error", "An unexpected error has occurred. Please contact an administrator.");
  }
}
