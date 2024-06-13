package com.apnagodam.staff.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.apnagodam.staff.R;
import com.apnagodam.staff.activity.StaffDashBoardActivity;
import com.apnagodam.staff.module.MenuItem;
import com.apnagodam.staff.module.UserDetails;
import java.util.ArrayList;

/**
 * Created by User on 31-07-2020.
 */

public class SubMenuNavigationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<MenuItem.SubList> menuItems;
    private static final int VIEW_TYPE_NORMAL = 1;
    private String pendingBillConnection, ofData, syndataServer;

    public SubMenuNavigationAdapter(ArrayList<MenuItem.SubList> menuItems, UserDetails userDetails, StaffDashBoardActivity dashboardClass) {
        this.menuItems = menuItems;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater from = LayoutInflater.from(parent.getContext());
        View inflate = from.inflate(R.layout.sub_menu_navigation_item, parent, false);
        return new NavigationViewHolder(inflate);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            NavigationViewHolder navigationViewHolder = (NavigationViewHolder) holder;
                navigationViewHolder.imageView.setImageResource(menuItems.get(position).getMenuImage());
                navigationViewHolder.menuName.setText(menuItems.get(position).getSubMenuTitle());
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    @Override
    public int getItemViewType(int position) {
            return VIEW_TYPE_NORMAL;
    }

    private static class NavigationViewHolder extends RecyclerView.ViewHolder {

        TextView menuName;
        ImageView imageView;

        public NavigationViewHolder(View view) {
            super(view);
            menuName = view.findViewById(R.id.menuTitle);
            imageView = view.findViewById(R.id.menuImage);
        }
    }

}
