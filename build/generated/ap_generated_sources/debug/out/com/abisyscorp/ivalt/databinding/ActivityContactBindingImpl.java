package com.abisyscorp.ivalt.databinding;
import com.abisyscorp.ivalt.R;
import com.abisyscorp.ivalt.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ActivityContactBindingImpl extends ActivityContactBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.frmBack, 1);
        sViewsWithIds.put(R.id.ivBack, 2);
        sViewsWithIds.put(R.id.tvBack, 3);
        sViewsWithIds.put(R.id.tvEmail, 4);
        sViewsWithIds.put(R.id.etEmail, 5);
        sViewsWithIds.put(R.id.tvMobile, 6);
        sViewsWithIds.put(R.id.etMobile, 7);
        sViewsWithIds.put(R.id.tvMessage, 8);
        sViewsWithIds.put(R.id.etMessage, 9);
        sViewsWithIds.put(R.id.loader, 10);
        sViewsWithIds.put(R.id.btnSend, 11);
    }
    // views
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ActivityContactBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 12, sIncludes, sViewsWithIds));
    }
    private ActivityContactBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (com.abisyscorp.ivalt.custom.NormalButton) bindings[11]
            , (com.abisyscorp.ivalt.custom.NormalEditText) bindings[5]
            , (android.widget.EditText) bindings[9]
            , (android.widget.EditText) bindings[7]
            , (android.widget.LinearLayout) bindings[1]
            , (android.widget.ImageView) bindings[2]
            , (android.widget.ProgressBar) bindings[10]
            , (android.widget.LinearLayout) bindings[0]
            , (com.abisyscorp.ivalt.custom.NormalTextView) bindings[3]
            , (com.abisyscorp.ivalt.custom.NormalTextView) bindings[4]
            , (com.abisyscorp.ivalt.custom.NormalTextView) bindings[8]
            , (com.abisyscorp.ivalt.custom.NormalTextView) bindings[6]
            );
        this.relMain.setTag(null);
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