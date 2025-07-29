package year2023.day19.part1;

class Part {
    int x, m, a, s;

    Part (int x, int m, int a, int s) {
        this.x = x;
        this.m = m;
        this.a = a;
        this.s = s;
    }

    int getRatingSum() {
        return x+m+a+s;
    }

}
