package com.cetc.spinnertest.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cetc.spinnertest.ArmyVehicle;
import com.cetc.spinnertest.R;

import java.util.ArrayList;
import java.util.List;


public class ArmyVehicleAdapter extends RecyclerView.Adapter<ArmyVehicleAdapter.ViewHolder> {

    private List<ArmyVehicle> mVehicleList;
    private ArmyVehicleAdapter.OnItemClickListener onItemClickListener;

    public ArmyVehicleAdapter(List<ArmyVehicle> vehicleList) {
        mVehicleList = vehicleList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView vehicleImage;
        TextView vehicleName;
        TextView BDSimID;

        public ViewHolder(View view) {
            super(view);
            vehicleImage = (ImageView) view.findViewById(R.id.iv_icon);
            vehicleName = (TextView) view.findViewById(R.id.tv_name);
            BDSimID = (TextView) view.findViewById(R.id.tv_sim);
        }
    }

    public void updateDataSource(List<ArmyVehicle> arrayList) {
        this.mVehicleList = arrayList;
        notifyDataSetChanged();
    }

    /**
     * 添加新的Item
     * @param position
     * @param vehicle
     */
    public void addItem(int position, ArmyVehicle vehicle) {
        if (this.mVehicleList == null) {
            mVehicleList = new ArrayList<>();
        }
        mVehicleList.add(position, vehicle);
        notifyItemInserted(position);
    }

    /**
     * 删除新的Item
     * @param position
     */
    public void removeItem(int position) {
        if (mVehicleList == null || mVehicleList.isEmpty()) {
            return;
        }
        mVehicleList.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * 定义点击毁掉接口
     */
    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    /**
     * 定义一个设置点击监听器的方法
     * @param listener
     */
    public void setOnItemClickListener(ArmyVehicleAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        ArmyVehicle armyVehicle = mVehicleList.get(i);

        viewHolder.vehicleName.setText(armyVehicle.getName());
        viewHolder.BDSimID.setText(String.valueOf(armyVehicle.getBDSimID()));
        viewHolder.itemView.setTag(mVehicleList.get(i));



        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    int pos = viewHolder.getLayoutPosition();
                    onItemClickListener.onItemClick(viewHolder.itemView, pos);
                }
            }
        });

        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemClickListener != null) {
                    int pos = viewHolder.getLayoutPosition();
                    onItemClickListener.onItemLongClick(viewHolder.itemView, pos);
                }
                // 表示此事件已经消费，不会触发单击事件
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mVehicleList == null ? 0 : mVehicleList.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_vehicle, viewGroup, false);
        return new ViewHolder(view);
    }
}
