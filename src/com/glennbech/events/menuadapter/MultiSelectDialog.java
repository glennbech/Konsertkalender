package com.glennbech.events.menuadapter;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import com.glennbech.events.ConfigActivity;
import com.glennbech.events.R;
import com.glennbech.events.menu.MultiSelectOptionItem;

/**
 * @author Glenn Bech
 */
public class MultiSelectDialog extends Dialog {

    private MultiSelectOptionItem item;
    private String title;

    public MultiSelectDialog(Context context, String title, MultiSelectOptionItem item) {
        super(context);
        this.title = title;
        this.item = item;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.

        setTitle(title);
        setContentView(R.layout.dialogmultiselect);

        Button okButton = (Button) findViewById(R.id.dialogTextInputOkbutton);
        Button cancelButton = (Button) findViewById(R.id.dialogTextInputCancelButton);

        Spinner spinner = (Spinner) findViewById(R.id.optionSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, item.getChoices());
        spinner.setAdapter(adapter);
        spinner.setSelection(adapter.getPosition(item.getValue()));

        okButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Spinner s = (Spinner) findViewById(R.id.optionSpinner);
                String value = s.getSelectedItem().toString();
                putPreferenceValue(item.getKey(), value);
                dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void putPreferenceValue(String key, String value) {
        SharedPreferences.Editor editor = getContext().getSharedPreferences(ConfigActivity.PREFERENCE_FILE_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.commit();
    }
}
