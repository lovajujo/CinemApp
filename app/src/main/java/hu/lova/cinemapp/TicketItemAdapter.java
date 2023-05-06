package hu.lova.cinemapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import com.bumptech.glide.Glide;

public class TicketItemAdapter extends RecyclerView.Adapter<TicketItemAdapter.ViewHolder> implements Filterable {
    private ArrayList<TicketItem> ticketItemsData;
    private ArrayList<TicketItem> ticketItemsAll;
    private Context context;
    private int lastPostion=-1;

    public TicketItemAdapter(Context context,ArrayList<TicketItem> ticketItemsData) {
        this.ticketItemsData = ticketItemsData;
        this.context = context;
        this.ticketItemsAll = ticketItemsData;
    }

    @NonNull
    @Override
    public TicketItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TicketItemAdapter.ViewHolder holder, int position){
        TicketItem currentItem= ticketItemsData.get(position);
        holder.bindTo(currentItem);
        if(holder.getAbsoluteAdapterPosition()>lastPostion){
            Animation animation= AnimationUtils.loadAnimation(context, R.anim.slidein);
            holder.itemView.startAnimation(animation);
            lastPostion=holder.getAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return ticketItemsData.size();
    }

    @Override
    public Filter getFilter() {
        return productFilter;
    }
    private Filter productFilter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            //mikor beírunk a keresőbe valamit, hogyan történjen szűrés
            //elemek, amik a szűrésnek eleget fognak tenni
            ArrayList<TicketItem> filteredList=new ArrayList<>();
            FilterResults results=new FilterResults();
            //nem írtunk be semmit vagy nem akarunk semmire szűrni
            if(charSequence==null || charSequence.length()==0){
                results.count= ticketItemsAll.size();
                results.values= ticketItemsAll;
            }else{
                String filterPattern=charSequence.toString().toLowerCase().trim();
                for(TicketItem item: ticketItemsAll){
                    if(item.getTitle().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
                results.count=filteredList.size();
                results.values=filteredList;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            //a szűrés eredménye hogyan kerüljön visszaadásra
            ticketItemsData =(ArrayList)filterResults.values;
            //recyclerview értesítése, ha adatmódosítás történt
            notifyDataSetChanged();

        }
    };

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView nameText;
        private TextView dateText;
        private TextView priceText;
        private ImageView itemImage;
        private RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            nameText=itemView.findViewById(R.id.title);
            dateText=itemView.findViewById(R.id.date);
            priceText=itemView.findViewById(R.id.price);
            itemImage=itemView.findViewById(R.id.image);
            ratingBar=itemView.findViewById(R.id.rating);

        }

        public void bindTo(TicketItem currentItem) {
            nameText.setText(currentItem.getTitle());
            dateText.setText(currentItem.getDate());
            priceText.setText(currentItem.getPrice());
            ratingBar.setRating(currentItem.getRating());

            Glide.with(context).load(currentItem.getImageRes()).into(itemImage);
            itemView.findViewById(R.id.cart).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((TicketActivity) context).updateAlertIcon(currentItem);
                    Animation animscale= AnimationUtils.loadAnimation(context,R.anim.scale);
                    view.startAnimation(animscale);
                }
            });
            itemView.findViewById(R.id.delete).setOnClickListener(view -> ((TicketActivity) context).deleteItem(currentItem));
        }

    }
}
