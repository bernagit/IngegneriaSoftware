package db;

import model.user.Configuratore;
import model.user.Fruitore;
import model.user.Utente;
import java.sql.*;

public class DbConnect {
    String url = "jdbc:sqlite:./Data.db";
    public Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public void createNewTable(String tableName) {
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + "(\n"
                + "	id integer PRIMARY KEY,\n"
                + "	username text NOT NULL,\n"
                + "	password text NOT NULL,\n"
                + " firstlogin boolean NOT NULL,\n"
                + " usertype boolean NOT NULL\n"
                + ");";

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Utente insertUser(String username, String password, boolean firstLogin, boolean userType) {
        String sql = "INSERT INTO utenti(username,password,firstlogin,usertype) VALUES(?,?,?,?)";
        String sql2 = "SELECT id FROM utenti WHERE username = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            //se primo login TRUE, altrimenti FALSE
            pstmt.setBoolean(3, firstLogin);
            //se l'utente è un configuratore TRUE, se è un fruitore FALSE
            pstmt.setBoolean(4, userType);
            pstmt.executeUpdate();
            int id = getId(username);
            if(userType)
                return new Configuratore(id, username, password);
            else
                return new Fruitore(id, username, password);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private int getId(String username) {
        String sql = "SELECT id FROM utenti WHERE username = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            int id = rs.getInt("id");
            return id;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    public Utente checkLogin(String usr, String pwd) {

        String sql = "SELECT id, username, password, firstlogin, usertype FROM utenti"
                + " WHERE username = ?"
                + " AND password = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, usr);
            pstmt.setString(2, pwd);
            ResultSet rs = pstmt.executeQuery();
            int id = rs.getInt("id");
            String user = rs.getString("username");
            String pass = rs.getString("password");
            boolean firstLogin = rs.getBoolean("firstlogin");
            boolean userType = rs.getBoolean("usertype");
            if (userType) {
                Configuratore c = new Configuratore(id, user, pass);
                c.setFirstLogin(firstLogin);
                return c;
            } else {
                Fruitore f = new Fruitore(id, user, pass);
                f.setFirstLogin(firstLogin);
                return f;
            }
        } catch (SQLException e) {
            return null;
        }
    }

    public boolean updateCredentials(Utente utente, String newUser, String newPass) {
        boolean checked = this.checkNewUser(newUser);
        if (checked) {
            String sql = "UPDATE utenti SET username = ? , "
                    + "password = ? , "
                    + "firstlogin = ? "
                    + "WHERE id = ?";
            utente.setFirstLogin(false);
            try (Connection conn = this.connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, newUser);
                pstmt.setString(2, newPass);
                pstmt.setBoolean(3, utente.getFirstLogin());
                pstmt.setInt(4, utente.getId());
                pstmt.executeUpdate();
                return checked;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return checked;
    }

    public boolean checkNewUser(String newUser) {
        String sql = "SELECT username, firstlogin FROM utenti"
                + " WHERE username = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newUser);
            ResultSet rs = pstmt.executeQuery();
            boolean firstlogin = rs.getBoolean("firstlogin");
            String username = rs.getString("username");
            //se user esiste e fa il primo login
            return (username != null && firstlogin);
        } catch (SQLException e) {
            return true;
        }
    }

    public boolean checkUsername(String user) {
        String sql = "SELECT username FROM utenti"
                + " WHERE username = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user);
            ResultSet rs = pstmt.executeQuery();
            String username = rs.getString("username");
            //se user esiste
            return (username != null);
        } catch (SQLException e) {
            return false;
        }
    }
}