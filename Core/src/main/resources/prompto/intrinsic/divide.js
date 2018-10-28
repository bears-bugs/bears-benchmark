function divide( a, b ) {
    if(b===0)
        throw new DivideByZeroError();
    else
        return a / b;
}

