/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package luce.mypadangku.database;

package luce.mypadangku.database;

import java.sql.Connection;
import java.sql.DriverManager;

import java.sql.SQLException;

public final class DatabaseConnection {

    private static final String URL
            = "jdbc:mysql://localhost:3306/mypadangku?useSSL=false&serverTimezone=Asia/Jakarta";
    private static final String USER = "root";
    private static final String PASSWORD = "underscore"; // Sesuaikan dengan password MySQL kamu.

    private DatabaseConnection() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
