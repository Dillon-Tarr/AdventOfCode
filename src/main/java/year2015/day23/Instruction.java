package year2015.day23;

record Instruction(Type type, Character registerName, Integer jumpValue) {

    enum Type {
        hlf,
        tpl,
        inc,
        jmp,
        jie,
        jio
    }

}
