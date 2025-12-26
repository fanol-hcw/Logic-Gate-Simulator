package org.example.logicgatesimulator;
import javafx.scene.shape.Line;

/*
 * das hier ist unsere Klasse für die Verbindungskabel.
 * Im Moment ist das nur eine leere Hülle (erbt von "Line"), macht also noch gar nichts.
 * Wir müssen uns hier überlegen, wie wir das am besten umsetzen.
 * Mein Vorschlag für die Optik wäre, dass wir im Konstruktor "Property Binding" nutzen.
 * Damit würde das Kabel quasi automatisch an den Ports der Gatter "kleben" bleiben, wenn man sie verschiebt.
 * Und technisch müssen wir dann noch schauen, wie das Kabel den Strom (true/false) speichert
 * und wie es dem Ziel-Gatter Bescheid gibt, dass ein Signal ankommt.
 */

public class LogicWire extends Line {

}