package com.example.toursplitter.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.toursplitter.R;
import com.example.toursplitter.model.Balance;
import java.util.List;
import java.util.Locale;

public class BalanceAdapter extends RecyclerView.Adapter<BalanceAdapter.BalanceViewHolder> {
    private List<Balance> balances;
    private String currency;

    public BalanceAdapter(List<Balance> balances, String currency) {
        this.balances = balances;
        this.currency = currency;
    }

    @NonNull
    @Override
    public BalanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_balance, parent, false);
        return new BalanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BalanceViewHolder holder, int position) {
        Balance balance = balances.get(position);
        holder.bind(balance, currency);
    }

    @Override
    public int getItemCount() {
        return balances.size();
    }

    public void updateBalances(List<Balance> newBalances) {
        this.balances = newBalances;
        notifyDataSetChanged();
    }

    static class BalanceViewHolder extends RecyclerView.ViewHolder {
        private TextView tvMemberName, tvPaid, tvOwed, tvBalance, tvStatus;

        public BalanceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMemberName = itemView.findViewById(R.id.tvMemberName);
            tvPaid = itemView.findViewById(R.id.tvPaid);
            tvOwed = itemView.findViewById(R.id.tvOwed);
            tvBalance = itemView.findViewById(R.id.tvBalance);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }

        public void bind(Balance balance, String currency) {
            tvMemberName.setText(balance.getMemberName());
            tvPaid.setText(String.format(Locale.getDefault(),
                    "Paid: %s %.2f", currency, balance.getTotalPaid()));
            tvOwed.setText(String.format(Locale.getDefault(),
                    "Owes: %s %.2f", currency, balance.getTotalOwed()));

            double netBalance = balance.getNetBalance();
            tvBalance.setText(String.format(Locale.getDefault(),
                    "%s %.2f", currency, Math.abs(netBalance)));

            if (netBalance > 0.01) {
                tvStatus.setText("Should Receive");
                tvStatus.setTextColor(Color.parseColor("#4CAF50")); // Green
                tvBalance.setTextColor(Color.parseColor("#4CAF50"));
            } else if (netBalance < -0.01) {
                tvStatus.setText("Should Pay");
                tvStatus.setTextColor(Color.parseColor("#F44336")); // Red
                tvBalance.setTextColor(Color.parseColor("#F44336"));
            } else {
                tvStatus.setText("Settled");
                tvStatus.setTextColor(Color.parseColor("#9E9E9E")); // Gray
                tvBalance.setTextColor(Color.parseColor("#9E9E9E"));
            }
        }
    }
}