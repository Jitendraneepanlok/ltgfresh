package com.ltg.ltgfresh.Helper;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ltg.ltgfresh.R;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.expand.Collapse;
import com.mindorks.placeholderview.annotations.expand.Expand;
import com.mindorks.placeholderview.annotations.expand.Parent;
import com.mindorks.placeholderview.annotations.expand.SingleTop;

@Parent
@SingleTop
@Layout(R.layout.header_layout)
public class HeaderView {

    private static String TAG = "HeaderView";
    private final String imgurl;

    @View(R.id.header_text)
    TextView headerText;

    @View(R.id.img_category)
    ImageView img_category;

    private Context mContext;
    private String mHeaderText;

    public HeaderView(Context context, String headerText, String imgurl) {
        this.mContext = context;
        this.mHeaderText = headerText;
        this.imgurl = imgurl;
    }

    @Resolve
    private void onResolve() {
        Glide.with(mContext).load(imgurl).into(img_category);

        headerText.setText(mHeaderText);
    }

    @Expand
    private void onExpand() {
        Toast.makeText(mContext, mHeaderText, Toast.LENGTH_SHORT).show();
    }

    @Collapse
    private void onCollapse() {
        Toast.makeText(mContext, mHeaderText, Toast.LENGTH_SHORT).show();
    }
}
