package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.akshaychavan.vaxicov.R;

import java.util.ArrayList;

import com.akshaychavan.vaxicov.pojo.SessionDistrict;

/**
 * Created by Akshay Chavan on 09,May,2021
 * akshaychavan.kkwedu@gmail.com
 */
public class SubrowsDistrictAdapter extends RecyclerView.Adapter<SubrowsDistrictAdapter.DetailsViewHolder> {

    final String TAG = "AvailabilityDetailsRowAdapter";
    private ArrayList<SessionDistrict> mDetailsList;
    private Context mContext;

    public SubrowsDistrictAdapter(ArrayList<SessionDistrict> detailsList, Context context) {
        mDetailsList = detailsList;
        this.mContext = context;
    }


    @NonNull
    @Override
    public DetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.details_subrows, parent, false);
        DetailsViewHolder holdingsViewHolder = new DetailsViewHolder(v, mContext);
        return holdingsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DetailsViewHolder holder, int position) {
        SessionDistrict currentItem = mDetailsList.get(position);

        holder.tvVaccineName.setText(currentItem.getVaccine());
        holder.tvDate.setText(currentItem.getDate());
        holder.tvCount.setText(currentItem.getAvailableCapacity().toString());

    }

    @Override
    public int getItemCount() {
        return mDetailsList.size();
    }


    public static class DetailsViewHolder extends RecyclerView.ViewHolder {
        TextView tvCenterName, tvVaccineName, tvDate, tvCount, tvFeeType;
        // Availability Details Adapter
        RecyclerView sessionsRecylcer;
        RecyclerView.LayoutManager sessionsLayoutManager;
        RecyclerView.Adapter sessionsAdapter;
        /////////////////////////////////

        public DetailsViewHolder(@NonNull View itemView, Context mContext) {
            super(itemView);

            tvVaccineName = itemView.findViewById(R.id.tv_vaccine_name);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvCount = itemView.findViewById(R.id.tv_count);
        }
    }
}
