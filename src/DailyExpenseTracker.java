import java.io.*;
import java.util.*;

class Expense {
    private String category;
    private double amount;
    private String description;
    private Date date;

    public Expense(String category, double amount, String description) {
        this.category = category;
        this.amount = amount;
        this.description = description;
        this.date = new Date();
    }

    public double getAmount() {
        return amount;
    }

    public Date getDate() {
        return date;
    }

    public String toString() {
        return date + " | Category: " + category + " | Amount: " + amount + " | Description: " + description;
    }
}

class ExpenseManager {
    private List<Expense> expenses;
    private final String FILE_NAME = "expenses.txt";

    public ExpenseManager() {
        expenses = new ArrayList<>();
        loadExpenses();
    }

    public void addExpense(String category, double amount, String description) {
        Expense expense = new Expense(category, amount, description);
        expenses.add(expense);
        saveExpenses();
    }

    public void viewSummary(String period) {
        double total = 0;
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);

        for (Expense expense : expenses) {
            calendar.setTime(expense.getDate());
            switch (period.toLowerCase()) {
                case "day":
                    if (isSameDay(now, expense.getDate())) total += expense.getAmount();
                    break;
                case "week":
                    if (isSameWeek(now, expense.getDate())) total += expense.getAmount();
                    break;
                case "month":
                    if (isSameMonth(now, expense.getDate())) total += expense.getAmount();
                    break;
                default:
                    System.out.println("Invalid period. Use 'day', 'week', or 'month'.");
                    return;
            }
        }

        System.out.println("Total expenses for the " + period + ": " + total);
    }

    private boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    private boolean isSameWeek(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR);
    }

    private boolean isSameMonth(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
    }

    private void saveExpenses() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Expense expense : expenses) {
                writer.write(expense.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving expenses: " + e.getMessage());
        }
    }

    private void loadExpenses() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("Loaded: " + line);
            }
        } catch (IOException e) {
            System.out.println("No existing data found.");
        }
    }
}

public class DailyExpenseTracker {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ExpenseManager manager = new ExpenseManager();

        while (true) {
            System.out.println("\n1. Add Expense\n2. View Summary\n3. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter category: ");
                    String category = scanner.nextLine();
                    System.out.print("Enter amount: ");
                    double amount = scanner.nextDouble();
                    scanner.nextLine();
                    System.out.print("Enter description: ");
                    String description = scanner.nextLine();
                    manager.addExpense(category, amount, description);
                    break;
                case 2:
                    System.out.print("View summary for (day/week/month): ");
                    String period = scanner.nextLine();
                    manager.viewSummary(period);
                    break;
                case 3:
                    System.out.println("Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}