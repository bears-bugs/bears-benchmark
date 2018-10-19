class MissingCurlyBraces {
    bool condition = true;
    int doSomething();
    void method() {
        int doSomethingElse();
        if (condition) doSomething(); // NOK
        if (condition) doSomething(); // NOK
        else doSomethingElse(); // NOK
        if (condition) { // OK
        }
        else doSomething(); // NOK
        if (condition) { // OK
        }
        else { // OK
        }
        if (condition) { // OK
        }
        if (condition) { // OK
        }
        else if (condition) { // OK
        }
        for (int i = 0; i < 10; i++) doSomething(); // NOK
        for (int i = 0; i < 10; i++) { // OK
        }
        while (condition) doSomething(); // NOK
        while (condition) { // OK
        }
        do doSomething(); while (condition); // NOK
        do { // OK
            doSomething();
        } while (condition);
    }
};

void testme()
{
// empty expression statement should not create an issue
if(parameterSet->GetParameter<unsigned int>("OutputWidth", m_OutputSize0)); 
}

