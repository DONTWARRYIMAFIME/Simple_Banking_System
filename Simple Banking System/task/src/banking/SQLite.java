package banking;

import java.sql.*;

interface User {

    void insert(String number, String pin);

    Integer getId(String number, String pin);

    boolean isAvailableCardNumber(String number);
    
    int getBalance(int id);

    String getCardNumber(int id);

    void addMoney(int balance, int id);

    void writeOfMoney(int balance, int id);

    void closeAccount(int id);

}

public class SQLite implements User {

    private final String url;

    private Connection connect() {

        // SQLite connection string
        Connection con = null;

        try {
            con = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return con;
    }

    public SQLite(String pathToDataBase) {
        url = "jdbc:sqlite:" + pathToDataBase;

        String sql = "CREATE TABLE IF NOT EXISTS card(" +
                "\n\t id      INTEGER PRIMARY KEY AUTOINCREMENT," +
                "\n\t number  TEXT," +
                "\n\t pin     TEXT," +
                "\n\t balance INTEGER DEFAULT 0" +
                "\n)";

        try (Connection con = this.connect();

            // Statement creation
            PreparedStatement statement = con.prepareStatement(sql)) {

            // Statement execution
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isEmpty() {
        String sql = "SELECT * FROM card";

        try (Connection con = this.connect();

             Statement statement = con.createStatement()) {

            try (ResultSet cardInfo = statement.executeQuery(sql)) {

                if (cardInfo.next()) { return false; }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return true;
    }

    public boolean isAvailableCardNumber(String number) {
        String sql = "SELECT * FROM card WHERE number = ?";

        try (Connection con = this.connect();
            PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, number);

            try (ResultSet card = statement.executeQuery()) {

                if (card.next()) {

                    return false;
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return true;
    }

    public void insert(String number, String pin) {
        String sql = "INSERT INTO card (number, pin) VALUES(?,?)";

        try (Connection con = this.connect();

            PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, number);
            statement.setString(2, pin);
            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Integer getId(String number, String pin) {

        String sql = "SELECT id FROM card WHERE number = ? AND pin = ?;";

        try (Connection con = this.connect();

            PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, number);
            statement.setString(2, pin);

            try (ResultSet card = statement.executeQuery()) {

                if (card.next()) {
                    return card.getInt("id");
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;

    }

    public Integer getId(String number) {

        String sql = "SELECT id FROM card WHERE number = ?;";

        try (Connection con = this.connect();

            PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, number);

            try (ResultSet card = statement.executeQuery()) {

                if (card.next()) {
                    return card.getInt("id");
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public int getBalance(int id) {

        String sql = "SELECT balance FROM card WHERE id = ?;";

        try (Connection con = this.connect();

            PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, Integer.toString(id));

            try (ResultSet card = statement.executeQuery()) {

                return card.getInt("balance");

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return 0;
    }

    public String getCardNumber(int id) {

        String sql = "SELECT number FROM card WHERE id = ?;";

        try (Connection con = this.connect();

             PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, Integer.toString(id));

            try (ResultSet card = statement.executeQuery()) {

                return card.getString("number");

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public void addMoney(int balance, int id) {

        String sql = "UPDATE card SET balance = balance + ? WHERE id = ?;";

        try (Connection con = this.connect();

            PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, Integer.toString(balance));
            statement.setString(2, Integer.toString(id));
            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public void writeOfMoney(int balance, int id) {
        String sql = "UPDATE card SET balance = balance - ? WHERE id = ?;";

        try (Connection con = this.connect();

            PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, Integer.toString(balance));
            statement.setString(2, Integer.toString(id));
            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void closeAccount(int id) {

        String sql = "DELETE FROM card WHERE id = ?;";

        try (Connection con = this.connect();

            PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, Integer.toString(id));
            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public void printUserInfo() {
        String sql = "SELECT * FROM card";

        try (Connection con = this.connect();

            PreparedStatement statement = con.prepareStatement(sql)) {

            try (ResultSet cardInfo = statement.executeQuery()) {

                while (cardInfo.next()) {

                    // Retrieve column values
                    int id            = cardInfo.getInt("id");
                    String cardNumber = cardInfo.getString("number");
                    String pin        = cardInfo.getString("pin");
                    int balance       = cardInfo.getInt("balance");

                    System.out.printf("User id: %d%n", id);
                    System.out.printf("Card number: %s%n", cardNumber);
                    System.out.printf("PIN: %s%n", pin);
                    System.out.printf("Balance: %s%n", balance);

                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}