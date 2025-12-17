package lukes.pokemonapp;

import java.util.ArrayList;
import java.util.Arrays;
import android.os.Parcelable;
import android.os.Parcel;

import androidx.annotation.NonNull;

/**
 * The representation of a Pokémon. This contains the necessary information about the Pokémon,
 * including its name, type, statuses, ability, level, current currStats, starting currStats, and moves.
 * Its functionality is still being tweaked (including gender, accuracy, evasion, and others). This also
 * contains lists of all of the base current stats of known Pokémon (9 so far).
 * @author Luke Schoeberle 7/11/2016.
 */
@SuppressWarnings("unused")
public class Pokemon implements Parcelable {
    //add gender later //TODO
    //add accuracy/evasion eventually //TODO
    //add experience in due time //TODO

    /**
     * The Pokémon's name. This will be used as the main identifier for the Pokémon.
     */
    private String name;

    /**
     * The Pokémon's type. Types are separated by slashes for multi-typed Pokémon.
     */
    private String type;

    /**
     * The volatile status of a Pokémon, which means that the condition will disappear upon switching out.
     * This includes both all statuses other than the non-volatile ones. You can have more than one volatile
     * Status at a time, which will be separated by slashes.
     */
    private String volStatus;

    /**
     * The non-volatile status of a Pokémon, which means that the condition will remain upon switching out.
     * For reference, the current non-volatile statuses are None, Burned, Poisoned, Badly Poisoned,
     * Frozen, Sleeping, Resting, and Paralyzed. You can only have one non-volatile status at a time,
     * And these conditions generally cannot be overridden by other non-volatile statuses except by Rest.
     */
    private String nonVolStatus;

    /**
     * A Pokémon's ability. This variable includes only the name for the ability.
     */
    private String ability;

    /**
     * The Pokémon's level. This can range from 1-100.
     */
    private int level;

    /**
     * The list of stat levels of each stat. Each level can range from 0 (equivalent of -6) to 12 (equivalent of +6).
     */
    private Integer[] statStages;

    /**
     * The list of initial stats, which include the six base stats and accuracy and evasion.
     * These cannot be influenced by stat changes.
     */
    private Integer[] initStats;

    /**
     * The Pokémon's set of moves. A Pokémon can have from 1-4 moves. If the Pokémon has less than four
     * moves, the empty move slots will always come at the end (index 3). Each Pokémon must have at least one move.
     */
    private ArrayList<Move> moves;

    /**
     * This Pokémon's state of invulnerability. 0 indicates no invulnerability, and non-zero codes
     * indicate a state of semi-invulnerability (like Protect or Fly).
     */
    private int invulnerableCode;

    /**
     * This Pokémon's state of critical chance. There are essentially four states: 0 (1/16), 1 (1/8), 2 (1/2), and
     * 3 or more (1/1). Each state is caused by high-critical moves like Leaf Blade, Focus Energy,
     * or items like the Stick, and the effects can stack.
     */
    private int criticalState;

    /**
     * The max HP of a Pokémon. This is used to calculate HP percentages and to ensure that the HP
     * never goes above the max HP.
     */
    private int maxHP;

    /**
     * Sets the Pokémon to its preset configuration based on its name.
     * @param pName The name of the Pokémon.
     * @throws IllegalArgumentException When pName does not match a name in this method.
     */
    public Pokemon(String pName){
        setPokemon(pName);
    }

    /**
     * Handles Parcel creation, which sets the variables from the parcel in the same order
     * As the other constructor. For reference, it is important to match parcel I/O order,
     * So this is a critical detail that may need to be updated when this class is changed.
     * @param in The incoming Pokémon parcel.
     */
    protected Pokemon(Parcel in){
        // Initialize some variables for later
        Class<Integer> integerClass = Integer.class;
        ClassLoader integerClassLoader = integerClass.getClassLoader();
        Class<Move> moveClass = Move.class;

        // Set the variables to the inputs
        setName(in.readString());
        setLevel(in.readInt());
        setVolStatus(in.readString());
        setNonVolStatus(in.readString());
        setInvulnCode(in.readInt());
        setStatStages(in.readArray(integerClassLoader, integerClass));
        setCritState(in.readInt());
        setType(in.readString());
        setAbility(in.readString());
        setStats(in.readArray(integerClassLoader, integerClass));
        setMaxHP(in.readInt());
        setMoves(in.readArrayList(moveClass.getClassLoader(), moveClass));
    }

    /**
     * Handles Parcel output, which sends the data from the class to the parcel in the same order
     * As the main constructor. For reference, it is important to match parcel I/O order,
     * So this is a critical detail that may need to be updated when this class is changed.
     * @param out The outgoing Pokémon parcel.
     * @param flags Extra options for customization, which is currently unused.
     */
    @Override
    public void writeToParcel(Parcel out, int flags) {
        // Set the outputs based on the class variables
        out.writeString(name);
        out.writeInt(level);
        out.writeString(volStatus);
        out.writeString(nonVolStatus);
        out.writeInt(invulnerableCode);
        out.writeArray(statStages);
        out.writeInt(criticalState);
        out.writeString(type);
        out.writeString(ability);
        out.writeArray(initStats);
        out.writeInt(maxHP);
        out.writeList(moves);
    }

    /**
     * Indicates if there are special file contents, such as file descriptors.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Pokemon> CREATOR = new Creator<>() {
        public Pokemon createFromParcel(Parcel in) {
            return new Pokemon(in);
        }

        public Pokemon[] newArray(int size) {
            return new Pokemon[size];
        }
    };

    /**
     * Presets this Pokémon to its stats based on the data in this method. This is private because it
     * should never be accessed outside of this class.
     * @param pName The Pokémon's name.
     * @throws IllegalArgumentException When pName does not match a name in this method.
     */
    private void setPokemon(String pName) {
        setName(pName);
        setLevel(50); // Use a temporary level of 50 for the simulator
        setVolStatus("None");
        setNonVolStatus("None");  /* can be("None","Confused","Seeded" ... many more later) */
        setInvulnCode(0);
        setStatStages(new Integer[] {6, 6, 6, 6, 6, 6, 6, 6});
        setCritState(0);

        //database of stats (this is for the simulator only)
        switch(pName) {
            case "Bulbasaur":
                setType("Grass/Poison");
                setAbility("Overgrow"); // Consider hidden abilities/other abilities later //TODO
                setStats(new Integer[] {105, 49, 49, 65, 55, 45, 100, 100});
                setMaxHP(105);
                setMoves(new ArrayList<>(Arrays.asList(new StatusMove("Leech Seed"), new StatusMove("Synthesis"),
                                    new SpecialMove("Sludge Bomb"), new SpecialMove("Giga Drain"))));
                break;
            case "Charmander":
                setType("Fire");
                setAbility("Blaze");
                setStats(new Integer[] {99, 52, 43, 60, 50, 65, 100, 100});
                setMaxHP(99);
                setMoves(new ArrayList<>(Arrays.asList(new SpecialMove("Flamethrower"), new PhysicalMove("Dig"),
                        new PhysicalMove("Dragon Claw"), new StatusMove("Will-O-Wisp"))));
                break;
            case "Squirtle":
                setType("Water");
                setAbility("Torrent");
                setStats(new Integer[] {104, 48, 65, 50, 64, 43, 100, 100});
                setMaxHP(104);
                setMoves(new ArrayList<>(Arrays.asList(new SpecialMove("Scald"), new SpecialMove("Ice Beam"),
                        new StatusMove("Substitute"), new StatusMove("Toxic"))));
                break;
            case "Voltorb":
                setType("Electric");
                setAbility("Static");
                setStats(new Integer[] {100, 30, 50, 55, 55, 100, 100, 100});
                setMaxHP(100);
                setMoves(new ArrayList<>(Arrays.asList(new SpecialMove("Discharge"), new StatusMove("Magnet Rise"),
                        new SpecialMove("Volt Switch"), new StatusMove("Thunder Wave"))));
                break;
            case "Chikorita":
                setType("Grass");
                setAbility("Overgrow");
                setStats(new Integer[] {105, 49, 65, 49, 65, 45, 100, 100});
                setMaxHP(105);
                setMoves(new ArrayList<>(Arrays.asList(new StatusMove("Light Screen"), new StatusMove("Reflect"),
                        new SpecialMove("Energy Ball"), new StatusMove("Magic Coat"))));
                break;
            case "Cyndaquil":
                setType("Fire");
                setAbility("Blaze");
                setStats(new Integer[] {99, 52, 43, 60, 50, 65, 100, 100});
                setMaxHP(99);
                setMoves(new ArrayList<>(Arrays.asList(new SpecialMove("Eruption"), new SpecialMove("Lava Plume"),
                        new StatusMove("Sunny Day"), new SpecialMove("Solar Beam"))));
                break;
            case "Totodile":
                setType("Water");
                setAbility("Torrent");
                setStats(new Integer[] {110, 65, 64, 44, 48, 43, 100, 100});
                setMaxHP(110);
                setMoves(new ArrayList<>(Arrays.asList(new PhysicalMove("Aqua Tail"), new PhysicalMove("Superpower"),
                        new StatusMove("Screech"), new PhysicalMove("Ice Punch"))));
                break;
            case "Wooper":
                setType("Water/Ground");
                setAbility("Water Absorb");
                setStats(new Integer[] {115, 45, 45, 25, 25, 15, 100, 100});
                setMaxHP(115);
                setMoves(new ArrayList<>(Arrays.asList(new StatusMove("Amnesia"), new PhysicalMove("Earthquake"),
                        new StatusMove("Yawn"), new StatusMove("Rest"))));
                break;
            case "Snubbull":
                setType("Fairy");
                setAbility("Intimidate");
                setStats(new Integer[] {120, 80, 50, 40, 40, 30, 100, 100});
                setMaxHP(120);
                setMoves(new ArrayList<>(Arrays.asList(new PhysicalMove("Play Rough"), new StatusMove("Roar"),
                        new PhysicalMove("Earthquake"), new PhysicalMove("Payback"))));
                break;
            case "Treecko":
                setType("Grass");
                setAbility("Overgrow");
                setStats(new Integer[] {100, 45, 35, 65, 55, 70, 100, 100});
                setMaxHP(100);
                setMoves(new ArrayList<>(Arrays.asList(new PhysicalMove("Crunch"), new StatusMove("Swords Dance"),
                        new PhysicalMove("Seed Bomb"), new PhysicalMove("Drain Punch"))));
                break;
            case "Torchic":
                setType("Fire");
                setAbility("Blaze");
                setStats(new Integer[] {105, 60, 40, 70, 50, 45, 100, 100});
                setMaxHP(105);
                setMoves(new ArrayList<>(Arrays.asList(new PhysicalMove("Flare Blitz"), new StatusMove("Protect"),
                        new PhysicalMove("Aerial Ace"), new PhysicalMove("Low Kick"))));
                break;
            case "Mudkip":
                setType("Water");
                setAbility("Torrent");
                setStats(new Integer[] {110, 70, 50, 50, 50, 40, 100, 100});
                setMaxHP(110);
                setMoves(new ArrayList<>(Arrays.asList(new PhysicalMove("Liquidation"), new StatusMove("Rain Dance"),
                        new PhysicalMove("Rock Slide"), new SpecialMove("Earth Power"))));
                break;
            default:
                throw new IllegalArgumentException(pName + " is currently not a valid Pokémon in this simulator!");
        }
    } //add more pokemon once the game works //TODO

    /**
     * Increments the level of a Pokémon (to a maximum of 100).
     * This would be used during a future Pokémon adventure game.
     */
    public void levelUp() {
        if(level < 100) {
            setLevel(level + 1);
        }
    }

    /**
     * Sets this Pokémon's level to the new level.
     * @param nLevel The new level.
     */
    public void setLevel(int nLevel) {
        level = nLevel;
    }

    /**
     * Sets this Pokémon's name to the new name.
     * @param nName The new name.
     */
    public void setName(String nName) {
        name = nName;
    }

    /**
     * Sets this Pokémon's ability to the new ability.
     * @param nAbility The new ability.
     */
    public void setAbility(String nAbility) {
        ability = nAbility;
    }

    /**
     * Set this Pokémon's invulnerable code to the specified value.
     * @param code This Pokémon's new invulnerable code.
     */
    public void setInvulnCode(int code) {
        invulnerableCode = code;                    /*
                                                     0: no invulnerability
                                                     1: Protect, Detect
                                                     2: Substitute
                                                     3: Fly
                                                     4: Sky Drop
                                                     5: Bounce
                                                     6: Dig
                                                     7: Shadow Force, Phantom Force
                                                     8: Crafty Shield
                                                     9: Magic Coat //maybe others
                                                    */
    }

    /**
     * Checks to see if the Pokémon has the listed move and returns true if it has the move. It returns
     * false otherwise.
     * @param move The move to be checked.
     * @return True if the Pokémon has this move, false otherwise.
     */
    public boolean hasMove(Move move) {
        for(Move m : moves)
            if(m.equals(move))
                return true;
        return false;
    }

    /**
     * Precondition: This Pokémon does not have the named move.
     * Adds the named move to the Pokémon's move list and returns true if the move can be added without
     * replacing any other moves. If the Pokémon is full on moves, it
     * returns false and does not change any moves.
     * @param move The move to be added, if possible.
     * @return True if the Pokémon is not full on moves, false otherwise.
     */
    public boolean addMove(Move move) {
        if(hasMove(move))  return false; //
        if(moves.size() < 4) {
            moves.add(move);
            return true;
        }
        return false;
    }

    /**
     * Copies the provided ArrayList to this Pokémon's moves ArrayList array, which is used mainly
     * for initialization and potentially for changing the move list via rare moves like Transform.
     * @param nMoves The new move array.
     */
    public void setMoves(ArrayList<Move> nMoves) {
        moves = nMoves;
    }

    /**
     * Transforms the Pokémon to the named Pokémon. This will be called when the ability Imposter
     * takes effect or the move Transform is used.
     * @param pName The Pokémon to be transformed into.
     */
    public void transform(String pName) {
        setPokemon(pName);
        //will need more detail //TODO
    }

    /**
     * Sets this Pokémon's volatile status to s.
     * @param s The new volatile status.
     */
    public void setVolStatus(String s) {
        volStatus = s;
    } //this will require more detail later //TODO

    /**
     * Sets this Pokémon's type to t.
     * @param t The new type.
     */
    public void setType(String t) {
        type = t;
    }

    /**
     * Sets this Pokémon's non-volatile status to nvs.
     * @param nvs The new non-volatile status.
     */
    public void setNonVolStatus(String nvs) {
        nonVolStatus = nvs;
    } //this will require more detail later //TODO

    /**
     * Sets the stat stage at index to value. This changes one specific stat stage, and the indexes from 0-5
     * are HP, Attack, Defense, Special Attack, Special Defense, Speed, Accuracy, and Evasion.
     * @param index The specific stat to be changed, as per the indexes described above.
     * @param value The new value of the stat.
     */
    public void setStatStage(int index, int value) {
        statStages[index] = value; // Use this later //TODO
    }

    /**
     * Copies the stat stages from the provided array to this Pokémon's stat stage array, which is used mainly
     * for initialization and changing many different stat stages. For reference, the values in the array
     * from index 0-7 are HP, Attack, Defense, Special Attack, Special Defense, Speed, Accuracy, and Evasion.
     * @param newStatStages The new array that represents all of the stat stages.
     */
    public void setStatStages(Integer[] newStatStages) {
        statStages = newStatStages;
    }

    /**
     * Sets the stat at index to value. This changes that stat.
     * @param value The new value of the stat.
     * @param index The index of the stat.
     */
    public void setStats(int value, int index) {
        initStats[index] = value;
    } //more detail later //TODO

    /**
     * Copies the stats from the provided array to this Pokémon's stats array, which is used mainly
     * for initialization and changing the base stats for rare moves like Transform. For reference,
     * the values in the array from index 0-7 are HP, Attack, Defense, Special Attack, Special Defense,
     * Speed, Accuracy, and Evasion, respectively.
     * @param newStats The new array that represents all of the stats.
     */
    public void setStats(Integer[] newStats) {
        initStats = newStats;
    }

    /**
     * Adds the specified value to the critical state, as long as the critical state does not drop below zero.
     * @param state The critical state to be added.
     */
    public void addToCritState(int state) {
        if(criticalState + state < 0)
            throw new IllegalArgumentException("A Pokémon's critical state can never be negative!");
        criticalState += state;
    }

    /**
     * Sets the critical state to the specified value, assuming that it is positive, as long as the critical state does not drop below zero.
     * @param state The critical state to be added.
     */
    public void setCritState(int state) {
        if(state < 0)
            throw new IllegalArgumentException("A Pokémon's critical state can never be negative!");
        criticalState = state;
    }

    /**
     * Returns this Pokémon's max HP.
     * @return This Pokémon's max HP.
     */
    public int getMaxHP() {
        return maxHP;
    }

    /**
     * Sets this Pokémon's maxHP to the specified value.
     * @param mHP The new maxHP value.
     */
    public void setMaxHP(int mHP) {
        maxHP = mHP;
    }

    /**
     * Returns this Pokémon's invulnerability code.
     * @return This Pokémon's invulnerability code.
     */
    public int getInvulnCode() {
        return invulnerableCode;
    }

    /**
     * Returns the Pokemon's name.
     * @return the Pokemon's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the Pokemon's type.
     * @return the Pokemon's type.
     */
    public String getType() {
        return type;
    }

    /**
     * Returns the Pokemon's volatile status.
     * @return the Pokemon's volatile status.
     */
    public String getVolStatus() {
        return volStatus;
    }

    /**
     * Returns the Pokemon's level.
     * @return the Pokemon's level.
     */
    public int getLevel() {
        return level;
    }

    /**
     * Returns the Pokemon's moves.
     * @return the Pokemon's moves.
     */
    public ArrayList<Move> getMoves() {
        return moves;
    }

    /**
     * Returns the Pokemon's current stats.
     * @return the Pokemon's current stats.
     */
    public Integer[] getStatStages() {
        return statStages;
    }

    /**
     * Returns the Pokemon's ability.
     * @return the Pokemon's ability.
     */
    public String getAbility() {
        return ability;
    }

    /**
     * Returns the Pokemon's initial stats.
     * @return the Pokemon's initial stats.
     */
    public Integer[] getInitStats() {
        return initStats;
    }

    /**
     * Returns the Pokemon's non-volatile status.
     * @return the Pokemon's non-volatile status
     */
    public String getNonVolStatus() {
        return nonVolStatus;
    }

    /**
     * Returns this Pokémon's critical state.
     * @return This Pokémon's critical state.
     */
    public int getCritState() {
        return criticalState;
    }

    /**
     * Used for debugging purposes.
     * @return A String representation of this object.
     */
    @Override
    @NonNull
    public String toString() {
        StringBuilder result = new StringBuilder(name + " " + level + " " + volStatus + " "
                                                    + type + " " + ability + " ");
        for(int i : initStats) {
            result.append(i);
            result.append(" ");
        }

        for (Move m : moves) {
            result.append(m);
            result.append(" ");
        }
        return result.toString();
    }
}