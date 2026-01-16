package org.example.logicgatesimulator;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.example.logicgatesimulator.components.ComponentBase;

import javax.swing.*;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

import static java.io.File.separator;

public class Ribbon extends HBox {
    private Hashtable<String, ArrayList<Node>> groups;
    private Hashtable<String, HBox> groupComponents;

    private Button dragableButtonClone = null;
    private double xOffset;
    private double yOffset;

    Pane root;

    public Ribbon(Pane root){
        this.root = root;
        init();
    }

    private void init(){
        groups = new Hashtable<String, ArrayList<Node>>();
        groupComponents = new Hashtable<String, HBox>();
    }

    public void addGroup(String name){
        if(groups.get(name) != null){
            throw new RuntimeException("Group " + name + " already exists.");
        }

        groups.put(name, new ArrayList<>());

        //Icons
        HBox iconBox = new HBox(5);
        iconBox.setAlignment(Pos.CENTER_LEFT);
        iconBox.setBorder(new Border (new BorderStroke (
                Color.LIGHTGREY,
                BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY,
                new BorderWidths(2,0, 0,0)
        )));
        iconBox.setMinHeight(90);


        //Label
        javafx.scene.control.Label label = new javafx.scene.control.Label(name);
        label.setFont(Font.font("Aptos", FontWeight.BOLD, 16));
        label.setTextFill(Color.DIMGRAY);

        //Container for the icons and labels
        javafx.scene.layout.VBox groupBox = new javafx.scene.layout.VBox(5);
        groupBox.setAlignment(Pos.CENTER_LEFT);
        groupBox.setPadding(new Insets(10));
        groupBox.getChildren().addAll(label, iconBox);

        groupComponents.put(name, iconBox);

        this.getChildren().add(groupBox);

        HBox.setHgrow(groupBox, Priority.ALWAYS);
        groupBox.setMaxWidth(Double.MAX_VALUE);
    }

//    public void addComponent(String groupName, Node component ){
//        ArrayList<Node> group = groups.get(groupName);
//        if(group == null){
//            throw new RuntimeException("Group " + groupName + " does not exist.");
//        }
//        Node newComponent = component;
//
//        group.add(newComponent);
//        HBox groupHBox = groupComponents.get(groupName);
//        HBox.setHgrow(newComponent, Priority.ALWAYS);
//        groupHBox.getChildren().add(newComponent);
//    }


    public void addItem(String groupName, String name, String tooltipText, String imageName, ComponentBase referencedComponent) {
        ArrayList<Node> group = groups.get(groupName);
        if(group == null){
            throw new RuntimeException("Group " + groupName + " does not exist.");
        }

        ComponentRegistry.ComponentMetadata metadata = ComponentRegistry.getMetadata(referencedComponent.getClass().getSimpleName());

        Button btn = new Button();
        btn.setTooltip(new Tooltip(tooltipText));
        btn.setPrefSize(40, 40);
        btn.setStyle("-fx-background-color: transparent; -fx-padding: 15;");
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #F0F0F0; -fx-background-radius: 5; -fx-padding: 15;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: transparent; -fx-padding: 15;"));
        Image image = null;
        try {
            if (metadata != null) {
                image = ImageLoader.loadImageAsImage(metadata.imagePath);
                if (image != null) {
                    ImageView iv = new ImageView(image);
                    iv.setFitWidth(metadata.iconSize); // ← Nutzt die gleiche Größe wie Workspace
                    iv.setPreserveRatio(true);
                    btn.setGraphic(iv);
                }
            } else {
                btn.setText(name);
            }
        } catch (Exception e) { btn.setText("?"); }

        Image finalImage = image;

        btn.setOnDragDetected(event -> {
            Dragboard db = btn.startDragAndDrop(TransferMode.ANY);
            ClipboardContent content = new ClipboardContent();
            content.putString(referencedComponent.getClass().getName());
            db.setContent(content);
            if(finalImage != null){
                db.setDragView(finalImage);
            }
            event.consume();
        });
        group.add(btn);
        HBox groupHBox = groupComponents.get(groupName);
        HBox.setHgrow(btn, Priority.ALWAYS);
        groupHBox.getChildren().add(btn);
    }

    public HBox getGroupIconBox (String groupName) {
        HBox box = groupComponents.get(groupName);
        if( box == null) {
            throw new RuntimeException("Group" + groupName + "does not exist.");
        }
        return box;
    }




}
