package lukes.pokemonapp;

import android.os.Parcel;

/**
 * The representation of an attacking move, which is a type of Move. Each AttackingMove (in addition to
 * the characteristics of a Move) has BP, accuracy, a bpCode, additional effects and their percentage chance, and a makesContact boolean.
 * More characteristics may be added as necessary.
 * @author Luke Schoeberle 7/11/2016.
 */
@SuppressWarnings("unused")
public abstract class AttackingMove extends Move { // add accuracy codes //TODO

    /**
     * The base BP of the move. This can range from 10-250 (roughly).
     */
    private int BP;

    /**
     * The type of bp change that this move has. If it is zero, its BP cannot change; any other code
     * indicates that its BP can change. The codes so far are specified below.
     */
    private int bpCode;

    /**
     * A description of additional effects that this move has, like flinches or stat changes.
     * These descriptions contain keywords that signify their effects (so they must be worded consistently
     * each time).
     */
    private String addEffects;

    /**
     * Determines whether or not the move makes contact. This matters for certain abilities and moves.
     */
    private boolean makesContact;

    /**
     * Determines the chance of additional effects from this move. This will be zero by default for moves
     * with no additional effects, and it is also zero for effects that always happen.
     * For other cases, it is represented by a number from 1-100 (just like accuracy).
     */
    private int addEffectChance;

    /**
     * Constructs an attacking move to its preset state based on its name. This is the only constructor.
     * @param mName The name of the move.
     */
    public AttackingMove(String mName) {
        super(mName);
    }

    /**
     * Handles Parcel creation, which sets the variables from the parcel in the same order
     * As the other constructor. For reference, it is important to match parcel I/O order,
     * So this is a critical detail that may need to be updated when this class is changed.
     * @param in The incoming AttackingMove parcel.
     */
    protected AttackingMove(Parcel in) {
        super(in);

        setBP(in.readInt());
        setBPCode(in.readInt());
        setMakesContact(in.readBoolean());
        setAddEffectChance(in.readInt());
    }

    /**
     * Handles Parcel output, which sends the data from the class to the parcel in the same order
     * As the main constructor. For reference, it is important to match parcel I/O order,
     * So this is a critical detail that may need to be updated when this class is changed.
     * @param out The outgoing AttackingMove parcel.
     * @param flags Extra options for customization, which is currently unused.
     */
    @Override
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);

        // Set the outputs based on the class variables
        out.writeInt(BP);
        out.writeInt(bpCode);
        out.writeBoolean(makesContact);
        out.writeInt(addEffectChance);
    }

    /**
     * Sets the move to the preset state based on the name. This has no definition currently because
     * its meaning depends on whether the move is an PhysicalMove or a SpecialMove.
     * @param mName The name of the move.
     */
    public abstract void setMove(String mName);

    /**
     * Determines whether or not the move is physical. This is undefined in this superclass
     * because this cannot be determined without knowing its subtype (PhysicalMove or SpecialMove).
     * @return True if this move is physical, false otherwise.
     */
    public abstract boolean isPhysical();

    /**
     * Checks whether or not the move is an attacking move, which is mainly used for polymorphism.
     * For this class, it always returns true since we know the subtype at this point (AttackingMove).
     * @return True if the move is an attacking move, which means that it always returns true.
     */
    @Override
    public boolean isAttackingMove() {
        return true;
    }

    /**
     * Returns this move's BP.
     * @return This move's BP.
     */
    public int getBP() {
        return BP;
    }

    /**
     * Returns this move's additional effects.
     * @return This move's additional effects.
     */
    public String getAdditionalEffects() {
        return addEffects;
    }

    /**
     * Returns this move's additional effect chance.
     * @return This move's additional effect chance.
     */
    public int getAddEffectChance() {
        return addEffectChance;
    }

    /**
     * Returns whether or not this move makes contact.
     * @return True if this move makes contact, false otherwise.
     */
    public boolean makesContact() {
        return makesContact;
    }

    /**
     * Sets the BP code of this move to the specified value.
     * @param code The new code.
     */
    public void setBPCode(int code) {
        bpCode = code;
    }

    /**
     * Determines whether or not this move's BP can change.
     * @return True if this move's BP can change, false otherwise.
     */
    public boolean canBPChange() {  //think about adding a similar method for accuracy later (Thunder, Hurricane, etc.)
        /*code:
            1: Eruption, Water Spout (BP = 150 * currentHP / maxHP)
            2: Earthquake (BP = 200 if the target is underground)
            3: Payback (BP = 100 if the target moves first)
            4: Solar Beam (BP is halved in weather other than sunlight)
            5: Low Kick and Grass Knot (BP depends directly on weight)
         */
        return bpCode != 0;
    } //watch out for changed BP codes //TODO

    /**
     * *Precondition: bpCode is not zero.
     * Determines if this move has the specified BP code.
     * @param bCode The BP code in question.
     * @return True if this move has this BP code, false otherwise.
     */
    public boolean hasBPCode(int bCode) {
        return bpCode == bCode;
    }

    /**
     * Sets the boolean makesContact to the specified value.
     * @param mC The new value of the makesContact boolean.
     */
    public void setMakesContact(boolean mC) {
        makesContact = mC;
    }

    /**
     * Sets this move's BP to the specified value.
     * @param bp The move's new BP.
     */
    public void setBP(int bp) {
        BP = bp;
    }

    /**
     * Sets this move's additional effects to the specified String.
     * @param aE This move's new additional effects.
     */
    public void setAdditionalEffects(String aE) {
        addEffects = aE;
    }

    /**
     * Sets this move's additional effect chance to the specified. value.
     * @param aec The new chance of additional effects for this move.
     */
    public void setAddEffectChance(int aec) {
        addEffectChance = aec;
    }
}
