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
import luce.mypadangku.model.Recipe;
import luce.mypadangku.model.RecipeIngredients;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecipeDaoMySql implements RecipeDao {

    @Override
    public List<Recipe> findAll() throws SQLException {
        String sql = """
            SELECT r.id_resep, r.id_menu, m.nama_menu, r.nama_resep, r.deskripsi
            FROM resep r JOIN menu m ON r.id_menu = m.id_menu
            ORDER BY r.id_resep DESC
            """;
        List<Recipe> recipes = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                int recipeId = result.getInt("id_resep");
                recipes.add(new Recipe(
                        recipeId,
                        result.getInt("id_menu"),
                        result.getString("nama_menu"),
                        result.getString("nama_resep"),
                        result.getString("deskripsi"),
                        findIngredients(connection, recipeId)
                ));
            }
        }
        return recipes;
    }

    private List<RecipeIngredients> findIngredients(Connection connection, int recipeId)
            throws SQLException {
        String sql = "SELECT nama_bahan, jumlah, satuan FROM detail_resep WHERE id_resep = ?";
        List<RecipeIngredients> ingredients = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, recipeId);
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    ingredients.add(new RecipeIngredients(
                            result.getString("nama_bahan"),
                            result.getBigDecimal("jumlah"),
                            result.getString("satuan")
                    ));
                }
            }
        }
        return ingredients;
    }

    @Override
    public void save(Recipe recipe) throws SQLException {
        String recipeSql = "INSERT INTO resep (id_menu, nama_resep, deskripsi) VALUES (?, ?, ?)";
        String ingredientSql = """
            INSERT INTO detail_resep (id_resep, nama_bahan, jumlah, satuan)
            VALUES (?, ?, ?, ?)
            """;

        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);
            try {
                int recipeId;
                try (PreparedStatement statement = connection.prepareStatement(
                        recipeSql, Statement.RETURN_GENERATED_KEYS)) {
                    statement.setInt(1, recipe.getMenuId());
                    statement.setString(2, recipe.getRecipeName());
                    statement.setString(3, recipe.getDescription());
                    statement.executeUpdate();
                    try (ResultSet keys = statement.getGeneratedKeys()) {
                        if (!keys.next()) {
                            throw new SQLException("ID resep gagal dibuat.");
                        }
                        recipeId = keys.getInt(1);
                    }
                }
                insertIngredients(connection, ingredientSql, recipeId, recipe.getIngredients());
                connection.commit();
            } catch (SQLException ex) {
                connection.rollback();
                throw ex;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    @Override
    public void update(Recipe recipe) throws SQLException {
        String updateSql = "UPDATE resep SET id_menu = ?, nama_resep = ?, deskripsi = ? WHERE id_resep = ?";
        String deleteDetails = "DELETE FROM detail_resep WHERE id_resep = ?";
        String insertDetails = """
            INSERT INTO detail_resep (id_resep, nama_bahan, jumlah, satuan)
            VALUES (?, ?, ?, ?)
            """;

        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);
            try {
                try (PreparedStatement statement = connection.prepareStatement(updateSql)) {
                    statement.setInt(1, recipe.getMenuId());
                    statement.setString(2, recipe.getRecipeName());
                    statement.setString(3, recipe.getDescription());
                    statement.setInt(4, recipe.getId());
                    statement.executeUpdate();
                }
                try (PreparedStatement statement = connection.prepareStatement(deleteDetails)) {
                    statement.setInt(1, recipe.getId());
                    statement.executeUpdate();
                }
                insertIngredients(connection, insertDetails, recipe.getId(), recipe.getIngredients());
                connection.commit();
            } catch (SQLException ex) {
                connection.rollback();
                throw ex;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    private void insertIngredients(Connection connection, String sql, int recipeId,
            List<RecipeIngredients> ingredients) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (RecipeIngredients ingredient : ingredients) {
                statement.setInt(1, recipeId);
                statement.setString(2, ingredient.getName());
                statement.setBigDecimal(3, ingredient.getQuantity());
                statement.setString(4, ingredient.getUnit());
                statement.addBatch();
            }
            statement.executeBatch();
        }
    }

    @Override
    public void delete(int recipeId) throws SQLException {
        String sql = "DELETE FROM resep WHERE id_resep = ?";
        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, recipeId);
            statement.executeUpdate();
        }
    }
}
