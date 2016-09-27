package com.github.maricn.fantasticrpg.model.exception;

/**
 * Generic exception class used as a base for all checked exceptions thrown in game.
 *
 * @author nikola
 */
public class FantasticRpgException extends Throwable {
    public FantasticRpgException(String message) {
        super(message);
    }
}
