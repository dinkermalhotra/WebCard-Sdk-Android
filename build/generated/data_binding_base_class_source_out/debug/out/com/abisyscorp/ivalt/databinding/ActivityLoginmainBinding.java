// Generated by data binding compiler. Do not edit!
package com.abisyscorp.ivalt.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ToggleButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.abisyscorp.ivalt.R;
import com.abisyscorp.ivalt.custom.BoldTextView;
import com.abisyscorp.ivalt.custom.NormalButton;
import com.abisyscorp.ivalt.custom.NormalEditText;
import com.abisyscorp.ivalt.custom.NormalTextView;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class ActivityLoginmainBinding extends ViewDataBinding {
  @NonNull
  public final NormalButton btnBack;

  @NonNull
  public final NormalButton btnSumbit;

  @NonNull
  public final ToggleButton checkbox;

  @NonNull
  public final NormalEditText etEmail;

  @NonNull
  public final NormalEditText etName;

  @NonNull
  public final NormalEditText etNumber;

  @NonNull
  public final ImageView ivImage;

  @NonNull
  public final ImageView ivImageFloag;

  @NonNull
  public final LinearLayout linCode;

  @NonNull
  public final ProgressBar loader;

  @NonNull
  public final BoldTextView tvAName;

  @NonNull
  public final BoldTextView tvAName1;

  @NonNull
  public final NormalTextView tvCountryCode;

  @NonNull
  public final NormalTextView tvPolicy;

  protected ActivityLoginmainBinding(Object _bindingComponent, View _root, int _localFieldCount,
      NormalButton btnBack, NormalButton btnSumbit, ToggleButton checkbox, NormalEditText etEmail,
      NormalEditText etName, NormalEditText etNumber, ImageView ivImage, ImageView ivImageFloag,
      LinearLayout linCode, ProgressBar loader, BoldTextView tvAName, BoldTextView tvAName1,
      NormalTextView tvCountryCode, NormalTextView tvPolicy) {
    super(_bindingComponent, _root, _localFieldCount);
    this.btnBack = btnBack;
    this.btnSumbit = btnSumbit;
    this.checkbox = checkbox;
    this.etEmail = etEmail;
    this.etName = etName;
    this.etNumber = etNumber;
    this.ivImage = ivImage;
    this.ivImageFloag = ivImageFloag;
    this.linCode = linCode;
    this.loader = loader;
    this.tvAName = tvAName;
    this.tvAName1 = tvAName1;
    this.tvCountryCode = tvCountryCode;
    this.tvPolicy = tvPolicy;
  }

  @NonNull
  public static ActivityLoginmainBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.activity_loginmain, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static ActivityLoginmainBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<ActivityLoginmainBinding>inflateInternal(inflater, R.layout.activity_loginmain, root, attachToRoot, component);
  }

  @NonNull
  public static ActivityLoginmainBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.activity_loginmain, null, false, component)
   */
  @NonNull
  @Deprecated
  public static ActivityLoginmainBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<ActivityLoginmainBinding>inflateInternal(inflater, R.layout.activity_loginmain, null, false, component);
  }

  public static ActivityLoginmainBinding bind(@NonNull View view) {
    return bind(view, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.bind(view, component)
   */
  @Deprecated
  public static ActivityLoginmainBinding bind(@NonNull View view, @Nullable Object component) {
    return (ActivityLoginmainBinding)bind(component, view, R.layout.activity_loginmain);
  }
}
