package com.example.incomes_and_other;

import java.util.ArrayList;

public interface IncomesProvider {
    ArrayList<Income> getIncomesFromDB();
}
