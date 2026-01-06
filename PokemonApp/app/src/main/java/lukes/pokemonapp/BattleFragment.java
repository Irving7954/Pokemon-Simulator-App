package lukes.pokemonapp;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import androidx.core.graphics.BlendModeColorFilterCompat;
import androidx.core.graphics.BlendModeCompat;
import androidx.fragment.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Button;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

/**
 * The fragment for the battle simulation. Generally, this is the most complex fragment with the most parts
 * and the most logic. The other classes, ideally, should be designed to minimize difficulties in this
 * fragment. This fragment controls weather and all of the buttons that appear on the battle screen. This contains
 * the damage formula, type effectiveness, and many other crucial aspects of Pokémon battling.
 * @author Luke Schoeberle 7/23/2016.
 */
@SuppressWarnings("unused")
public class BattleFragment extends Fragment { //Fragment code 3

    /**
     * The list of stat modifiers for the normal stats (Attack, Defense, Sp. Att., Sp. Def., and HP). These
     * are used in conjunction with Pokémon's statStages array to ease stat changes.
     */
    public static final double[] NORMAL_STAT_STAGES = new double[] {.25, .286, .333, .4, .5, .66, 1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0};

    /**
     * The list of stat modifiers for the evasion stats (Accuracy and Evasion). These
     * are used in conjunction with Pokémon's statStages array to ease stat changes.
     */
    public static final double[] ACCURACY_STAT_STAGES = new double[] {.333, .375, .429, .5, .6, .75, 1.0, 1.333, 1.666, 2, 2.333, 2.666, 3};

    /**
     * The representation of the player and enemy trainers. The player will be of a Player type, and
     * the other will be of an EnemyTrainer type.
     */
    private Trainer player, enemy;

    /**
     * The name of the player, which is provided initially by the user in the pop-up box.
     */
    private String playerNameText;

    /**
     * The name of the player's first Pokémon, which is provided in this fragment's Arguments.
     */
    private String firstPokeName;

    /**
     * The various text boxes that are on the screen, including the Trainer name's, the label for the Pokémon's
     * HPs, and the description of each turn for both players.
     */
    private TextView playerName, enemyName, playerPokeAndHP, enemyPokeAndHP, playerConditions, enemyConditions;

    /**
     * The small images of Pokémon that shows each Trainer's team. Each Trainer has up to six images (one
     * for each Pokémon on their team).
     */
    private ArrayList<ImageView> playerImages, enemyImages;

    /**
     * An image of the current Pokémon for both the player and the enemy. This image does not change
     * if the Pokémon is injured.
     */
    private ImageView playerPoke, enemyPoke;

    /**
     * The buttons that provide the move choices for the player. This button array's indexes correspond
     * to the move array's indexes.
     */
    private ArrayList<Button> moveButtons;

    /**
     * The HP bar for each of the current Pokémon. This displays a green bar until 50% HP,
     * a yellow bar until 25% HP, and a red bar until 0% HP. At 0%, fainting should occur.
     */
    private ProgressBar playerHPBar, enemyHPBar;

    /**
     * The actual current Pokémon for each of the players. This Pokémon's HP is the one that will
     * be affected by the other current Pokémon.
     */
    private Pokemon leadPlayerPoke, leadEnemyPoke;

    /**
     * A Random object used for determining damage rolls, accuracy/evasion, and other additional effects.
     */
    private Random rand;

    /**
     * The two-turn code of the current used move. This only matters if a two-turn move was used.
     */
    private int currTTCode;

    /**
     * An integer that specifies the weather. This can deal residual damage and affects certain moves
     * like Hurricane, Thunder, and Blizzard, along with several abilities. The meaning of all of the codes
     * is specified below.
     */
    private int weather; /* weather codes:
                                0: none, 1: rain, 2: sun, 3: sand, 4: hail, 5: heavy rain, 6: harsh sunlight, 7: air current
                                                                               POgre        PDon                MRay        */

    /**
     * A text box that shows the results of the last move or end-of-turn effects. This generally contains the
     * text from the StringBuilder commentary, which is created as action occurs.
     */
    private TextView commentaryBox;

    /**
     * A StringBuilder that accumulates as a result of moves or end-of-turn effects. This is cleared every time the nextButton
     * is pressed after its contents are copied to the commentaryBox.
     */
    private String commentary;

    /**
     * The default constructor. This essentially does nothing because everything is initialized in onCreateView().
     */
    public BattleFragment() {
        super();
    }

    /**
     * Creates this view based on the parameters. Essentially, it initializes all of the important parts and causes them
     * to have their intended effects. The main battle loop occurs in here because it depends on the result from
     * the name box, which is a bit clunky.
     * @param inflater Creates this view from the layout file.
     * @param container Puts this view into the specified container.
     * @param savedInstance Not used in this case.
     * @return The view created by the inflater.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        // This Fragment's view, which is described by the XML layout file
        View myView = inflater.inflate(R.layout.battle_screen2, container, false);

        Bundle bundle = getArguments();
        if(bundle != null) {
            Pokemon poke = bundle.getParcelable("key", Pokemon.class); // Actually use the full pokemon later //TODO
            firstPokeName = poke.getName();
        }

        Log.d("AddPersonActivity", firstPokeName);
        weather = 0;
        rand = new Random();

        // A button that controls the turn timing, which means that clicking it allows the user to
        // Advance to the next action, such as another move or end-of-turn effects).
        Button nextButton = myView.findViewById(R.id.nextButton);

        //nextButton.setOnClickListener((v) -> nextClicked = !nextClicked); // Use this later //TODO

        //creates the name box
        final EditText et = new EditText(getContext());
        et.setInputType(InputType.TYPE_CLASS_TEXT);
        AlertDialog.Builder adb = new AlertDialog.Builder(getContext());
        adb.setCancelable(false);
        adb.setView(et);
        adb.setTitle("Name?");
        adb.setMessage("Enter your name here: ");
        adb.setPositiveButton("Ok", (dialog, id) -> {
            //initializations for the player
            playerNameText = et.getText().toString();
            playerName = myView.findViewById(R.id.playerName);
            playerName.setText(playerNameText);
            player = new Player(playerNameText);
            player.addPokemon(firstPokeName);
            switch(firstPokeName) { // Determines the rest of the team based on the first Pokemon
                case "Bulbasaur":
                    player.addPokemon("Charmander");
                    player.addPokemon("Squirtle");
                    break;
                case "Charmander":
                    player.addPokemon("Bulbasaur");
                    player.addPokemon("Squirtle");
                    break;
                case "Squirtle":
                    player.addPokemon("Bulbasaur");
                    player.addPokemon("Charmander");
                    break;
                case "Chikorita":
                    player.addPokemon("Cyndaquil");
                    player.addPokemon("Totodile");
                    break;
                case "Cyndaquil":
                    player.addPokemon("Chikorita");
                    player.addPokemon("Totodile");
                    break;
                case "Totodile":
                    player.addPokemon("Chikorita");
                    player.addPokemon("Cyndaquil");
                    break;
                case "Treecko":
                    player.addPokemon("Torchic");
                    player.addPokemon("Mudkip");
                    break;
                case "Torchic":
                    player.addPokemon("Treecko");
                    player.addPokemon("Totodile");
                    break;
                case "Mudkip":
                    player.addPokemon("Treecko");
                    player.addPokemon("Torchic");
                    break;
                default:
                    throw new IllegalArgumentException(firstPokeName + " cannot yet be the first possible Pokémon!");
            }

            leadPlayerPoke = player.getTeam().get(0);
            //initializes text boxes
            playerPokeAndHP = myView.findViewById(R.id.playerPokeAndHP);
            playerPokeAndHP.setText(String.format("%s HP: 100%%", leadPlayerPoke.getName()));
            playerConditions = myView.findViewById(R.id.playerConditions);
            playerConditions.setText("");

            Log.d("AddPersonActivity", player.toString());
            moveButtons = new ArrayList<>();

            ArrayList<Move> playerMoves = leadPlayerPoke.getMoves();
            for (int i = 0; i < playerMoves.size(); i++) {
               //initialize buttons to R.id.move# and set their text to the leadPoke's moves
               switch (i) {
                   case 0:
                       moveButtons.add(myView.findViewById(R.id.move1));
                       break;
                   case 1:
                        moveButtons.add(myView.findViewById(R.id.move2));
                        break;
                   case 2:
                       moveButtons.add(myView.findViewById(R.id.move3));
                       break;
                   case 3:
                       moveButtons.add(myView.findViewById(R.id.move4));
                       break;
                   default:
                       throw new IllegalArgumentException(i + " is not a possible argument here!");
               }
               moveButtons.get(i).setText(playerMoves.get(i).toString());
            }

            playerImages = new ArrayList<>();
            for (int i = 0; i < player.getTeam().size(); i++) {
                //initialize images to R.id.image# and set their text to the leadPoke's moves
                switch (i) {
                    case 0:
                        playerImages.add(myView.findViewById(R.id.playerPoke1));
                        break;
                    case 1:
                        playerImages.add(myView.findViewById(R.id.playerPoke2));
                        break;
                    case 2:
                        playerImages.add(myView.findViewById(R.id.playerPoke3));
                        break;
                    case 3:
                        playerImages.add(myView.findViewById(R.id.playerPoke4));
                        break;
                    case 4:
                        playerImages.add(myView.findViewById(R.id.playerPoke5));
                        break;
                    case 5:
                        playerImages.add(myView.findViewById(R.id.playerPoke6));
                        break;
                    default:
                        throw new IllegalArgumentException(i + " is not a possible argument here!");
                }
            }
            setImages(playerImages, player.getTeam());

            playerPoke = myView.findViewById(R.id.currentPlayerPoke);
            setImage(playerPoke, leadPlayerPoke);
            //initializes the progress bars
            playerHPBar = myView.findViewById(R.id.playerHPBar);
            adjustHPBars(playerHPBar, leadPlayerPoke, playerPokeAndHP);

            //initializations for the enemy trainer
            enemy = new EnemyTrainer("Angel");
            enemy.addPokemon("Voltorb");
            enemy.addPokemon("Wooper");
            enemy.addPokemon("Snubbull");
            // Add more enemy pokemon later //TODO

            leadEnemyPoke = enemy.getTeam().get(0);
            //initializes text boxes
            enemyName = myView.findViewById(R.id.enemyName);
            enemyName.setText(enemy.getName());

            enemyPokeAndHP = myView.findViewById(R.id.enemyPokeAndHP);
            enemyPokeAndHP.setText(String.format("%s HP: 100%%", leadEnemyPoke.getName()));
            enemyConditions = myView.findViewById(R.id.enemyConditions);
            enemyConditions.setText("");

            enemyImages = new ArrayList<>();
            for (int i = 0; i < enemy.getTeam().size(); i++) {
                //initialize images to R.id.image# and set their text to the leadPoke's moves
                switch (i) {
                    case 0:
                        enemyImages.add(myView.findViewById(R.id.enemyPoke1));
                        break;
                    case 1:
                        enemyImages.add(myView.findViewById(R.id.enemyPoke2));
                        break;
                    case 2:
                        enemyImages.add(myView.findViewById(R.id.enemyPoke3));
                        break;
                    case 3:
                        enemyImages.add(myView.findViewById(R.id.enemyPoke4));
                        break;
                    case 4:
                        enemyImages.add(myView.findViewById(R.id.enemyPoke5));
                        break;
                    case 5:
                        enemyImages.add(myView.findViewById(R.id.enemyPoke6));
                        break;
                    default:
                        throw new IllegalArgumentException(i + " is not a possible argument here!");
                }
            }
            setImages(enemyImages, enemy.getTeam());

            enemyPoke = myView.findViewById(R.id.currentEnemyPoke);
            setImage(enemyPoke, leadEnemyPoke);
            //initializes HP bars
            enemyHPBar = myView.findViewById(R.id.enemyHPBar);
            adjustHPBars(enemyHPBar, leadEnemyPoke, enemyPokeAndHP);

            commentaryBox = myView.findViewById(R.id.commentaryBox);
            commentary = "";

            moveButtons.get(0).setOnClickListener((v) -> resolveSpeedTiers(0));
            if(moveButtons.size() > 1)
                moveButtons.get(1).setOnClickListener((v) -> resolveSpeedTiers(1));
            if(moveButtons.size() > 2)
                moveButtons.get(2).setOnClickListener((v) -> resolveSpeedTiers(2));
            if(moveButtons.size() > 3)
                moveButtons.get(3).setOnClickListener((v) -> resolveSpeedTiers(3));
        });
        adb.show();
        return myView;
    }

    /**
     * A helper method that sets all of the images to the desired Pokémon. This is here for code reuse.
     * @param ivs The list of images.
     * @param pTeam The Pokémon to show.
     */
    private void setImages(ArrayList <ImageView> ivs, ArrayList<Pokemon> pTeam) {
        for (int i = 0; i < pTeam.size(); i++)
            setImage(ivs.get(i), pTeam.get(i));
    }

    /**
     * Sets each image based on the Pokémon's name. This must be done in this clunky way with the
     * switch statement because the R.drawable.NAME is not a String.
     * @param iv The image to be displayed.
     * @param poke The Pokémon to be shown.
     */
    private void setImage(ImageView iv, Pokemon poke) {
        String name = poke.getName();
        switch (name) {
            case "Bulbasaur":
                iv.setImageResource(R.drawable.bulbasaur);
                break;
            case "Charmander":
                iv.setImageResource(R.drawable.charmander);
                break;
            case "Chikorita":
                iv.setImageResource(R.drawable.chikorita);
                break;
            case "Cyndaquil":
                iv.setImageResource(R.drawable.cyndaquil);
                break;
            case "Snubbull":
                iv.setImageResource(R.drawable.snubbull);
                break;
            case "Squirtle":
                iv.setImageResource(R.drawable.squirtle);
                break;
            case "Totodile":
                iv.setImageResource(R.drawable.totodile);
                break;
            case "Voltorb":
                iv.setImageResource(R.drawable.voltorb);
                break;
            case "Wooper":
                iv.setImageResource(R.drawable.wooper);
                break;
            case "Treecko":
                iv.setImageResource(R.drawable.treecko);
                break;
            case "Torchic":
                iv.setImageResource(R.drawable.torchic);
                break;
            case "Mudkip":
                iv.setImageResource(R.drawable.mudkip);
                break;
            default:
                throw new IllegalArgumentException(name + " has not been defined yet!");
            // Add more cases later for the simulator later //TODO
        }
    }

    /**
     * A helper method used to reduce repetitive code in resolving speed tiers. This simply compares speeds
     * And resolves moves in that order, barring additional effects like priority. It also considers if
     * The Pokemon have fainted before or during the turn during move resolution.
     * @param moveIndex The index of the chosen move.
     */
    private void resolveSpeedTiers(int moveIndex) {
        int playerSpeed = (int) (leadPlayerPoke.getInitStats()[5] * NORMAL_STAT_STAGES[leadPlayerPoke.getStatStages()[5]]);
        int enemySpeed = (int) (leadEnemyPoke.getInitStats()[5] * NORMAL_STAT_STAGES[leadEnemyPoke.getStatStages()[5]]);
        if(playerSpeed > enemySpeed) {
            movePlayerFirst(moveIndex);
        }
        else if(playerSpeed < enemySpeed) {
            moveEnemyFirst(moveIndex);
        }
        else { //speed tie
            int randomNum = rand.nextInt(2); // This will properly give a fifty 50% chance of 0 and 1
            if (randomNum == 0) {
                movePlayerFirst(moveIndex);
            }
            else {
                moveEnemyFirst(moveIndex);
            }
        }
    } // Resolve priority and end-of-turn effects //TODO

    /**
     * A helper method used to reduce repetitive code in resolving speed tiers, which is primarily concerned
     * With proper move resolution order. Most notably, this method is one of the main places that considers if
     * The Pokemon have fainted before or during the turn during move resolution.
     * @param moveIndex The index of the chosen move.
     */
    private void movePlayerFirst(int moveIndex) {
        if(leadPlayerPoke.getInitStats()[0] > 0) {
            resolveMoveType(moveIndex, true);  // Player
        }
        if(leadEnemyPoke.getInitStats()[0] > 0) {
            resolveAIMove(true); // Enemy
        }
    }

    /**
     * A helper method used to reduce repetitive code in resolving speed tiers, which is primarily concerned
     * With proper move resolution order. Most notably, this method is one of the main places that considers if
     * The Pokemon have fainted before or during the turn during move resolution.
     * @param moveIndex The index of the chosen move.
     */
    private void moveEnemyFirst(int moveIndex) {
        if(leadEnemyPoke.getInitStats()[0] > 0) {
            resolveAIMove(false); // Enemy
        }
        if(leadPlayerPoke.getInitStats()[0] > 0) {
            resolveMoveType(moveIndex, false);  // Player
        }
    }

    /**
     * A helper method that checks if the chosen move is valid and passes it off to the appropriate resolveMove
     * method based whether or not it is an AttackingMove.
     * @param index The index of the chosen move.
     * @param didPlayerMoveFirst True if the player moved first this round, and false otherwise,
     *                           which is important for calculating BP and certain effects in some cases.
     */
    private void resolveMoveType(int index, boolean didPlayerMoveFirst) {
        Move genericMove = leadPlayerPoke.getMoves().get(index);
        if(genericMove == null || genericMove.getName() == null)
            throw new IllegalStateException("The move at index " + index + " is not valid!");
        if (!genericMove.isAttackingMove()) {
            StatusMove move = (StatusMove) genericMove;
            resolveMove(move, leadPlayerPoke, leadEnemyPoke, true, didPlayerMoveFirst);
        }
        else {
            AttackingMove move = (AttackingMove) genericMove;
            resolveMove(move, leadPlayerPoke, leadEnemyPoke, true, didPlayerMoveFirst);
        }
    }

    /**
     * This method determines the AI's move selection based on the opponent. Essentially, it picks the neutral
     * damage or better moves, and it picks the highest BP and effectiveness out of those. If there are no neutral or better moves,
     * it picks the best useful status move. If there is no good status move and no neutral move, the Pokémon switches out.
     * @param didPlayerMoveFirst True if the player moved first this round, and false otherwise,
     *                           which is important for calculating BP and certain effects in some cases.
     */
    private void resolveAIMove(boolean didPlayerMoveFirst) {
        ArrayList<Move> moves = leadEnemyPoke.getMoves(), firstMoveChoices = new ArrayList<>();
        int numAdded = 0;
        for (Move m : moves)  // Check for neutral/super-effective hits
            if (m.isAttackingMove() && checkTypeMatchups(leadPlayerPoke.getType(), m.getType(), 1, leadEnemyPoke) >= 1) {
                firstMoveChoices.add(m);
                numAdded++;
            }

        if (numAdded == 0) { // Use status moves if no moves hit for neutral or better
            for (Move m : moves)
                if (!m.isAttackingMove()) { // Likely add other arguments later //TODO
                    firstMoveChoices.add(m);
                    numAdded++;
                }
            if(numAdded == 0) {
                for (Move m : moves)  // Check for stab and avoid 4x resists as a last case
                    if(m.isAttackingMove() && checkTypeMatchups(leadPlayerPoke.getType(), m.getType(), 1, leadEnemyPoke) >= 0.5) {
                        firstMoveChoices.add(m);
                        numAdded++;
                    }

                if(numAdded == 0) {
                    // Switch out, which will be implemented later once 1v1 fights work properly //TODO
                }
                else if (numAdded == 1)
                    resolveMove((AttackingMove) firstMoveChoices.get(0), leadEnemyPoke, leadPlayerPoke, false, didPlayerMoveFirst);
                else
                    compareBP(firstMoveChoices, didPlayerMoveFirst);
            }
            else if(numAdded == 1)
                resolveMove((AttackingMove) firstMoveChoices.get(0), leadEnemyPoke, leadPlayerPoke, false, didPlayerMoveFirst);
            else {
                int randomNum = rand.nextInt(numAdded); // Choose randomly from the status moves until status moves are implemented, at least for now //TODO
                resolveMove((StatusMove) firstMoveChoices.get(randomNum), leadEnemyPoke, leadPlayerPoke, false, didPlayerMoveFirst);
            }
        }
        else if(numAdded == 1) // Handle the simple case with one neutral or better attack
            resolveMove((AttackingMove) firstMoveChoices.get(0), leadEnemyPoke, leadPlayerPoke, false, didPlayerMoveFirst);
        else { // Handle the more complex case with more than one neutral or better attack
            ArrayList<Move> secondMoveChoices = new ArrayList<>();
            int numAdded2 = 0;
            for(int i = 0; i < numAdded; i++)
                if (checkTypeMatchups(leadPlayerPoke.getType(), firstMoveChoices.get(i).getType(), 1, leadEnemyPoke) >= 1.5) {
                    secondMoveChoices.add(numAdded2++, firstMoveChoices.get(i));
                }

            if(numAdded2 == 0)
                compareBP(firstMoveChoices, didPlayerMoveFirst);
            else if(numAdded2 == 1)
                resolveMove((AttackingMove) secondMoveChoices.get(0), leadEnemyPoke, leadPlayerPoke, false, didPlayerMoveFirst);
            else
                compareBP(secondMoveChoices, didPlayerMoveFirst);
        }
    }

    /**
     * Selects the highest BP move among the AI's choices in the provided array. If there are multiple choices,
     * it picks randomly between them.
     * @param moveChoices The array of possible moves for the AI.
     * @param didPlayerMoveFirst True if the player moved first this round, and false otherwise,
     *                           which is important for calculating BP and certain effects in some cases.
     */
    private void compareBP(ArrayList<Move> moveChoices, boolean didPlayerMoveFirst) {
        Move[] equalBPMoves = new Move[4];
        int numAdded = 0; //this is the effective length of the array
        AttackingMove maxMove = (AttackingMove) moveChoices.get(0);
        equalBPMoves[numAdded++] = maxMove;
        double maxBPModifier = checkTypeMatchups(leadPlayerPoke.getType(), maxMove.getType(), 1, leadEnemyPoke);
        int maxBP = (int) (calculateBP(maxMove, leadEnemyPoke, leadPlayerPoke, didPlayerMoveFirst) * maxBPModifier);

        for(int i = 1; i < moveChoices.size(); i++) { //compare each move
            AttackingMove move = (AttackingMove) moveChoices.get(i);
            double modifier = checkTypeMatchups(leadPlayerPoke.getType(), move.getType(), 1, leadEnemyPoke);
            int newBP = (int) (calculateBP(move, leadEnemyPoke, leadPlayerPoke, didPlayerMoveFirst) * modifier);
            if(newBP > maxBP) {
                maxBP = newBP; //reset the maxBP and the array of equal BPs
                equalBPMoves = new Move[4];
                numAdded = 0;
                equalBPMoves[numAdded++] = move;
            }
            else if((calculateBP(maxMove, leadEnemyPoke, leadPlayerPoke, didPlayerMoveFirst) * maxBPModifier) ==
                    (calculateBP(move, leadEnemyPoke, leadPlayerPoke, didPlayerMoveFirst) * modifier))
                equalBPMoves[numAdded++] = move; //add the duplicate BP to the array
        }
        if(numAdded == 1)
            resolveMove(maxMove, leadEnemyPoke, leadPlayerPoke, false, didPlayerMoveFirst);
        else {
            int randomNum = rand.nextInt(numAdded); //choose randomly from the equal BP moves
            resolveMove((AttackingMove) equalBPMoves[randomNum], leadEnemyPoke, leadPlayerPoke, false, didPlayerMoveFirst);
        }
    }

    /**
     * Resolves an attacking move used by the moveUser on the moveTarget. This contains the main logic for
     * damage and additional effects, although its functions will be increasing as more abilities and
     * additional effects come into play. IsPlayerTheUser indicates whether or not the moveUser is the player (which
     * determines which text boxes, hp bars, and other important objects on the screen to use).
     * @param move The attacking move to be used.
     * @param moveUser The user of the move.
     * @param moveTarget The target of the move.
     * @param isPlayerTheUser Determines whether or not the moveUser is on the player's team.
     * @param didPlayerMoveFirst True if the player moved first this round, and false otherwise,
                                 which is important for calculating BP and certain effects in some cases.
     */
    private void resolveMove(AttackingMove move, Pokemon moveUser, Pokemon moveTarget, boolean isPlayerTheUser, boolean didPlayerMoveFirst) {
        int acc = rand.nextInt(100);
        switch(weather) { //finish this section later //TODO
            case 1: case 5:
                if(move.getName().equals("Thunder") || move.getName().equals("Hurricane")) {
                    acc = -1; // Ensure that it hits
                    break;
                }
            case 4:
                if(move.getName().equals("Blizzard")) {
                    acc -= 30; // Increase accuracy
                    break;
                }
            default:
                break;
        }
        int moveAcc = move.getAccuracy(); // Save the move accuracy primarily for moves that never miss like Aerial Ace
        if (moveAcc == 1000 || ((acc + 1) <= (moveAcc * moveUser.getInitStats()[6] * ACCURACY_STAT_STAGES[moveUser.getStatStages()[6]]
                / moveTarget.getInitStats()[7] * ACCURACY_STAT_STAGES[moveUser.getStatStages()[7]]) && !isInvulnerable(moveTarget, move))) {

            if(takesTwoTurns(move, moveUser, weather)) // Check for two-turn moves
                return;
            //first part of formula
            double damage = (2 * moveUser.getLevel() + 10) * calculateBP(move, moveUser, moveTarget, didPlayerMoveFirst) / 250.0;
            Log.d("AddPersonActivity", damage + "");

            boolean isCrit = isCriticalHit(move, moveUser, moveTarget);
            damage = getStatsModifier(move, moveUser, moveTarget, damage, isCrit); //compare attacking to defensive values
            Log.d("AddPersonActivity", damage + "");

            damage += 2;
            String mType = move.getType();
            String eType = moveTarget.getType();
            damage = checkTypeMatchups(eType, mType, damage, moveUser); //adjust based on type matchups
            Log.d("AddPersonActivity", damage + "");

            if(isCrit)
                damage *= 1.5;
            if(move.isPhysical() && moveUser.getNonVolStatus().contains("Burned"))
                damage /= 2; //consider more factors like abilities and items later //TODO
            damage = resolveWeatherEffects(move, moveUser, moveTarget, damage, weather);
            Log.d("AddPersonActivity", damage + "");

            int randomNum = rand.nextInt(16) + 85; //random modifier between .85 and 1
            damage *= randomNum / 100.0;
            Log.d("AddPersonActivity", damage + "");

            int roundedDownDamage = (int) damage; //round down
            int initHP = moveTarget.getInitStats()[0];
            int actualDamage = Math.min(roundedDownDamage, initHP);
            moveTarget.setStat(initHP - actualDamage, 0); //Reduce HP

            ProgressBar tempBar = enemyHPBar;
            TextView tempV = enemyPokeAndHP; //choose the appropriate hp bar and text box
            if(!isPlayerTheUser) {
                tempBar = playerHPBar;
                tempV = playerPokeAndHP;
            }
            adjustHPBars(tempBar, moveTarget, tempV); //adjust the hp bars

            resolveAdditionalEffects(move, moveUser, moveTarget, isPlayerTheUser, actualDamage); //stat changes, status changes, other
        }
    }

    /**
     * Checks to see if this move can hit through the target Pokémon's semi-invulnerable state, if applicable.
     * @param moveTarget The Pokémon who will take the next attack.
     * @param move The chosen move.
     * @return True if the target Pokémon is invulnerable to the chosen move, false otherwise
     */
    private boolean isInvulnerable(Pokemon moveTarget, AttackingMove move) {
        int invulnCode = moveTarget.getInvulnCode();
        return invulnCode != 0 && !move.getIBList().contains(invulnCode);
    }

    /**
     * Determines whether or not the moveUser critically hits against the moveTarget with the given move. This
     * contains the logic for dealing with increased critical chances.
     * @param move The move to be used.
     * @param moveUser The user of the move.
     * @param moveTarget The target of the move (relevant for abilities)
     * @return True if a critical hit occurs, false otherwise.
     */
    private boolean isCriticalHit(AttackingMove move, Pokemon moveUser, Pokemon moveTarget) {
        int critState = moveUser.getCritState();
        if(move.getAdditionalEffects().contains("high critical chance"))
            critState++;
        int critChance = 16; //critical chance is 1/16 unless it is increased in some way
        if(critState == 1)
            critChance /= 2;
        else if(critState == 2)
            critChance /= 8;
        else if(critState != 0)
            return true;
        int randomNum = rand.nextInt(critChance);
        return randomNum == 0;
    } //consider abilities //TODO

    /**
     * Resolves any weather effects, like rain's boosts or sun's boosts. Eventually, this will also
     * deal with abilities and items as well.
     * @param move The move used.
     * @param moveUser The user of the move.
     * @param moveTarget The target of the move.
     * @param weather The current weather code.
     * @return The current damage modified by the weather.
     */
    private double resolveWeatherEffects(AttackingMove move, Pokemon moveUser, Pokemon moveTarget, double damage, int weather) {
        switch(weather) {
            case 1:
                if(move.getType().equals("Water"))
                    damage *= 1.5;
                else if(move.getType().equals("Fire"))
                    damage *= 0.5;
                break;
            case 2:
                if(move.getType().equals("Fire"))
                    damage *= 1.5;
                else if(move.getType().equals("Water"))
                    damage *= 0.5;
                break;
        }
        //add in other effects later, such as abilities //TODO
        return damage;
    }

    /**
     * Adjusts the HP bar and the HP text box of the injured Pokémon. This can be used whenever any HP change
     * occurs, even not as the effect of a move.
     * @param hpBar The HP bar of the injured Pokémon.
     * @param injuredPoke The Pokémon that was injured (or gained health, technically).
     * @param hpText The HP text of the injured Pokémon.
     */
    private void adjustHPBars(ProgressBar hpBar, Pokemon injuredPoke, TextView hpText) {
        double hpPercentage = (double) injuredPoke.getInitStats()[0] / injuredPoke.getMaxHP(); //find HP percentage
        long roundedHPPercent = Math.round(hpPercentage * 100); //round hpPercentage

        hpBar.setProgress((int) roundedHPPercent);  //changes HP bar percentage
        if(hpBar.getProgress() <= 50 && hpBar.getProgress() >= 25) {
            hpBar.getProgressDrawable().setColorFilter(BlendModeColorFilterCompat.createBlendModeColorFilterCompat(Color.rgb(255, 255, 25), BlendModeCompat.SRC_IN));
        }
        else if(hpBar.getProgress() <= 25) {  //change color
            hpBar.getProgressDrawable().setColorFilter(BlendModeColorFilterCompat.createBlendModeColorFilterCompat(Color.rgb(255, 50, 50), BlendModeCompat.SRC_IN));
        }
        else {
            hpBar.getProgressDrawable().setColorFilter(BlendModeColorFilterCompat.createBlendModeColorFilterCompat(Color.rgb(25, 255, 25), BlendModeCompat.SRC_IN));
        }
        String commonHpPrefix = injuredPoke.getName() + " HP: ";
        if (roundedHPPercent >= 0) {
            hpText.setText(String.format("%s%s%%", commonHpPrefix, roundedHPPercent)); //set the text to the percentage
        }
        else {
            hpText.setText(String.format("%s0%%", commonHpPrefix)); //Deal with fainting once it is at most zero //TODO
        }
    }

    /**
     * Resolves a status move used by the moveUser on the moveTarget, which is not yet implemented.
     * For reference, IsPlayerTheUser indicates whether or not the moveUser is the player, which
     * determines which text boxes, hp bars, and other important objects on the screen to use.
     * @param move The attacking move to be used.
     * @param moveUser The user of the move.
     * @param moveTarget The target of the move.
     * @param isPlayerTheUser Determines whether or not the moveUser is on the player's team.
     * @param didPlayerMoveFirst True if the player moved first this round, and false otherwise,
     *                           which is important for calculating BP and certain effects in some cases.
     */
    private void resolveMove(StatusMove move, Pokemon moveUser, Pokemon moveTarget, boolean isPlayerTheUser, boolean didPlayerMoveFirst) {
        //Actually handle these moves //TODO
    }

    /**
     * Checks if this move takes two turns. If it does not take two turns, it returns false, but if it does,
     * it performs the necessary actions to start the two-turn move, disabling the buttons and setting up the correct
     * semi-invulnerability states. It detects if it is in a two-turn move and undoes its actions in the previous turn, if
     * this is the case. It detects if SolarBeam should take two turns. However, the functionality of this has not been
     * tested, so it may need edits.
     * @param move The move to be checked.
     * @param moveUser The user of the move.
     * @param weather The current weather.
     * @return True if this move will be taking a charging turn, false otherwise.
     */
    private boolean takesTwoTurns(AttackingMove move, Pokemon moveUser, int weather) {
        int ttCode = move.getTwoTurnCode();
        if(ttCode == 2 && (weather == 2 || weather == 6))
            return false; //check this //TODO
        if(ttCode != 100) {
            currTTCode = ttCode; //store initial ttCode
            if(ttCode == 0)
                return false;
            else {
                for(int i = 0; i < moveButtons.size(); i++)
                    if (moveButtons.get(i) != null && !moveButtons.get(i).getText().toString().equals(move.getName()))
                        moveButtons.get(i).setEnabled(false);  //disable all move buttons
                move.setTwoTurnCode(100); //set ttCode to charging turn
                if(ttCode > 2)
                    moveUser.setInvulnCode(ttCode);
                return true;
            }
        }
        else { //deal with yawn and interruptions //TODO
            move.setTwoTurnCode(currTTCode); //reset two-turn code and invulnerability
            moveUser.setInvulnCode(0);
            for(int i = 0; i < moveButtons.size(); i++)
                if(!moveButtons.get(i).isEnabled()) //re-enable move buttons
                    moveButtons.get(i).setEnabled(true);
            return false;
        }
    }

    /**
     * Calculates the BP of the move given the circumstances. This encapsulates calculations relating to
     * changing BP (like for Eruption, Payback, and other moves). This does not include STAB or other modifiers;
     * those are calculated later.
     * @param move The move for which BP is to be calculated.
     * @param moveUser The user of the move.
     * @param moveTarget The target of the move.
     * @param didPlayerMoveFirst True if the player moved first this round, and false otherwise,
     *                           Which is important for calculating BP and certain effects in some cases.
     * @return The rounded down value of the new BP (by truncating it back to int).
     */
    private int calculateBP(AttackingMove move, Pokemon moveUser, Pokemon moveTarget, boolean didPlayerMoveFirst) {
        if(move.hasBPCode(1))
            return move.getBP() * moveUser.getInitStats()[0] / moveUser.getMaxHP(); //eruption/water spout
        if(move.hasBPCode(2) && moveTarget.getInvulnCode() == 6)
            return move.getBP() * 2 ; //earthquake with dig
        if(move.hasBPCode(3) && !didPlayerMoveFirst)
            return move.getBP() * 2; //payback
        if(move.hasBPCode(4) && (weather != 2 && weather != 0 && weather != 6 && weather != 7))
            return move.getBP() / 2; //solar beam in non-sun, non air current, or no weather
        //add more codes later with more moves //TODO
        return move.getBP();
    }

    /**
     * Modifies the damage based on the attacking stats of the attacker and the defending stats of the
     * defender, for the most part. It chooses the correct defenses to use based on the move's type, and
     * even considers abnormalities like Psyshock, Secret Sword, and Foul Play. This also considers Rock
     * types in Sandstorm.
     * @param move The move that is being used.
     * @param moveUser The attacking Pokémon.
     * @param moveTarget The defending Pokémon.
     * @param damage The damage total before this modifier.
     * @return The damage after it has been modified by this method.
     */
    private double getStatsModifier(AttackingMove move, Pokemon moveUser, Pokemon moveTarget, double damage, boolean isCrit) {
        if (move.getName().equals("Foul Play")) { //Foul Play
            if (isCrit && moveTarget.getStatStages()[1] <= 0)
                damage *= moveTarget.getInitStats()[1]; //choose greater attack on crit (between 0 and -something)
            else
                damage *= moveTarget.getInitStats()[1] * NORMAL_STAT_STAGES[moveTarget.getStatStages()[1]]; // check this later //TODO
            if (isCrit && moveTarget.getStatStages()[2] >= 0)
                damage /= moveTarget.getInitStats()[2];
            else              //choose lower defense on crit (between 0 and +something)
                damage /= moveTarget.getInitStats()[2] * NORMAL_STAT_STAGES[moveTarget.getStatStages()[2]];
        }

        else if (move.isPhysical()) { //normal physical move
            if (isCrit && moveUser.getStatStages()[1] <= 0)
                damage *= moveUser.getInitStats()[1];
            else
                damage *= moveUser.getInitStats()[1] * NORMAL_STAT_STAGES[moveUser.getStatStages()[1]];
            if (isCrit && moveTarget.getStatStages()[2] >= 0)
                damage /= moveTarget.getInitStats()[2];
            else
                damage /= moveTarget.getInitStats()[2] * NORMAL_STAT_STAGES[moveTarget.getStatStages()[2]];

        }
        else if (!move.isPhysical() && !move.getName().equals("Psyshock") &&
                !move.getName().equals("Psystrike") && !move.getName().equals("Secret Sword")) {
            if (isCrit && moveUser.getStatStages()[3] <= 0)
                damage *= moveUser.getInitStats()[3];
            else
                damage *= moveUser.getInitStats()[3] * NORMAL_STAT_STAGES[moveUser.getStatStages()[3]];
            if (isCrit && moveTarget.getStatStages()[4] >= 0)
                damage /= moveTarget.getInitStats()[4];
            else
                damage /= moveTarget.getInitStats()[4] * NORMAL_STAT_STAGES[moveTarget.getStatStages()[4]];
            if(moveTarget.getType().contains("Rock") && weather == 3)
                damage /= 1.5; //sandstorm Sp.Def. modifier

        } //normal special move

        else { //Psyshock, Psystrike, Secret Sword
            if (isCrit && moveUser.getStatStages()[3] <= 0)
                damage *= moveUser.getInitStats()[3];
            else
                damage *= (double) moveUser.getInitStats()[3] * NORMAL_STAT_STAGES[moveUser.getStatStages()[3]];
            if (isCrit && moveTarget.getStatStages()[2] >= 0)
                damage /= moveTarget.getInitStats()[2];
            else
                damage /= moveTarget.getInitStats()[2] * NORMAL_STAT_STAGES[moveTarget.getStatStages()[2]];
        }
        return damage;
    }

    /**
     * Modifies the specified damage based on the strengths and weaknesses of the move against the enemy type.
     * This also adds in a STAB modifier if applicable.
     * @param eType The enemy's type.
     * @param mType The move's type.
     * @param damage The starting damage.
     * @param moveUser The Pokémon using the move.
     * @return A modified damage based on type effectiveness and STAB.
     */
    private double checkTypeMatchups(String eType, String mType, double damage, Pokemon moveUser) {
        switch (mType) { // add modifiers for type effectiveness
            case "Dark":
                if (eType.contains("Ghost"))
                    damage *= 2;
                if (eType.contains("Psychic")) //cumbersome but necessary
                    damage *= 2;
                if (eType.contains("Dark"))
                    damage /= 2;
                if (eType.contains("Fighting"))
                    damage /= 2;
                if (eType.contains("Fairy"))
                    damage /= 2;
                break;
            case "Dragon":
                if (eType.contains("Dragon"))
                    damage *= 2;
                if (eType.contains("Steel"))
                    damage /= 2;
                if (eType.contains("Fairy"))
                    damage = 0;
                break;
            case "Electric":
                if (eType.contains("Water"))
                    damage *= 2;
                if (eType.contains("Flying"))
                    damage *= 2;
                if (eType.contains("Ground"))
                    damage = 0;
                if (eType.contains("Electric"))
                    damage /= 2;
                if (eType.contains("Dragon"))
                    damage /= 2;
                if (eType.contains("Grass"))
                    damage /= 2;
                break;
            case "Fairy":
                if (eType.contains("Dark"))
                    damage *= 2;
                if (eType.contains("Dragon"))
                    damage *= 2;
                if (eType.contains("Fighting"))
                    damage *= 2;
                if (eType.contains("Steel"))
                    damage /= 2;
                if (eType.contains("Fire"))
                    damage /= 2;
                if (eType.contains("Poison"))
                    damage /= 2;
                break;
            case "Fire":
                if (eType.contains("Grass"))
                    damage *= 2;
                if (eType.contains("Steel"))
                    damage *= 2;
                if (eType.contains("Ice"))
                    damage *= 2;
                if (eType.contains("Bug"))
                    damage *= 2;
                if (eType.contains("Rock"))
                    damage /= 2;
                if (eType.contains("Water"))
                    damage /= 2;
                if (eType.contains("Dragon"))
                    damage /= 2;
                if (eType.contains("Fire"))
                    damage /= 2;
                break;
            case "Fighting":
                if (eType.contains("Rock"))
                    damage *= 2;
                if (eType.contains("Steel"))
                    damage *= 2;
                if (eType.contains("Ice"))
                    damage *= 2;
                if (eType.contains("Normal"))
                    damage *= 2;
                if (eType.contains("Dark"))
                    damage *= 2;
                if (eType.contains("Fairy"))
                    damage /= 2;
                if (eType.contains("Flying"))
                    damage /= 2;
                if (eType.contains("Poison"))
                    damage /= 2;
                if (eType.contains("Ghost"))
                    damage = 0;
                break;
            case "Grass":
                if (eType.contains("Rock"))
                    damage *= 2;
                if (eType.contains("Ground"))
                    damage *= 2;
                if (eType.contains("Water"))
                    damage *= 2;
                if (eType.contains("Grass"))
                    damage /= 2;
                if (eType.contains("Fire"))
                    damage /= 2;
                if (eType.contains("Dragon"))
                    damage /= 2;
                if (eType.contains("Flying"))
                    damage /= 2;
                if (eType.contains("Poison"))
                    damage /= 2;
                if (eType.contains("Bug"))
                    damage /= 2;
                if (eType.contains("Steel"))
                    damage /= 2;
                break;
            case "Ground":
                if (eType.contains("Rock"))
                    damage *= 2;
                if (eType.contains("Steel"))
                    damage *= 2;
                if (eType.contains("Electric"))
                    damage *= 2;
                if (eType.contains("Poison"))
                    damage *= 2;
                if (eType.contains("Fire"))
                    damage *= 2;
                if (eType.contains("Flying"))
                    damage = 0;
                if (eType.contains("Grass"))
                    damage /= 2;
                if (eType.contains("Bug"))
                    damage /= 2;
                break;
            case "Ice":
                if (eType.contains("Ground"))
                    damage *= 2;
                if (eType.contains("Dragon"))
                    damage *= 2;
                if (eType.contains("Grass"))
                    damage *= 2;
                if (eType.contains("Flying"))
                    damage *= 2;
                if (eType.contains("Fire"))
                    damage /= 2;
                if (eType.contains("Ice"))
                    damage /= 2;
                if (eType.contains("Steel"))
                    damage /= 2;
                if (eType.contains("Water"))
                    damage /= 2;
                break;
            case "Poison":
                if (eType.contains("Grass"))
                    damage *= 2;
                if (eType.contains("Fairy"))
                    damage *= 2;
                if (eType.contains("Ground"))
                    damage /= 2;
                if (eType.contains("Ghost"))
                    damage /= 2;
                if (eType.contains("Poison"))
                    damage /= 2;
                if (eType.contains("Rock"))
                    damage /= 2;
                if (eType.contains("Steel"))
                    damage = 0;
                break;
            case "Water":
                if (eType.contains("Rock"))
                    damage *= 2;
                if (eType.contains("Fire"))
                    damage *= 2;
                if (eType.contains("Ground"))
                    damage *= 2;
                if (eType.contains("Water"))
                    damage /= 2;
                if (eType.contains("Grass"))
                    damage /= 2;
                if (eType.contains("Dragon"))
                    damage /= 2;
                break;
            case "Flying":
                if (eType.contains("Bug"))
                    damage *= 2;
                if (eType.contains("Fighting"))
                    damage *= 2;
                if (eType.contains("Grass"))
                    damage *= 2;
                if (eType.contains("Electric"))
                    damage /= 2;
                if (eType.contains("Rock"))
                    damage /= 2;
                if (eType.contains("Steel"))
                    damage /= 2;
                break;
            case "Rock":
                if (eType.contains("Bug"))
                    damage *= 2;
                if (eType.contains("Fire"))
                    damage *= 2;
                if (eType.contains("Flying"))
                    damage *= 2;
                if (eType.contains("Ice"))
                    damage /= 2;
                if (eType.contains("Fighting"))
                    damage /= 2;
                if (eType.contains("Ground"))
                    damage /= 2;
                if (eType.contains("Steel"))
                    damage /= 2;
                break;
            default:
                throw new IllegalArgumentException(mType + " is not yet a valid offensive type in this simulator!");
            //add more offensive types //TODO
        }
        if (moveUser.getType().contains(mType)) //STAB bonus
            damage *= 1.5;
        return damage;
    }

    /**
     * Resolves the additional effect of the move, which include stat changes and both types of status changes.
     * This contains the logic for the probability of these effects occurring as well.
     * @param move The move used.
     * @param moveUser The user of the move.
     * @param moveTarget The target of the move.
     * @param isPlayerTheUser True if the player is the moveUser, false otherwise.
     * @param moveDamage The damage inflicted by the move, which is relevant at this stage for HP-draining and recoil-inducing moves.
     */
    private void resolveAdditionalEffects(AttackingMove move, Pokemon moveUser, Pokemon moveTarget, boolean isPlayerTheUser, int moveDamage) {
        int randomNum = rand.nextInt(100) + 1;
        if(randomNum <= move.getAddEffectChance()) {
            changeStats(move, moveUser, moveTarget, isPlayerTheUser, moveDamage);
            changeNonVolStatus(move, moveUser, moveTarget, isPlayerTheUser);
            changeVolStatus(move, moveUser, moveTarget, isPlayerTheUser);
        }
    }

    /**
     * Determines the necessary stat changes for the used move on the moveUser or the moveTarget. This deals with HP changes
     * as well.
     * @param move The used move.
     * @param moveUser The user of the move,
     * @param moveTarget The target of the move.
     * @param isPlayerTheUser Whether or not the player used this move.
     * @param moveDamage The damage inflicted by the move, which is relevant at this stage for HP-draining and recoil-inducing moves.
     */
    private void changeStats(Move move, Pokemon moveUser, Pokemon moveTarget, boolean isPlayerTheUser, int moveDamage) {
        Integer[] statChanges = move.getStatChanges();
        if(statChanges == null) {
            return;
        }
        boolean cus = move.changesUserStats();
        for(int i = 0; i < 6; i++) {
            int change = statChanges[i];
            if(change == 0) {
                continue;
            }
            // Treat HP as a special case since it does not follow a -6 to 6 scale that maps to a percentage of the original value;
            // Instead, the most standard case is based on a percentage from recoil or HP-draining moves
            // Then there are plenty of more exceptional cases
            if (i == 0) {
                int absChange = Math.abs(change);
                //Verify first that this is a standard damage percentage-based change (recoil/HP-draining)
                if(absChange <= 100 && absChange > 0) {
                    if (cus) {
                        moveUser.getInitStats()[0] += (int) (change / 100.0 * moveDamage);
                        if(moveUser.getInitStats()[0] > moveUser.getMaxHP()) {
                            moveUser.getInitStats()[0] = moveUser.getMaxHP();
                        }
                        if(isPlayerTheUser) {
                            adjustHPBars(playerHPBar, moveUser, playerPokeAndHP);
                        }
                        else {
                            adjustHPBars(enemyHPBar, moveUser, enemyPokeAndHP);
                        }
                    }
                    else {
                        moveTarget.getInitStats()[0] += (int) (change / 100.0 * moveTarget.getMaxHP());
                        if(moveTarget.getInitStats()[0] > moveTarget.getMaxHP()) {
                            moveTarget.getInitStats()[0] = moveTarget.getMaxHP();
                        }
                        if (isPlayerTheUser) {
                            adjustHPBars(playerHPBar, moveTarget, playerPokeAndHP);
                        }
                        else {
                            adjustHPBars(enemyHPBar, moveTarget, enemyPokeAndHP);
                        }
                    }
                }
                //deal with other cases like Sonic Boom or Nature's Madness later //TODO
            }
            else if (cus) {
                resolveStatChange(i, change, moveUser, isPlayerTheUser);
            }
            else {
                resolveStatChange(i, change, moveTarget, !isPlayerTheUser);
            }
        }
    }

    /**
     * Resolves a stat change (except for HP). It changes the status texts on the screen,
     * and it also catches stat changes beyond +/- 6.
     * @param statIndex The index of the stat to be changed.
     * @param numStages The number of stages to increase or decrease the stat.
     * @param statChanger The Pokémon whose stats are to be increased.
     * @param isPlayerChangingStats True if the statChanger is the player's Pokémon, false otherwise.
     */
    private void resolveStatChange(int statIndex, int numStages, Pokemon statChanger, boolean isPlayerChangingStats) {
        if(statChanger.getInitStats()[0] == 0) {
            return; // Skip the stat change if the Pokemon has fainted
        }
        statChanger.getStatStages()[statIndex] += numStages; // Change the stat stage
        if(statChanger.getStatStages()[statIndex] > 12) {
            statChanger.getStatStages()[statIndex] = 12; // Catch out of bounds above +6
        }
        else if(statChanger.getStatStages()[statIndex] < 0)   {
            statChanger.getStatStages()[statIndex] = 0; // Catch out of bounds below -6
        }
        String statName;
        double modifier;
        int currStage = statChanger.getStatStages()[statIndex];
        switch(statIndex) {
            case 1:
                statName = "Att.";
                modifier = NORMAL_STAT_STAGES[currStage];
                break;
            case 2:
                statName = "Def.";
                modifier = NORMAL_STAT_STAGES[currStage]; // Pick the correct stat and modifier
                break;
            case 3:
                statName = "Sp. Att.";
                modifier = NORMAL_STAT_STAGES[currStage];
                break;
            case 4:
                statName = "Sp. Def.";
                modifier = NORMAL_STAT_STAGES[currStage];
                break;
            case 5:
                statName = "Speed";
                modifier = NORMAL_STAT_STAGES[currStage];
                break;
            case 6:
                statName = "Acc.";
                modifier = ACCURACY_STAT_STAGES[currStage];
                break;
            case 7:
                statName = "Evas.";
                modifier = ACCURACY_STAT_STAGES[currStage];
                break;
            default:
                throw new IllegalArgumentException(statIndex + " is not a possible index in this scenario!");
        }
        changeStatsText(statName, modifier, isPlayerChangingStats);
    }

    /**
     * Changes the stats text underneath the HP bars. This method is somewhat complex currently because
     * it deals with manipulating strings and only displaying the total change for each stat. This needs to
     * be reviewed more because the logic behind it is a bit murky.
     * @param stat The stat that is being changed (in String form).
     * @param modifier The multiplier to display.
     * @param isPlayerChangingStats True if the player's Pokémon's stats are changing, false otherwise.
     */
    private void changeStatsText(String stat, double modifier, boolean isPlayerChangingStats) {
        StringBuilder result = new StringBuilder();
        TextView tempV = playerConditions;
        if(!isPlayerChangingStats)
            tempV = enemyConditions;
        String csc = tempV.getText() + "";
        String statModifier = stat + " x" + String.format(Locale.US, "%.2f",modifier) + " ";
        //check to see if a stat has already been changed
        if (!csc.contains(stat + " x")) {
            String change = csc + statModifier;
            tempV.setText(change); //add the sentence if it doesn't already have it
        }
        else {
            for (int i = 0; i < csc.length() - stat.length() - 1; i++) {
                if (csc.startsWith(stat)) {
                    result.append(csc.substring(i + stat.length() + 6));
                    break; //add everything but the "_x1.55"
                }
                else
                    result.append(csc.charAt(i));
            }
            Log.d("AddPersonActivity", result + " Line 919");
            result.append(statModifier);
            tempV.setText(result);
            csc = tempV.getText() + "";
            if(!csc.isEmpty() && csc.charAt(0) == ' ') {
                result = new StringBuilder(csc); //delete leading space
                result.deleteCharAt(0);
                tempV.setText(result);
            }
            else if(csc.substring(0, csc.indexOf(stat)).contains("  ")) {
                result = new StringBuilder(csc);
                for(int i = 0; i < result.length() - 2; i++)
                    if(result.substring(i, i + 2).equals("  ")) //delete the first space in double spaces
                        result.deleteCharAt(i);
                tempV.setText(result);
            }
        }
    } // Review this later when I have time //TODO

    /**
     * Changes the non-volatile status of the Pokémon done by the move, if necessary and applicable. It uses other methods
     * to decide whether or not the stat change if valid, and then it changes the appropriate text boxes as well.
     * @param move The move used.
     * @param moveUser The user of the move.
     * @param moveTarget The target of the move.
     * @param isPlayerTheUser True if the player is the moveUser, false otherwise.
     */
    private void changeNonVolStatus(AttackingMove move, Pokemon moveUser, Pokemon moveTarget, boolean isPlayerTheUser) {
        String moveStatus = move.getNonVolChanges();
        if(moveStatus == null || moveTarget.getInitStats()[0] == 0)  {
            return; //skip this if it causes no non-volatile changes or if the target has fainted
        }
        String moveUserAbility = moveUser.getAbility();
        boolean nonVolCheck = move.avoidsNonVolStatus(moveStatus, moveUser.getNonVolStatus(), moveUser.getType(), moveUserAbility, moveUserAbility);
        if(move.statusesUser() && !nonVolCheck) { //if it would affect the user
            moveUser.setNonVolStatus(moveStatus);
            changeStatusText(moveStatus, isPlayerTheUser);
        }
        else if(!move.statusesUser() && !move.avoidsNonVolStatus(moveStatus, moveTarget.getNonVolStatus(),
                moveTarget.getType(), moveUserAbility, moveTarget.getAbility())) { //if it would affect the opponent
            moveTarget.setNonVolStatus(moveStatus);
            changeStatusText(moveStatus, !isPlayerTheUser);
        }
    }

    /**
     * Changes the volatile status of the Pokémon done by the move, if necessary and applicable. It uses other methods
     * to decide whether or not the status change if valid, and then it changes the appropriate text boxes as well.
     * @param move The move used.
     * @param moveUser The user of the move.
     * @param moveTarget The target of the move.
     * @param isPlayerTheUser True if the player is the moveUser, false otherwise.
     */
    private void changeVolStatus(AttackingMove move, Pokemon moveUser, Pokemon moveTarget, boolean isPlayerTheUser) {
        String moveStatus = move.getVolChanges();
        if(moveStatus == null  || moveTarget.getInitStats()[0] == 0)  {
            return; //skip this if it causes no volatile changes or if the target has fainted
        }
        String moveType = move.getType();
        String userStatus = moveUser.getVolStatus();
        String moveUserAbility = moveUser.getAbility();
        boolean volCheck = move.avoidsVolStatus(moveStatus, userStatus, moveUser.getType(), moveUserAbility, moveUserAbility);
        if(move.statusesUser() && !volCheck) { //if it would affect the user
            if(!userStatus.contains(moveStatus)) {
                moveUser.setNonVolStatus(moveStatus);
                changeStatusText(moveStatus, isPlayerTheUser); //user case
            }
        }
        else if(!move.statusesUser() && !move.avoidsVolStatus(moveStatus, moveTarget.getVolStatus(),
                moveTarget.getType(), moveUserAbility, moveTarget.getAbility())) { //if it would affect the opponent
            String targetStatus = moveTarget.getVolStatus();
            if(!targetStatus.contains(moveStatus)) {
                moveTarget.setNonVolStatus(moveStatus);
                changeStatusText(moveStatus, !isPlayerTheUser); //target case
            }
        }
    }

    /**
     * Adds the new status to the relevant player text, depending on whether the player or the enemy
     * is changing status.
     * @param status The new status to be added.
     * @param isPlayerChangingStatus True if the player is changing stats, false otherwise.
     */
    private void changeStatusText(String status, boolean isPlayerChangingStatus) {
        StringBuilder csc = new StringBuilder(playerConditions.getText() + "");
        TextView tempConditions = playerConditions;
        if(!isPlayerChangingStatus) {
            csc = new StringBuilder(enemyConditions.getText() + "");
            tempConditions = enemyConditions; //choose player or enemy
        }
        if(csc.length() > 0 && csc.charAt(0) == ' ')
            csc.deleteCharAt(0);
        csc.append(" ");
        csc.append(status); //append the new status
        tempConditions.setText(csc);
    }
}