package org.openbooth.gui.specialCells;

import org.openbooth.gui.GUIImageHelper;
import org.openbooth.util.ImageHandler;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.control.*;
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
    private Button txLogoUpload;
    private ImageHandler imageHandler;
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
                searchResult.addAll(entries.subSet(getText().toLowerCase(), getText().toLowerCase() + Character.MAX_VALUE));
                if (entries.size() > 0)
                {
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
                    String[] parts = result.split(" #");
                    setText(parts[0]); //result.substring(0,result.lastIndexOf(" #")));
                    txLogoPath.setText(logo.getId());
                    txLogoPath.setId(parts[1]); //if logo is selected -> to avoid a new creation of logo
                    txLogoUpload.setBackground(GUIImageHelper.getButtonBackground(imageHandler,"/images/upload1.png",50,50));
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
    public void setTxLogoUpload(Button button){
        txLogoUpload = button;
    }
    public void setImageHandler(ImageHandler imageHandler){
        this.imageHandler = imageHandler;
    }
}