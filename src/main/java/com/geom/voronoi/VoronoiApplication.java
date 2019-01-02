package com.geom.voronoi;

import javafx.application.Application;
import javafx.event.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;


public class VoronoiApplication extends Application {

    @Override
    public void start(Stage primaryStage) {


        final Label caption = new Label("2 player Voronoi game");
        caption.setFont(Global.boldFont(16));
        caption.setPadding(new Insets(8));
        final Label message = new Label("Select a mode to play the game");
        message.setPadding(new Insets(8));

        final VBox root = new VBox(createMenuBar(), caption, message);
        final Scene scene = new Scene(root, 400, 300);

        // background thread executor must be shut down manually


        primaryStage.setTitle("Voronoi Demo");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    private static MenuBar createMenuBar() {
        final MenuBar menu = new MenuBar();

        final Menu fileMenu = createMenu("_File",
            new SeparatorMenuItem(),
            createMenuItem("E_xit", t -> Global.primaryStage().close(), null)
        );

        final Menu geoMenu = createMenu("_Modes",
            createMenuItem("_Human vs Human", t -> new VoronoiDialog().showAndWait(),
                null)
        );

        menu.getMenus().addAll(fileMenu, geoMenu);
        return menu;
    }

    private static Menu createMenu(String title, MenuItem... items) {
        final Menu menu = new Menu(title);
        menu.setMnemonicParsing(true);
        menu.getItems().addAll(items);
        return menu;
    }

    private static MenuItem createMenuItem(String text,
                                           EventHandler<ActionEvent> onAction, KeyCombination key) {

        final MenuItem item = new MenuItem(text);
        item.setMnemonicParsing(true);
        item.setOnAction(onAction);
        if (key != null) item.setAccelerator(key);
        return item;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
