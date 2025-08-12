package lukes.pokemonapp;

/**
 * The representation of an enemy trainer in a Pokémon game. Currently, this is no different than a simple
 * Trainer, but its uses will change in the future.
 * @author Luke Schoeberle 7/11/2016.
 */
public class EnemyTrainer extends Trainer {

    /**
     * Initializes a named EnemyTrainer with an empty team. This acts as the default constructor.
     * @param pName The enemy trainer's name.
     */
    public EnemyTrainer(String pName) {
        super(pName);
    }
}
