package com.animoto.android.dgvSample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.animoto.android.dgv.DraggableGridView;

public class DraggableGridViewSampleActivity extends Activity implements OnItemClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        DraggableGridView dgv = ((DraggableGridView)findViewById(R.id.vgv));
        dgv.setAdapter(new DraggableGridViewAdapter(this));
        dgv.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
        Toast.makeText(this, "Click position " + position, Toast.LENGTH_SHORT);
    }

}