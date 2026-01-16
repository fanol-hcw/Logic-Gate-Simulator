package org.example.logicgatesimulator;

import org.example.logicgatesimulator.components.*;

import java.util.Arrays;
import java.util.List;

public class ComponentConfig {

    public static class ComponentItem {
        public String componentClassName;
        public ComponentBase componentInstance;

        public ComponentItem(String componentClassName, ComponentBase componentInstance) {
            this.componentClassName = componentClassName;
            this.componentInstance = componentInstance;
        }

        public String getDisplayName() {
            return ComponentRegistry.getMetadata(componentClassName).displayName;
        }

        public String getTooltip() {
            return ComponentRegistry.getMetadata(componentClassName).tooltip;
        }

        public String getImageName() {
            return ComponentRegistry.getMetadata(componentClassName).imagePath;
        }
    }

    public static List<ComponentItem> getInputComponents(Workspace workspace) {
        return Arrays.asList(
                new ComponentItem("ButtonComponent", new ButtonComponent("Button", workspace)),
                new ComponentItem("SwitchComponent", new SwitchComponent("Switch", workspace)),
                new ComponentItem("ClockComponent", new ClockComponent("Clock", workspace)),
                new ComponentItem("OneConstantComponent", new OneConstantComponent("One", workspace)),
                new ComponentItem("ZeroConstantComponent", new ZeroConstantComponent("Zero", workspace))
        );
    }

    public static List<ComponentItem> getOutputComponents(Workspace workspace) {
        return Arrays.asList(
                new ComponentItem("LedComponent", new LedComponent("Led", workspace))
        );
    }

    public static List<ComponentItem> getGateComponents(Workspace workspace) {
        return Arrays.asList(
                new ComponentItem("AndGateComponent", new AndGateComponent("And Gate", workspace)),
                new ComponentItem("OrGateComponent", new OrGateComponent("Or Gate", workspace)),
                new ComponentItem("NotGateComponent", new NotGateComponent("Not Gate", workspace))
        );
    }
}
