package com.orgella.repository;

import com.orgella.controller.AuctionController;
import com.orgella.exceptions.CredentialsToShortException;
import com.orgella.exceptions.LoginNullException;
import com.orgella.helpers.CategoryBuilder;
import com.orgella.helpers.DatabaseAccess;
import com.orgella.model.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class DatabaseTest {

    public static void main(String[] args) {
        String query = "INSERT INTO users (login, password) VALUES('jacek', '54321');";

        String selectQuery = "SELECT login FROM users;";
        String deleteQuery = "DELETE FROM users where id = '2';";



        DatabaseAccess dao = new DatabaseAccess();

        //CategoryBuilder categoryBuilder = new CategoryBuilder(dao);

        //Node<Category> root = categoryBuilder.getBuilder();
        //System.out.println(root.getChildren().size());
        //System.out.println(categoryBuilder.getBuilder().getChildren().get(0).getChildren().size());
        //System.out.println(categoryBuilder.getRootCategories().getChildren().get(0).getItem().getName());


        AuctionsRegistry ar = new AuctionsRegistry(dao);

        AuctionController auctionController = new AuctionController(ar);



        User user = new User(1, "misiek", "123456");


        auctionController.printAuctions(user);



        //dao.createData(query);

        //dao.createData(deleteQuery);

//        ResultSet rs = dao.readData(selectQuery);
//
//        try {
//            while(rs.next()){
//                System.out.println(rs.getString("login") ); // + " " + rs.getInt("id"));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

       // AuctionsRegistry ar = new AuctionsRegistry(dao);

        //ar.insertAuction(new Auction(true, "sprzedam apple", new BigDecimal(20), 13, "dobry nie smigany", 1));
        //Map<Integer, Auction> maps = new HashMap<>();


        //maps.putAll();

        //System.out.println(ar.getAllAuctions().get(2).toString());
        System.out.println("-------------------- Aukcja jedna: ");

        System.out.println(ar.findAuctionById(3).toString());
        ar.insertBid(new Bid(3, 1, new BigDecimal(21)));

        System.out.println("--------------------> z ceną 21");
        System.out.println(ar.getSingleAuction(3).toString());
        System.out.println("-------------------- z ceną 21 wszystkie");
        auctionController.printAuctions(user);
        System.out.println("");
        System.out.println(ar.makeWinningBid(3,new BigDecimal(25),"jacek"));
        System.out.println(ar.validateAuctionToMakeBid(4, 3));

        System.out.println(ar.getSingleAuction(3).toString());

        Auction auction = ar.getSingleAuction(3);
        if(auction.validateBid(new BigDecimal(26))){
            System.out.println(ar.makeWinningBid(3,new BigDecimal(26),"jacek"));
        }
        System.out.println(ar.validateAuctionToMakeBid(4, 3));



    }
}
