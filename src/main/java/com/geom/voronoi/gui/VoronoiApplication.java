package com.geom.voronoi.gui;

import javafx.application.Application;
import javafx.event.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Paths;


public class VoronoiApplication extends Application {

    private Scene scene;
    private String p1StrategyFileString;
    private String p2StrategyFileString;
    private Label message;

    @Override
    public void start(Stage primaryStage) {
        p1StrategyFileString = "C:\\git\\voronoi\\src\\main\\resources\\circle10.txt";
        p2StrategyFileString = "C:\\git\\voronoi\\src\\main\\resources\\p2circle10.txt";
//
//        p1StrategyFileString = "./resources/circle10.txt";
//        p2StrategyFileString = "./resources/p2circle10.txt";

        final Label caption = new Label("2 player Voronoi game");
        caption.setFont(Global.boldFont(16));
        caption.setPadding(new Insets(8));
        message = new Label();
        message.setPadding(new Insets(8));

        final VBox root = new VBox(createMenuBar(), caption, message);
        scene = new Scene(root, 400, 300);

        // background thread executor must be shut down manually

        updateWelcomeMessage();
        primaryStage.setTitle("Voronoi Demo");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    private MenuBar createMenuBar() {
        final MenuBar menu = new MenuBar();

        final Menu fileMenu = createMenu("_File",
            new SeparatorMenuItem(),
            createMenuItem("E_xit", t -> Global.primaryStage().close(), null),
            createMenuItem("_Open P1 Strategy", t -> setPlayer1StrategyFile(), null),
            createMenuItem("O_pen P2 Strategy", t -> setPlayer2StrategyFile(), null)
        );

        final Menu geoMenu = createMenu("_Modes",
            createMenuItem("_Input file mode", t -> new VoronoiDialog(p1StrategyFileString, p2StrategyFileString).showAndWait(),
                null)
        );

        menu.getMenus().addAll(fileMenu, geoMenu);
        return menu;
    }

    private void setPlayer1StrategyFile() {
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File(Paths.get("./resources").toString()));
        File dialogResult = fc.showOpenDialog(scene.getWindow());
        if (dialogResult != null) {
            p1StrategyFileString = dialogResult.toString();
        }
        updateWelcomeMessage();
    }

    private void updateWelcomeMessage() {
        message.setText("Select a mode to play the game.\nP1: " + p1StrategyFileString + "\nP2: " + p2StrategyFileString + "\n\nTo write a strategy, open the file 'p2writestrategy.txt'");
    }

    private void setPlayer2StrategyFile() {
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File(Paths.get("./resources").toString()));
        File dialogResult = fc.showOpenDialog(scene.getWindow());
        if (dialogResult != null) {
            p2StrategyFileString = dialogResult.toString();
        }
        updateWelcomeMessage();
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
