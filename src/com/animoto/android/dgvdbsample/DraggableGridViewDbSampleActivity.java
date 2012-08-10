package com.animoto.android.dgvdbsample;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.animoto.android.dgv.DraggableGridView;
import com.animoto.android.dgv.OnRearrangeListener;
import com.animoto.android.dgvdbsample.model.ORMHelper;
import com.animoto.android.dgvdbsample.model.Photo;


public class DraggableGridViewDbSampleActivity extends Activity implements OnRearrangeListener {

	static Random random = new Random();
	static String[] words = "the of and a to in is be that was he for it with as his I on have at by not they this had are but from or she an which you one we all were her would there their will when who him been has more if no out do so can what up said about other into than its time only could new them man some these then two first may any like now my such make over our even most me state after also made many did must before back see through way where get much go well your know should down work year because come people just say each those take day good how long Mr own too little use US very great still men here life both between old under last never place same another think house while high right might came off find states since used give against three himself look few general hand school part small American home during number again Mrs around thought went without however govern don't does got public United point end become head once course fact upon need system set every war put form water took".split(" ");
	DraggableGridView dgv;
	Button button1, button2;
	ArrayList<String> poem = new ArrayList<String>();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		dgv = ((DraggableGridView)findViewById(R.id.vgv));

		dgv.setAdapter(new DgvDatabaseAdapter(getBaseContext()));
		dgv.setOnRearrangeListener(this);

		try {
			List<Photo> photos = ORMHelper.photoDao.queryForAll();
			Log.i("dgv", "Number of photos: " + photos.size());

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void onRearrange(int oldIndex, int newIndex) {
		ORMHelper.photoDao.rearrangePhotos(oldIndex, newIndex);
		Log.i("dgv", "Item at position " + oldIndex + " moved to " + newIndex + ".");
	}


}