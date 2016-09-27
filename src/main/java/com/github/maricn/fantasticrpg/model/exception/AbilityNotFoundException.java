package com.github.maricn.fantasticrpg.model.exception;

import com.github.maricn.fantasticrpg.model.character.Ability;

/**
 * Exception thrown when some {@link com.github.maricn.fantasticrpg.command.Command} is tried
 * which fails when {@link com.github.maricn.fantasticrpg.model.character.Player} doesn't have
 * an ability needed for thet {@link com.github.maricn.fantasticrpg.command.Command}.
 *
 * @author nikola
 */
public class AbilityNotFoundException extends FantasticRpgException {

    public AbilityNotFoundException(Ability ability) {
        super("Ability not found: " + ability);
    }
}
