package lukes.pokemonapp;

/**
 * The representation of the player in a Pokémon game. Currently, this is no different from a simple
 * Trainer, but its uses will change in the future.
 * @author Luke Schoeberle 7/11/2016.
 */
public class Player extends Trainer {

    /**
     * Initializes a named Player with an empty team. This acts as the default constructor.
     * @param pName The player's name.
     */
    public Player(String pName) {
        super(pName);
    }
}
