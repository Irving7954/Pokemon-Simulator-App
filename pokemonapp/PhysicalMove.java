package lukes.pokemonapp;

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
                setType("Water"); setPP(10); setTwoTurnCode(0); setBP(90); setAccuracy(90); setMakesContact(true); setBPCode(0);
                setAdditionalEffects("This physical move has no additional effects.");
                break;
            case "Dig":
                setType("Ground"); setPP(10); setTwoTurnCode(6); setBP(80); setAccuracy(100); setMakesContact(true); setBPCode(0);
                setAdditionalEffects("This physical move burrows the user underground for one turn, making the user " +
                                     "invulnerable to most attacks. On the second turn, the user deals damage and comes " +
                                     "out of this semi-invulnerable state.");
                break;
            case "Dragon Claw":
                setType("Dragon"); setPP(15); setTwoTurnCode(0); setBP(80); setAccuracy(100); setMakesContact(true); setBPCode(0);
                setAdditionalEffects("This physical move has no additional effects.");
                break;
            case "Earthquake":
                setType("Ground"); setPP(10); setTwoTurnCode(0); setBP(100); setAccuracy(100); setMakesContact(false); setBPCode(2);
                setAdditionalEffects("This physical move has no additional effects, except for dealing double damage to " +
                                     "an opponent that is underground due to the move Dig."); addToIBList(6);
                break;
            case "Ice Punch":
                setType("Ice"); setPP(15); setTwoTurnCode(0); setBP(75); setAccuracy(100); setMakesContact(true); setBPCode(0);
                setAdditionalEffects("This physical move has a 10% chance to freeze the opponent, assuming that " +
                                     "the opponent is not immune to being frozen."); setAddEffectChance(10);
                break;
            case "Payback":
                setType("Dark"); setPP(10); setTwoTurnCode(0);setBP(50); setAccuracy(100); setMakesContact(true); setBPCode(3);
                setAdditionalEffects("This physical move doubles in power if the opponent moves before the user.");
                break;
            case "Play Rough":
                setType("Fairy"); setPP(10); setTwoTurnCode(0); setBP(90); setAccuracy(90); setMakesContact(true); setBPCode(0);
                setAdditionalEffects("This physical move has a 10% chance of lowering the opponent's Attack by one stage.");
                setStatChanges(new int[] {0, -1, 0, 0, 0, 0, 0, 0}); setChangesUserStats(false); setAddEffectChance(10);
                break;
            case "Superpower":
                setType("Fighting"); setPP(5); setTwoTurnCode(0); setBP(120); setAccuracy(100); setMakesContact(true); setBPCode(0);
                setAdditionalEffects("This physical move will lower the user's Attack by one stage after " +
                                     "this move is used. It also will lower the user's Defense by one stage in the same manner.");
                setStatChanges(new int[] {0, -1, -1, 0, 0, 0, 0, 0}); setChangesUserStats(true);
                break;
            default:
                setName("");
        }
    }
}
