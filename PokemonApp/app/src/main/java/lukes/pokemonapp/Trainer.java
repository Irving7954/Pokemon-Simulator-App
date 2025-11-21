package lukes.pokemonapp;

import java.util.ArrayList;

import androidx.annotation.NonNull;

/**
 * The representation of a general trainer. Each trainer has a name and a Pokémon team by default.
 * They must also be either a Player, an EnemyTrainer, or a FriendlyTrainer because this class is
 * abstract. At this point, only Player and EnemyTrainer are in use for the battle simulator, but,
 * in future applications, all three types of Trainers may be used.
 * @author Luke Schoeberle 7/11/2016.
 */
public abstract class Trainer {

    /**
     * The trainer's name. Each trainer must always have a name.
     */
    private final String name;

    /**
     * The trainer's Pokémon team. Each team consists of 0-6 Pokémon.
     */
    private final ArrayList<Pokemon> team;

    /**
     * Constructs a named Trainer with an empty team. This acts as the default constructor for its subclasses.
     * @param tName The Trainer's name.
     */
    protected Trainer(String tName) {
        this(tName, new ArrayList<>());
    }

    /**
     * Constructs a named Trainer with the given team. This can only be used through its subclasses.
     * @param tName The Trainer's name.
     * @param pTeam The Trainer's starting team.
     */
    protected Trainer(String tName, ArrayList<Pokemon> pTeam) {
        name = tName;
        team = pTeam;
    }

    /**
     * Adds the named Pokémon to the team, as long as the team is already not full of six Pokémon.
     * @param pName The Pokémon to be added to the team.
     * @throws IllegalStateException When there are already six Pokémon on the team.
     */
    public void addPokemon(String pName) {
        if(team.size() < 6)
            team.add(new Pokemon(pName));
        else
            throw new IllegalStateException("No Pokémon team can have greater than six Pokémon.");
    }

    /**
     * Returns the trainer's name.
     * @return The trainer's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the Trainer's team.
     * @return The Pokémon in the Trainer's team.
     */
    public ArrayList<Pokemon> getTeam() {
        return team;
    }

    /**
     * Used for debugging purposes. May be eliminated later.
     * @return A String representation of this object.
     */
    @Override //TODO
    @NonNull
    public String toString() {
        StringBuilder s = new StringBuilder(name + " ");
        for(Pokemon p : team) {
            s.append(p.toString());
            s.append("\n");
        }
        return s.toString();
    }

}
