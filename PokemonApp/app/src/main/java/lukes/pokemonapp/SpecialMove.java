package lukes.pokemonapp;

/**
 * The representation of a special move. A PhysicalMove is no different than an attacking
 * move except that it uses the S.Attack and S.Defense stats in battle.
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
     * Returns true if this is move is physical. This is always false for this class.
     * @return False for this class.
     */
    @Override
    public boolean isPhysical() {
        return false; //for polymorphism
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
                setType("Electric"); setPP(15); setTwoTurnCode(0); setBP(80); setAccuracy(100); setMakesContact(false); setBPCode(0);
                setAdditionalEffects("This special move has a 30% chance to paralyze the opponent."); setAddEffectChance(30);
                break;
            case "Energy Ball":
                setType("Grass"); setPP(10); setTwoTurnCode(0); setBP(90); setAccuracy(100); setMakesContact(false); setBPCode(0);
                setAdditionalEffects("This special move has a 10% chance to lower the opponent's Sp. Def. by one stage.");
                setStatChanges(new int[] {0, 0, 0, 0, -1, 0, 0, 0}); setChangesUserStats(false); setAddEffectChance(10);
                break;
            case "Eruption":
                setType("Fire"); setPP(5); setTwoTurnCode(0); setBP(150); setAccuracy(100); setMakesContact(false); setBPCode(1);
                setAdditionalEffects("This special move decreases in power as the user's HP decreases.");
                break;
            case "Flamethrower":
                setType("Fire"); setPP(15); setTwoTurnCode(0); setBP(90); setAccuracy(100); setMakesContact(false); setBPCode(0);
                setAdditionalEffects("This special move has a 10% chance to burn the opponent, assuming that " +
                                     "the opponent is not immune to being burned."); setAddEffectChance(10);
                break;
            case "Giga Drain": //deal with half HP recovery (currently has bogus code) //TODO
                setType("Grass"); setPP(10); setTwoTurnCode(0); setBP(75); setAccuracy(100); setMakesContact(false); setBPCode(0);
                setAdditionalEffects("This special move deals damage and also allows the user to recover " +
                                     "half of the HP dealt by this move."); setStatChanges(new int[] {500, 0, 0, 0, 0, 0, 0, 0}); setChangesUserStats(true);
                break;
            case "Ice Beam":
                setType("Ice"); setPP(10); setTwoTurnCode(0); setBP(90); setAccuracy(100); setMakesContact(false); setBPCode(0);
                setAdditionalEffects("This special move has a 10% chance to freeze the opponent."); setAddEffectChance(10);
                break;
            case "Lava Plume":
                setType("Fire"); setPP(15); setTwoTurnCode(0); setBP(80); setAccuracy(100); setMakesContact(false); setBPCode(0);
                setAdditionalEffects("This special move has a 30% chance to burn the opponent."); setAddEffectChance(30);
                break;
            case "Scald":
                setType("Water"); setPP(15); setTwoTurnCode(0); setBP(80); setAccuracy(100); setMakesContact(false); setBPCode(0);
                setAdditionalEffects("This special move has a 30% chance to burn the opponent. It also " +
                                     "unfreezes a frozen target that it hits."); setAddEffectChance(30);
                break;
            case "Sludge Bomb":
                setType("Poison"); setPP(10); setTwoTurnCode(0); setBP(90); setAccuracy(100); setMakesContact(false); setBPCode(0);
                setAdditionalEffects("This special move has a 30% chance to poison the opponent, assuming " +
                                     "that the opposing Pokémon is not immune to being poisoned."); setAddEffectChance(30);
                break;
            case "Solar Beam":
                setType("Grass"); setPP(10); setTwoTurnCode(2); setBP(120); setAccuracy(100); setMakesContact(false); setBPCode(4);
                setAdditionalEffects("This special move allows the user to gather up light for one turn " +
                                     "and then unleash it on the next turn. In sunlight, no charging turn " +
                                     "is required, and this move's power is decreased during other weather conditions.");
                break;
            case "Volt Switch":
                setType("Electric"); setPP(20); setTwoTurnCode(0); setBP(70); setAccuracy(100); setMakesContact(false); setBPCode(0);
                setAdditionalEffects("This special move deals damage and then forces the user to switch to another " +
                                     "Pokémon on their team. The user will not switch if Volt Switch deals no damage.");
                break;
            default:
                setName("");
        }
    } //watch out for BP Codes //TODO
}
