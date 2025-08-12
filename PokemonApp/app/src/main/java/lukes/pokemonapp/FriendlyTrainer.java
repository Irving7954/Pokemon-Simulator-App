package lukes.pokemonapp;

/**
 * The representation of a friendly trainer in a Pokémon game. Currently, this is no different than a simple
 * Trainer, and it is not currently in use. It may be used in later versions of the app.
 * @author Luke Schoeberle 7/11/2016.
 */
@SuppressWarnings("unused")
public class FriendlyTrainer extends Trainer { //this class is irrelevant for now

    /**
     * Initializes a named FriendlyTrainer with an empty team. This acts as the default constructor.
     * @param pName The friendly trainer's name.
     */
    public FriendlyTrainer(String pName) {
        super(pName);
    }
}
