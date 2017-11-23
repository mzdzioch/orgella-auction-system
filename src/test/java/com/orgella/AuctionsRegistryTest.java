//package com.orgella;
//
//import com.orgella.helpers.AuctionsCounter;
//import com.orgella.model.Auction;
//import com.orgella.repository.AuctionsRegistry;
//import org.junit.Before;
//import org.junit.Test;
//
//import java.math.BigDecimal;
//
//import static org.junit.Assert.assertTrue;
//
//
//public class AuctionsRegistryTest {
//
//    String fileAuctionsName;
//    Auction auction1;
//    Auction auction0;
//    AuctionsRegistry auctionsRegistry;
//
//    @Before
//    public void setUp() throws Exception {
//        fileAuctionsName = "auctions.txt";
//
//        auction0 = new Auction(true, "Fiat", new BigDecimal(5000), 10, "bla bla bla", "tomek");
//        auction1 = new Auction(true,"Toyota", new BigDecimal(14000), 10, "bla bla bla", "bartek");
//        auctionsRegistry = new AuctionsRegistry(fileAuctionsName);
//        AuctionsCounter auctionsCounter = new AuctionsCounter("testAuctionsCounter.txt");
//        int currentId = auctionsCounter.readCurrentID();
//    }
//
//    @Test
//    public void writeAuction() throws Exception {
//        AuctionsCounter auctionsCounter = new AuctionsCounter("testAuctionsCounter.txt");
//        int currentId = auctionsCounter.readCurrentID();
//
//        //AuctionsRegistry auctionsRegistry = new AuctionsRegistry(fileAuctionsName);
//        auctionsRegistry.insertAuction(auction0);
//        auctionsRegistry.insertAuction(auction1);
//        assertTrue(auctionsRegistry.getAllAuctions().get(currentId-2).getCategoryId() == 10);
//        assertTrue(auctionsRegistry.getAllAuctions().get(currentId-1).getCategoryId() == 10);
//    }
//
//}
