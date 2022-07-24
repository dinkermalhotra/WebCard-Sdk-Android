package com.abisyscorp.ivalt.databinding;
import com.abisyscorp.ivalt.R;
import com.abisyscorp.ivalt.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ActivityProcessEnrollmentBindingImpl extends ActivityProcessEnrollmentBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.ivImage, 1);
        sViewsWithIds.put(R.id.tvAName, 2);
        sViewsWithIds.put(R.id.tvAName1, 3);
        sViewsWithIds.put(R.id.linSuccessText, 4);
        sViewsWithIds.put(R.id.tvSuccessText, 5);
        sViewsWithIds.put(R.id.tvSuccessText2, 6);
        sViewsWithIds.put(R.id.tvSuccessText3, 7);
        sViewsWithIds.put(R.id.linInfo, 8);
        sViewsWithIds.put(R.id.btnSumbit, 9);
        sViewsWithIds.put(R.id.loader, 10);
    }
    // views
    @NonNull
    private final android.widget.RelativeLayout mboundView0;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ActivityProcessEnrollmentBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 11, sIncludes, sViewsWithIds));
    }
    private ActivityProcessEnrollmentBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (com.abisyscorp.ivalt.custom.NormalButton) bindings[9]
            , (android.widget.ImageView) bindings[1]
            , (android.widget.LinearLayout) bindings[8]
            , (android.widget.LinearLayout) bindings[4]
            , (android.widget.ProgressBar) bindings[10]
            , (com.abisyscorp.ivalt.custom.BoldTextView) bindings[2]
            , (com.abisyscorp.ivalt.custom.NormalTextView) bindings[3]
            , (android.widget.ImageView) bindings[5]
            , (com.abisyscorp.ivalt.custom.BoldTextView) bindings[6]
            , (com.abisyscorp.ivalt.custom.NormalTextView) bindings[7]
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