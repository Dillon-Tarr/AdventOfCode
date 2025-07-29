package year2015.day22;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.PriorityQueue;
import java.util.Queue;

public class BothParts {
    static private final int DAY = 22;
    static private final File INPUT_FILE = new File("input-files/2015/"+DAY+".txt");
    static private final int playerStartingHP = 50, playerStartingMP = 500;
    static private int bossStartingHP, bossDamage, leastManaSpendToWin;
    static private String winningHistory;
    static private final Queue<BattleState> queuedBattleStates = new PriorityQueue<>();

    public static void main(String[] args) {
        // No args = normal mode (Part1)
        // Otherwise, hard mode (Part2)

        long startTime = System.nanoTime();

        getBossStats();
        findLeastManaSpendToWin(args.length != 0);

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getBossStats() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String inputString = br.readLine();
            String numberString = inputString.substring(inputString.indexOf(':')+2);
            System.out.println("\nBoss starting HP: "+numberString);
            bossStartingHP = Integer.parseInt(numberString);
            inputString = br.readLine();
            numberString = inputString.substring(inputString.indexOf(':')+2);
            System.out.println("Boss Damage: "+numberString);
            bossDamage = Integer.parseInt(numberString);
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void findLeastManaSpendToWin(boolean hardMode) {
        // Set initial possible states where boss goes next (Magic Missile, Drain, Shield, Poison, Recharge):
        queuedBattleStates.add(new BattleState("m", playerStartingHP, playerStartingMP-53,
                bossStartingHP-4, 53, 0, 0, 0));
        queuedBattleStates.add(new BattleState("d", playerStartingHP+2, playerStartingMP-73,
                bossStartingHP-2, 73, 0, 0, 0));
        queuedBattleStates.add(new BattleState("s", playerStartingHP, playerStartingMP-113,
                bossStartingHP, 113, 6, 0, 0));
        queuedBattleStates.add(new BattleState("p", playerStartingHP, playerStartingMP-173,
                bossStartingHP, 173, 0, 6, 0));
        queuedBattleStates.add(new BattleState("r", playerStartingHP, playerStartingMP-229,
                bossStartingHP, 229, 0, 0, 5));

        BattleState battleState;
        String history;
        int playerHP, playerMP, bossHP, manaSpent, shieldTimer, poisonTimer, rechargeTimer,
                bossDamageIfShielded = Math.max(1, bossDamage-7);
        while (!queuedBattleStates.isEmpty()) {
            battleState = queuedBattleStates.poll();
            history = battleState.history();
            manaSpent = battleState.manaSpent();
            bossHP = battleState.bossHP();
            // Boss-turn poison (counter is always even on start of player's turn, so dropping timer only on player's turn):
            poisonTimer = battleState.poisonTimer();
            if (poisonTimer > 0) bossHP -= 3;
            // Win check:
            if (bossHP <= 0) {
                leastManaSpendToWin = manaSpent;
                winningHistory = history;
                break;
            } // Boss attack:
            shieldTimer = battleState.shieldTimer();
            playerHP = battleState.playerHP();
            playerHP -= shieldTimer > 0 ? bossDamageIfShielded : bossDamage;
            shieldTimer = Math.max(0, battleState.shieldTimer()-2);

            // Start of player's turn:
            if (hardMode) playerHP--;
            if (playerHP <= 0) continue; // Oops, you died.

            // Player-turn poison (counter now drops to account for boss and player turns):
            if (poisonTimer > 0) bossHP -= 3;
            poisonTimer = Math.max(0, battleState.poisonTimer()-2);
            // Recharge (twice if 2 or more stacks, or only once):
            playerMP = battleState.playerMP();
            rechargeTimer = battleState.rechargeTimer();
            if (rechargeTimer >= 2) playerMP += 202;
            else if (rechargeTimer == 1) playerMP += 101;
            rechargeTimer = Math.max(0, battleState.rechargeTimer()-2);
            // MP check:
            if (playerMP < 53) continue;

            // Set new states (Magic Missile, Drain, Shield, Poison, Recharge):
            queuedBattleStates.add(new BattleState(history+"m", playerHP, playerMP-53,
                    bossHP-4, manaSpent+53, shieldTimer, poisonTimer, rechargeTimer));
            if (playerMP < 73) continue;
            queuedBattleStates.add(new BattleState(history+"d", playerHP+2, playerMP-73,
                    bossHP-2, manaSpent+73, 0, 0, 0));
            if (playerMP < 113) continue;
            if (shieldTimer == 0) queuedBattleStates.add(new BattleState(history+"s", playerHP, playerMP-113,
                    bossHP, manaSpent+113, 6, poisonTimer, rechargeTimer));
            if (playerMP < 173) continue;
            if (poisonTimer == 0) queuedBattleStates.add(new BattleState(history+"p", playerHP, playerMP-173,
                    bossHP, manaSpent+173, shieldTimer, 6, rechargeTimer));
            if (rechargeTimer == 0 && playerMP >= 229) queuedBattleStates.add(new BattleState(history+"r", playerHP,
                    playerMP-229, bossHP, manaSpent+229, shieldTimer, poisonTimer, 5));
        }
        System.out.println("\nLeast mana spend to win: "+leastManaSpendToWin);
        System.out.println("Winning history: "+winningHistory);
    }

}
