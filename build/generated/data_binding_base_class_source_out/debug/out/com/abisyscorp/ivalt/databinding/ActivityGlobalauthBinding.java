// Generated by data binding compiler. Do not edit!
package com.abisyscorp.ivalt.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.abisyscorp.ivalt.R;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class ActivityGlobalauthBinding extends ViewDataBinding {
  @NonNull
  public final ProgressBar loader;

  protected ActivityGlobalauthBinding(Object _bindingComponent, View _root, int _localFieldCount,
      ProgressBar loader) {
    super(_bindingComponent, _root, _localFieldCount);
    this.loader = loader;
  }

  @NonNull
  public static ActivityGlobalauthBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.activity_globalauth, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static ActivityGlobalauthBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<ActivityGlobalauthBinding>inflateInternal(inflater, R.layout.activity_globalauth, root, attachToRoot, component);
  }

  @NonNull
  public static ActivityGlobalauthBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.activity_globalauth, null, false, component)
   */
  @NonNull
  @Deprecated
  public static ActivityGlobalauthBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<ActivityGlobalauthBinding>inflateInternal(inflater, R.layout.activity_globalauth, null, false, component);
  }

  public static ActivityGlobalauthBinding bind(@NonNull View view) {
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
  public static ActivityGlobalauthBinding bind(@NonNull View view, @Nullable Object component) {
    return (ActivityGlobalauthBinding)bind(component, view, R.layout.activity_globalauth);
  }
}
