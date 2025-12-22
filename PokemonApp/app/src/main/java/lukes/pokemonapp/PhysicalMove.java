package lukes.pokemonapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * The representation of a physical move. Essentially, a PhysicalMove is an AttackingMove
 * That typically uses the Attack and Defense stats and often makes contact with the opponent in battle.
 * @author Luke Schoeberle 7/11/2016.
 */
public class PhysicalMove extends AttackingMove {

    /**
     * Sets the move to its preset state based on its name. This is the only constructor.
     * @param mName The name of the move.
     */
    public PhysicalMove(String mName) {
        super(mName);
    }

    /**
     * Handles Parcel creation, which sets the variables from the parcel in the same order
     * As the other constructor. For reference, it is important to match parcel I/O order,
     * So this is a critical detail that may need to be updated when this class is changed.
     * @param in The incoming PhysicalMove parcel.
     */
    protected PhysicalMove(Parcel in) {
        super(in);
    }

    /**
     * Handles Parcel output, which sends the data from the class to the parcel in the same order
     * As the main constructor. For reference, it is important to match parcel I/O order,
     * So this is a critical detail that may need to be updated when this class is changed.
     * @param out The outgoing PhysicalMove parcel.
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

    public static final Parcelable.Creator<PhysicalMove> CREATOR = new Creator<>() {
        public PhysicalMove createFromParcel(Parcel in) {
            return new PhysicalMove(in);
        }

        public PhysicalMove[] newArray(int size) {
            return new PhysicalMove[size];
        }
    };

    /**
     * Determines whether or not the move is physical.
     * For this class, it always returns true since we know the subtype at this point (PhysicalMove).
     * @return True if the move is physical, which means that it always returns true.
     */
    @Override
    public boolean isPhysical() {
        return true;
    }

    /**
     * Sets the move to its preset state based on its name. This contains all of the known physical moves
     * in the game at this point.
     * @param mName The name of the move.
     */
    public void setMove(String mName) {
        setName(mName);
        switch(mName) {
            case "Aqua Tail":
                setType("Water");
                setPP(10);
                setTwoTurnCode(0);
                setBP(90);
                setAccuracy(90);
                setMakesContact(true);
                setBPCode(0);
                setAdditionalEffects("This physical move deals damage and has no additional effects.");
                break;
            case "Dig":
                setType("Ground");
                setPP(10);
                setTwoTurnCode(6);
                setBP(80);
                setAccuracy(100);
                setMakesContact(true);
                setBPCode(0);
                setAdditionalEffects("This physical move burrows the user underground for one turn, making the user " +
                                     "invulnerable to most attacks. On the second turn, the user deals damage and comes " +
                                     "out of this semi-invulnerable state.");
                break;
            case "Dragon Claw":
                setType("Dragon");
                setPP(15);
                setTwoTurnCode(0);
                setBP(80);
                setAccuracy(100);
                setMakesContact(true);
                setBPCode(0);
                setAdditionalEffects("This physical move deals damage and has no additional effects.");
                break;
            case "Earthquake":
                setType("Ground");
                setPP(10);
                setTwoTurnCode(0);
                setBP(100);
                setAccuracy(100);
                setMakesContact(false);
                setBPCode(2);
                setAdditionalEffects("This physical move deals damage and has no additional effects, " +
                                     "except for dealing double damage to an opponent that is underground due to the move Dig." +
                                     "It also affects all other Pokémon in double battles.");
                addToIBList(6);
                break;
            case "Ice Punch":
                setType("Ice");
                setPP(15);
                setTwoTurnCode(0);
                setBP(75);
                setAccuracy(100);
                setMakesContact(true);
                setBPCode(0);
                setAdditionalEffects("This physical move deals damage and has a 10% chance to freeze the opponent, " +
                                     "assuming that the opponent is not immune to being frozen.");
                setAddEffectChance(10);
                setNonVolChanges("Frozen");
                break;
            case "Payback":
                setType("Dark");
                setPP(10);
                setTwoTurnCode(0);
                setBP(50);
                setAccuracy(100);
                setMakesContact(true);
                setBPCode(3);
                setAdditionalEffects("This physical move doubles in power if the opponent moves before the user.");
                break;
            case "Play Rough":
                setType("Fairy");
                setPP(10);
                setTwoTurnCode(0);
                setBP(90);
                setAccuracy(90);
                setMakesContact(true);
                setBPCode(0);
                setAdditionalEffects("This physical moved deals damage and has a 10% chance of " +
                                     "lowering the opponent's Attack by one stage.");
                setStatChanges(new Integer[] {0, -1, 0, 0, 0, 0, 0, 0});
                setChangesUserStats(false);
                setAddEffectChance(10);
                break;
            case "Superpower":
                setType("Fighting");
                setPP(5);
                setTwoTurnCode(0);
                setBP(120);
                setAccuracy(100);
                setMakesContact(true);
                setBPCode(0);
                setAdditionalEffects("This physical move deals damage and will lower the user's Attack and Defense " +
                                     "by one stage after the move is used.");
                setStatChanges(new Integer[] {0, -1, -1, 0, 0, 0, 0, 0});
                setChangesUserStats(true);
                setAddEffectChance(100);
                break;
            case "Seed Bomb":
                setType("Grass");
                setPP(15);
                setTwoTurnCode(0);
                setBP(80);
                setAccuracy(100);
                setMakesContact(false);
                setBPCode(0);
                setAdditionalEffects("This physical move deals damage and has no additional effects. " +
                                     "For future reference, it is blocked by the Bulletproof ability.");
                break;
            case "Drain Punch":
                setType("Fighting");
                setPP(10);
                setTwoTurnCode(0);
                setBP(75);
                setAccuracy(100);
                setMakesContact(false);
                setBPCode(0);
                setAdditionalEffects("This physical move deals damage and also allows the user to recover " +
                                     "half of the damage in HP dealt by this move. For reference, this move is entirely " +
                                     "disabled by the Heal Block or the Psychic Noise effects, and it hurts the user " +
                                     "if this move is used against a Pokemon with the Liquid Ooze ability.");
                setStatChanges(new Integer[] {50, 0, 0, 0, 0, 0, 0, 0});
                setAddEffectChance(100);
                setChangesUserStats(true);
                break;
            case "Crunch":
                setType("Dark");
                setPP(15);
                setTwoTurnCode(0);
                setBP(80);
                setAccuracy(100);
                setMakesContact(true);
                setBPCode(0);
                setAdditionalEffects("This physical move deals damage and has a 20% chance of " +
                                     "lowering the opponent's Defense by one stage.");
                setStatChanges(new Integer[] {0, 0, -1, 0, 0, 0, 0, 0});
                setChangesUserStats(false);
                setAddEffectChance(20);
                break;
            case "Flare Blitz":
                setType("Fire");
                setPP(15);
                setTwoTurnCode(0);
                setBP(120);
                setAccuracy(100);
                setMakesContact(true);
                setBPCode(0);
                setAdditionalEffects("This physical move deals damage and also causes the user to suffer " +
                                     "30% of the target's damage as recoil. Additionally, if necessary, " +
                                     "the user is thawed out before the moved is performed.");
                setStatChanges(new Integer[] {-30, 0, 0, 0, 0, 0, 0, 0});
                setChangesUserStats(true);
                setAddEffectChance(100);
                break;
            case "Aerial Ace":
                setType("Flying");
                setPP(20);
                setTwoTurnCode(0);
                setBP(60);
                setAccuracy(1000);
                setMakesContact(true);
                setBPCode(0);
                setAdditionalEffects("This physical move deals damage and bypasses accuracy checks. For reference, " +
                                     "this means the move can only be directly avoided by Pokemon in semi-invulnerable states.");
                break;
            case "Low Kick":
                setType("Fighting");
                setPP(20);
                setTwoTurnCode(0);
                setBP(20); // This is the default value, but actually set it dynamically based on weight in the formula //TODO
                setAccuracy(100);
                setMakesContact(true);
                setBPCode(5);
                setAdditionalEffects("This physical move deals a variable amount of damage depending on the target's base weight value. " +
                                     "For reference, this increases by weight, so generally it is stronger against heavier foes.");
                break;
            case "Liquidation":
                setType("Water");
                setPP(10);
                setTwoTurnCode(0);
                setBP(85);
                setAccuracy(100);
                setMakesContact(true);
                setBPCode(0);
                setAdditionalEffects("This physical move deals damage and has a 20% chance to " +
                                     "lower the opponent's Defense by one stage.");
                setStatChanges(new Integer[] {0, 0, -1, 0, 0, 0, 0, 0});
                setChangesUserStats(false);
                setAddEffectChance(20);
                break;
            case "Rock Slide":
                setType("Rock"); // Implement flinching and deal with multiple targets //TODO
                setPP(10);
                setTwoTurnCode(0);
                setBP(75);
                setAccuracy(90);
                setMakesContact(false);
                setBPCode(0);
                setAdditionalEffects("This physical move deals damage, has a 30% chance of causing the opponent " +
                                     "to flinch, and hits both targets in a double battle.");
                setStatChanges(new Integer[] {0, 0, -1, 0, 0, 0, 0, 0});
                setChangesUserStats(false);
                setAddEffectChance(20);
                break;
            default:
                setName("");
        }
    }
}