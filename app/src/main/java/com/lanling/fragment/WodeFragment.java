package com.lanling.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.lanling.activity.LocalDataActivity;
import com.lanling.activity.LoginActivity;
import com.lanling.activity.MainActivity;
import com.lanling.activity.MoreActivity;
import com.lanling.activity.R;
import com.lanling.activity.WebViewActivity;
import com.lanling.listener.BaseUiListener;
import com.lanling.util.Util;
import com.lanling.view.PerSelectView;
import com.lanling.view.TitlebarView;
import com.tencent.connect.common.Constants;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.Tencent;

import java.net.URL;
import java.util.ArrayList;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WodeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WodeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ImageView head_background;//头像背景
    private ImageView head;//头像
    private TextView user_name;//用户名
    private PerSelectView shangchuang;//我的上传
    private PerSelectView xiaozhishi;//肥料小知识
    private TitlebarView titlebarView;//标题栏
    private PerSelectView shezhi;//设置
    private PerSelectView bendi;//本地数据
    private Context context;//上下文
    private SharedPreferences sharedPreferences;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public WodeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WodeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WodeFragment newInstance(String param1, String param2) {
        WodeFragment fragment = new WodeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();//上下文
        sharedPreferences = context.getSharedPreferences("user",context.MODE_PRIVATE);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wode, container, false);
        head_background = view.findViewById(R.id.wode_head_background);
        head = view.findViewById(R.id.wode_head);//头像
        user_name = view.findViewById(R.id.user_name);//用户名
        titlebarView = view.findViewById(R.id.wode_titlebarview_title);//标题栏
        titlebarView.setOnViewClick(new TitlebarView.onViewClick() {
            @Override
            public void leftClick() {
            }

            @Override
            public void rightClick() {
                Intent intent = new Intent(context, MoreActivity.class);
                startActivity(intent);
            }
        });
        Login();
        //点击头像
        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.isLogin(getActivity()) != 0){//如果已经登录的话
                    Intent intent = new Intent(getActivity(), WebViewActivity.class);
                    intent.putExtra("title","我的资料");
                    intent.putExtra("url","http://www.zhengzhoudaxue.cn:8080/SaveData/MyInformation.jsp");
                    intent.putExtra("username",sharedPreferences.getString("username","0"));
                    intent.putExtra("openid",sharedPreferences.getString("openid","0"));
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(context, LoginActivity.class);//跳转到登录界面
                    startActivity(intent);
                }
            }
        });
        shangchuang = view.findViewById(R.id.wode_shangchuan);//我的上传
        //我的上传监听事件
        shangchuang.setOnClickView(new PerSelectView.OnClickView() {
            @Override
            public void onclick() {
                if(Util.isLogin(getActivity()) != 0){//如果已经登录的话
                    Intent intent = new Intent(getActivity(), WebViewActivity.class);
                    intent.putExtra("title","云端数据");
                    intent.putExtra("url","http://www.zhengzhoudaxue.cn:8080/SaveData/MyUpload.jsp");
                    intent.putExtra("username",sharedPreferences.getString("username","0"));
                    intent.putExtra("openid",sharedPreferences.getString("openid","0"));
                    startActivity(intent);
                }else{
                    Util.toastShort(getContext(),"您暂时没有登录，请先登录");
                    Intent intent = new Intent(getActivity(),LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        //本地存储
        bendi = view.findViewById(R.id.wode_bendi);//本地存储
        bendi.setOnClickView(new PerSelectView.OnClickView() {
            @Override
            public void onclick() {
                Intent intent = new Intent(getContext(), LocalDataActivity.class);
                startActivity(intent);
            }
        });

        //小知识
        xiaozhishi = view.findViewById(R.id.wode_xiaozhishi);//小知识
        xiaozhishi.setOnClickView(new PerSelectView.OnClickView() {
            @Override
            public void onclick() {
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("title","肥料小知识");
                intent.putExtra("url","http://www.zhengzhoudaxue.cn:8080/SaveData/index.html");
                startActivity(intent);
            }
        });

        //设置
        shezhi = view.findViewById(R.id.wode_shezhi);//设置
        shezhi.setOnClickView(new PerSelectView.OnClickView() {
            @Override
            public void onclick() {
                Intent intent = new Intent(getActivity(), MoreActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        Login();
    }
    //android从服务器端servlet获取数据
//    https://www.cnblogs.com/zhawj159753/p/3949956.html
    public void Login(){
        if (Util.isLogin(getActivity()) == 0){//如果没有登录的话
            user_name.setText("点击头像登录");
            Glide.with(getContext()).load(R.mipmap.denglu)
                    .bitmapTransform(new BlurTransformation(getContext(), 25), new CenterCrop(getContext()))
                    .into(head_background);
            Glide.with(getContext()).load(R.mipmap.denglu)
                    .bitmapTransform(new CropCircleTransformation(getContext()))
                    .into(head);
        }else if(Util.isLogin(getActivity()) == 1){//如果是第三方qq登录的话
            //重启一个线程，从网络中获取图片
            String text = sharedPreferences.getString("name","获取昵称失败");
            user_name.setText(text);
            Glide.with(getContext()).load(Uri.parse(sharedPreferences.getString("photoqq","http://www.zhengzhoudaxue.cn:8080/SaveData/images/error.jpg")))
                    .bitmapTransform(new BlurTransformation(getContext(), 25), new CenterCrop(getContext()))
                    .into(head_background);
            Glide.with(getContext()).load(Uri.parse(sharedPreferences.getString("photoqq","http://www.zhengzhoudaxue.cn:8080/SaveData/images/error.jpg")))
                    .bitmapTransform(new CropCircleTransformation(getContext()))
                    .into(head);
        }else{
            String text = "账号："+sharedPreferences.getString("username","");
            user_name.setText(text);
            Glide.with(getContext()).load(Uri.parse(sharedPreferences.getString("photouser","http://www.zhengzhoudaxue.cn:8080/SaveData/images/error.jpg")))
                    .bitmapTransform(new BlurTransformation(getContext(), 25), new CenterCrop(getContext()))
                    .into(head_background);
            Glide.with(getContext()).load(Uri.parse(sharedPreferences.getString("photouser","http://www.zhengzhoudaxue.cn:8080/SaveData/images/error.jpg")))
                    .bitmapTransform(new CropCircleTransformation(getContext()))
                    .into(head);
            Util.log("LoginActivity",sharedPreferences.getString("photouser","http://www.zhengzhoudaxue.cn:8080/SaveData/images/error.jpg"));
        }
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}