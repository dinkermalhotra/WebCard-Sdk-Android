package com.abisyscorp.ivalt.databinding;
import com.abisyscorp.ivalt.R;
import com.abisyscorp.ivalt.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ActivityOtpBindingImpl extends ActivityOtpBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.ivImage, 1);
        sViewsWithIds.put(R.id.tvAName1, 2);
        sViewsWithIds.put(R.id.etOtp1, 3);
        sViewsWithIds.put(R.id.etOtp2, 4);
        sViewsWithIds.put(R.id.etOtp3, 5);
        sViewsWithIds.put(R.id.etOtp4, 6);
        sViewsWithIds.put(R.id.btnResend, 7);
        sViewsWithIds.put(R.id.errMsg, 8);
        sViewsWithIds.put(R.id.linInfo, 9);
        sViewsWithIds.put(R.id.btnBack, 10);
        sViewsWithIds.put(R.id.btnSumbit, 11);
        sViewsWithIds.put(R.id.loader, 12);
    }
    // views
    @NonNull
    private final android.widget.RelativeLayout mboundView0;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ActivityOtpBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 13, sIncludes, sViewsWithIds));
    }
    private ActivityOtpBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (com.abisyscorp.ivalt.custom.NormalButton) bindings[10]
            , (com.abisyscorp.ivalt.custom.NormalButton) bindings[7]
            , (com.abisyscorp.ivalt.custom.NormalButton) bindings[11]
            , (com.abisyscorp.ivalt.custom.NormalTextView) bindings[8]
            , (com.abisyscorp.ivalt.custom.NormalEditText) bindings[3]
            , (com.abisyscorp.ivalt.custom.NormalEditText) bindings[4]
            , (com.abisyscorp.ivalt.custom.NormalEditText) bindings[5]
            , (com.abisyscorp.ivalt.custom.NormalEditText) bindings[6]
            , (android.widget.ImageView) bindings[1]
            , (android.widget.LinearLayout) bindings[9]
            , (android.widget.ProgressBar) bindings[12]
            , (com.abisyscorp.ivalt.custom.NormalTextView) bindings[2]
            );
        this.mboundView0 = (android.widget.RelativeLayout) bindings[0];
        this.mboundView0.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x1L;
        }
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean setVariable(int variableId, @Nullable Object variable)  {
        boolean variableSet = true;
            return variableSet;
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
        }
        return false;
    }

    @Override
    protected void executeBindings() {
        long dirtyFlags = 0;
        synchronized(this) {
            dirtyFlags = mDirtyFlags;
            mDirtyFlags = 0;
        }
        // batch finished
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): null
    flag mapping end*/
    //end
}