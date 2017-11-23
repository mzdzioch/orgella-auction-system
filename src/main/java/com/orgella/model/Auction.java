package com.orgella.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Auction {

    private int auctionId;
    private boolean active;
    private String title;
    private BigDecimal price;
    private int categoryId;
    private String description;
    private int userId;
    private List<Bid> bidList = new ArrayList<>();

    public Auction(boolean active, String title, BigDecimal price, int categoryID, String description, int userId) {
        this.active = active;
        this.title = title;
        this.price = price;
        this.categoryId = categoryID;
        this.description = description;
        this.userId = userId;
    }

    public Auction(int auctionId, boolean active, String title, BigDecimal price, int categoryID, String description, int userId) {
        this.auctionId = auctionId;
        this.active = active;
        this.title = title;
        this.price = price;
        this.categoryId = categoryID;
        this.description = description;
        this.userId = userId;

    }

    public Auction(int auctionId, boolean active, String title, BigDecimal price, int categoryID, String description, int userId, List<Bid> bidList) {
        this.auctionId = auctionId;
        this.active = active;
        this.title = title;
        this.price = price;
        this.categoryId = categoryID;
        this.description = description;
        this.userId = userId;
        this.bidList = bidList;

    }


    public int getAuctionId() {
        return auctionId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<Bid> getListBids() {
        return bidList;
    }

    public void setBidList(List<Bid> bidList) {
        this.bidList = bidList;
    }

    private BigDecimal getLastPrice(){
        return getListBids().get(getListBids().size()-1).getBidPrice();
    }

    public boolean validateBid(BigDecimal bidValue) {

        if(!getListBids().isEmpty()) {
            if(bidValue.compareTo(getListBids().get(getListBids().size() - 1).getBidPrice()) == 1)
                return true;
            return false;
        } else if(bidValue.compareTo(getPrice()) == 1) {
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        return "id: " + auctionId + " | " + title + " | "
                + (getListBids().isEmpty() ? price : getLastPrice())
                + " | " + categoryId + " | " + description;
    }
}