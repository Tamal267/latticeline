package com.example.latticeline;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBconnect {
    private static final String url = "jdbc:mysql://localhost:3306/lattice";

    private static final String username = "debian-sys-maint";
    private static final String password = "RMPj25ms6RmzZ5X7";

    public static Connection getConnect() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}
