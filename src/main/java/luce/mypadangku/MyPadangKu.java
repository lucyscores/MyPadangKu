/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package luce.mypadangku;

/**
 *
 * @author MSI_
 */
import luce.mypadangku.controller.RecipeController;
import luce.mypadangku.dao.MenuDao;
import luce.mypadangku.dao.RecipeDaoMySql;
import luce.mypadangku.view.RecipeView;

import javax.swing.SwingUtilities;

public class MyPadangKu {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RecipeView view = new RecipeView();
            new RecipeController(view, new RecipeDaoMySql(), new MenuDao());
            view.setVisible(true);
        });
    }
}
