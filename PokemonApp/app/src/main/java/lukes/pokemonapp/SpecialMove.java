package lukes.pokemonapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * The representation of a special move. Essentially, a Special is an AttackingMove that typically
 * Uses the Special Attack and Special Defense stats and rarely makes contact with the opponent in battle.
 * @author Luke Schoeberle 7/11/2016.
 */
public class SpecialMove extends AttackingMove {

    /**
     * Sets the move to its preset state based on its name. This is the only constructor.
     * @param mName The name of the move.
     */
    public SpecialMove(String mName) {
        super(mName);
    }

    /**
     * Handles Parcel creation, which sets the variables from the parcel in the same order
     * As the other constructor. For reference, it is important to match parcel I/O order,
     * So this is a critical detail that may need to be updated when this class is changed.
     * @param in The incoming SpecialMove parcel.
     */
    protected SpecialMove(Parcel in) {
        super(in);
    }

    /**
     * Handles Parcel output, which sends the data from the class to the parcel in the same order
     * As the main constructor. For reference, it is important to match parcel I/O order,
     * So this is a critical detail that may need to be updated when this class is changed.
     * @param out The outgoing SpecialMove parcel.
     * @param flags Extra options for customization, which is currently unused.
     */
    @Override
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
    }

    /**
     * Indicates if there are special file contents, such as file descriptors.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<SpecialMove> CREATOR = new Creator<>() {
        public SpecialMove createFromParcel(Parcel in) {
            return new SpecialMove(in);
        }

        public SpecialMove[] newArray(int size) {
            return new SpecialMove[size];
        }
    };

    /**
     * Determines whether or not the move is physical.
     * For this class, it always returns false since we know the subtype at this point (SpecialMove).
     * @return True if the move is physical, which means that it always returns false.
     */
    @Override
    public boolean isPhysical() {
        return false;
    }

    /**
     * Sets the move to its preset state based on its name. This contains all of the known special moves
     * in the game at this point.
     * @param mName The name of the move.
     */
    @Override
    public void setMove(String mName) {
        setName(mName);
        switch(mName) {
            case "Discharge":
                setType("Electric");
                setPP(15);
                setTwoTurnCode(0);
                setBP(80);
                setAccuracy(100);
                setMakesContact(false);
                setBPCode(0);
                setAdditionalEffects("This special move deals damage and has a 30% chance to paralyze the opponent," +
                                     "assuming that the opposing Pokémon is not immune to the paralysis condition.");
                setAddEffectChance(30);
                setNonVolChanges("Paralyzed");
                break;
            case "Energy Ball":
                setType("Grass");
                setPP(10);
                setTwoTurnCode(0);
                setBP(90);
                setAccuracy(100);
                setMakesContact(false);
                setBPCode(0);
                setAdditionalEffects("This special move deals damage and has a 10% chance to " +
                                     "lower the opponent's Sp. Def. by one stage.");
                setStatChanges(new Integer[] {0, 0, 0, 0, -1, 0, 0, 0});
                setChangesUserStats(false);
                setAddEffectChance(10);
                break;
            case "Eruption":
                setType("Fire");
                setPP(5);
                setTwoTurnCode(0);
                setBP(150);
                setAccuracy(100);
                setMakesContact(false);
                setBPCode(1);
                setAdditionalEffects("This special move decreases in power as the user's HP decreases " +
                                     "and also hits both targets in a double battle.");
                break;
            case "Flamethrower":
                setType("Fire");
                setPP(15);
                setTwoTurnCode(0);
                setBP(90);
                setAccuracy(100);
                setMakesContact(false);
                setBPCode(0);
                setAdditionalEffects("This special move deals damage and has a 10% chance to burn the opponent, " +
                                     "assuming that the opposing Pokémon is not immune to the burn condition.");
                setAddEffectChance(10);
                setNonVolChanges("Burned");
                break;
            case "Giga Drain":
                setType("Grass");
                setPP(10);
                setTwoTurnCode(0);
                setBP(75);
                setAccuracy(100);
                setMakesContact(false);
                setBPCode(0);
                setAdditionalEffects("This special move deals damage and also allows the user to recover " +
                                     "half of the damage in HP dealt by this move. For reference, this move is entirely " +
                                     "disabled by the Heal Block or the Psychic Noise effects, and it hurts the user " +
                                     "if this move is used against a Pokemon with the Liquid Ooze ability.");
                setStatChanges(new Integer[] {50, 0, 0, 0, 0, 0, 0, 0});
                setAddEffectChance(100);
                setChangesUserStats(true);
                break;
            case "Ice Beam":
                setType("Ice");
                setPP(10);
                setTwoTurnCode(0);
                setBP(90);
                setAccuracy(100);
                setMakesContact(false);
                setBPCode(0);
                setAdditionalEffects("This special move deals damage and has a 10% chance to freeze the opponent," +
                                     "assuming that the opposing Pokémon is not immune to the freeze condition.");
                setAddEffectChance(10);
                setNonVolChanges("Frozen");
                break;
            case "Lava Plume":
                setType("Fire");
                setPP(15);
                setTwoTurnCode(0);
                setBP(80);
                setAccuracy(100);
                setMakesContact(false);
                setBPCode(0);
                setAdditionalEffects("This special move deals damage, has a 30% chance to burn the opponent, " +
                                     "and also hits both targets in a double battle.");
                setAddEffectChance(30);
                setNonVolChanges("Burned");
                break;
            case "Scald":
                setType("Water");
                setPP(15);
                setTwoTurnCode(0);
                setBP(80);
                setAccuracy(100);
                setMakesContact(false);
                setBPCode(0);
                setAdditionalEffects("This special move deals damage and has a 30% chance to burn the opponent. " +
                                     "Additionally, if necessary, the user is thawed out before the moved is performed," +
                                     "It also unfreezes a frozen target hit by the attack.");
                setAddEffectChance(30);
                setNonVolChanges("Burned");
                break;
            case "Sludge Bomb":
                setType("Poison");
                setPP(10);
                setTwoTurnCode(0);
                setBP(90);
                setAccuracy(100);
                setMakesContact(false);
                setBPCode(0);
                setAdditionalEffects("This special move deals damage and has a 30% chance to poison the opponent, " +
                                     "assuming that the opposing Pokémon is not immune to the poison condition.");
                setAddEffectChance(30);
                setNonVolChanges("Poisoned");
                break;
            case "Solar Beam":
                setType("Grass");
                setPP(10);
                setTwoTurnCode(2);
                setBP(120);
                setAccuracy(100);
                setMakesContact(false);
                setBPCode(4);
                setAdditionalEffects("This special move allows the user to gather up light for one turn " +
                                     "and then unleash it on the next turn. In sunlight, no charging turn " +
                                     "is required, and this move's power is decreased during other weather conditions.");
                break;
            case "Volt Switch":
                setType("Electric");
                setPP(20);
                setTwoTurnCode(0);
                setBP(70);
                setAccuracy(100);
                setMakesContact(false);
                setBPCode(0);
                setAdditionalEffects("This special move deals damage and then forces the user to switch to another " +
                                     "Pokémon on their team. The user will not switch if Volt Switch deals no damage.");
                break;
            case "Earth Power":
                setType("Ground");
                setPP(10);
                setTwoTurnCode(0);
                setBP(90);
                setAccuracy(100);
                setMakesContact(false);
                setBPCode(0);
                setAdditionalEffects("This special move deals damage has a 20% chance to " +
                                     "lower the opponent's Sp. Def. by one stage.");
                setStatChanges(new Integer[] {0, 0, 0, 0, -1, 0, 0, 0});
                setChangesUserStats(false);
                setAddEffectChance(20);
                break;
            default:
                setName("");
        }
    }
}