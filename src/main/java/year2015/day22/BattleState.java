package year2015.day22;

record BattleState(String history, int playerHP, int playerMP, int bossHP, int manaSpent,
                   int shieldTimer, int poisonTimer, int rechargeTimer) implements Comparable<BattleState> {

    @Override
    public int compareTo(BattleState otherState) {
        return Integer.compare(this.manaSpent, otherState.manaSpent);
    }
}
