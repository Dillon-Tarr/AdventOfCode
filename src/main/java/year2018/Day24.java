package year2018;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class Day24 {
    static private final int DAY = 24;
    static private final File INPUT_FILE = new File("input-files/2018/"+DAY+".txt");
    static private final ArrayList<Group> allGroups = new ArrayList<>(), allGroupsBackup = new ArrayList<>(),
            immuneSystem = new ArrayList<>(), infection = new ArrayList<>();

    static void main() {
        long startTime = System.nanoTime();

        getAndProcessInputData();
        solvePart1();
        solvePart2();

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
    }

    private static void getAndProcessInputData() {
        ArrayList<String> immuneSystemStrings = new ArrayList<>(), infectionStrings = new ArrayList<>();
        boolean infectionMode = false;
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            br.readLine();
            String s = br.readLine();
            while (s != null) {
                if (s.isEmpty()) { infectionMode = true; br.readLine(); s = br.readLine(); continue; }
                if (infectionMode) infectionStrings.add(s); else immuneSystemStrings.add(s);
                s = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
        for (String s : immuneSystemStrings) allGroupsBackup.add(new Group(true, s));
        for (String s : infectionStrings) allGroupsBackup.add(new Group(false, s));
    }

    private static void setScenario(int immuneSystemBoost) {
        allGroups.clear(); immuneSystem.clear(); infection.clear();
        for (var group : allGroupsBackup) {
            var newGroup = new Group(group, immuneSystemBoost);
            allGroups.add(newGroup);
            if (group.goodGuy) immuneSystem.add(newGroup);
            else infection.add(newGroup);
        }
    }

    private static void fight() {
        allGroups.sort((g1, g2) -> Integer.compare(g2.initiative, g1.initiative));
        for (var group : allGroups) group.clearTargeting();
        immuneSystem.sort(selectionOrderComparator); infection.sort(selectionOrderComparator);
        for (var group : immuneSystem) group.selectTarget(infection);
        for (var group : infection) group.selectTarget(immuneSystem);
        for (int i = 0; i < allGroups.size(); i++) {
            var group =  allGroups.get(i);
            var target = group.currentTarget;
            if (target == null) continue;
            int realDamage = group.currentTargetIsWeak ? group.effectiveDoublePower : group.effectivePower;
            if (!target.handleDamageAndReturnWhetherStillStanding(realDamage)){
                boolean needToDecrementI = allGroups.indexOf(target) < i;
                allGroups.remove(target);
                if (target.goodGuy) immuneSystem.remove(target); else infection.remove(target);
                if (needToDecrementI) i--;
            }
        }
    }

    private static int countRemainingUnits() { int sum = 0; for (var group : allGroups) sum += group.unitCount; return sum; }

    private static void solvePart1() {
        setScenario(0);
        while (!immuneSystem.isEmpty() && !infection.isEmpty()) fight();
        System.out.println("\nNumber of remaining units (part 1 answer): "+countRemainingUnits());
    }

    private static void solvePart2() {
        int currentBoost = 1, roundCount = 0;
        setScenario(1);
        while (!infection.isEmpty()) {
            fight();
            if (++roundCount > 9998 || immuneSystem.isEmpty()) { roundCount = 0; setScenario(++currentBoost); }
        }
        System.out.println("\nNumber of remaining units with lowest successful immune system boost (part 2 answer): "+countRemainingUnits());
    }

    private static class Group {
        boolean goodGuy;
        int unitCount, perUnitHP, damage, initiative, effectivePower, effectiveDoublePower;
        String damageType;
        ArrayList<String> immunities =  new ArrayList<>(), weaknesses = new ArrayList<>();
        Group currentTarget = null;
        boolean currentTargetIsWeak = false;
        boolean targeted = false;

        Group(boolean goodGuy, String s) {
            this.goodGuy = goodGuy;
            unitCount = Integer.parseInt(s.substring(0, s.indexOf(" ")));
            int spaceHIndex = s.indexOf(" h");
            perUnitHP = Integer.parseInt(s.substring(1+ s.lastIndexOf(" ", spaceHIndex -1), spaceHIndex));
            int lastSpaceIndex = s.lastIndexOf(" ");
            int typeIndex = 1+s.lastIndexOf(" ",  lastSpaceIndex-22);
            int damageIndex = 1+s.lastIndexOf(" ", typeIndex-2);
            damage = Integer.parseInt(s.substring(1+s.lastIndexOf(" ", typeIndex-2), typeIndex-1));
            damageType = s.substring(typeIndex, lastSpaceIndex-21);
            initiative = Integer.parseInt(s.substring(lastSpaceIndex+1));
            updateEffectivePower();
            int rightRoundBracketIndex = damageIndex-27;
            if (s.charAt(rightRoundBracketIndex) == ')') {
                int leftRoundBracketIndex = s.indexOf("(", 31, 45);
                if (leftRoundBracketIndex == -1) throw new RuntimeException("Well, that shouldn't have happened!");
                String sub = s.substring(leftRoundBracketIndex+1, rightRoundBracketIndex);
                int scIndex = sub.indexOf(';');
                if (scIndex == -1) {
                    if (sub.charAt(0) == 'i') immunities.addAll(List.of(sub.substring(10).split(", ")));
                    else weaknesses.addAll(List.of(sub.substring(8).split(", ")));
                } else {
                    String s1 = sub.substring(0, scIndex), s2 = sub.substring(scIndex+2);
                    if (sub.charAt(0) == 'i') {
                        immunities.addAll(List.of(s1.substring(10).split(", ")));
                        weaknesses.addAll(List.of(s2.substring(8).split(", ")));
                    } else {
                        immunities.addAll(List.of(s2.substring(10).split(", ")));
                        weaknesses.addAll(List.of(s1.substring(8).split(", ")));
                    }
                }
            }
        }

        Group(Group group, int goodGuyBoost) {
            goodGuy = group.goodGuy; unitCount = group.unitCount; perUnitHP = group.perUnitHP;
            damage = group.damage; if (goodGuy) damage += goodGuyBoost; updateEffectivePower();
            initiative = group.initiative; damageType = group.damageType; immunities = group.immunities;
            weaknesses = group.weaknesses; currentTarget = null; currentTargetIsWeak = targeted = false;
        }

        private void updateEffectivePower() {
            effectivePower = unitCount * damage;
            effectiveDoublePower = effectivePower << 1;
        }

        private void clearTargeting() {
            currentTarget = null;
            currentTargetIsWeak = targeted = false;
        }

        private void selectTarget(ArrayList<Group> candidates) {
            if (candidates.isEmpty()) return;
            Group selectedCandidate = null; boolean selectedCandidateIsWeak = false;
            for (var candidate : candidates) {
                if (!candidate.targeted && !candidate.immunities.contains(damageType)) {
                    if (selectedCandidate == null) {
                        selectedCandidate = candidate;
                        selectedCandidateIsWeak = candidate.weaknesses.contains(damageType);
                    } else {
                        boolean currentCandidateIsWeak = candidate.weaknesses.contains(damageType);
                        if (!selectedCandidateIsWeak && currentCandidateIsWeak) {
                            selectedCandidate = candidate;
                            selectedCandidateIsWeak = true;
                        } else if (selectedCandidateIsWeak == currentCandidateIsWeak && // <- this AND
                                (candidate.effectivePower > selectedCandidate.effectivePower || // <- either this or this:
                                (candidate.effectivePower == selectedCandidate.effectivePower && candidate.initiative > selectedCandidate.initiative))
                        ) selectedCandidate = candidate;
                    }
                }
            }
            if (selectedCandidate != null) {
                currentTarget = selectedCandidate;
                currentTargetIsWeak = selectedCandidateIsWeak;
                selectedCandidate.targeted = true;
            }
        }

        private boolean handleDamageAndReturnWhetherStillStanding(int incomingDamage) {
            if ((unitCount -= (incomingDamage/perUnitHP)) > 0) { updateEffectivePower(); return true; }
            return false;
        }

    }

    private static final Comparator<Group> selectionOrderComparator = (g1, g2) -> {
        if (g2.effectivePower == g1.effectivePower) return Integer.compare(g2.initiative, g1.initiative);
        else return Integer.compare(g2.effectivePower, g1.effectivePower);
    };

}
