package at.ac.tuwien.sepm.ws16.qse01.gui.specialCells;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

import java.util.*;

/**
 * This class is a TextField which implements an "autocomplete" functionality, based on a supplied list of entries.
 * @author Caleb Brinkman
 */
public class AutoCompleteTextField extends TextField
{
    /** The existing autocomplete entries. */
    private final SortedSet<String> entries;
    private final Map<String,ImageView> imgViews;
    private TextField txLogoPath;
    /** The popup used to select an entry. */
    private ContextMenu entriesPopup;

    /** Construct a new AutoCompleteTextField. */
    public AutoCompleteTextField() {
        super();
        entries = new TreeSet<>();
        imgViews = new HashMap<>();
        entriesPopup = new ContextMenu();

        textProperty().addListener((observableValue, s, s2) -> {

            if (getText().length() == 0)
            {
                entriesPopup.hide();
            } else
            {
                LinkedList<String> searchResult = new LinkedList<>();
                System.out.println(getText().toLowerCase()+"_"+getText().toLowerCase()+Character.MAX_VALUE);
                searchResult.addAll(entries.subSet(getText().toLowerCase(), getText().toLowerCase() + Character.MAX_VALUE));
                if (entries.size() > 0)
                {
                  //  System.out.println(searchResult.size());
                    populatePopup(searchResult);
                    if (!entriesPopup.isShowing())
                    {
                        entriesPopup.show(AutoCompleteTextField.this, Side.BOTTOM, 0, 0);
                    }
                } else
                {
                    entriesPopup.hide();
                }
            }
        });

        focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean aBoolean2) {
                entriesPopup.hide();
            }
        });

    }

    /**
     * Get the existing set of autocomplete entries.
     * @return The existing autocomplete entries.
     */
    public SortedSet<String> getEntries() { return entries; }

    /**
     * Get the existing set of autocomplete entries.
     * @return The existing autocomplete entries.
     */
    public Map<String,ImageView> getImgViews() { return imgViews; }

    /**
     * Populate the entry set with the given search results.  Display is limited to 10 entries, for performance.
     * @param searchResult The set of matching strings.
     */
    private void populatePopup(List<String> searchResult) {
        List<CustomMenuItem> menuItems = new LinkedList<>();
        // If you'd like more entries, modify this line.
        int maxEntries = 10;
        int count = Math.min(searchResult.size(), maxEntries);
        for (int i = 0; i < count; i++)
        {
            final String result = searchResult.get(i);
            Label entryLabel = new Label(result);
            final ImageView logo = imgViews.get(result);
            GridPane box = new GridPane();
            box.add(entryLabel,0,0);


            ColumnConstraints con = new ColumnConstraints();
            con.setPrefWidth(200);
            box.getColumnConstraints().add(con);

            box.add(logo,1,0);
            ColumnConstraints con2 = new ColumnConstraints();
            con2.setPrefWidth(40);
            box.getColumnConstraints().add(con2);

            CustomMenuItem item = new CustomMenuItem(box, true);
            item.setOnAction(new EventHandler<ActionEvent>()
            {
                @Override
                public void handle(ActionEvent actionEvent) {
                    setText(result.substring(0,result.lastIndexOf(" #")));
                    txLogoPath.setText(logo.getId());
                    entriesPopup.hide();
                }
            });
            menuItems.add(item);
        }
        entriesPopup.getItems().clear();
        entriesPopup.getItems().addAll(menuItems);

    }
    public void setTxLogoPath(TextField tx){
        txLogoPath = tx;
    }
}