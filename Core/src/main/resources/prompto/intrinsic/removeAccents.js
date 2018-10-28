function removeAccents(s) {
    return s.replace(/[ÁÀÃÂÄ]/gi,"A")
        .replace(/[áàãâä]/gi,"a")
        .replace(/[ÉÈËÊ]/gi,"E")
        .replace(/[éèëê]/gi,"e")
        .replace(/[ÍÌÏÎ]/gi,"I")
        .replace(/[íìïî]/gi,"i")
        .replace(/[ÓÒÖÔÕ]/gi,"O")
        .replace(/[óòöôõ]/gi,"o")
        .replace(/[ÚÙÜÛ]/gi, "U")
        .replace(/[úùüû]/gi, "u")
        .replace(/[Ç]/gi, "C")
        .replace(/[ç]/gi, "c")
        .replace(/[Ñ]/gi, "N")
        .replace(/[ñ]/gi, "n");
}