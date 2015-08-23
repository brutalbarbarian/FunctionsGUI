package com.brutalbarbarian.functions.app;

import com.brutalbarbarian.functions.interfaces.Function;
import com.brutalbarbarian.utils.Console;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;

import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.util.*;

public class MainController implements Initializable{
    @FXML public TextArea taConsole;
    @FXML Button btnAddFunction;
    @FXML TreeView tvFunctions;
    @FXML TextField tfSearch;
    @FXML TabPane tpFunctionTabs;

    private Console console;

    @FXML
    public void onAddFunction(ActionEvent event) throws IOException {
        TreeItem selectedItem = (TreeItem)tvFunctions.getSelectionModel().getSelectedItem();
        if (selectedItem != null && selectedItem.isLeaf()) {
            FunctionValue functionValue = (FunctionValue)selectedItem.getValue();
            if (functionValue != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("function_content.fxml"));
                Parent content = (Parent)loader.load();
                FunctionContentController controller = loader.<FunctionContentController>getController();
                try {
                    // Try to create a new instance of the function rather then using the cached one
                    // This way, each instance of the function is able to take advantage of local variables
                    controller.setFunction(functionValue.function.getClass().newInstance());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                controller.setConsole(console);

                Tab tab = new Tab(functionValue.function.getDisplayName());
                tab.setClosable(true);
                tab.setContent(content);

                tpFunctionTabs.getTabs().add(tab);
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialise tvFunctions
        treeItemCategories = new HashMap<>();
        treeItemFunctions = new HashMap<>();
        tvFunctions.setRoot(new TreeItem());
        tvFunctions.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tvFunctions.setOnMouseClicked((e) -> {
            if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2) {
                try {
                    onAddFunction(null);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        buildFunctionTree();
        displayFunctionTree("");


        // Initialise tfSearch
        tfSearch.textProperty().addListener((o, s, s2) -> {
            displayFunctionTree(s2);
        });

        // Initialise console
        console = new Console(taConsole);
        PrintStream ps = new PrintStream(console);
        System.setOut(ps);
        System.setErr(ps);
    }

    HashMap<String, TreeItem> treeItemCategories;
    HashMap<String, TreeItem> treeItemFunctions;

    public void buildFunctionTree() {
        for (Function f : FunctionCache.getCache().getFunctions()) {
            String categoryName = f.getCategory();
            TreeItem category = treeItemCategories.get(categoryName);
            if (category == null) {
                category = new TreeItem(categoryName);
                category.setExpanded(true);
                treeItemCategories.put(categoryName, category);
            }
            TreeItem item = new TreeItem(new FunctionValue(f));
            treeItemFunctions.put(f.getName(), item);
        }
    }

    public void displayFunctionTree(final String filter) {
        HashMap<String, List<TreeItem>> items = new HashMap<>();
        List<TreeItem> categories = new ArrayList<>(items.size());
        String sFilter = null;
        if (filter != null) {
            sFilter = filter.toLowerCase();
        }
        // Build up which treeItemFunctions and treeItemCategories we want
        for (TreeItem item : treeItemFunctions.values()) {
            // Check if this item should be visible or not...
            Function f = ((FunctionValue)item.getValue()).function;
            if (filter == null || filter.isEmpty() ||
                    f.getDisplayName().toLowerCase().contains(sFilter)) {
                String categoryName = f.getCategory();
                List<TreeItem> category = items.get(categoryName);
                if (category == null) {
                    category = new ArrayList<>();
                    items.put(categoryName, category);
                    categories.add(treeItemCategories.get(categoryName));
                }
                category.add(item);
            }
        }

        for (Map.Entry<String, List<TreeItem>> entry : items.entrySet()) {
            TreeItem category = treeItemCategories.get(entry.getKey());
            entry.getValue().sort((t1, t2) -> {return t1.toString().compareTo(t2.toString());});
            category.getChildren().setAll(entry.getValue());
        }

        categories.sort((t1, t2) -> {return t1.toString().compareTo(t2.toString());});
        tvFunctions.getRoot().getChildren().setAll(categories);

        // Select the first item available.
        Object previousItem = null;
        if (!categories.isEmpty()) {
            tvFunctions.getSelectionModel().selectFirst();
            while ((tvFunctions.getSelectionModel().getSelectedItem() != previousItem) &&
                    !( (TreeItem)tvFunctions.getSelectionModel().getSelectedItem()).isLeaf()) {
                previousItem = tvFunctions.getSelectionModel().getSelectedItem();
                tvFunctions.getSelectionModel().selectNext();
            }
        }
    }

    class FunctionValue {
        Function function;

        FunctionValue(Function function) {
            this.function = function;
        }

        @Override
        public String toString() {
            return function.getDisplayName();
        }
    }
}
