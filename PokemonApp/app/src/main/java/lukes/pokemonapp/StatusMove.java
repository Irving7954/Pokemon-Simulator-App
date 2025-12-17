package lukes.pokemonapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * The representation of a status move. Each StatusMove has (in addition to the characteristics of a
 * Move) a set of effects, which are stored in a String. More moves will be added to this database
 * in the future.
 * @author Luke Schoeberle 7/11/2016.
 */
public class StatusMove extends Move {

    /**
     * The effects of this status move. The keywords in this small paragraph will determine what the move
     * does, for the most part.
     */
    private String effects;

    /**
     * Sets the move to its preset state based on its name. This is the only constructor.
     * @param mName The name of the move.
     */
    public StatusMove(String mName) {
        super(mName);
    }

    /**
     * Handles Parcel creation, which sets the variables from the parcel in the same order
     * As the other constructor. For reference, it is important to match parcel I/O order,
     * So this is a critical detail that may need to be updated when this class is changed.
     * @param in The incoming StatusMove parcel.
     */
    protected StatusMove(Parcel in) {
        super(in);

        setEffects(in.readString());
    }

    /**
     * Handles Parcel output, which sends the data from the class to the parcel in the same order
     * As the main constructor. For reference, it is important to match parcel I/O order,
     * So this is a critical detail that may need to be updated when this class is changed.
     * @param out The outgoing StatusMove parcel.
     * @param flags Extra options for customization, which is currently unused.
     */
    @Override
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);

        // Set the outputs based on the class variables
        out.writeString(effects);
    }

    /**
     * Indicates if there are special file contents, such as file descriptors.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<StatusMove> CREATOR = new Creator<>() {
        public StatusMove createFromParcel(Parcel in) {
            return new StatusMove(in);
        }

        public StatusMove[] newArray(int size) {
            return new StatusMove[size];
        }
    };

    /**
     * Checks whether or not the move is an attacking move, which is mainly used for polymorphism.
     * For this class, it always returns false since we know the subtype at this point (StatusMove).
     * @return True if the move is an attacking move, which means that it always returns false.
     */
    @Override
    public boolean isAttackingMove() {
        return false;
    }

    public void setMove(String mName) {
        setName(mName);
        switch(mName) {
            case "Amnesia":
                setType("Psychic");
                setPP(20);
                setTwoTurnCode(0);
                setAccuracy(1000);
                setStatChanges(new Integer[] {0, 0, 0, 0, 2, 0, 0, 0});
                setEffects("This status move raises the user's S.Defense by two stages. Keep in mind that " +
                           "each stat can only be increased or decreased by six stages.");
                setChangesUserStats(true);
                break;
            case "Leech Seed":
                setType("Grass");
                setPP(10);
                setTwoTurnCode(0);
                setAccuracy(90);
                setEffects("This status move has a 90% chance to drain a small amount of the opponent's HP " +
                           "each turn, which heals the user. This move fails against other Grass types and " +
                           "its effects disappear if an opponent afflicted with Leech Seed switches out.");
                break;
            case "Light Screen":
                setType("Psychic");
                setPP(30);
                setTwoTurnCode(0);
                setAccuracy(1000);
                setEffects("This status move uses psychic power to increase the S.Defense of the user's " +
                           "team for five turns.");
                break;
            case "Magic Coat":
                setType("Psychic");
                setPP(30);
                setTwoTurnCode(0);
                setAccuracy(1000);
                setEffects("This status move reflects the effects of most status moves that are used by the opponent. " +
                           "However, the user will still be vulnerable to direct damage during this time.");
                break;
            case "Magnet Rise":
                setType("Electric");
                setPP(10);
                setTwoTurnCode(0);
                setAccuracy(1000);
                setEffects("This status move makes the user immune to Ground type moves for five turns.");
                break;
            case "Reflect":
                setType("Psychic");
                setPP(20);
                setTwoTurnCode(0);
                setAccuracy(1000);
                setEffects("This status move uses psychic power to increase the Defense of the user's " +
                           "team for five turns.");
                break;
            case "Rest":
                setType("Psychic");
                setPP(10);
                setTwoTurnCode(0);
                setAccuracy(1000);
                setEffects("This status move will put the user to sleep for two turns. As a result, the " +
                           "user is fully healed and loses all of its status conditions.");
                break;
            case "Roar":
                setType("Normal");
                setPP(20);
                setTwoTurnCode(0);
                setAccuracy(1000);
                addToIBList(1);
                addToIBList(2);
                setEffects("This status move scares the opponent out and replaces it with a different random Pokémon " +
                           "on their team. However, the user almost always moves last when using this move.");
                break;
            case "Screech":
                setType("Normal");
                setPP(40);
                setTwoTurnCode(0);
                setAccuracy(85);
                setStatChanges(new Integer[] {0, 0, -2, 0, 0, 0, 0, 0});
                setEffects("This status move has an 85% chance to reduce the opponent's Defense by two stages. Keep in mind that " +
                           "each stat can only be increased or decreased by six stages.");
                setChangesUserStats(false);
                break;
            case "Substitute": // Handle sub health drop (and sub in general) //TODO
                setType("Normal");
                setPP(10);
                setTwoTurnCode(0);
                setAccuracy(1000);
                setStatChanges(new Integer[] {-25, 0, 0, 0, 0, 0, 0, 0});
                setEffects("This status move uses 25% of the user's HP to create a substitute for the user. " +
                           "This substitute is immune to the effects of most status moves but is usually " +
                           "destroyed when an attacking move takes at least 25% of the user's HP." +
                           "For reference, sound-based moves and certain other effects bypass substitutes, so" +
                           "this does not ensure that the user and their side of the field is fully protected.");
                setChangesUserStats(true);
                break;
            case "Sunny Day":
                setType("Fire");
                setPP(5);
                setTwoTurnCode(0);
                setAccuracy(1000);
                setEffects("This status move brings out the sunlight for five turns, which strengthens Fire " +
                           "type moves, weakens Water type moves, and has many other small effects.");
                break;
            case "Synthesis": // Handle variable health changes from synthesis from weather //TODO
                setType("Grass");
                setPP(5);
                setTwoTurnCode(0);
                setAccuracy(1000);
                setStatChanges(new Integer[] {50, 0, 0, 0, 0, 0, 0, 0});
                setEffects("This status move restores HP to the user depending on the weather. If used while " +
                           "no weather conditions are present, it heals 50% of the user's HP, but it heals more " +
                           "in sunlight and less in other weather conditions.");
                setChangesUserStats(true);
                break;
            case "Thunder Wave":
                setType("Electric");
                setPP(20);
                setTwoTurnCode(0);
                setAccuracy(90);
                setEffects("This status move paralyzes the opponent. This makes the opponent have a 25% chance of being " +
                           "unable to act due to paralysis and reduces the opponent's speed significantly. Electric types " +
                           "and Ground types are immune to the effects of this move.");
                break;
            case "Toxic":
                setType("Poison");
                setPP(10);
                setTwoTurnCode(0);
                setAccuracy(90);
                setEffects("This status move has an 90% chance to badly poison the target. Each turn, " +
                           "the damage from this poison increases, but Steel and Poison type Pokémon are " +
                           "immune to this status condition.");
                break;
            case "Will-O-Wisp":
                setType("Fire");
                setPP(15);
                setTwoTurnCode(0);
                setAccuracy(85);
                setEffects("This status move has an 85% chance to burn the opponent, which deals damage each turn " +
                           "and reduces the opponent's Attack by two stages. Fire types are immune to this status condition.");
                break;
            case "Yawn":
                setType("Normal");
                setPP(10);
                setTwoTurnCode(0);
                setAccuracy(1000);
                setEffects("This status move makes the opponent drowsy, which means that the opponent will fall asleep " +
                           "for 1-3 turns after the opponent's next turn. If the opponent switches out before the opponent " +
                           "falls asleep, the effects of Yawn on that Pokémon are nullified.");
                break;
            case "Swords Dance":
                setType("Normal");
                setPP(20);
                setTwoTurnCode(0);
                setAccuracy(1000);
                setStatChanges(new Integer[] {0, 2, 0, 0, 0, 0, 0, 0});
                setEffects("This status move increases the user's Attack by two stages. Keep in mind that " +
                           "each stat can only be increased or decreased by six stages.");
                setChangesUserStats(true);
                break;
            case "Protect": //implement protection as some sort of status move attribute code //TODO
                setType("Normal");
                setPP(10);
                setTwoTurnCode(0);
                setAccuracy(1000);
                setEffects("This status move protects the user from most attacks and the majority of status moves " +
                           "that only target the user. This has increased priority (+4) and has a large failure chance " +
                           "after the initial usage when used consecutively with itself or other protection moves.");
                setChangesUserStats(true);
                // implement priority for all moves //TODO
                break;
            case "Rain Dance":
                setType("Water"); // Implement weather as some sort of status move/ability attribute code //TODO
                setPP(5);
                setTwoTurnCode(0);
                setAccuracy(1000);
                setEffects("This status move brings out the rain for five turns, which strengthens Water " +
                           "type moves, weakens Fire type moves, and has many other small effects.");
                break;
            default:
                setName("");
        }
    } //add more moves once the game works //TODO

    /**
     * Returns this move's effects.
     * @return This move's effects.
     */
    public String getEffects() {
        return effects;
    }

    /**
     * Sets this move's effects to the specified String.
     * @param sE This move's new effects.
     */
    public void setEffects(String sE) {
        effects = sE;
   }
}