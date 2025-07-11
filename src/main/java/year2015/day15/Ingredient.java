package year2015.day15;

class Ingredient {
    int capacity, durability, flavor, texture, calories;

    Ingredient(String inputString) {
        int commaIndex, precedingSpaceIndex;
        commaIndex = inputString.indexOf(',');
        precedingSpaceIndex = inputString.lastIndexOf(' ', commaIndex-1);
        capacity = Integer.parseInt(inputString.substring(precedingSpaceIndex+1, commaIndex));
        commaIndex = inputString.indexOf(',', commaIndex+1);
        precedingSpaceIndex = inputString.lastIndexOf(' ', commaIndex-1);
        durability = Integer.parseInt(inputString.substring(precedingSpaceIndex+1, commaIndex));
        commaIndex = inputString.indexOf(',', commaIndex+1);
        precedingSpaceIndex = inputString.lastIndexOf(' ', commaIndex-1);
        flavor = Integer.parseInt(inputString.substring(precedingSpaceIndex+1, commaIndex));
        commaIndex = inputString.indexOf(',', commaIndex+1);
        precedingSpaceIndex = inputString.lastIndexOf(' ', commaIndex-1);
        texture = Integer.parseInt(inputString.substring(precedingSpaceIndex+1, commaIndex));
        precedingSpaceIndex = inputString.lastIndexOf(' ');
        calories = Integer.parseInt(inputString.substring(precedingSpaceIndex+1));
    }
}
