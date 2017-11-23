package com.orgella;

import com.orgella.controller.AuctionController;
import com.orgella.exceptions.CredentialsToShortException;
import com.orgella.exceptions.LoginExistException;
import com.orgella.exceptions.LoginNullException;
import com.orgella.helpers.CategoryBuilder;
import com.orgella.model.Auction;
import com.orgella.model.Category;
import com.orgella.model.Node;
import com.orgella.model.User;
import com.orgella.repository.AuctionsRegistry;
import com.orgella.helpers.DatabaseAccess;
import com.orgella.repository.UserRegistry;
import com.orgella.view.CategoryView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public enum State {
        START,
        DURING_LOGIN,
        LOGGED_IN,
        DURING_REGISTRATION,
        DURING_ADDING_AUCTION,
        DURING_DELETING_AUCTION,
        DURING_DISPLAYING_FINISHED_AUCTIONS,
        DURING_MAKING_A_BID,
        EXIT}

    public enum BidState {
        NO_SUCH_AUCTION,
        PRICE_TOO_LOW,
        BID_MADE,
        BID_WON_AUCTION,
    }

    private static User currentUser;
    private static int  categoryNumber = 10;


    public static void main(String[] args) {

        State state = State.START;

        DatabaseAccess databaseAccess = new DatabaseAccess();
        AuctionsRegistry auctionsRegistry = new AuctionsRegistry(databaseAccess);
        AuctionController auctionController = new AuctionController(auctionsRegistry);
        UserRegistry userRegistry = new UserRegistry(databaseAccess);

        Scanner input = new Scanner(System.in).useDelimiter("\\n");

        while(state != State.EXIT) {
            switch(state) {
                case START:
                    state = printStartScreen(input, userRegistry);
                    break;
                case DURING_LOGIN:
                    state = printLoginScreen(input, userRegistry);
                    break;
                case DURING_REGISTRATION:
                    state = printRegistrationScreen(input, userRegistry);
                    break;
                case LOGGED_IN:
                    state = printLoggedInScreen(input, auctionsRegistry, auctionController);
                    break;
                case DURING_ADDING_AUCTION:
                    state = printAddAuctionScreen(input, userRegistry, auctionsRegistry);
                    break;
                case DURING_DELETING_AUCTION:
                    state = printDeleteAuctionScreen(input, auctionsRegistry);
                    break;
                case DURING_DISPLAYING_FINISHED_AUCTIONS:
                    state = printFinishedAuctionScreen(input, userRegistry, auctionsRegistry);
                case DURING_MAKING_A_BID:
                    state = printMakingBid(input, auctionController);
            }
        }
        System.out.println("Bye");
        input.close();
    }

    private static State printMakingBid(
            Scanner input,
            AuctionController auctionController) {
        displayAuctionsCategoryTree(auctionController);
        System.out.println("Select category to display");


        int categoryNumber = Integer.parseInt(input.next());
        if (auctionController.validateCategoryNumber(categoryNumber)) {
            // check if categoryNumber is valid number of category, like below :)

            auctionController.printAllAuctionsUnderCategory(categoryNumber);
            System.out.println("Select auction to make an offer");
            int auctionId = Integer.parseInt(input.next());
            if (auctionController.validateAuctionToMakeBid(categoryNumber, auctionId)) {
                //chceck if auction number entered is one of auctions IDs of category and subcategories displayed
                System.out.println("Enter your bid");
                BigDecimal bidValue = new BigDecimal(input.next());
                if (auctionController.validateBid(bidValue, auctionId)) {
                    // check if bidValue is not too low
                    if (auctionController.makeWinningBid(auctionId, bidValue, currentUser.getLogin())) {
                        // try to make winnig bid,
                        // in case of yes (won)
                        // - add winning bid to auction
                        // - set iSActive to false
                        // - return true
                        // in case of no
                        // - add  bid to auction
                        // return false
                        System.out.println("You won an auction, " +
                                auctionController.getSingleAuction(auctionId).getTitle() +
                                // let me get single auction with ID == auctionNumber
                                " is yours.");
                        System.out.println("Your credit card was charged, you have " + bidValue + " less.");
                    } else {
                        System.out.println("you made a bid, auction is still active.");
                    }
                }
            }
        }
        return State.LOGGED_IN;
    }


    /**
     * Prints screen with the list of finished auctions owned by the current user
     * @param input             current input from scanner
     * @param userRegistry      registry of users
     * @param auctionsRegistry  registry of auctions
     * @return                  state that user can see when logged in
     */
    private static State printFinishedAuctionScreen(
            Scanner input,
            UserRegistry userRegistry,
            AuctionsRegistry auctionsRegistry) {
        ArrayList<Auction> auctionsList = new ArrayList<>();
        auctionsList = auctionsRegistry.getUserFinishedAuctionList(currentUser);
        System.out.println("Finished auction of " + currentUser);
        for (Auction auction : auctionsList) {
            System.out.println(auction.toString());
        }
        return State.LOGGED_IN;
    }

    private static State printDeleteAuctionScreen(
            Scanner input,
            AuctionsRegistry auctionsRegistry) {
        AuctionController auctionController = new AuctionController(auctionsRegistry);
        auctionController.printAuctions(currentUser);
        System.out.println("Enter id number of auction you wish to delete.");
        try {
            int auctionIdToDelete = Integer.parseInt(input.next()); // entry data validation
            auctionsRegistry.removeAuction(auctionIdToDelete);
            System.out.println("Your auction " + auctionIdToDelete + " was deleted.");
        } catch (InputMismatchException e) {
            System.out.println("Enter integer number please");
        }
        return State.LOGGED_IN;
    }

    private static State printAddAuctionScreen(
            Scanner input,
            UserRegistry userRegistry,
            AuctionsRegistry auctionsRegistry) {
        System.out.println("Select category ID for your new auction");
        int categoryNumber = Integer.parseInt(input.next()); //TODO entry data validation
        System.out.println("Enter auction title");
        String auctionTitle = input.next(); //TODO entry data validation
        System.out.println("Enter description");
        String auctionDescription = input.next(); //TODO entry data validation
        System.out.println("Enter price");
        BigDecimal auctionPrice = new BigDecimal(input.next()); //TODO entry data validation
        AuctionController auctionController = new AuctionController(auctionsRegistry);

        auctionController.addAuction(
                auctionTitle,
                auctionPrice,
                categoryNumber,
                auctionDescription,
                userRegistry.findUserIdByLogin(currentUser.getLogin()));
        System.out.println("Your auction was added.");
        return State.LOGGED_IN;
    }

    private static State printLoggedInScreen(
            Scanner input,
            AuctionsRegistry auctionsRegistry,
            AuctionController auctionController) {
        System.out.println("[4] display your auctions");
        System.out.println("[5] delete auction");
        System.out.println("[6] make a bid");
        System.out.println("[7] add auction");
        System.out.println("[8] display your finished auctions");
        System.out.println("[9] display auctions");
        System.out.println("[category number] to display auctions in this category");
        System.out.println("[0] Exit ");
        int numberEntered = input.nextInt(); //TODO entry data validation
        switch (numberEntered) {
            case 0: return State.EXIT;
            case 4:
                displayUsersAuctions(currentUser, auctionsRegistry);
                return State.LOGGED_IN;
            case 5:
                return State.DURING_DELETING_AUCTION;
            case 6:
                printMakingBid(input, auctionController);
                return State.LOGGED_IN;
            case 7:
                displayAuctionsCategoryTree(auctionController);
                return State.DURING_ADDING_AUCTION;
            case 8:
                return dispalyUsersClosedAuctions(currentUser, auctionsRegistry);
            case 9:
                return displayAuctions(input, auctionsRegistry);
            default:
                if (auctionController.validateCategoryNumber(categoryNumber)) {
                    displayCategoryAuctions(numberEntered, auctionsRegistry);
                } else {
                    System.out.println("Sorry mate, no such category");
                }
                return State.LOGGED_IN;
        }
    }

    private static State displayAuctions(
            Scanner input,
            AuctionsRegistry auctionsRegistry) {
        AuctionController auctionController = new AuctionController(auctionsRegistry);
        displayAuctionsCategoryTree(auctionController);
        System.out.println("Enter category number");
        int categoryNumber = Integer.parseInt(input.next());
        auctionController.printAllAuctionsUnderCategory(categoryNumber);
        return State.LOGGED_IN;
    }

    private static State dispalyUsersClosedAuctions(
            User currentUser,
            AuctionsRegistry auctionsRegistry) {

        System.out.println("- - - - - - - - - - - ");
        System.out.println("Closed auctions of " + currentUser.getLogin());
        System.out.println("- - - - - - - - - - - ");
        AuctionController auctionController = new AuctionController(auctionsRegistry);
        auctionController.printInactiveAuctions(currentUser);
        return State.LOGGED_IN;
    }

    private static void displayCategoryAuctions(
            int categoryNumber,
            AuctionsRegistry auctionsRegistry) {
        System.out.println("- - - - - - - - - - - ");
        System.out.println("Display auctions in category number " + categoryNumber);
        System.out.println("- - - - - - - - - - - ");
        ArrayList<Auction> categoryAuctionList = new ArrayList<>();
        categoryAuctionList = (ArrayList<Auction>)auctionsRegistry.getAllAuctionsUnderCategory(categoryNumber);
        for (Auction auction : categoryAuctionList) {
            System.out.println(auction);
        }


    }

/*    private static void MakeBid() {
        System.out.println("- - - - - - - - - - - ");
        System.out.println("Make a bid");
        System.out.println("- - - - - - - - - - - ");
    }*/

    private static void displayUsersAuctions(
            User user,
            AuctionsRegistry auctionsRegistry) {

        System.out.println("- - - - - - - - - - - ");
        System.out.println("Display all auctions of user");
        System.out.println("- - - - - - - - - - - ");
        AuctionController auctionController = new AuctionController(auctionsRegistry);
        auctionController.printAuctions(user);
    }

    private static void displayAuctionsCategoryTree(AuctionController auctionController) {
        System.out.println("- - - - - - - - - - - ");
        System.out.println("Display auctions category tree");
        auctionController.displayCategory();
        System.out.println("- - - - - - - - - - - ");
    }

    private static State printRegistrationScreen(
            Scanner input,
            UserRegistry userRegistry) {
        System.out.println("create you login: ");
        String login = input.next(); //TODO entry data validation
        System.out.println("create your password: ");
        String password = input.next(); //TODO entry data validation

        try {
            User user = new User(login, password);
            userRegistry.addUser(user);
            System.out.println("User has been created!");
            return State.LOGGED_IN;
        } catch (LoginExistException e) {
            System.out.println("Ups... " + login + " already exist in database or password is incorrect\n");;
        } catch (CredentialsToShortException e) {
            e.printStackTrace();
        } catch (LoginNullException e) {
            e.printStackTrace();
        }
        return State.START;

    }

    private static State printLoginScreen(
            Scanner input,
            UserRegistry userRegistry) {
        System.out.println("enter you login: ");
        String login = input.next(); //TODO entry data validation
        System.out.println("enter your password: ");
        String password = input.next(); //TODO entry data validation
        User user = null;
        try {
            user = new User(login, password);
            if (userRegistry.existUser(user) == true && userRegistry.isLoginAndPasswordCorrect(user)) {
                System.out.println("Welcome " + user.getLogin());
                currentUser = user;
                return State.LOGGED_IN;
            } else {
                LoginExistException exception = new LoginExistException("This marvelous application couldn't find such login in current database or password " +
                        "you've entered is " +
                        "incorrect." +
                        " Please " +
                        "try again.\n");
                System.out.println(exception.getMessage());
                return State.START;
            }
        } catch (CredentialsToShortException e) {
            e.printStackTrace();
        } catch (LoginNullException e) {
            e.printStackTrace();
        }
        return State.START;

    }

    private static State printStartScreen(
            Scanner input,
            UserRegistry userRegistry) {
        System.out.println("Welcome to Allegro auction portal!");
        System.out.println("type [1] if you want to log in\n" +
                "type [2] if you want to create new account\n" +
                "type [0] anytime you want to exit\n");
        int numberEntered = input.nextInt(); //TODO entry data validation
        switch (numberEntered) {
            case 0: return State.EXIT;
            case 1: return State.DURING_LOGIN;
            case 2: return State.DURING_REGISTRATION;
            default:
                System.out.println("Sorry mate, wrong number");
                return State.START;
        }
    }
}