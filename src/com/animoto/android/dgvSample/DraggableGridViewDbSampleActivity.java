package com.animoto.android.dgvSample;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.animoto.android.dgv.DraggableGridView;
import com.animoto.android.dgv.OnRearrangeListener;

public class DraggableGridViewDbSampleActivity extends Activity implements OnRearrangeListener {

	DraggableGridView dgv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		dgv = ((DraggableGridView)findViewById(R.id.vgv));

		dgv.setAdapter(new DgvDatabaseAdapter(getBaseContext()));
		dgv.setOnRearrangeListener(this);
	}

	@Override
	public void onRearrange(int oldIndex, int newIndex) {
		ORMHelper.photoDao.rearrangePhotos(oldIndex, newIndex);
		Log.i("dgv", "Item at position " + oldIndex + " moved to " + newIndex + ".");
	}


}