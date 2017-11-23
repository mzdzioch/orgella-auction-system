package com.orgella.repository;

import com.orgella.helpers.CategoryBuilder;
import com.orgella.helpers.DatabaseAccess;
import com.orgella.model.Auction;
import com.orgella.model.Bid;
import com.orgella.model.User;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuctionsRegistry {

    private Map<Integer, Auction> auctionsMapWithId;
    private List<Bid> bidList;
    private DatabaseAccess databaseAccess;

    private static final int MAX_NUMBER_OF_BIDS = 2;

    public AuctionsRegistry(DatabaseAccess databaseAccess) {
        this.databaseAccess = databaseAccess;
        this.auctionsMapWithId = new HashMap<>();
    }

    public DatabaseAccess getDatabaseAccess() {
        return databaseAccess;
    }

    public Map<Integer, Auction> getAllAuctions() {

        String allAuctionQuery = "SELECT * FROM auctions;";
        ResultSet resultSet;
        resultSet = databaseAccess.readData(allAuctionQuery);

        try {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");

                auctionsMapWithId.put(id, new Auction(
                        id,
                        resultSet.getBoolean("is_active"),
                        resultSet.getString("title"),
                        resultSet.getBigDecimal("price"),
                        resultSet.getInt("category_id"),
                        resultSet.getString("description"),
                        resultSet.getInt("user_id"),
                        getBids(id)
                ));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return auctionsMapWithId;
    }

    public List<Bid> getBids(int auctionId){

        String allBidsQuery = "SELECT * FROM bids WHERE auction_id='" + auctionId + "';";
        ResultSet resultSet;
        List<Bid> bidList = new ArrayList<>();

        resultSet = databaseAccess.readData(allBidsQuery);

        try {
            while (resultSet.next()) {
                bidList.add(
                        new Bid(
                                resultSet.getInt("auction_id"),
                                resultSet.getInt("user_id"),
                                resultSet.getBigDecimal("price")
                        )
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bidList;
    }

    public boolean insertAuction(Auction auction) {

        String addAuctionQuery = "INSERT INTO auctions (is_active, title, price, category_id, description, user_id) VALUES(" +
                "'" + auction.isActive() + "', " +
                "'" + auction.getTitle() + "', " +
                "'" + auction.getPrice() + "', " +
                "'" + auction.getCategoryId() + "', " +
                "'" + auction.getDescription() + "', " +
                "'" + auction.getUserId() + "');";

        databaseAccess.createData(addAuctionQuery);

        return true;
    }

    public boolean removeAuction(int auctionId) {

        String removeAuction = "DELETE FROM auctions where id = '" + auctionId + "';";

        if(databaseAccess.createData(removeAuction))
            return  true;
        else
            return false;
    }

    public Auction findAuctionById(int auctionId){
        Auction auction = null;
        ResultSet resultSet = null;
        List<Bid> bidList = new ArrayList<>();
        String findAuctionQuery = "SELECT * FROM auctions WHERE id='" + auctionId + "';";

        resultSet = databaseAccess.readData(findAuctionQuery);

        try {
            while(resultSet.next()) {
                auction = new Auction(
                        resultSet.getInt("id"),
                        resultSet.getBoolean("is_active"),
                        resultSet.getString("title"),
                        resultSet.getBigDecimal("price"),
                        resultSet.getInt("category_id"),
                        resultSet.getString("description"),
                        resultSet.getInt("user_id")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(bidList.addAll(getBids(auctionId))) {
            auction.setBidList(getBids(auctionId));
        }

        return auction;
    }

    public ArrayList<Auction> getUserAuctions(User user) {
        ArrayList<Auction> userAuctions = new ArrayList<>();

        for (Auction auction : getAllAuctions().values()) {
            if (auction.getUserId() == user.getId()) {
                userAuctions.add(auction);
            }
        }

        return userAuctions;
    }

    public ArrayList<Auction> getAllAuctionsUnderCategory(int categoryID) {
        CategoryBuilder categoryBuilder = new CategoryBuilder(databaseAccess);
        List<Integer> categoryList = categoryBuilder.getCategoryAndSubcategoriesListId(categoryID);

        ArrayList<Auction> categoryAuctions = new ArrayList<>();

        for (Integer categoryNum : categoryList) {
            for (Auction auction : getAllAuctions().values()) {
                if ((auction.getCategoryId() == categoryNum) && (auction.isActive())) {
                    categoryAuctions.add(auction);
                }
            }
        }

        return categoryAuctions;
    }

    public ArrayList<Auction> getUserFinishedAuctionList(User user) {
        ArrayList<Auction> listUserAuctions = new ArrayList<>();

        for (Auction auction : getAllAuctions().values()) {
            if ((auction.getUserId() == user.getId()) && (auction.isActive())) {
                listUserAuctions.add(auction);
            }
        }
        return listUserAuctions;
    }

    public boolean validateAuctionToMakeBid(int categoryNumber, int auctionNumber) {

        for (Auction auction : getAllAuctionsUnderCategory(categoryNumber)) {
            if(auction.getAuctionId() == auctionNumber)
                return true;
        }

        return false;
    }

    public Auction getSingleAuction(int auctionId) {

        for (Auction auction : getAllAuctions().values()) {
            if(auction.getAuctionId() == auctionId) {
                return auction;
            }
        }
        return null;
    }

    public boolean makeWinningBid(int auctionId, BigDecimal price, String user) {
        UserRegistry userRegistry = new UserRegistry(databaseAccess);
        int userId = userRegistry.findUserIdByLogin(user);
        bidList = (getSingleAuction(auctionId)).getListBids();
        int numOfBids = bidList.size();


        if(numOfBids == MAX_NUMBER_OF_BIDS) {
            setAuctionInactive(auctionId);
            insertBid(new Bid(auctionId, userId, price));
            return true;
        }

        insertBid(new Bid(auctionId, userId, price));
        return false;
    }

    private void setAuctionInactive(int auctionId){
        String updateAuction = "UPDATE auctions SET is_active = 'false' WHERE id = '" + auctionId + "';";

        databaseAccess.createData(updateAuction);

    }

    private boolean insertBid(Bid bid){

        String addBidQuery = "INSERT INTO bids (auction_id, user_id, price) VALUES(" +
                "'" + bid.getAuctionId() + "', " +
                "'" + bid.getUserId() + "', " +
                "'" + bid.getBidPrice() + "');";

        databaseAccess.createData(addBidQuery);

        return true;
    }


}