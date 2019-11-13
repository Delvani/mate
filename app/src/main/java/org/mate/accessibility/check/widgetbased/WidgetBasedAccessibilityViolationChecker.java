package org.mate.accessibility.check.widgetbased;

import org.mate.MATE;
import org.mate.state.IScreenState;
import org.mate.ui.Widget;

import java.util.ArrayList;
import java.util.List;

public class WidgetBasedAccessibilityViolationChecker {

    private static List<IWidgetAccessibilityCheck> widgetBasedChecks = null;

    public static List<IWidgetAccessibilityCheck> createWidgetBasedAccessibilityList(){
        if (widgetBasedChecks==null){
            widgetBasedChecks = new ArrayList<IWidgetAccessibilityCheck>();
            widgetBasedChecks.add(new FormControlLabelCheck());
            widgetBasedChecks.add(new ContrastRatioAccessibilityCheck());
        }

        return widgetBasedChecks;
    }


    public static void runAccessibilityChecks(IScreenState state) {

        widgetBasedChecks = createWidgetBasedAccessibilityList();
        MATE.log("RUN WIDGET BASED CHECKS");
        for (Widget widget: state.getWidgets()){

            for (IWidgetAccessibilityCheck widgetCheck: widgetBasedChecks){
                MATE.log("Check: " + widget.getId() + " - " + widget.getText());
                boolean violationFound = widgetCheck.check(state,widget);
                if (violationFound)
                    MATE.log("VIOLATION FOUND: "+widgetCheck.getType() + " - " + widget.getId() + " - " + widget.getText() + " - " + widgetCheck.getInfo());
            }

        }

    }
}
