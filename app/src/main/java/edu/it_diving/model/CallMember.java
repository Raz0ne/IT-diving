package edu.it_diving.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.InputFilter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.Arrays;

import edu.it_diving.R;
import edu.it_diving.viewModel.CameraViewModel;
import edu.it_diving.viewModel.MicViewModel;
import jp.wasabeef.glide.transformations.BlurTransformation;

public class CallMember {

    private String name;
    private int icon;

    private Bitmap iconBitmap;

    public CameraViewModel cameraViewModel;
    public MicViewModel micViewModel;

    public CallMember(String name, int icon) {
        setName(name);
        setIcon(icon);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setIcon(int icon) {
        this.icon = icon;
        iconBitmap = null;
    }

    public void setIcon(Bitmap iconBitmap) { this.iconBitmap = iconBitmap; }

    public int getIcon() {
        return icon;
    }

    public void fillView(Context context, androidx.constraintlayout.widget.ConstraintLayout view) {
        TextView textView = view.findViewById(R.id.name_txt);

        if (name.length() >= 48) {
            String limitedName = name.substring(0, 48) + "...";
            textView.setText(limitedName);
        }
        else
            textView.setText(name);

        ImageView iconView = view.findViewById(R.id.icon_img);
        ImageView bgView = view.findViewById(R.id.bg_img);

        if (iconBitmap == null) {
            iconView.setImageResource(icon);

            Glide.with(context).load(icon).apply(RequestOptions.bitmapTransform(
                    new BlurTransformation(100, 3))).into(bgView);
        }
        else {
            Glide.with(context).load(iconBitmap).into(iconView);

            Glide.with(context).load(iconBitmap).apply(RequestOptions.bitmapTransform(
                    new BlurTransformation(100, 3))).into(bgView);
        }
    }
}
