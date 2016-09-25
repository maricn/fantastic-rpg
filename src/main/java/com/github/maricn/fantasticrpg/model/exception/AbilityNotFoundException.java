package com.github.maricn.fantasticrpg.model.exception;

import com.github.maricn.fantasticrpg.model.character.Ability;

/**
 *
 * @author nikola
 */
public class AbilityNotFoundException extends FantasticRpgException {

    public AbilityNotFoundException(Ability ability) {
        super("Ability not found: " + ability);
    }
}
