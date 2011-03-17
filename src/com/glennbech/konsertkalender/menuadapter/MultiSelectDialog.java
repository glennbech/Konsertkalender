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

        config = new Configuration(getContext());
        setTitle(title);
        setContentView(R.layout.dialogmultiselect);

        Button okButton = (Button) findViewById(R.id.dialogTextInputOkbutton);
        Button cancelButton = (Button) findViewById(R.id.dialogTextInputCancelButton);

        okButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Spinner s = (Spinner) findViewById(R.id.optionSpinner);
                String value = s.getSelectedItem().toString();
                config.setProperty(item.getKey(), value);
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
        ArrayAdapter<String> adapter = new ArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, item.getChoices());
        spinner.setAdapter(adapter);
        if (item.getValue() != null) {
            spinner.setSelection(adapter.getPosition(item.getValue()));
        } else {
            spinner.setSelection(0);
        }
    }

    public void setItem(MultiSelectOptionItem item) {
        this.item = item;
    }
}
