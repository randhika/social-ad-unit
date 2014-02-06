package com.example.socialaddemo;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

public class FontProvider {
	
	private final static String HERO_LIGHT = "HelveticaNeueMedium.ttf";
	
	static Typeface mHelveticaBoldFont;
	static Typeface mHelveticaLightFont;
	static Typeface mHelveticaVeryLightFont;	  
	
	
	public static Typeface getHelveticaLightFont(Context context) {
	    if (mHelveticaLightFont == null) {
	    	mHelveticaLightFont = Typeface.createFromAsset(context.getAssets(),HERO_LIGHT );
	    }
	    return mHelveticaLightFont;
	}
}
