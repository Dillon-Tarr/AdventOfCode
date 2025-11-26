package year2018;

class Day19Part2 {

    static void main() {
/* This is a code analysis problem, not so much a write-code-to-solve problem.

                Initial annotions:
        registers, initially: r0: 1, r2: 0, r3: 0, r4: 0, r5: 0
        ip1 (r1): 0
            #ip 1
        0   addi ip1 16 ip1     #   reallygoto 17;
        17  addi 3 2 3          #   r3 += 2;
        18  mulr 3 3 3          #   r3 *= r3;
        19  mulr ip1 3 3        #   r3 *= 19;
        20  muli 3 11 3         #   r3 *= 11;
        21  addi 4 3 4          #   r4 += 3;
        22  mulr 4 ip1 4        #   r4 *= 22;
        23  addi 4 18 4         #   r4 += 18;
        24  addr 3 4 3          #   r3 += r4; // == 920
        25  addr ip1 0 ip1      #   jump by r0+1;
        26  seti 0 x ip1        #   reallygoto 1;
        27  setr ip1 x 4        #   r4 = 27;        // if r0 == 1, do this stuff
        28  mulr 4 ip1 4        #   r4 *= 28;       // ||
        29  addr ip1 4 4        #   r4 += 29;       // ||
        30  mulr ip1 4 4        #   r4 *= 30;       // ||
        31  muli 4 14 4         #   r4 *= 14;       // ||
        32  mulr 4 ip1 4        #   r4 *= 32;       // ||
        33  addr 3 4 3          #   r3 += r4;       // ||
        34  seti 0 x 0          #   r0 = 0;         // ||
        35  seti 0 x ip1        #   reallygoto 1;
        1   seti 1 x 5          #   r5 = 1
        2   seti 1 x 2          #   r2 = 1;
        3   mulr 5 2 4          #   r4 = r5*r2;
        4   eqrr 4 3 4          #   r4 = r4 == r3 ? 1 : 0;
        5   addr 4 ip1 ip1      #   reallygoto 6+r4;
        6   addi ip1 1 ip1      #   reallygoto 8;
        7   addr 5 0 0          #   r0 += r5;
        8   addi 2 1 2          #   r2++;
        9   gtrr 2 3 4          #   r4 = r2 > r3 ? 1 : 0;
        10  addr ip1 4 ip1      #   jump by r4+1;
        11  seti 2 x ip1        #   reallygoto 3;
        12  addi 5 1 5          #   r5++;
        13  gtrr 5 3 4          #   r4 = r5 > r3 ? 1 : 0;
        14  addr 4 ip1 ip1      #   jump by r4+1;
        15  seti 1 x ip1        #   reallygoto 2;
        16  mulr ip1 ip1 ip1    #   reallygoto 257; // break

                Further edits to the above:
        registers, initially: r0: 1, r2: 0, r3: 0, r4: 0, r5: 0
        0   addi ip1 16 ip1     #   reallygoto 17;
     17-24                      #   r3 = 920 ((2*2*19*11)+((3*22)+18))
     25-26                      #   causes 27-35 to be skipped for part 1
     27-33                      #   r3 = 10_551_320 // r3 += 10_550_400 ((27*28)+29)*30*14*32
     34-35                      #   r0 = 0;
        1   seti 1 x 5          #   r5 = 1
        2   seti 1 x 2          #   r2 = 1;
      3-7                       #   if (r5*r2 == r3) r0 += r5;
      8-11                      #   if (++r2 <= r3) continue at line 3;
     12-16                      #   if (++r5 > r3) exit program; else continue at line 2;

                Even further simplification for part 2, *technically* starting with r0 = 1:
        registers, initially: r0: 0, r2: 0, r3: 10_551_320, r4: 0, r5: 1
        2   seti 1 x 2          #   r2 = 1;
      3-7                       #   if (r5*r2 == r3) r0 += r5;
      8-11                      #   if (++r2 <= r3) continue at line 3;
     12-16                      #   if (++r5 > r3) exit program; else continue at line 2;

                Further, essentially:
          int sum = 0;
          for (long i = 1; i <= 10_551_320; i++) {
              for (long j = 1; j <= 10_551_320; j++) {
                  if (i*j == 10_551_320) sum += j;
              }
          }*/

        // And the actual programmatic way to answer this question reasonably...:
        long startTime = System.nanoTime();

        int sum = 0;
        for (int i = 1; i <= 10_551_320; i++) if (10_551_320 % i == 0) sum += i;
        System.out.println("\nSum of the factors of 10,551,320 (part 2 answer): "+sum);

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));

    }

}
