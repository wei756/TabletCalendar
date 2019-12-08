package com.wei756.tabletcalendar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CalendarAdapter extends RecyclerViewCustomAdapter {

    public final static int THEME_DEFAULT = 0;

    public final static int SUBTHEME_DEFAULT = 0;

    private RecyclerView mRecyclerView;

    int height = 800;

    /**
     * ItemView 정의
     */
    public class ItemViewHolder extends RecyclerView.ViewHolder {
        protected ConstraintLayout layout;
        protected TextView dateNum;
        protected View dateBadge;

        protected TextView holiday;


        public ItemViewHolder(View view) {
            super(view);
            this.layout = (ConstraintLayout) view.findViewById(R.id.cl_date);
            this.dateNum = (TextView) view.findViewById(R.id.tv_date);
            this.dateBadge = (View) view.findViewById(R.id.view_today);

            this.holiday = (TextView) view.findViewById(R.id.tv_holiday);
        }
    }

    /**
     * CalendarAdapter 정의
     *
     * @param list CalendarAdapter 가 표시할 Item 리스트
     * @param act  recyclerView 가 표시되는 액티비티
     */
    public CalendarAdapter(ArrayList<Item> list, Activity act, final RecyclerView mRecyclerView) {
        this.mList = list;
        this.context = act;
        this.mRecyclerView = mRecyclerView;

        Log.e("CalendarAdapter", "height: " + height);

        maxItemCount = 0;
        headerHasItem = false;
        scrollable = false;
        hasHeader = false;
        hasFooter = false;

        THEME_NUMBER = 1;
        SUBTHEME_NUMBER = 1;


        setItemLayout();
    }

    /**
     * layout 정의
     */
    protected void setItemLayout() {
        layoutHeader = new int[THEME_NUMBER * SUBTHEME_NUMBER];
        layoutItem = new int[THEME_NUMBER * SUBTHEME_NUMBER];
        layoutFooter = new int[THEME_NUMBER * SUBTHEME_NUMBER];

        layoutHeader[THEME_DEFAULT + SUBTHEME_DEFAULT] = 0; // dummy

        layoutItem[THEME_DEFAULT + SUBTHEME_DEFAULT] = R.layout.layout_date;

        layoutFooter[THEME_DEFAULT + SUBTHEME_DEFAULT] = 0; // dummy
    }

    /**
     * header layout 정의
     */
    @Override
    protected RecyclerView.ViewHolder createHeaderViewHolder(ViewGroup viewGroup) {
        return null;
    }

    /**
     * item layout 정의
     */
    @Override
    protected RecyclerView.ViewHolder createItemViewHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(layoutItem[TYPE_THEME + TYPE_SUBTHEME], viewGroup, false);

        return new ItemViewHolder(view);
    }

    /**
     * footer layout 정의
     */
    @Override
    protected RecyclerView.ViewHolder createFooterViewHolder(ViewGroup viewGroup) {
        return null;
    }

    /**
     * header content 정의
     */
    @Override
    protected void bindHeaderViewHolder(@NonNull RecyclerView.ViewHolder viewholder) {
    }

    /**
     * item content 정의
     */
    @Override
    protected void bindItemViewHolder(@NonNull RecyclerView.ViewHolder viewholder, final int pos) {
        ItemViewHolder itemViewHolder = (ItemViewHolder) viewholder;
        final Date date = (Date) mList.get(pos);

        // size
        ViewGroup.LayoutParams params = itemViewHolder.layout.getLayoutParams();
        params.height = (int) Math.round(((double) height) / Math.ceil((double) mList.size() / 7d));
        itemViewHolder.layout.setLayoutParams(params);

        // date
        String dateNum = "" + date.getDate();
        itemViewHolder.dateNum.setText(dateNum.equals("0") ? "" : dateNum);

        // color
        int day = pos % 7;
        if (day == 0)
            itemViewHolder.dateNum.setTextColor(context.getColor(R.color.colorWeekend));
        else if (day == 6)
            itemViewHolder.dateNum.setTextColor(context.getColor(R.color.colorSaturday));
        else
            itemViewHolder.dateNum.setTextColor(context.getColor(R.color.colorWeekday));

        // today icon
        if (date.isToday()) // today
            itemViewHolder.dateBadge.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorToday));
        else
            itemViewHolder.dateBadge.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorBackground));

        // 공휴일
        for (CalendarEvent event : date.getEvents()) {
            if (event.getType() == CalendarEvent.HOLIDAY) { // 해당 이벤트가 국경일일 때
                itemViewHolder.holiday.setText(event.getName());
            }
        }

    }

    /**
     * footer content 정의
     */
    @Override
    protected void bindFooterViewHolder(@NonNull RecyclerView.ViewHolder viewholder) {
    }

    /**
     * layout 업데이트
     */
    public void updateLayout(Activity act) {
        act.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }
}
