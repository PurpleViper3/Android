package com.example.memo.bean;

import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.memo.R;
import com.example.memo.db.MyDbHelper;

import java.util.List;
import java.util.Random;

public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.ViewHolder> {
    private Context mccontext;
    private List<MemoBean> arr1;

    private MyDbHelper drop_helper;
    private SQLiteDatabase db;

    public MemoAdapter(Context mccontext, List<MemoBean> arr1) {
        this.mccontext = mccontext;
        this.arr1 = arr1;
    }

    @NonNull
    @Override
    public MemoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mccontext).inflate(R.layout.recy_item, parent, false);
        ViewHolder mholder = new ViewHolder(view);
        return mholder;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final MemoBean memoBean = arr1.get(position);
        holder.item_title.setText(memoBean.getTitle());
        holder.item_content.setText(memoBean.getContent());
        holder.item_time.setText(memoBean.getTime());
        Glide.with(mccontext).load(memoBean.getImgpath()).into(holder.item_img);

        Random random = new Random();
        int color = Color.argb(255, random.nextInt(), random.nextInt(), random.nextInt());

        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setCornerRadius(10f);
        gradientDrawable.setColor(color);
        holder.item_layout.setBackground(gradientDrawable);

        holder.item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = holder.getAdapterPosition();
                if (currentPosition == RecyclerView.NO_POSITION) return; // 避免无效位置的操作

                AlertDialog.Builder builder = new AlertDialog.Builder(mccontext);
                builder.setMessage("确认删除吗？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        drop_helper = new MyDbHelper(mccontext);
                        db = drop_helper.getWritableDatabase();

                        db.delete("tb_memory", "title=?", new String[]{arr1.get(currentPosition).getTitle()});
                        arr1.remove(currentPosition);
                        notifyItemRemoved(currentPosition);
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.setCancelable(false);
                builder.create().show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arr1.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView item_title, item_content, item_time;
        ImageView item_img;
        LinearLayout item_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_title = itemView.findViewById(R.id.item_title);
            item_content = itemView.findViewById(R.id.item_content);
            item_img = itemView.findViewById(R.id.item_image);
            item_time = itemView.findViewById(R.id.item_time);
            item_layout = itemView.findViewById(R.id.item_layout);

        }
    }
}
