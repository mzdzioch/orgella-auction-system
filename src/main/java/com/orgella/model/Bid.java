package com.orgella.model;

import java.math.BigDecimal;

public class Bid {

    private int auctionId;
    private int userId;
    private BigDecimal bidPrice;

    public Bid(int auctionId, int userId, BigDecimal bidPrice) {
        this.auctionId = auctionId;
        this.userId = userId;
        this.bidPrice = bidPrice;
    }

    public int getAuctionId() {
        return auctionId;
    }

    public int getUserId() {
        return userId;
    }

    public BigDecimal getBidPrice() {
        return bidPrice;
    }

}
