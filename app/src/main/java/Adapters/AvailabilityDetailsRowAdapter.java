package Adapters;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.akshaychavan.covid19vaccineavailabilitychecker.R;

import java.util.ArrayList;

import pojo.Center;

/**
 * Created by Akshay Chavan on 15,February,2021
 * akshay.chavan@finiq.com
 * FinIQ Consulting India
 */
public class AvailabilityDetailsRowAdapter extends RecyclerView.Adapter<AvailabilityDetailsRowAdapter.DetailsViewHolder> {

    final String TAG = "AvailabilityDetailsRowAdapter";
    private ArrayList<Center> mDetailsList;
    private Context mContext;

    public AvailabilityDetailsRowAdapter(ArrayList<Center> detailsList, Context context) {
        mDetailsList = detailsList;
        this.mContext = context;
    }


    @NonNull
    @Override
    public DetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.details_row, parent, false);
        DetailsViewHolder holdingsViewHolder = new DetailsViewHolder(v);
        return holdingsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DetailsViewHolder holder, int position) {
        Center currentItem = mDetailsList.get(position);

        holder.tvCenterName.setText(currentItem.getName());
        holder.tvFeeType.setText(currentItem.getFeeType());
        holder.tvVaccineName.setText(currentItem.getSessions().get(0).getVaccine());
        holder.tvDate.setText(currentItem.getSessions().get(0).getDate());
        holder.tvCount.setText(currentItem.getSessions().get(0).getAvailableCapacity().toString());

    }

    @Override
    public int getItemCount() {
        return mDetailsList.size();
    }


    public static class DetailsViewHolder extends RecyclerView.ViewHolder {
        TextView tvCenterName, tvVaccineName, tvDate, tvCount, tvFeeType;

        public DetailsViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCenterName = itemView.findViewById(R.id.tv_center_name);
            tvVaccineName = itemView.findViewById(R.id.tv_vaccine_name);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvCount = itemView.findViewById(R.id.tv_count);
            tvFeeType = itemView.findViewById(R.id.tv_fee_type);

        }
    }
}
