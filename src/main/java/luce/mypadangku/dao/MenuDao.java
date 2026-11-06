/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package luce.mypadangku.dao;

/**
 *
 * @author MSI_
 */
import luce.mypadangku.database.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuDao {

    public List<MenuOption> findAll() throws SQLException {
        List<MenuOption> menus = new ArrayList<>();
        String sql = "SELECT id_menu, nama_menu FROM menu ORDER BY nama_menu";
        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                menus.add(new MenuOption(result.getInt("id_menu"),
                        result.getString("nama_menu")));
            }
        }
        return menus;
    }
}
