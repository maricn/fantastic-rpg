package com.github.maricn.fantasticrpg.model.exception;

/**
 * Runtime exception thrown when trying access to element out of bounds.
 *
 * @author nikola
 */
public class OutOfBoundsFantasticRpgException extends RuntimeException {
    public OutOfBoundsFantasticRpgException(String message) {
        super(message);
    }
}
