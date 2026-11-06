/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package luce.mypadangku.view;

import luce.mypadangku.dao.MenuOption;
import luce.mypadangku.model.Recipe;
import luce.mypadangku.model.RecipeIngredients;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author MSI_
 */
public class RecipeView extends JFrame {

    private final JComboBox<MenuOption> menuBox = new JComboBox<>();
    private final JTextField recipeNameField = new JTextField(22);
    private final JTextArea descriptionArea = new JTextArea(3, 22);
    private final JTextArea ingredientsArea = new JTextArea(5, 22);
    private final JTable recipeTable;
    private final DefaultTableModel tableModel;
    private final JButton addButton = new JButton("Tambah");
    private final JButton updateButton = new JButton("Ubah");
    private final JButton deleteButton = new JButton("Hapus");
    private final JButton clearButton = new JButton("Bersihkan");
    private final List<Recipe> displayedRecipes = new ArrayList<>();

    public RecipeView() {
        setTitle("MyPadangku - Kelola Resep");
        setSize(950, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Menu", "Nama Resep", "Deskripsi", "Jumlah Bahan"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        recipeTable = new JTable(tableModel);
        recipeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Form Resep"));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 7, 5, 7);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;

        addRow(form, c, 0, "Menu", menuBox);
        addRow(form, c, 1, "Nama Resep", recipeNameField);
        JScrollPane descriptionScroll = new JScrollPane(descriptionArea);
        addRow(form, c, 2, "Deskripsi", descriptionScroll);
        ingredientsArea.setText("Daging sapi,250,gram\nSantan,200,ml");
        addRow(form, c, 3, "Bahan (satu per baris: nama,jumlah,satuan)", new JScrollPane(ingredientsArea));

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttons.add(addButton);
        buttons.add(updateButton);
        buttons.add(deleteButton);
        buttons.add(clearButton);

        JPanel top = new JPanel(new BorderLayout());
        top.add(form, BorderLayout.CENTER);
        top.add(buttons, BorderLayout.SOUTH);

        setLayout(new BorderLayout(8, 8));
        add(top, BorderLayout.NORTH);
        add(new JScrollPane(recipeTable), BorderLayout.CENTER);
    }

    private void addRow(JPanel panel, GridBagConstraints c, int row, String label, Component input) {
        c.gridx = 0;
        c.gridy = row;
        c.weightx = 0;
        panel.add(new JLabel(label), c);
        c.gridx = 1;
        c.weightx = 1;
        panel.add(input, c);
    }

    public JComboBox<MenuOption> getMenuBox() {
        return menuBox;
    }

    public String getRecipeName() {
        return recipeNameField.getText().trim();
    }

    public String getDescription() {
        return descriptionArea.getText().trim();
    }

    public JTable getRecipeTable() {
        return recipeTable;
    }

    public JButton getAddButton() {
        return addButton;
    }

    public JButton getUpdateButton() {
        return updateButton;
    }

    public JButton getDeleteButton() {
        return deleteButton;
    }

    public JButton getClearButton() {
        return clearButton;
    }

    public List<RecipeIngredients> readIngredients() {
        List<RecipeIngredients> list = new ArrayList<>();
        String[] lines = ingredientsArea.getText().split("\\R");
        for (String line : lines) {
            if (line.isBlank()) {
                continue;
            }
            String[] parts = line.split(",");
            if (parts.length != 3) {
                throw new IllegalArgumentException(
                        "Format bahan harus: nama,jumlah,satuan");
            }
            BigDecimal quantity = new BigDecimal(parts[1].trim());
            if (quantity.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Jumlah bahan harus lebih dari 0.");
            }
            list.add(new RecipeIngredients(parts[0].trim(), quantity, parts[2].trim()));
        }
        if (list.isEmpty()) {
            throw new IllegalArgumentException("Minimal isi satu bahan.");
        }
        return list;
    }

    public void showRecipes(List<Recipe> recipes) {
        displayedRecipes.clear();
        displayedRecipes.addAll(recipes);
        tableModel.setRowCount(0);
        for (Recipe r : recipes) {
            tableModel.addRow(new Object[]{r.getId(), r.getMenuName(), r.getRecipeName(),
                r.getDescription(), r.getIngredients().size()});
        }
    }

    public Recipe getSelectedRecipe() {
        int row = recipeTable.getSelectedRow();
        if (row < 0) {
            return null;
        }
        return displayedRecipes.get(recipeTable.convertRowIndexToModel(row));
    }

    public void fillForm(Recipe recipe) {
        recipeNameField.setText(recipe.getRecipeName());
        descriptionArea.setText(recipe.getDescription());
        for (int i = 0; i < menuBox.getItemCount(); i++) {
            if (menuBox.getItemAt(i).getId() == recipe.getMenuId()) {
                menuBox.setSelectedIndex(i);
                break;
            }
        }
        StringBuilder text = new StringBuilder();
        for (RecipeIngredients ingredient : recipe.getIngredients()) {
            text.append(ingredient.getName()).append(",")
                    .append(ingredient.getQuantity()).append(",")
                    .append(ingredient.getUnit()).append("\n");
        }
        ingredientsArea.setText(text.toString());
    }

    public void clearForm() {
        recipeNameField.setText("");
        descriptionArea.setText("");
        ingredientsArea.setText("");
        if (menuBox.getItemCount() > 0) {
            menuBox.setSelectedIndex(0);
        }
        recipeTable.clearSelection();
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Kesalahan", JOptionPane.ERROR_MESSAGE);
    }

    public void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Informasi", JOptionPane.INFORMATION_MESSAGE);
    }

    public boolean confirmDelete() {
        return JOptionPane.showConfirmDialog(this, "Hapus resep terpilih?",
                "Konfirmasi", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }
}
