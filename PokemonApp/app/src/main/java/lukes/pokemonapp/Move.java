package lukes.pokemonapp;

import java.util.ArrayList;

import androidx.annotation.NonNull;

/**
 * The representation of a default Pokémon move. Each default move has a name, a type, PP, and a
 * two turn status (priority is missing). Each Move must be either a StatusMove or an AttackingMove (which
 * also includes PhysicalMove and SpecialMove) since Move is abstract.
 * @author Luke Schoeberle 7/11/2016.
 */
@SuppressWarnings("unused")
public abstract class Move {

    /**
     * The move's name. This is its primary identifier.
     */
    private String name;

    /**
     * The move's type. This can be any type or ??? (Curse). Each move has only only one type.
     */
    private String type;

    /**
     * The Move's PP, which indicates how many times it can be used in battle. PP ranges from 5-64.
     */
    private int PP;

    /**
     * A code that indicates what type of two-turn move this is. For non-two-turn moves, the value is zero,
     * and it ranges from 1-7 currently for two-turn moves.
     */
    private int twoTurnCode;

    /**
     * This move's accuracy. This is usually a number between 1 and 100, but for certain moves,
     * the accuracy will not be listed (like stat-changing moves).
     */
    private int accuracy;

    /**
     * A list of the invulnerable codes that this move passes through. This is used to reduce the ugliness
     * of breaking semi-invulnerability by name only. This depends on the invulnCodes in the Pokemon class.
     */
    private final ArrayList<Integer> IBList;

    /**
     * A list of the stat changes caused by this move. The HP one indicates recoil or health gain (represented
     * by a percentage), and the others indicate the number of stat changes. For both kinds, zeroes indicate no change.
     */
    private int[] statChanges;

    /**
     * Determines whether or not the stat changes affect the user or the opponent.
     */
    private boolean changesUserStats;

    /**
     * A list of the non-volatile statuses that this move can cause. This can only contain one status (since
     * only one volatile status can occur at one time).
     */
    private String nonVolChanges;

    /**
     * A list of the volatile statuses that this move can cause. This can possibly contain multiple
     * statuses since volatile statuses can stack.
     */
    private String volChanges;

    /**
     * Determines whether this move's status effects affect the user or the target.
     */
    private boolean statusesUser;

    //consider adding priority tiers later //TODO

    //add sound based (breaks through sub) //TODO

    //add multi-target moves //TODO

    /**
     * Presets the move to its effects based on its name.
     * @param mName The name of the move.
     */
    public Move(String mName) {
        IBList = new ArrayList<>();
        setMove(mName);
    }

    /**
     * Sets the move to the preset state based on the name. This has no definition currently because
     * its meaning depends on whether the move is an AttackingMove or a StatusMove.
     * @param mName The name of the move.
     */
    public abstract void setMove(String mName);

    /**
     * Checks whether or not the move is an AttackingMove. This is mainly used for polymorphism.
     * @return True if the move is an AttackingMove, false otherwise.
     */
    public abstract boolean isAttackingMove();

    /**
     * Returns the move's type.
     * @return The move's type.
     */
    public String getType() {
        return type;
    }

    /**
     * Returns the move's name.
     * @return The move's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the move's two-turn code.
     * @return The move's two-turn code.
     */
    public int getTwoTurnCode() {
        return twoTurnCode;
    }

    /**
     * Returns the move's PP.
     * @return The move's PP.
     */
    public int getPP() {
        return PP;
    }

    /**
     * Checks if the move takes two turns.
     * @return True if the move takes two turns, false otherwise.
     */
    public boolean takesTwoTurns() {
        return twoTurnCode != 0;
    }

    /**
     * *Precondition: ttCode is not zero.
     * Checks if the move has the specified two-turn code.
     * @param ttCode The two-turn code in question.
     * @return True if the move has the specified code, false otherwise.
     */
    public boolean hasTwoTurnCode(int ttCode) {
        return ttCode == twoTurnCode;    /* code:
                                            0: non two-turn
                                            1: not semi-invulnerable (Geomancy, Freeze Shock, Ice Burn, Razor Wind, Skull Bash, Sky Attack)
                                            2: Solar Beam
                                            3: Fly
                                            4: Sky Drop
                                            5: Bounce
                                            6: Dig
                                            7: Shadow Force, Phantom Force
                                            100: currently charging
                                         */
    }

    /**
     * Sets this move's PP to the specified number.
     * @param pp The new PP.
     */
    public void setPP(int pp) {
        if(pp >= 5 && pp <= 64)
            PP = pp;
        else
            throw new IllegalArgumentException("PP values can only range between 5 and 64!");
    }

    /**
     * Changes the PP by the specified number. This number can be positive or negative, but the PP
     * can never go below zero. This returns false if the PP began at zero.
     * @param del The change in PP.
     */
    public boolean changePP(int del) {
        if(PP == 0 && del < 0)
            return false;
        PP += del;
        if(PP < 0)
            PP = 0;
        return true;
    }

    /**
     * Sets this move's type to the specified type.
     * @param type The new type.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Sets this move's name to the specified name.
     * @param mName The new name.
     */
    public void setName(String mName) {
        name = mName;
    }

    /**
     * Sets this move's two-turn code to the specified number.
     * @param ttCode The new two-turn code.
     */
    public void setTwoTurnCode(int ttCode) {
        twoTurnCode = ttCode;
    }

    /**
     * Returns this move's invulnerable break list.
     * @return This move's invulnerable break list.
     */
    public ArrayList<Integer> getIBList() {
        return IBList;
    }

    /**
     * Adds the specified code to this move's invulnerable break list.
     * @param code The code to be added.
     */
    public void addToIBList(int code) {
        IBList.add(code);
    }

    /**
     * Sets this move's stat changes to the specified array.
     * @param sc The new value of the stat changes.
     */
    public void setStatChanges(int[] sc) {
        statChanges = sc;
    }

    /**
     * Sets whether or not this move changes the user's stats, as opposed to the opponent's stats.
     * @param cus The new value of the boolean variable.
     */
    public void setChangesUserStats(boolean cus) {
        changesUserStats = cus;
    }

    /**
     * Sets statusesUser to the specified value.
     * @param su The new value of statusesUser.
     */
    public void setStatusesUser(boolean su) {
        statusesUser = su;
    }

    /**
     * Sets the non-volatile status changes to the specified String.
     * @param sc The non-volatile status changes of this move.
     */
    public void setNonVolChanges(String sc) {
        nonVolChanges = sc;
    }

    /**
     * Sets the volatile status changes to the specified String.
     * @param vc The volatile status changes of this move.
     */
    public void setVolChanges(String vc) {
        volChanges = vc;
    }

    /**
     * Sets the move's accuracy to the specified value. This throws an exception if the new value
     * is not within the range of 1-100.
     * @param acc The move's new accuracy value.
     * @throws IllegalArgumentException When the new accuracy number is invalid.
     */
    public void setAccuracy(int acc) {
        if(acc >= 1 && acc <= 100 || acc == 1000)
            accuracy = acc;
        else
            throw new IllegalArgumentException("A move's accuracy can only be between 1 and 100 (or 1000 if accuracy is unnecessary)!");
    }

    /**
     * Returns the list of stat changes caused by this move.
     * @return The stat changes caused by this move.
     */
    public int[] getStatChanges() {
        return statChanges;
    }

    /**
     * Returns this move's accuracy.
     * @return This move's accuracy.
     */
    public int getAccuracy() {
        return accuracy;
    }

    /**
     * Returns whether or not this move can change the user's stats (or if it changes the opponent's stats).
     * @return If this move changes the user's stats.
     */
    public boolean changesUserStats() {
        return changesUserStats;
    }

    /**
     * Determines whether or not this move's statuses conditions affect the user or the target.
     * @return True if the status is inflicted on user, false otherwise.
     */
    public boolean statusesUser() {
        return statusesUser;
    }

    /**
     * Returns the non-volatile status changes of this move.
     * @return The non-volatile status changes of this move.
     */
    public String getNonVolChanges() {
        return nonVolChanges;
    }

    /**
     * Returns the volatile status changes caused by this move.
     * @return The volatile status changes caused by this move.
     */
    public String getVolChanges() {
        return volChanges;
    }

    /**
     * Used for debugging purposes.
     * @return A String representation of this object.
     */
    @Override
    @NonNull
    public String toString() {
        return name;
    }
}
