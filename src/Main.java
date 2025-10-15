import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        UserDAO userDAO = new UserDAO();
        PublicationDAO pubDAO = new PublicationDAO();
        SubscriptionDAO subDAO = new SubscriptionDAO();
        PaymentDAO payDAO = new PaymentDAO();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== ГЛАВНОЕ МЕНЮ ===");
            System.out.println("1. Показать все издания");
            System.out.println("2. Показать издания пользователя");
            System.out.println("3. Показать неоплаченные подписки");
            System.out.println("4. Провести платеж");
            System.out.println("5. Зарегистрировать новое издание");
            System.out.println("0. Выход");
            System.out.print("Выберите: ");
            int choice = sc.nextInt(); sc.nextLine();

            switch (choice) {
                case 1:
                    List<Publication> pubs = pubDAO.getAllPublications();
                    for (Publication publication : pubs) {
                        System.out.println("Издание №" + publication.getId() + ": " + publication.getTitle() + "|\t|" + publication.getPublisher() + "|\t|" + publication.getDescription() + "|\t|" + publication.getPrice());
                    }
                    break;

                case 2:
                    System.out.print("Введите ID пользователя: ");
                    int userId = sc.nextInt();
                    List<Subscription> subs = subDAO.getSubscriptionsByUser(userId);
                    if (subs.isEmpty()) {
                        System.out.println("Подписок не найдено.");
                    } else {
                        for (Subscription subscription : subs) {
                            System.out.println("Подписка №" + subscription.getId() + ": " + subscription.getPublicationId() + "|\t|" + subscription.getStartDate() + "|\t|" + subscription.getMonths());
                        }
                    }
                    break;

                case 3:
                    System.out.println("Неоплаченные подписки:");
                    List<Subscription> unsubs = subDAO.getUnpaidSubscriptions();
                    for (Subscription subscription : unsubs) {
                        System.out.println("Подписка №" + subscription.getId() + ": " + subscription.getPublicationId() + "|\t|" + subscription.getStartDate() + "|\t|" + subscription.getMonths());
                    }
                    break;

                case 4:
                    System.out.print("Введите ID подписки: ");
                    int subId = sc.nextInt();
                    System.out.print("Введите сумму: ");
                    double amt = sc.nextDouble();
                    Date date = new Date(System.currentTimeMillis());
                    payDAO.addPayment(new Payment(subId, amt, date));
                    break;

                case 5:
                    System.out.print("Название издания: ");
                    String title = sc.nextLine();
                    System.out.print("Название издателя: ");
                    String publisher = sc.nextLine();
                    System.out.print("Описание: ");
                    String desc = sc.nextLine();
                    System.out.print("Цена за месяц: ");
                    double price = sc.nextDouble();
                    pubDAO.addPublication(new Publication(title, publisher, desc, price));
                    break;

                case 0:
                    System.out.println("До свидания!");
                    return;

                default:
                    System.out.println("Неверный выбор!");
            }
        }
    }
}
