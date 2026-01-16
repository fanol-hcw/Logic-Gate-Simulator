package org.example.logicgatesimulator;

import java.util.HashMap;
import java.util.Map;

public class ComponentRegistry {
    private static final Map<String, ComponentMetadata> COMPONENTS = new HashMap<>();

    static {
        // Inputs
        COMPONENTS.put("ButtonComponent", new ComponentMetadata(
                "Button",
                "Knopf",
                "Button.png",
                "ButtonOn.png",
                25
        ));

        COMPONENTS.put("SwitchComponent", new ComponentMetadata(
                "",
                "Schalter",
                "SwitchOff.png",
                "SwitchOff.png",
                40
        ));

        COMPONENTS.put("ClockComponent", new ComponentMetadata(
                "Clock",
                "Taktsignal",
                "ClockOff.png",
                "ClockOn.png",
                40
        ));

        COMPONENTS.put("OneConstantComponent", new ComponentMetadata(
                "One",
                "Konstante 1",
                "ONE.png",
                "ONE.png" ,
                30
        ));

        COMPONENTS.put("ZeroConstantComponent", new ComponentMetadata(
                "Zero",
                "Konstante 0",
                "ZERO.png",
                "ZERO.png",
                30
        ));

        // Outputs
        COMPONENTS.put("LedComponent", new ComponentMetadata(
                "Led",
                "LED-Leuchte",
                "LedOff.png",
                "LedOn.png",
                60
        ));

        // Gates
        COMPONENTS.put("AndGateComponent", new ComponentMetadata(
                "And Gate",
                "UND-Gatter",
                "And.png",
                "And.png",
                80
        ));

        COMPONENTS.put("OrGateComponent", new ComponentMetadata(
                "Or Gate",
                "ODER-Gatter",
                "Or.png",
                "Or.png",
                80
        ));

        COMPONENTS.put("NotGateComponent", new ComponentMetadata(
                "Not Gate",
                "NICHT-Gatter",
                "Not.png",
                "Not.png",
                80
        ));

        COMPONENTS.put("XorGateComponent", new ComponentMetadata(
                "Xor Gate",
                "XOR-Gatter",
                "xor.png",
                "xor.png",
                80
        ));

        COMPONENTS.put("ResetButton", new ComponentMetadata(
                "Reset",
                "Alles zurücksetzen",
                "Reset.png",
                "Reset.png"
        ));
    }

    public static ComponentMetadata getMetadata(String componentClassName) {
        return COMPONENTS.get(componentClassName);
    }

    public static class ComponentMetadata {
        public final String displayName;
        public final String tooltip;
        public final String imagePath;
        public final String imagePathActive;
        public final double iconSize; // Neue Eigenschaft


        public ComponentMetadata(String displayName, String tooltip, String imagePath, String imagePathActive) {
            this(displayName, tooltip, imagePath, imagePathActive, 50); // Workspace: 50px, Toolbar: 24px
        }

        public ComponentMetadata(String displayName, String tooltip, String imagePath, String imagePathActive, double iconSize) {
            this.displayName = displayName;
            this.tooltip = tooltip;
            this.imagePath = imagePath;
            this.imagePathActive = imagePathActive;
            this.iconSize = iconSize;
        }

        public String getFullImagePath() {
            return "/images/" + imagePath;
        }

        public String getFullImagePathActive() {
            return "/images/" + imagePathActive;
        }
    }
}
