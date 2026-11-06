/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package luce.mypadangku.services;

import luce.mypadangku.model.Recipe;
import java.util.List;

public interface RecipeServices {

    List<Recipe> getAllRecipes();

    void addRecipe(Recipe recipe);

    void updateRecipe(Recipe recipe);

    void deleteRecipe(int id);

}
