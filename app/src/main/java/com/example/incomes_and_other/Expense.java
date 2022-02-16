package com.example.incomes_and_other;

public class Expense {
    private String data;
    private int summa;
    private String type;

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
