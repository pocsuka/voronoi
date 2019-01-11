package com.geom.voronoi.gui;

import java.io.PrintWriter;
import java.io.StringWriter;

import java.util.concurrent.ThreadLocalRandom;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;
import javafx.stage.*;

/**
 * Provides global resources and helpers for the application.
 * @author Christoph Nahr
 * @version 6.1.0
 */
public final class Global {

    private static double _fontSize;
    private static Stage _primaryStage;

    /**
     * The random number generator for the application.
     * Shared instance for use by various test dialogs.
     */
    public final static ThreadLocalRandom RANDOM = ThreadLocalRandom.current();

    /**
     * Creates a {@link Global} instance.
     * Private to prevent instantiation.
     */
    private Global() { }

    /**
     * Gets the primary {@link Stage} of the application.
     * @return the primary {@link Stage} of the application
     */
    public static Stage primaryStage() {
        return _primaryStage;
    }

    /**
     * Sets the primary {@link Stage} of the application.
     * @param primaryStage the primary {@link Stage} of the application
     * @throws IllegalStateException if the method has already been called
     * @throws NullPointerException if {@code primaryStage} is {@code null}
     */
    static void setPrimaryStage(Stage primaryStage) {
        if (_primaryStage != null)
            throw new IllegalStateException("primaryStage != null");
        if (primaryStage == null)
            throw new NullPointerException("primaryStage");

        _primaryStage = primaryStage;
    }

    /**
     * Clips the children of the specified {@link Region} to its current size.
     * This requires attaching a change listener to the region’s layout bounds,
     * as JavaFX does not currently provide any built-in way to clip children.
     *
     * @param region the {@link Region} whose children to clip
     * @throws NullPointerException if {@code region} is {@code null}
     */
    public static void clipChildren(Region region) {

        final Rectangle outputClip = new Rectangle();
        region.setClip(outputClip);

        region.layoutBoundsProperty().addListener((ov, oldValue, newValue) -> {
            outputClip.setWidth(newValue.getWidth());
            outputClip.setHeight(newValue.getHeight());
        });
    }

    /**
     * Gets a bold {@link Font} using the default family and specified size.
     * @param size the size of the {@link Font}, in points
     * @return a bold {@link Font} using the default family and specified {@code size}
     */
    public static Font boldFont(double size) {
        return Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, size);
    }


    /**
     * Gets the default {@link Font} size for the application.
     * @return the default {@link Font} size for the application
     */
    public static double fontSize() {
        if (_fontSize == 0)
            _fontSize = Font.getDefault().getSize();

        return _fontSize;
    }

    /**
     * Shows a modal {@link Alert} with the specified error message.
     * @param owner the {@link Window} that owns the {@link Alert}
     * @param header the header text of the {@link Alert}
     * @param content the content text of the {@link Alert}
     */
    public static void showError(Window owner, String header, String content) {

        final Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(owner == null ? primaryStage() : owner);
        alert.setTitle("Operation Failed");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Shows a modal {@link Alert} with the specified {@link Throwable}.
     * @param owner the {@link Window} that owns the {@link Alert}
     * @param header the header text of the {@link Alert}
     * @param e the {@link Throwable} providing content for the {@link Alert}
     */
    public static void showError(Window owner, String header, Throwable e) {

        String content = "Unspecified error.";
        String stackTrace = "No stack trace available.";
        if (e != null) {
            content = e.toString();
            final StringWriter writer = new StringWriter();
            try (PrintWriter print = new PrintWriter(writer)) {
                e.printStackTrace(print);
                stackTrace = writer.toString();
            }
        }

        final TextArea textArea = new TextArea(stackTrace);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);

        final Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(owner == null ? primaryStage() : owner);
        alert.setTitle("Operation Failed");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.getDialogPane().setExpandableContent(new StackPane(textArea));
        alert.showAndWait();
    }
}
