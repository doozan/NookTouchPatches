package com.android.internal.policy.impl;

import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;

public class ModUtils {
  
  private static final String VERSION          = "0.1.2";
  private static final String TAG              = "NookMod";

  private static volatile com.android.internal.policy.impl.PhoneWindowManager mContext;
  
  public static String getVersion() { return VERSION; }

  public static boolean onHomeKeyPress(Context context) {
    ContentResolver resolver = context.getContentResolver();
    String action = Settings.System.getString(resolver, "mod.key.home.cmd");
    
    //Log.v(TAG, "OnHomeKeyPress()");
    return doAction(context, action);
  }

  public static boolean onHomeKeyPressLong(Context context) {
    ContentResolver resolver = context.getContentResolver();
    String action = Settings.System.getString(resolver, "mod.key.home_long.cmd");
    
    //Log.v(TAG, "OnHomeKeyLongPress()");
    return doAction(context, action);
  }

  // call doAction in the other package just to avoid having to duplicate the functionality here
  private static boolean doAction(Context context, String action) {
    return com.android.server.status.ModUtils.doAction(context,  action);
  }
  
  public static void setContext(com.android.internal.policy.impl.PhoneWindowManager context)
  {
    //Log.v(TAG, "setContext =  " + context );
    mContext = context;
  }
  
  public static com.android.internal.policy.impl.PhoneWindowManager getContext()
  {
    //Log.v(TAG, "getContext =  " + mContext );
    return mContext;
  }
  
}