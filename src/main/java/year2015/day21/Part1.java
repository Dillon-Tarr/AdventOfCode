package year2015.day21;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class Part1 {
    static private final int DAY = 21;
    static private final File INPUT_FILE = new File("input-files/2015/"+DAY+".txt");
    static private final int playerHP = 100;
    static private int bossHP, bossDamage, bossArmor, lowestCostVictory = Integer.MAX_VALUE, lowestCostTurnsUntilBossFalls, lowestCostTurnsUntilPlayerFalls;
    static private final List<EquipmentOption> weaponOptions = new ArrayList<>();
    static private final List<EquipmentOption> armorOptions = new ArrayList<>();
    static private final List<EquipmentOption> ringOptions = new ArrayList<>();
    static private EquipmentOption winningWeaponOption, winningArmorOption, winningRingOption;

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        getBossStats();
        setEquipmentOptions();
        testOptions();

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getBossStats() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String inputString = br.readLine();
            String numberString = inputString.substring(inputString.indexOf(':')+2);
            System.out.println("\nBoss HP: "+numberString);
            bossHP = Integer.parseInt(numberString);
            inputString = br.readLine();
            numberString = inputString.substring(inputString.indexOf(':')+2);
            System.out.println("Boss Damage: "+numberString);
            bossDamage = Integer.parseInt(numberString);
            inputString = br.readLine();
            numberString = inputString.substring(inputString.indexOf(':')+2);
            System.out.println("Boss Armor: "+numberString);
            bossArmor = Integer.parseInt(numberString);
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void setEquipmentOptions() {
        weaponOptions.add(new EquipmentOption("Dagger", 8, 4, 0));
        weaponOptions.add(new EquipmentOption("Shortsword", 10, 5, 0));
        weaponOptions.add(new EquipmentOption("Warhammer", 25, 6, 0));
        weaponOptions.add(new EquipmentOption("Longsword", 40, 7, 0));
        weaponOptions.add(new EquipmentOption("Greataxe", 74, 8, 0));

        armorOptions.add(new EquipmentOption("NONE", 0, 0, 0));
        armorOptions.add(new EquipmentOption("Leather", 13, 0, 1));
        armorOptions.add(new EquipmentOption("Chainmail", 31, 0, 2));
        armorOptions.add(new EquipmentOption("Splintmail", 53, 0, 3));
        armorOptions.add(new EquipmentOption("Bandedmail", 75, 0, 4));
        armorOptions.add(new EquipmentOption("Platemail", 102, 0, 5));

        ringOptions.add(new EquipmentOption("Damage +1", 25, 1, 0));
        ringOptions.add(new EquipmentOption("Damage +2", 50, 2, 0));
        ringOptions.add(new EquipmentOption("Damage +3", 100, 3, 0));
        ringOptions.add(new EquipmentOption("Defense +1", 20, 0, 1));
        ringOptions.add(new EquipmentOption("Defense +2", 40, 0, 2));
        ringOptions.add(new EquipmentOption("Defense +3", 80, 0, 3));

        List<EquipmentOption> ringCombos = new ArrayList<>();
        EquipmentOption r1;
        EquipmentOption r2;
        for (int i = 0; i < ringOptions.size()-1; i++) {
            for (int j = i+1; j < ringOptions.size(); j++) {
                r1 = ringOptions.get(i);
                r2 = ringOptions.get(j);
                ringCombos.add(new EquipmentOption(r1.name+" and "+ r2.name, r1.cost+r2.cost, r1.damage+r2.damage, r1.armor+r2.armor));
            }
        }
        ringOptions.addAll(ringCombos);
        ringOptions.add(0, new EquipmentOption("NONE", 0, 0, 0));
    }

    private static void testOptions() {
        int cost, playerDamage, playerArmor, damageToBossPerTurn, damageToPlayerPerTurn, turnsUntilBossFalls, turnsUntilPlayerFalls;
        for (EquipmentOption weaponOption : weaponOptions) {
            for (EquipmentOption armorOption : armorOptions) {
                for (EquipmentOption ringOption : ringOptions) {
                    cost = weaponOption.cost + armorOption.cost + ringOption.cost;
                    if (cost > lowestCostVictory) continue;

                    playerDamage = weaponOption.damage + ringOption.damage;
                    damageToBossPerTurn = bossArmor >= playerDamage ? 1 : playerDamage-bossArmor;
                    turnsUntilBossFalls = bossHP/damageToBossPerTurn;
                    if (bossHP%damageToBossPerTurn != 0) turnsUntilBossFalls++;

                    playerArmor = armorOption.armor + ringOption.armor;
                    damageToPlayerPerTurn = playerArmor >= bossDamage ? 1 : bossDamage-playerArmor;
                    turnsUntilPlayerFalls = playerHP/damageToPlayerPerTurn;
                    if (playerHP%damageToPlayerPerTurn != 0) turnsUntilPlayerFalls++;

                    if (turnsUntilBossFalls <= turnsUntilPlayerFalls && cost < lowestCostVictory) {
                        lowestCostVictory = cost;
                        winningWeaponOption = weaponOption;
                        winningArmorOption = armorOption;
                        winningRingOption = ringOption;
                        lowestCostTurnsUntilBossFalls = turnsUntilBossFalls;
                        lowestCostTurnsUntilPlayerFalls = turnsUntilPlayerFalls;
                    }
                }
            }
        }
        System.out.println("\nCheapest cost for player to beat the boss: "+lowestCostVictory);
        System.out.println("Loadout:");
        System.out.println(winningWeaponOption);
        System.out.println(winningArmorOption);
        System.out.println(winningRingOption);
        System.out.println("\nTurns taken to beat boss: "+lowestCostTurnsUntilBossFalls);
        System.out.println("Full turns after which boss would have beaten player: "+lowestCostTurnsUntilPlayerFalls);
    }

}
