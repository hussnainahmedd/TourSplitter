package com.example.toursplitter.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.toursplitter.R;
import com.example.toursplitter.data.FirebaseManager;
import com.example.toursplitter.model.Tour;
import com.example.toursplitter.model.Expense;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TourAdapter extends RecyclerView.Adapter<TourAdapter.TourViewHolder> {
    private List<Tour> tours;
    private OnTourClickListener listener;
    private FirebaseManager firebaseManager;
    private String currentUserId;

    public interface OnTourClickListener {
        void onTourClick(Tour tour);
    }

    public TourAdapter(List<Tour> tours, OnTourClickListener listener) {
        this.tours = tours;
        this.listener = listener;
        this.firebaseManager = FirebaseManager.getInstance();
        if (firebaseManager.getCurrentUser() != null) {
            this.currentUserId = firebaseManager.getCurrentUser().getUid();
        }
    }

    @NonNull
    @Override
    public TourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tour, parent, false);
        return new TourViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TourViewHolder holder, int position) {
        Tour tour = tours.get(position);
        holder.bind(tour, listener, firebaseManager, currentUserId);
    }

    @Override
    public int getItemCount() {
        return tours.size();
    }

    public void updateTours(List<Tour> newTours) {
        this.tours = newTours;
        notifyDataSetChanged();
    }

    static class TourViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTourName, tvTourType, tvTourDate;
        private TextView tvPaid, tvOwes, tvBalance, tvBalanceLabel;

        public TourViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTourName = itemView.findViewById(R.id.tvTourName);
            tvTourType = itemView.findViewById(R.id.tvTourType);
            tvTourDate = itemView.findViewById(R.id.tvTourDate);
            tvPaid = itemView.findViewById(R.id.tvPaid);
            tvOwes = itemView.findViewById(R.id.tvOwes);
            tvBalance = itemView.findViewById(R.id.tvBalance);
            tvBalanceLabel = itemView.findViewById(R.id.tvBalanceLabel);
        }

        public void bind(Tour tour, OnTourClickListener listener,
                         FirebaseManager firebaseManager, String currentUserId) {
            tvTourName.setText(tour.getName());
            tvTourType.setText(tour.getType().toUpperCase());

            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            tvTourDate.setText(sdf.format(new Date(tour.getCreatedAt())));

            // Load expenses and calculate balances
            loadExpensesForTour(tour.getTourId(), tour.getType(), firebaseManager, currentUserId);

            itemView.setOnClickListener(v -> listener.onTourClick(tour));
        }

        private void loadExpensesForTour(String tourId, String tourType,
                                         FirebaseManager firebaseManager, String currentUserId) {
            // Determine currency based on tour type
            String currency = tourType.equalsIgnoreCase("local") ? "PKR" : "$";

            // Set default values with currency
            tvPaid.setText(String.format(Locale.getDefault(), "Paid: %s 0.00", currency));
            tvOwes.setText(String.format(Locale.getDefault(), "Owes: %s 0.00", currency));
            tvBalance.setText(String.format(Locale.getDefault(), "%s 0.00", currency));
            tvBalanceLabel.setText("Settled");
            tvBalanceLabel.setTextColor(Color.parseColor("#9E9E9E")); // Gray
            tvBalance.setTextColor(Color.parseColor("#9E9E9E"));

            // Load expenses from Firestore
            firebaseManager.getDb().collection("expenses")
                    .whereEqualTo("tourId", tourId)
                    .get()
                    .addOnSuccessListener(querySnapshot -> {
                        double totalPaid = 0.0;
                        double totalOwed = 0.0;

                        // Calculate totals
                        for (QueryDocumentSnapshot doc : querySnapshot) {
                            Expense expense = doc.toObject(Expense.class);

                            // Calculate what current user paid
                            if (expense.getPaidBy() != null &&
                                    expense.getPaidBy().equals(currentUserId)) {
                                totalPaid += expense.getAmount();
                            }

                            // Calculate what current user owes
                            List<String> splitAmong = expense.getSplitAmong();
                            if (splitAmong != null && splitAmong.contains(currentUserId)) {
                                totalOwed += expense.getAmount() / splitAmong.size();
                            }
                        }

                        // Update UI with calculated values
                        tvPaid.setText(String.format(Locale.getDefault(),
                                "Paid: %s %.2f", currency, totalPaid));
                        tvOwes.setText(String.format(Locale.getDefault(),
                                "Owes: %s %.2f", currency, totalOwed));

                        double netBalance = totalPaid - totalOwed;
                        tvBalance.setText(String.format(Locale.getDefault(),
                                "%s %.2f", currency, Math.abs(netBalance)));

                        // Set color based on balance
                        if (netBalance > 0.01) {
                            tvBalanceLabel.setText("Should Receive");
                            tvBalanceLabel.setTextColor(Color.parseColor("#4CAF50")); // Green
                            tvBalance.setTextColor(Color.parseColor("#4CAF50"));
                        } else if (netBalance < -0.01) {
                            tvBalanceLabel.setText("Should Pay");
                            tvBalanceLabel.setTextColor(Color.parseColor("#F44336")); // Red
                            tvBalance.setTextColor(Color.parseColor("#F44336"));
                        } else {
                            tvBalanceLabel.setText("Settled");
                            tvBalanceLabel.setTextColor(Color.parseColor("#9E9E9E")); // Gray
                            tvBalance.setTextColor(Color.parseColor("#9E9E9E"));
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Keep default values on error
                        // Optionally log error: Log.e("TourAdapter", "Error loading expenses", e);
                    });
        }
    }
}