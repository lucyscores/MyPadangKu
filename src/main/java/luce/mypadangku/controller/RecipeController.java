/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package luce.mypadangku.controller;

/**
 *
 * @author MSI_
 */
import luce.mypadangku.dao.MenuDao;
import luce.mypadangku.dao.MenuOption;
import luce.mypadangku.dao.RecipeDao;
import luce.mypadangku.model.Recipe;
import luce.mypadangku.model.RecipeIngredients;
import luce.mypadangku.view.RecipeView;

import java.sql.SQLException;
import java.util.List;

public class RecipeController {

    private final RecipeView view;
    private final RecipeDao recipeDao;
    private final MenuDao menuDao;

    public RecipeController(RecipeView view, RecipeDao recipeDao, MenuDao menuDao) {
        this.view = view;
        this.recipeDao = recipeDao;
        this.menuDao = menuDao;

        view.getAddButton().addActionListener(e -> addRecipe());
        view.getUpdateButton().addActionListener(e -> updateRecipe());
        view.getDeleteButton().addActionListener(e -> deleteRecipe());
        view.getClearButton().addActionListener(e -> view.clearForm());
        view.getRecipeTable().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Recipe selected = view.getSelectedRecipe();
                if (selected != null) {
                    view.fillForm(selected);
                }
            }
        });

        loadInitialData();
    }

    private void loadInitialData() {
        try {
            for (MenuOption menu : menuDao.findAll()) {
                view.getMenuBox().addItem(menu);
            }
            refreshTable();
        } catch (SQLException ex) {
            view.showError("Gagal memuat data: " + ex.getMessage());
        }
    }

    private Recipe createRecipeFromForm() {
        MenuOption selectedMenu = (MenuOption) view.getMenuBox().getSelectedItem();
        if (selectedMenu == null) {
            throw new IllegalArgumentException("Belum ada data menu. Isi tabel menu terlebih dahulu.");
        }
        if (view.getRecipeName().isBlank()) {
            throw new IllegalArgumentException("Nama resep wajib diisi.");
        }

        List<RecipeIngredients> ingredients = view.readIngredients();
        Recipe recipe = new Recipe();
        recipe.setMenuId(selectedMenu.getId());
        recipe.setMenuName(selectedMenu.toString());
        recipe.setRecipeName(view.getRecipeName());
        recipe.setDescription(view.getDescription());
        recipe.setIngredients(ingredients);
        return recipe;
    }

    private void addRecipe() {
        try {
            recipeDao.save(createRecipeFromForm());
            refreshTable();
            view.clearForm();
            view.showInfo("Resep berhasil ditambahkan.");
        } catch (SQLException | IllegalArgumentException ex) {
            view.showError(ex.getMessage());
        }
    }

    private void updateRecipe() {
        Recipe selected = view.getSelectedRecipe();
        if (selected == null) {
            view.showError("Pilih resep pada tabel terlebih dahulu.");
            return;
        }
        try {
            Recipe updated = createRecipeFromForm();
            updated.setId(selected.getId());
            recipeDao.update(updated);
            refreshTable();
            view.clearForm();
            view.showInfo("Resep berhasil diperbarui.");
        } catch (SQLException | IllegalArgumentException ex) {
            view.showError(ex.getMessage());
        }
    }

    private void deleteRecipe() {
        Recipe selected = view.getSelectedRecipe();
        if (selected == null) {
            view.showError("Pilih resep pada tabel terlebih dahulu.");
            return;
        }
        if (!view.confirmDelete()) {
            return;
        }
        try {
            recipeDao.delete(selected.getId());
            refreshTable();
            view.clearForm();
            view.showInfo("Resep berhasil dihapus.");
        } catch (SQLException ex) {
            view.showError("Gagal menghapus resep: " + ex.getMessage());
        }
    }

    private void refreshTable() throws SQLException {
        view.showRecipes(recipeDao.findAll());
    }
}
