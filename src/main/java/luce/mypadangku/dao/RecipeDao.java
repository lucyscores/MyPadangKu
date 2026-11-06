/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package luce.mypadangku.dao;

/**
 *
 * @author MSI_
 */
import luce.mypadangku.model.Recipe;
import java.sql.SQLException;
import java.util.List;

public interface RecipeDao {

    List<Recipe> findAll() throws SQLException;

    void save(Recipe recipe) throws SQLException;

    void update(Recipe recipe) throws SQLException;

    void delete(int recipeId) throws SQLException;
}
