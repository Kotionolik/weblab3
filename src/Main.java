import db.*;
import dao.*;
import models.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Date;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
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
                System.out.println("1. Show publications");
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
                int choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {
                    case 1:
                        logger.info("Getting all publications...");
                        List<Publication> pubs = pubDAO.getAllPublications();
                        System.out.println("\n" + "=".repeat(100));
                        System.out.printf("%-5s | %-30s | %-20s | %-30s | %-10s%n",
                                "ID", "Title", "Publisher", "Description", "Price");
                        System.out.println("=".repeat(100));
                        for (Publication publication : pubs) {
                            System.out.printf("%-5d | %-30s | %-20s | %-30s | %-10.2f%n",
                                    publication.getId(),
                                    truncate(publication.getTitle(), 30),
                                    truncate(publication.getPublisher(), 20),
                                    truncate(publication.getDescription(), 30),
                                    publication.getPrice());
                        }
                        System.out.println("=".repeat(100));
                        logger.info("Got publication list successfully");
                        break;

                    case 2:
                        logger.info("Getting all users...");
                        List<User> users = userDAO.getUsers();
                        System.out.println("\n" + "=".repeat(80));
                        System.out.printf("%-5s | %-25s | %-40s%n", "ID", "Username", "Email");
                        System.out.println("=".repeat(80));
                        for (User user : users) {
                            System.out.printf("%-5d | %-25s | %-40s%n",
                                    user.getId(),
                                    truncate(user.getUsername(), 25),
                                    truncate(user.getEmail(), 40));
                        }
                        System.out.println("=".repeat(80));
                        logger.info("Got user list successfully");
                        break;

                    case 3:
                        logger.info("Getting all payments...");
                        List<Payment> payments = payDAO.getAllPayments();
                        System.out.println("\n" + "=".repeat(70));
                        System.out.printf("%-5s | %-15s | %-15s | %-20s%n",
                                "ID", "Subscription ID", "Amount", "Payment Date");
                        System.out.println("=".repeat(70));
                        for (Payment payment : payments) {
                            System.out.printf("%-5d | %-15d | %-15.2f | %-20s%n",
                                    payment.getId(),
                                    payment.getSubscriptionId(),
                                    payment.getAmount(),
                                    payment.getPaymentDate());
                        }
                        System.out.println("=".repeat(70));
                        logger.info("Got payment list successfully");
                        break;

                    case 4:
                        logger.info("Getting all subscriptions...");
                        List<Subscription> allsubs = subDAO.getAllSubscriptions();
                        System.out.println("\n" + "=".repeat(80));
                        System.out.printf("%-5s | %-10s | %-15s | %-20s | %-10s%n",
                                "ID", "User ID", "Publication ID", "Start Date", "Months");
                        System.out.println("=".repeat(80));
                        for (Subscription sub : allsubs) {
                            System.out.printf("%-5d | %-10d | %-15d | %-20s | %-10d%n",
                                    sub.getId(),
                                    sub.getUserId(),
                                    sub.getPublicationId(),
                                    sub.getStartDate(),
                                    sub.getMonths());
                        }
                        System.out.println("=".repeat(80));
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
                            System.out.println("\n" + "=".repeat(70));
                            System.out.printf("%-5s | %-15s | %-20s | %-10s%n",
                                    "ID", "Publication ID", "Start Date", "Months");
                            System.out.println("=".repeat(70));
                            for (Subscription subscription : subs) {
                                System.out.printf("%-5d | %-15d | %-20s | %-10d%n",
                                        subscription.getId(),
                                        subscription.getPublicationId(),
                                        subscription.getStartDate(),
                                        subscription.getMonths());
                            }
                            System.out.println("=".repeat(70));
                        }
                        logger.info("Got subscription list for user {} successfully", userId);
                        break;

                    case 6:
                        logger.info("Getting unpaid subscriptions...");
                        System.out.println("\nUnpaid Subscriptions:");
                        List<Subscription> unsubs = subDAO.getUnpaidSubscriptions();
                        if (unsubs.isEmpty()) {
                            System.out.println("No unpaid subscriptions found.");
                        } else {
                            System.out.println("=".repeat(70));
                            System.out.printf("%-5s | %-15s | %-20s | %-10s%n",
                                    "ID", "Publication ID", "Start Date", "Months");
                            System.out.println("=".repeat(70));
                            for (Subscription subscription : unsubs) {
                                System.out.printf("%-5d | %-15d | %-20s | %-10d%n",
                                        subscription.getId(),
                                        subscription.getPublicationId(),
                                        subscription.getStartDate(),
                                        subscription.getMonths());
                            }
                            System.out.println("=".repeat(70));
                        }
                        logger.info("Got unpaid subscription list successfully");
                        break;

                    case 7:
                        System.out.print("Enter subscription ID: ");
                        int subId = sc.nextInt();
                        System.out.print("Enter payment amount: ");
                        double amt = sc.nextDouble();
                        logger.info("Creating new payment for subscription {} with amount of {}...", subId, amt);
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
                        double price = sc.nextDouble();
                        logger.info("Registering new publication {}...", title);
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
                        logger.info("Registering new subscription for publication {} for user {} for {} months...",
                                publication_id, user_id, months);
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
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            logger.fatal("Unexpected error", e);
        } finally {
            if (sc != null) {
                sc.close();
            }
            EntityManagerUtil.close();
            logger.info("Application closed");
        }
    }

    private static String truncate(String str, int maxLength) {
        if (str == null) return "";
        if (str.length() <= maxLength) return str;
        return str.substring(0, maxLength - 3) + "...";
    }
}