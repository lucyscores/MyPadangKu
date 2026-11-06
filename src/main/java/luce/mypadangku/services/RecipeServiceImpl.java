package luce.mypadangku.services;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import luce.mypadangku.model.Recipe;
import luce.mypadangku.dao.RecipeDao;

import java.util.List;
import java.sql.SQLException;

public class RecipeServiceImpl implements RecipeServices {

    private final RecipeDao recipeDao;

    public RecipeServiceImpl(RecipeDao recipeDao) {
        this.recipeDao = recipeDao;
    }

    @Override
    public List<Recipe> getAllRecipes() {
        try {
            return recipeDao.findAll();
        } catch (SQLException e) {
            throw new RuntimeException("Gagal mengambil data resep", e);
        }
    }

    @Override
    public void addRecipe(Recipe recipe) {

        if (recipe.getRecipeName() == null
                || recipe.getRecipeName().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Nama resep tidak boleh kosong"
            );
        }

        try {
            recipeDao.save(recipe);
        } catch (SQLException e) {
            throw new RuntimeException("Gagal menyimpan resep", e);
        }
    }

    @Override
    public void updateRecipe(Recipe recipe) {
        try {
            recipeDao.update(recipe);
        } catch (SQLException e) {
            throw new RuntimeException("Gagal mengubah resep", e);
        }
    }

    @Override
    public void deleteRecipe(int id) {
        try {
            recipeDao.delete(id);
        } catch (SQLException e) {
            throw new RuntimeException("Gagal menghapus resep", e);
        }
    }
}
