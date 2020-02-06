package com.lanling.view;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.lanling.activity.R;

public class PerSelectView extends LinearLayout{

    private LinearLayout linearLayout;//布局容器
    private ImageView imageView;//左边的图标
    private TextView textView;//中间的文字
    private OnClickView onClickView;

    public PerSelectView(Context context) {
        this(context,null);
    }

    public PerSelectView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public void setOnClickView(OnClickView onClickView){
        this.onClickView = onClickView;
    }

    public PerSelectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(R.layout.perselectview, this);
        linearLayout = (LinearLayout) view.findViewById(R.id.perselect_relativelayout);
        linearLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickView.onclick();
            }
        });
        imageView = (ImageView) view.findViewById(R.id.perselect_imageview);
        textView = (TextView) view.findViewById(R.id.perselect_textview);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.PerSelectView, defStyleAttr, 0);
        imageView.setImageResource(array.getResourceId(R.styleable.PerSelectView_imageview,R.mipmap.shezhi));
        String text = (String) array.getText(R.styleable.PerSelectView_text);
        float textSize = array.getDimension(R.styleable.PerSelectView_textSize,15);
        if (text !=null && textSize!=0){
            textView.setText(text);
            textView.setTextSize(textSize);
        }
        array.recycle();
    }

    public void setText(String text){
        textView.setText(text);
    }

    public void setImage(int imageId){
        imageView.setImageResource(imageId);
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }
    public PerSelectView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public interface OnClickView{
        public void onclick();
    }
}
