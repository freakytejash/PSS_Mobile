package com.example.pssmobile.ui.login.reader;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pssmobile.R;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

public class ExpandableLayoutMaterialDesign extends AppCompatActivity {
    Button btn_back;
    ExpandableRelativeLayout expandableLayout1;
    ExpandableRelativeLayout expandableLayout2;
    ExpandableRelativeLayout expandableLayout3;
    ExpandableRelativeLayout expandableLayout4;
    ExpandableRelativeLayout expandableLayout5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandable_layout_material_design);
        this.btn_back = (Button) findViewById(R.id.btn_back);
        this.btn_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                com.example.pssmobile.ui.login.reader.ExpandableLayoutMaterialDesign.this.AskOption().show();
            }
        });
    }

    public void onBackPressed() {
        AskOption().show();
    }

    /* access modifiers changed from: private */
    public AlertDialog AskOption() {
        return new AlertDialog.Builder(this).setTitle((CharSequence) "PSS").setMessage((CharSequence) "Are you sure you want go back to menu?").setPositiveButton((CharSequence) "Yes", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                com.example.pssmobile.ui.login.reader.ExpandableLayoutMaterialDesign.this.startActivity(new Intent(com.example.pssmobile.ui.login.reader.ExpandableLayoutMaterialDesign.this, ReadNFC.class));
                com.example.pssmobile.ui.login.reader.ExpandableLayoutMaterialDesign.this.finish();
            }
        }).setNegativeButton((CharSequence) "No", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create();
    }

    public void expandableButton1(View view) {
        this.expandableLayout1 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout1);
        this.expandableLayout1.toggle();
    }

    public void expandableButton2(View view) {
        this.expandableLayout2 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout2);
        this.expandableLayout2.toggle();
    }

    public void expandableButton3(View view) {
        this.expandableLayout3 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout3);
        this.expandableLayout3.toggle();
    }

    public void expandableButton4(View view) {
        this.expandableLayout4 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout4);
        this.expandableLayout4.toggle();
    }
}
