// Generated by data binding compiler. Do not edit!
package com.abisyscorp.ivalt.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.abisyscorp.ivalt.R;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class ActivityMainzoomBinding extends ViewDataBinding {
  @NonNull
  public final Button helpButton;

  @NonNull
  public final ImageView ivImage;

  @NonNull
  public final ProgressBar loader;

  protected ActivityMainzoomBinding(Object _bindingComponent, View _root, int _localFieldCount,
      Button helpButton, ImageView ivImage, ProgressBar loader) {
    super(_bindingComponent, _root, _localFieldCount);
    this.helpButton = helpButton;
    this.ivImage = ivImage;
    this.loader = loader;
  }

  @NonNull
  public static ActivityMainzoomBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.activity_mainzoom, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static ActivityMainzoomBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<ActivityMainzoomBinding>inflateInternal(inflater, R.layout.activity_mainzoom, root, attachToRoot, component);
  }

  @NonNull
  public static ActivityMainzoomBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.activity_mainzoom, null, false, component)
   */
  @NonNull
  @Deprecated
  public static ActivityMainzoomBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<ActivityMainzoomBinding>inflateInternal(inflater, R.layout.activity_mainzoom, null, false, component);
  }

  public static ActivityMainzoomBinding bind(@NonNull View view) {
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
  public static ActivityMainzoomBinding bind(@NonNull View view, @Nullable Object component) {
    return (ActivityMainzoomBinding)bind(component, view, R.layout.activity_mainzoom);
  }
}
