package lukes.pokemonapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * The representation of a physical move. A PhysicalMove is no different than an attacking
 * move except that it uses the Attack and Defense stats in battle.
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
     * Returns true if this is move is physical. This is always true for this class.
     * @return True for this class.
     */
    @Override
    public boolean isPhysical() {
        return true; //for polymorphism
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
                setAdditionalEffects("This physical move has no additional effects.");
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
                setAdditionalEffects("This physical move has no additional effects.");
                break;
            case "Earthquake":
                setType("Ground");
                setPP(10);
                setTwoTurnCode(0);
                setBP(100);
                setAccuracy(100);
                setMakesContact(false);
                setBPCode(2);
                setAdditionalEffects("This physical move has no additional effects, except for dealing double damage to " +
                                     "an opponent that is underground due to the move Dig.");
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
                setAdditionalEffects("This physical move has a 10% chance to freeze the opponent, assuming that " +
                                     "the opponent is not immune to being frozen.");
                setAddEffectChance(10);
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
                setAdditionalEffects("This physical move has a 10% chance of lowering the opponent's Attack by one stage.");
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
                setAdditionalEffects("This physical move will lower the user's Attack by one stage after " +
                                     "this move is used. It also will lower the user's Defense by one stage in the same manner.");
                setStatChanges(new Integer[] {0, -1, -1, 0, 0, 0, 0, 0});
                setChangesUserStats(true);
                break;
            default:
                setName("");
        }
    }
}