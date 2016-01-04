/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package uk.co.senab.photoview.sample;

import android.app.Activity;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher;
import uk.co.senab.photoview.PhotoViewAttacher.OnMatrixChangedListener;
import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;

public class SimpleSampleActivity extends Activity {

	static final String PHOTO_TAP_TOAST_STRING = "Photo Tap! X: %.2f %% Y:%.2f %%";

	ImageView iv_background;
	ImageView iv_overlay;
	TextView mCurrMatrixTv;
	Toast mCurrentToast;

	PhotoViewAttacher mAttacher;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		iv_background = (ImageView) findViewById(R.id.iv_background);
		iv_overlay = (ImageView) findViewById(R.id.iv_overlay);
		mCurrMatrixTv = (TextView) findViewById(R.id.tv_current_matrix);

		Drawable bitmap = getResources().getDrawable(R.drawable.singapore_map_white);
		iv_background.setImageDrawable(bitmap);
		Drawable overlay = getResources().getDrawable(R.drawable.map_over_mrt);
		iv_overlay.setImageDrawable(overlay);

		ImageView iv_icon = new ImageView(getApplicationContext());
		iv_icon.setImageResource(R.drawable.ic_launcher);
		iv_icon.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//mGrid.show(iv_icon);
			}
		});

		ImageView iv_icon2 = new ImageView(getApplicationContext());
		iv_icon2.setImageResource(R.drawable.ic_launcher);

		List<ImageView> icons = new ArrayList<>();
		icons.add(iv_icon);
		icons.add(iv_icon2);

		RelativeLayout relative = (RelativeLayout) findViewById(R.id.relative);
		relative.addView(iv_icon, new LayoutParams(50, 50));
		relative.addView(iv_icon2, new LayoutParams(50, 50));

		// The MAGIC happens here!
		mAttacher = new PhotoViewAttacher(iv_background, iv_overlay, icons);

		// Lets attach some listeners, not required though!
		mAttacher.setOnMatrixChangeListener(new MatrixChangeListener());
		mAttacher.setOnPhotoTapListener(new PhotoTapListener());
	}


	@Override
	public void onDestroy() {
		super.onDestroy();

		// Need to call clean-up
		mAttacher.cleanup();
	}

	

	private class PhotoTapListener implements OnPhotoTapListener {

		@Override
		public void onPhotoTap(View view, float x, float y) {
			float xPercentage = x * 100f;
			float yPercentage = y * 100f;

			if (null != mCurrentToast) {
				mCurrentToast.cancel();
			}

			mCurrentToast = Toast.makeText(SimpleSampleActivity.this, String.format(PHOTO_TAP_TOAST_STRING, xPercentage, yPercentage), Toast.LENGTH_SHORT);
			mCurrentToast.show();
		}
	}

	private class MatrixChangeListener implements OnMatrixChangedListener {

		@Override
		public void onMatrixChanged(RectF rect) {
			mCurrMatrixTv.setText(rect.toString());
		}
	}

}
