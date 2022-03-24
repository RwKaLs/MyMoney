package com.example.incomes_and_other;

import com.univocity.parsers.annotations.Parsed;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Expense {

    @Parsed
    private String data;
    @Parsed
    private int summa;
    @Parsed
    private String type;

    public Expense(){

    }

    public Expense(String data, int summa, String type){
        this.data = data;
        this.summa = summa;
        this.type = type;
    }

    public int getSumma() {
        return this.summa;
    }

    public void setSumma(int summa){
        this.summa = summa;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type){
        this.type = type;
    }

    public String getData() {
        return this.data;
    }

    public void setData(String data){
        this.data = data;
    }


}
