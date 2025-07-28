package year2015.day21;

class EquipmentOption {
    String name;
    int cost, damage, armor;

    EquipmentOption(String name, int cost, int damage, int armor) {
        this.name = name;
        this.cost = cost;
        this.damage = damage;
        this.armor = armor;
    }

    @Override
    public String toString() {
        return "EquipmentOption{" +
                "name='" + name + '\'' +
                ", cost=" + cost +
                ", damage=" + damage +
                ", armor=" + armor +
                '}';
    }
}
