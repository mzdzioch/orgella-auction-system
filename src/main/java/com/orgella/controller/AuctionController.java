package com.orgella.controller;

import com.orgella.helpers.CategoryBuilder;
import com.orgella.helpers.DatabaseAccess;
import com.orgella.model.Auction;
import com.orgella.model.User;
import com.orgella.repository.AuctionsRegistry;
import com.orgella.repository.UserRegistry;
import com.orgella.view.AuctionView;
import com.orgella.view.CategoryView;

import javax.xml.crypto.Data;
import java.math.BigDecimal;

public class AuctionController {

    private AuctionsRegistry auctionsRegistry;
    private CategoryBuilder categoryBuilder;
    private AuctionView auctionView;
    private CategoryView categoryView;

    public AuctionController(AuctionsRegistry auctionsRegistry) {
        this.auctionsRegistry = auctionsRegistry;
        this.categoryBuilder = new CategoryBuilder(auctionsRegistry.getDatabaseAccess());
        this.auctionView = new AuctionView(auctionsRegistry);
        this.categoryView = new CategoryView();
    }

    public boolean validateCategoryNumber(int idCategory){
        return categoryBuilder.isParentExist(idCategory);
    }

    public boolean validateAuctionToMakeBid(int categoryNumber, int auctionNumber) {
        return auctionsRegistry.validateAuctionToMakeBid(categoryNumber, auctionNumber);
    }

    public boolean validateBid(BigDecimal bidValue, int auctionNumber) {
        return getSingleAuction(auctionNumber).validateBid(bidValue);
    }

    public Auction getSingleAuction(int auctionId) {
        return auctionsRegistry.getSingleAuction(auctionId);
    }

    public boolean makeWinningBid(int auctionId, BigDecimal price, String user) {

        return auctionsRegistry.makeWinningBid(auctionId, price, user);
    }

    public boolean removeAuction(int auctionId){
        return auctionsRegistry.removeAuction(auctionId);
    }

    public void addAuction(String auctionTitle, BigDecimal auctionPrice, int categoryNumber, String auctionDescription, int userId) {
        Auction newAuction = new Auction(true, auctionTitle,  auctionPrice, categoryNumber,  auctionDescription, userId);
        auctionsRegistry.insertAuction(newAuction);
    }

    public void printAuctions(User user){
        auctionView.printAuctionsList(auctionsRegistry.getUserAuctions(user));
    }

    public void printInactiveAuctions(User user){
        auctionView.printAuctionsList(auctionsRegistry.getUserFinishedAuctionList(user));
    }

    public void printActiveAuctions() {
        auctionView.printActiveAuctions();
    }

    public void printAllAuctionsUnderCategory(int idCategory){
        auctionView.printAuctionsList(auctionsRegistry.getAllAuctionsUnderCategory(idCategory));
    }

    public void displayCategory(){
        categoryView.display(categoryBuilder.getBuilder());
    }

}
