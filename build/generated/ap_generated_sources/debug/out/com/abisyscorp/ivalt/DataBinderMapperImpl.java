package com.abisyscorp.ivalt;

import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import androidx.databinding.DataBinderMapper;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.ViewDataBinding;
import com.abisyscorp.ivalt.databinding.ActivityAboutUsBindingImpl;
import com.abisyscorp.ivalt.databinding.ActivityContactBindingImpl;
import com.abisyscorp.ivalt.databinding.ActivityGlobalauthBindingImpl;
import com.abisyscorp.ivalt.databinding.ActivityIntroScreenBindingImpl;
import com.abisyscorp.ivalt.databinding.ActivityLoginAuthenticateBindingImpl;
import com.abisyscorp.ivalt.databinding.ActivityLoginmainBindingImpl;
import com.abisyscorp.ivalt.databinding.ActivityMainzoomBindingImpl;
import com.abisyscorp.ivalt.databinding.ActivityOtpBindingImpl;
import com.abisyscorp.ivalt.databinding.ActivityProcessEnrollmentBindingImpl;
import com.abisyscorp.ivalt.databinding.ActivityViewLogBindingImpl;
import java.lang.IllegalArgumentException;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.RuntimeException;
import java.lang.String;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataBinderMapperImpl extends DataBinderMapper {
  private static final int LAYOUT_ACTIVITYABOUTUS = 1;

  private static final int LAYOUT_ACTIVITYCONTACT = 2;

  private static final int LAYOUT_ACTIVITYGLOBALAUTH = 3;

  private static final int LAYOUT_ACTIVITYINTROSCREEN = 4;

  private static final int LAYOUT_ACTIVITYLOGINAUTHENTICATE = 5;

  private static final int LAYOUT_ACTIVITYLOGINMAIN = 6;

  private static final int LAYOUT_ACTIVITYMAINZOOM = 7;

  private static final int LAYOUT_ACTIVITYOTP = 8;

  private static final int LAYOUT_ACTIVITYPROCESSENROLLMENT = 9;

  private static final int LAYOUT_ACTIVITYVIEWLOG = 10;

  private static final SparseIntArray INTERNAL_LAYOUT_ID_LOOKUP = new SparseIntArray(10);

  static {
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.abisyscorp.ivalt.R.layout.activity_about_us, LAYOUT_ACTIVITYABOUTUS);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.abisyscorp.ivalt.R.layout.activity_contact, LAYOUT_ACTIVITYCONTACT);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.abisyscorp.ivalt.R.layout.activity_globalauth, LAYOUT_ACTIVITYGLOBALAUTH);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.abisyscorp.ivalt.R.layout.activity_intro_screen, LAYOUT_ACTIVITYINTROSCREEN);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.abisyscorp.ivalt.R.layout.activity_login_authenticate, LAYOUT_ACTIVITYLOGINAUTHENTICATE);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.abisyscorp.ivalt.R.layout.activity_loginmain, LAYOUT_ACTIVITYLOGINMAIN);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.abisyscorp.ivalt.R.layout.activity_mainzoom, LAYOUT_ACTIVITYMAINZOOM);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.abisyscorp.ivalt.R.layout.activity_otp, LAYOUT_ACTIVITYOTP);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.abisyscorp.ivalt.R.layout.activity_process_enrollment, LAYOUT_ACTIVITYPROCESSENROLLMENT);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.abisyscorp.ivalt.R.layout.activity_view_log, LAYOUT_ACTIVITYVIEWLOG);
  }

  @Override
  public ViewDataBinding getDataBinder(DataBindingComponent component, View view, int layoutId) {
    int localizedLayoutId = INTERNAL_LAYOUT_ID_LOOKUP.get(layoutId);
    if(localizedLayoutId > 0) {
      final Object tag = view.getTag();
      if(tag == null) {
        throw new RuntimeException("view must have a tag");
      }
      switch(localizedLayoutId) {
        case  LAYOUT_ACTIVITYABOUTUS: {
          if ("layout/activity_about_us_0".equals(tag)) {
            return new ActivityAboutUsBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_about_us is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYCONTACT: {
          if ("layout/activity_contact_0".equals(tag)) {
            return new ActivityContactBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_contact is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYGLOBALAUTH: {
          if ("layout/activity_globalauth_0".equals(tag)) {
            return new ActivityGlobalauthBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_globalauth is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYINTROSCREEN: {
          if ("layout/activity_intro_screen_0".equals(tag)) {
            return new ActivityIntroScreenBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_intro_screen is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYLOGINAUTHENTICATE: {
          if ("layout/activity_login_authenticate_0".equals(tag)) {
            return new ActivityLoginAuthenticateBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_login_authenticate is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYLOGINMAIN: {
          if ("layout/activity_loginmain_0".equals(tag)) {
            return new ActivityLoginmainBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_loginmain is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYMAINZOOM: {
          if ("layout/activity_mainzoom_0".equals(tag)) {
            return new ActivityMainzoomBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_mainzoom is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYOTP: {
          if ("layout/activity_otp_0".equals(tag)) {
            return new ActivityOtpBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_otp is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYPROCESSENROLLMENT: {
          if ("layout/activity_process_enrollment_0".equals(tag)) {
            return new ActivityProcessEnrollmentBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_process_enrollment is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYVIEWLOG: {
          if ("layout/activity_view_log_0".equals(tag)) {
            return new ActivityViewLogBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_view_log is invalid. Received: " + tag);
        }
      }
    }
    return null;
  }

  @Override
  public ViewDataBinding getDataBinder(DataBindingComponent component, View[] views, int layoutId) {
    if(views == null || views.length == 0) {
      return null;
    }
    int localizedLayoutId = INTERNAL_LAYOUT_ID_LOOKUP.get(layoutId);
    if(localizedLayoutId > 0) {
      final Object tag = views[0].getTag();
      if(tag == null) {
        throw new RuntimeException("view must have a tag");
      }
      switch(localizedLayoutId) {
      }
    }
    return null;
  }

  @Override
  public int getLayoutId(String tag) {
    if (tag == null) {
      return 0;
    }
    Integer tmpVal = InnerLayoutIdLookup.sKeys.get(tag);
    return tmpVal == null ? 0 : tmpVal;
  }

  @Override
  public String convertBrIdToString(int localId) {
    String tmpVal = InnerBrLookup.sKeys.get(localId);
    return tmpVal;
  }

  @Override
  public List<DataBinderMapper> collectDependencies() {
    ArrayList<DataBinderMapper> result = new ArrayList<DataBinderMapper>(1);
    result.add(new androidx.databinding.library.baseAdapters.DataBinderMapperImpl());
    return result;
  }

  private static class InnerBrLookup {
    static final SparseArray<String> sKeys = new SparseArray<String>(1);

    static {
      sKeys.put(0, "_all");
    }
  }

  private static class InnerLayoutIdLookup {
    static final HashMap<String, Integer> sKeys = new HashMap<String, Integer>(10);

    static {
      sKeys.put("layout/activity_about_us_0", com.abisyscorp.ivalt.R.layout.activity_about_us);
      sKeys.put("layout/activity_contact_0", com.abisyscorp.ivalt.R.layout.activity_contact);
      sKeys.put("layout/activity_globalauth_0", com.abisyscorp.ivalt.R.layout.activity_globalauth);
      sKeys.put("layout/activity_intro_screen_0", com.abisyscorp.ivalt.R.layout.activity_intro_screen);
      sKeys.put("layout/activity_login_authenticate_0", com.abisyscorp.ivalt.R.layout.activity_login_authenticate);
      sKeys.put("layout/activity_loginmain_0", com.abisyscorp.ivalt.R.layout.activity_loginmain);
      sKeys.put("layout/activity_mainzoom_0", com.abisyscorp.ivalt.R.layout.activity_mainzoom);
      sKeys.put("layout/activity_otp_0", com.abisyscorp.ivalt.R.layout.activity_otp);
      sKeys.put("layout/activity_process_enrollment_0", com.abisyscorp.ivalt.R.layout.activity_process_enrollment);
      sKeys.put("layout/activity_view_log_0", com.abisyscorp.ivalt.R.layout.activity_view_log);
    }
  }
}
