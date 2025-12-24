package com.example.toursplitter.model;

import java.util.ArrayList;
import java.util.List;

public class Expense {
    private String expenseId;
    private String tourId;
    private String description;
    private double amount;
    private String paidBy; // memberId
    private String paidByName;
    private List<String> splitAmong; // memberIds
    private String category;
    private long date;

    public Expense() {
        this.splitAmong = new ArrayList<>();
    }

    public Expense(String expenseId, String tourId, String description, double amount,
                   String paidBy, String paidByName, List<String> splitAmong, String category) {
        this.expenseId = expenseId;
        this.tourId = tourId;
        this.description = description;
        this.amount = amount;
        this.paidBy = paidBy;
        this.paidByName = paidByName;
        this.splitAmong = splitAmong;
        this.category = category;
        this.date = System.currentTimeMillis();
    }

    // Getters and Setters
    public String getExpenseId() { return expenseId; }
    public void setExpenseId(String expenseId) { this.expenseId = expenseId; }
    public String getTourId() { return tourId; }
    public void setTourId(String tourId) { this.tourId = tourId; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public String getPaidBy() { return paidBy; }
    public void setPaidBy(String paidBy) { this.paidBy = paidBy; }
    public String getPaidByName() { return paidByName; }
    public void setPaidByName(String paidByName) { this.paidByName = paidByName; }
    public List<String> getSplitAmong() { return splitAmong; }
    public void setSplitAmong(List<String> splitAmong) { this.splitAmong = splitAmong; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public long getDate() { return date; }
    public void setDate(long date) { this.date = date; }
}