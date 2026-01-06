package org.example.logicgatesimulator;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import org.example.logicgatesimulator.components.ComponentBase;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

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
        HBox group = new HBox(0);
        HBox.setHgrow(group, Priority.ALWAYS);
        groupComponents.put(name, group);
        getChildren().add(group);
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

        Button btn = new Button();
        btn.setTooltip(new Tooltip(tooltipText));
        btn.setPrefSize(60, 60);
        btn.setStyle("-fx-background-color: transparent; -fx-padding: 5;");
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #F0F0F0; -fx-background-radius: 5; -fx-padding: 5;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: transparent; -fx-padding: 5;"));
        Image image = null;
        try {
            InputStream is = getClass().getResourceAsStream("/images/" + imageName);
            if (is != null) {
                image = new Image(is);
                ImageView iv = new ImageView(image);
                iv.setFitWidth(50); iv.setPreserveRatio(true);
                btn.setGraphic(iv);
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
        //return btn;
    }


}
