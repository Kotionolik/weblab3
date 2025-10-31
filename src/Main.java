import db.*;
import dao.*;
import models.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        logger.info("=== Launching ===");
        Scanner sc = new Scanner(System.in);

        try {
            UserDAO userDAO = new UserDAO();
            PublicationDAO pubDAO = new PublicationDAO();
            SubscriptionDAO subDAO = new SubscriptionDAO();
            PaymentDAO payDAO = new PaymentDAO();
            
            logger.info("DAOs initialised successfully");

            while (true) {
                System.out.println("\n=== MENU ===");
                System.out.println("1. Show publication");
                System.out.println("2. Show users");
                System.out.println("3. Show payments");
                System.out.println("4. Show subscriptions");
                System.out.println("5. Show publications of user");
                System.out.println("6. Show unpaid subscriptions");
                System.out.println("7. Conduct payment");
                System.out.println("8. Register new publication");
                System.out.println("9. Register new subscription");
                System.out.println("0. Exit");
                System.out.print("Choice: ");
                int choice = sc.nextInt(); sc.nextLine();

                switch (choice) {
                    case 1:
                        logger.info("Getting all publications...");
                        List<Publication> pubs = pubDAO.getAllPublications();
                        System.out.println("Publication"+ "|\t|" + "Name" + "|\t|" + "Publisher" + "|\t|" + "Description" + "|\t|" + "Price");
                        for (Publication publication : pubs) {
                            System.out.println("Publication №" + publication.getId() + ": " + publication.getTitle() + "|\t|" + publication.getPublisher() + "|\t|" + publication.getDescription() + "|\t|" + publication.getPrice());
                        }
                        logger.info("Got publication list successfully");
                        break;

                    case 2:
                        logger.info("Getting all users...");
                        List<User> users = userDAO.getUsers();
                        System.out.println("User"+ "|\t|" + "Name" + "|\t|" + "Email");
                        for (User user : users) {
                            System.out.println("User №" + user.getId() + ": " + user.getUsername() + "|\t|" + user.getEmail());
                        }
                        logger.info("Got user list successfully");
                        break;
                    
                    case 3:
                        logger.info("Getting all payments...");
                        List<Payment> payments = payDAO.getAllPayments();
                        System.out.println("Payment"+ "|\t|" + "Subscription ID" + "|\t|" + "Payment amount" + "|\t|" + "Payment date");
                        for (Payment payment : payments) {
                            System.out.println("Payment №" + payment.getId() + ": " + payment.getSubscriptionId() + "|\t|" + payment.getAmount() + "|\t|" + payment.getPaymentDate());
                        }
                        logger.info("Got payment list successfully");
                        break;
                    
                    case 4:
                        logger.info("Getting all subscriptions...");
                        List<Subscription> allsubs = subDAO.getAllSubscriptions();
                        System.out.println("Subcription"+ "|\t|" + "User ID" + "|\t|" + " Publication ID" + "|\t|" + "Date of subscription" + "|\t|" + "Months");
                        for (Subscription sub : allsubs) {
                            System.out.println("Subscription №" + sub.getId() + ": " + sub.getUserId() + "|\t|" + sub.getPublicationId() + "|\t|" + sub.getStartDate() + "|\t|" + sub.getMonths());
                        }
                        logger.info("Got subscription list successfully");
                        break;

                    case 5:
                        System.out.print("Enter user ID: ");
                        int userId = sc.nextInt();
                        logger.info("Getting subscription list for user {}...", userId);
                        List<Subscription> subs = subDAO.getSubscriptionsByUser(userId);
                        if (subs.isEmpty()) {
                            System.out.println("Didn't find any subscriptions.");
                        } else {
                            for (Subscription subscription : subs) {
                                System.out.println("Subscription №" + subscription.getId() + ": " + subscription.getPublicationId() + "|\t|" + subscription.getStartDate() + "|\t|" + subscription.getMonths());
                            }
                        }
                        logger.info("Got subscription list for user {} successfully", userId);
                        break;

                    case 6:
                        logger.info("Getting unpaid subsciptions...");
                        System.out.println("Unpaid Subscriptions:");
                        List<Subscription> unsubs = subDAO.getUnpaidSubscriptions();
                        for (Subscription subscription : unsubs) {
                            System.out.println("Subscription №" + subscription.getId() + ": " + subscription.getPublicationId() + "|\t|" + subscription.getStartDate() + "|\t|" + subscription.getMonths());
                        }
                        logger.info("Got unpaid subscription list successfully");
                        break;

                    case 7:
                        System.out.print("Enter subscription ID: ");
                        int subId = sc.nextInt();
                        System.out.print("Enter payment amount: ");
                        double amt = sc.nextDouble();
                        logger.info("Getting new payment for subscription {} with amount of {}...", subId, amt);
                        Date date = new Date(System.currentTimeMillis());
                        payDAO.addPayment(new Payment(subId, amt, date));
                        logger.info("Payment registered successfully.");
                        break;

                    case 8:
                        System.out.print("Publication name: ");
                        String title = sc.nextLine();
                        System.out.print("Publisher name: ");
                        String publisher = sc.nextLine();
                        System.out.print("Description: ");
                        String desc = sc.nextLine();
                        System.out.print("Price per month: ");
                        logger.info("Registering new publication {}...", title);
                        double price = sc.nextDouble();
                        pubDAO.addPublication(new Publication(title, publisher, desc, price));
                        logger.info("Publication registered successfully.");
                        break;
                    
                    case 9:
                        System.out.print("User ID: ");
                        int user_id = sc.nextInt();
                        System.out.print("Publication ID: ");
                        int publication_id = sc.nextInt();
                        System.out.print("Subscription months: ");
                        int months = sc.nextInt();
                        logger.info("Registering new subcription for publication {} for user {} for {} months...", publication_id, user_id, months);
                        Date start_date = new Date(System.currentTimeMillis());
                        subDAO.addSubscription(new Subscription(user_id, publication_id, start_date, months));
                        logger.info("Subscription registered successfully.");
                        break;

                    case 0:
                        System.out.println("Exiting application...");
                        logger.info("=== Exiting ===");
                        return;

                    default:
                        System.out.println("Wrong choice, try again.");
                        logger.warn("Wrong menu input choice");
                }
            }
        } catch (DAOException e) {
            System.err.println("Initialisation critical error: " + e.getMessage());
            logger.fatal("Couldn't initialise application", e);
        } finally {
            if (sc != null) {
                sc.close();
            }
        }
    }
}
