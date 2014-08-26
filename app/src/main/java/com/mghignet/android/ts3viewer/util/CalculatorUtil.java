package com.mghignet.android.ts3viewer.util;

import android.content.Context;

public class CalculatorUtil {

	private static CalculatorUtil instance;
	private static Context context;
	
	private CalculatorUtil(Context context) {
		this.context = context;
	}
	
	public static CalculatorUtil getInstance(Context context) {
		if(instance == null) {
			instance = new CalculatorUtil(context);
		}
		return instance;
	}
	
	public int pxFromDp(int dp) {
	    final float scale = context.getResources().getDisplayMetrics().density;
	    int px = (int) (dp * scale + 0.5f);
	    return px;
	}
	
}
