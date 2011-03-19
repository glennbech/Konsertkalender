package com.glennbech.konsertkalender.menuadapter;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import com.glennbech.konsertkalender.Configuration;
import com.glennbech.konsertkalender.R;
import com.glennbech.konsertkalender.menu.MultiSelectOptionItem;

/**
 * @author Glenn Bech
 */
public class MultiSelectDialog extends Dialog {

    private String title;
    private MultiSelectOptionItem item;
    private Configuration config;


    public MultiSelectDialog(Context context) {
        super(context);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
        setContentView(R.layout.dialogmultiselect);

        config = new Configuration(getContext());
        setTitle(title);

        Button okButton = (Button) findViewById(R.id.dialogTextInputOkbutton);
        Button cancelButton = (Button) findViewById(R.id.dialogTextInputCancelButton);

        okButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Spinner s = (Spinner) findViewById(R.id.optionSpinner);
                MultiSelectOptionItem.Item selectedItem = (MultiSelectOptionItem.Item) s.getSelectedItem();
                config.setProperty(item.getKey(), selectedItem.getValue().toString());
                dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        Spinner spinner = (Spinner) findViewById(R.id.optionSpinner);
        ArrayAdapter<MultiSelectOptionItem.Item> adapter = new ArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, item.getChoices());
        spinner.setAdapter(adapter);
        if (item.getValue() != null) {

            spinner.setSelection(adapter.getPosition(new MultiSelectOptionItem.Item(config.getUpdateHour(), "")));
        } else {
            spinner.setSelection(0);
        }
    }

    public void setItem(MultiSelectOptionItem item) {
        this.item = item;
    }
}
