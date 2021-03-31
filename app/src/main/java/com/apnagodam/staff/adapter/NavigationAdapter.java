package com.apnagodam.staff.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apnagodam.staff.BuildConfig;
import com.apnagodam.staff.GlideApp;
import com.apnagodam.staff.R;
import com.apnagodam.staff.activity.StaffDashBoardActivity;
import com.apnagodam.staff.activity.StaffProfileActivity;
import com.apnagodam.staff.db.SharedPreferencesRepository;
import com.apnagodam.staff.interfaces.OnProfileClickListener;
import com.apnagodam.staff.module.MenuItem;
import com.apnagodam.staff.module.UserDetails;
import com.apnagodam.staff.utils.Constants;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

/**
 * Created by User on 31-07-2020.
 */

public class NavigationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private OnProfileClickListener onProfileClickInterface;
    private ArrayList<MenuItem> menuItems;
    //  private LoginResponse.Data user;
    private Activity activity;
    private UserDetails userDetailsValues;
    private static final int VIEW_TYPE_NORMAL = 1;
    private static final int VIEW_TYPE_HEADER = 0;
    private String pendingBillConnection, ofData, syndataServer;

    public void setOnProfileClickInterface(OnProfileClickListener onProfileClickInterface) {
        this.onProfileClickInterface = onProfileClickInterface;
    }

    public NavigationAdapter(ArrayList<MenuItem> menuItems, UserDetails userDetails, StaffDashBoardActivity dashboardClass) {
        this.menuItems = menuItems;
        this.userDetailsValues = userDetails;
        this.activity = dashboardClass;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater from = LayoutInflater.from(parent.getContext());
        View inflate;
        if (viewType == VIEW_TYPE_HEADER) {
            inflate = from.inflate(R.layout.navigation_header, parent, false);
            return new HeaderViewHolder(inflate, onProfileClickInterface);
        } else {
            inflate = from.inflate(R.layout.navigation_item, parent, false);
            return new NavigationViewHolder(inflate);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_HEADER) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            if (userDetailsValues.getProfileImage() != null) {
                GlideApp.with(headerViewHolder.profileImage.getContext())
                        .load(Constants.IMAGE_BASE_URL + userDetailsValues.getProfileImage())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.user_shape)
                        .circleCrop()
                        .into(headerViewHolder.profileImage);
            }
            headerViewHolder.userName.setText(userDetailsValues.getFname() + " " + userDetailsValues.getLname());
            if (BuildConfig.DEBUG) {
                headerViewHolder.versionName.setText("V " + BuildConfig.VERSION_NAME);
            }



           /* headerViewHolder.rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, StaffProfileActivity.class);
                    activity.startActivity(intent);
                }
            });*/
        } else {

            NavigationViewHolder navigationViewHolder = (NavigationViewHolder) holder;
            navigationViewHolder.subMenuItemsRecycler.setLayoutManager(new LinearLayoutManager(activity));
            SubMenuNavigationAdapter navigationVLCAdapter = new SubMenuNavigationAdapter(menuItems.get(position).getGetList(), userDetailsValues, (StaffDashBoardActivity) activity);
            navigationViewHolder.subMenuItemsRecycler.setAdapter(navigationVLCAdapter);

           /* if (position == 1) {
                navigationViewHolder.imageView.setImageResource(menuItems.get(position).getMenuImage());
                navigationViewHolder.menuName.setText(menuItems.get(position).getMenuTitle());
            } else if (position == 2) {
                navigationViewHolder.imageView.setImageResource(menuItems.get(position).getMenuImage());
                navigationViewHolder.menuName.setText(menuItems.get(position).getMenuTitle());
            } else if (position == 3) {
                navigationViewHolder.imageView.setImageResource(menuItems.get(position).getMenuImage());
                navigationViewHolder.menuName.setText(menuItems.get(position).getMenuTitle());
            } else if (position == 4) {
                navigationViewHolder.imageView.setImageResource(menuItems.get(position).getMenuImage());
                navigationViewHolder.menuName.setText(menuItems.get(position).getMenuTitle());
            } else if (position == 5) {
                navigationViewHolder.imageView.setImageResource(menuItems.get(position).getMenuImage());
                navigationViewHolder.menuName.setText(menuItems.get(position).getMenuTitle());
            } else {*/
                navigationViewHolder.imageView.setImageResource(menuItems.get(position).getMenuImage());
                navigationViewHolder.menuName.setText(menuItems.get(position).getMenuTitle());
//            }
        }
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_HEADER;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    private static class HeaderViewHolder extends RecyclerView.ViewHolder {

        ImageView profileImage;
        TextView userName, sdo_code, last_login;
        TextView versionName;
        RelativeLayout root, rl;


        @SuppressLint("ClickableViewAccessibility")
        public HeaderViewHolder(View view, OnProfileClickListener onProfileClickInterface) {
            super(view);
            userName = view.findViewById(R.id.headerName);
            versionName = view.findViewById(R.id.versionName);
            root = view.findViewById(R.id.root);
            rl = view.findViewById(R.id.rl);
            profileImage = view.findViewById(R.id.headerImage);
            profileImage.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        onProfileClickInterface.onProfileImgClick();
                    }
                    return false;
                }
            });
        }
    }

    private static class NavigationViewHolder extends RecyclerView.ViewHolder {

        TextView menuName;
        ImageView imageView;
        RecyclerView subMenuItemsRecycler;

        public NavigationViewHolder(View view) {
            super(view);
            menuName = view.findViewById(R.id.menuTitle);
            imageView = view.findViewById(R.id.menuImage);
            subMenuItemsRecycler = view.findViewById(R.id.subMenuItemsRecycler);

        }
    }

}
