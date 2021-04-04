package banking;

import java.util.Objects;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;

public class Banking {

    private final SQLite dataBase;

    private final Stack<Menu> menu = new Stack<>();
    private final Scanner scanner = new Scanner(System.in);
    private final Random random = new Random();

    private Integer currentUserId = null;

    //Public
    public Banking(String pathToFile) {
        dataBase = new SQLite(pathToFile);
        dataBase.printUserInfo();
        menu.push(initMainMenu());
    }

    public void checkUserInput() {

        while (!menu.isEmpty()) {

            menu.lastElement().printInformation();

            //User input
            switch(scanner.next()) {
                case "1":
                    menu.lastElement().firstMethod();
                    break;
                case "2":
                    menu.lastElement().secondMethod();
                    break;
                case "3":
                    menu.lastElement().thirdMethod();
                    break;
                case "4":
                    menu.lastElement().fourthMethod();
                    break;
                case "5":
                    menu.lastElement().fifthMethod();
                    break;
                case "0":
                    menu.lastElement().setExitProgram();
                    break;
                default:
                    System.out.println("Wrong input. Try again");
                    break;
            }

            if (menu.lastElement().isExitMenu()) {
                menu.pop();
            } else if (menu.lastElement().isExitProgram()) {
                while (!menu.isEmpty()) {
                    menu.pop();
                }
            }
        }

        System.out.println("bye");
    }

    //Private
    private Menu initMainMenu() {
        return new Menu(
                "1. Create an account\n" +
                        "2. Log into account\n" +
                        "0. Exit") {

            @Override
            void firstMethod() {

                String cardNumber = generateCardNumber();
                while(!dataBase.isAvailableCardNumber(cardNumber)) {
                    cardNumber = generateCardNumber();
                }

                String pin = generatePin();

                dataBase.insert(cardNumber, pin);
                currentUserId = dataBase.getId(cardNumber, pin);

                System.out.println("Your card has been created");
                System.out.format("Your card number:\n%s\n", cardNumber);
                System.out.format("Your card PIN:\n%s\n", pin);

            }

            @Override
            void secondMethod() {

                if (!dataBase.isEmpty()) {

                    System.out.println("Enter your card number:");
                    scanner.nextLine();
                    String cardNumber = scanner.nextLine();
                    System.out.println("Enter your PIN:");
                    String pin = scanner.nextLine();

                    currentUserId = dataBase.getId(cardNumber, pin);

                    if (currentUserId != null) {
                        System.out.println("You have successfully logged in!");
                        menu.push(initAccountMenu());
                    } else {
                        System.out.println("Wrong card number or PIN!");
                    }
                } else {
                    System.out.println("Data base is empty");
                }
            }

        };
    }

    private Menu initAccountMenu() {
        return new Menu("1. Balance\n" +
                        "2. Add income\n" +
                        "3. Do transfer\n" +
                        "4. Close account\n" +
                        "5. Log out\n" +
                        "0. Exit") {
            @Override
            void firstMethod() {
                System.out.format("Balance: %d\n", dataBase.getBalance(currentUserId));
            }

            @Override
            void secondMethod() {
                System.out.println("Enter income:");
                dataBase.addMoney(scanner.nextInt(), currentUserId);
                System.out.println("Income was added!");
            }

            @Override
            void thirdMethod() {
                System.out.println("Transfer");
                System.out.println("Enter card number:");

                scanner.nextLine();
                String cardNumber = scanner.nextLine();

                if (cardNumber.length() == 16) {

                    if (!Objects.equals(cardNumber, dataBase.getCardNumber(currentUserId))) {

                        if (isValidCardNumber(cardNumber)) {

                            if (!dataBase.isAvailableCardNumber(cardNumber)) {

                                System.out.println("Enter how much money you want to transfer:");
                                int transferMoney = scanner.nextInt();

                                if (transferMoney <= dataBase.getBalance(currentUserId)) {

                                    dataBase.writeOfMoney(transferMoney, currentUserId);
                                    dataBase.addMoney(transferMoney, dataBase.getId(cardNumber));

                                    System.out.println("Success!");

                                } else {
                                    System.out.println("Not enough money!");
                                }

                            } else {
                                System.out.println("Such a card does not exist.");
                            }

                        } else {
                            System.out.println("Probably you made a mistake in the card number. Please try again!");
                        }

                    } else {
                        System.out.println("You can't transfer money to the same account!");
                    }

                } else {
                    System.out.format("Wrong card number length %d, must be 16\n", cardNumber.length());
                }
            }

            @Override
            void fourthMethod() {
                dataBase.closeAccount(currentUserId);

                currentUserId = null;
                setExitMenu();

                System.out.println("The account has been closed!");
            }

            @Override
            void fifthMethod() {
                currentUserId = null;
                setExitMenu();

                System.out.println("You have successfully logged out!");
            }
        };
    }

    private boolean isValidCardNumber(String cardNumber) {
        return useLuhnAlgorithm(cardNumber) == Character.getNumericValue(cardNumber.charAt(15));
    }

    private int useLuhnAlgorithm(String cardNumber) {

        int len = cardNumber.length();
        int[] arr = new int[16];

        for(int i = 0; i < len; i++) {
            arr[i] = Character.getNumericValue(cardNumber.charAt(i));
        }

        int sum = 0;
        for (int i = 0; i < arr.length - 1; i++) {
            int original = arr[i];
            if (i % 2 == 0) {
                original *= 2;

                if (original > 9) {
                    original -= 9;
                }
            }

            sum += original;
        }


        for (int i = 0; i < 10; i++) {
            if ((sum + i) % 10 == 0) {
                return i;
            }
        }

        return 0;
    }

    private String generateCardNumber() {

        StringBuilder cardNumber = new StringBuilder("400000");

        for (int i = 6; i < 15; i++) {
            cardNumber.append(random.nextInt(10));
        }

        cardNumber.append(useLuhnAlgorithm(cardNumber.toString()));

        return cardNumber.toString();
    }

    private String generatePin() {
        StringBuilder pin = new StringBuilder();

        for (int i = 0; i < 4; i++) {
            pin.append(random.nextInt(10));
        }

        return pin.toString();
    }

}