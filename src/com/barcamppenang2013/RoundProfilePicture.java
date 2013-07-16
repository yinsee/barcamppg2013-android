package com.barcamppenang2013;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;

public class RoundProfilePicture {

	public Bitmap getBitMap(Context c, String fbid) {

		Bitmap bitm_default = BitmapFactory.decodeResource(c.getResources(),
				R.drawable.defaultpic);
		Bitmap round_default = getRoundedShape(bitm_default);
		Bitmap return_bitmap;
		URL imgUrl = null;
		try {
			imgUrl = new URL(
					"http://graph.facebook.com/"+fbid+"/picture?type=large"); 
		} catch (MalformedURLException e) {
			return_bitmap = round_default;
			// img.setImageBitmap(round_default);
			// e.printStackTrace();
		}

		try {
			Bitmap bitm = BitmapFactory.decodeStream(imgUrl.openConnection()
					.getInputStream());
			Bitmap round_fbpic = getRoundedShape(bitm);
			return_bitmap = round_fbpic;
			

		} catch (IOException e) {

			return_bitmap = round_default;
			// e.printStackTrace();
		}
		return return_bitmap;
	}

	public Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
		int targetWidth = 100;
		int targetHeight = 100;
		Bitmap targetBitmap = Bitmap.createBitmap(targetWidth, targetHeight,
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(targetBitmap);
		Path path = new Path();
		path.addCircle(((float) targetWidth) / 2, ((float) targetHeight) / 2,
				(Math.min(((float) targetWidth), ((float) targetHeight)) / 2),
				Path.Direction.CW);
		Paint paint = new Paint();
		paint.setColor(Color.GRAY);
		// paint.setStyle(Paint.Style.STROKE);
		paint.setStyle(Paint.Style.FILL);
		paint.setAntiAlias(true);
		paint.setDither(true);
		paint.setFilterBitmap(true);
		canvas.drawOval(new RectF(0, 0, targetWidth, targetHeight), paint);
		// paint.setColor(Color.TRANSPARENT);
		canvas.clipPath(path);
		Bitmap sourceBitmap = scaleBitmapImage;
		canvas.drawBitmap(sourceBitmap, new Rect(0, 0, sourceBitmap.getWidth(),
				sourceBitmap.getHeight()), new RectF(0, 0, targetWidth,
				targetHeight), paint);
		return targetBitmap;
	}
}
