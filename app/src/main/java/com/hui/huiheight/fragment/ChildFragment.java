package com.hui.huiheight.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hui.huiheight.HomeActivity;
import com.hui.huiheight.MainsActivity;
import com.hui.huiheight.R;
import com.hui.huiheight.first.ShopCarActivity;
import com.hui.huiheight.first.addresspicker.AddressPickerActivity;
import com.hui.huiheight.first.chooselocation.ChoiceLocationActivity;
import com.hui.huiheight.first.chooselocation.GetLocationActivity;
import com.hui.huiheight.first.oss.OSSActivity;
import com.hui.huiheight.first.phone.PhoneInfoActivity;
import com.hui.huiheight.first.phone.ScreenShotActivity;
import com.hui.huiheight.first.photo.CutPhonePictureActivity;
import com.hui.huiheight.first.photo.CutPhonePictureActivity2;
import com.hui.huiheight.first.photo.PhonePictureActivity;
import com.hui.huiheight.first.retrofit2.Retrofit2Activity;
import com.hui.huiheight.fragment.adapter.RecyclerViewAdapter;
import com.hui.wheelviewlibrary.WheelViewActivity;

import java.util.ArrayList;
import java.util.List;

import walke.base.AppFragment;
import walke.base.TestActivityTest;
import walke.base.tool.SetUtil;
import walke.demolibrary.completion.CompletionActivity;
import walke.demolibrary.interface1.Demo1Activity;
import walke.demolibrary.interface3.Demo3Activity;
import walke.widget.sunset.SunAnimationActivity;


/**
 * Created by walke.Z on 2017/8/2.
 */

public class ChildFragment extends AppFragment {

    private static ChildFragment mChildFragment;
    private TextView mTextView;
    private RecyclerView mRecyclerView;
    private List<AppCompatActivity> mActivities=new ArrayList<>();



    public static ChildFragment getChildFragment(String title,String[] arr){
        //Java.lang.IllegalStateException: Can't change tag of fragment fendo1MainActivity{531dbce4 index=0x7f0b0054
        // Android:switcher:2131427412:0}: was android:switcher:2131427412:0 now android:switcher:2131427412:1
        //if (mChildFragment==null)
            //mChildFragment=new ChildFragment();
        mChildFragment=new ChildFragment();
        Bundle args=new Bundle();
        args.putString("title",title);
        args.putStringArray("strArr",arr);
        mChildFragment.setArguments(args);
        return mChildFragment;
    }


    @Override
    protected int rootLayoutId() {
        List<Class> acts=new ArrayList<>();
        Class<HomeActivity> aClass = HomeActivity.class;
        acts.add(aClass);
        return R.layout.fragment_child;
    }

    @Override
    protected void initView(View rootView, Bundle savedInstanceState) {
        mTextView = ((TextView) rootView.findViewById(R.id.fc_text));
        mRecyclerView = ((RecyclerView) rootView.findViewById(R.id.fc_recyclerView));
        Bundle arguments = getArguments();
        if (arguments!=null){
            String title = arguments.getString("title", "title");
            String[] strArrs = arguments.getStringArray("strArr");
            mTextView.setText(title);
            initActivitys(title);

            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            mRecyclerView.setLayoutManager(layoutManager);
            RecyclerViewAdapter adapter = new RecyclerViewAdapter(getContext(), SetUtil.arrToList(strArrs));
            mRecyclerView.setAdapter(adapter);

            adapter.setItemClickListener(new RecyclerViewAdapter.onItemClickListener() {
                @Override
                public void onClick(int position) {

                    startActivity(new Intent(getContext(), mActivities.get(position).getClass()));

                }
            });
        }

    }

    private void initActivitys(String title) {
        if ("first+child".equals(title)){// R.array.tabTitles
            mActivities.add(new GetLocationActivity());
            mActivities.add(new ChoiceLocationActivity());
            mActivities.add(new WheelViewActivity());
            mActivities.add(new CutPhonePictureActivity());
            mActivities.add(new CutPhonePictureActivity2());
            mActivities.add(new PhonePictureActivity());
            mActivities.add(new PhoneInfoActivity());
            mActivities.add(new ScreenShotActivity());
            mActivities.add(new Retrofit2Activity());
            mActivities.add(new MainsActivity());
            mActivities.add(new TestActivityTest());
            mActivities.add(new SunAnimationActivity());
            mActivities.add(new Demo1Activity());//Java多态接口动态加载实例
            mActivities.add(new ShopCarActivity());
            mActivities.add(new AddressPickerActivity());
            mActivities.add(new OSSActivity());


        }else if ("demo".equals(title)){
            mActivities.add(new Demo1Activity());
            mActivities.add(new Demo3Activity());
            mActivities.add(new CompletionActivity());
//            mActivities.add(new GlideActivity());
        }else if ("views".equals(title)){

        }else if ("消息".equals(title)){

        }else {

        }

    }

    @Override
    protected void initData() {

    }

}
