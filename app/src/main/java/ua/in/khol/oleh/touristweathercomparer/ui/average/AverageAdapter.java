package ua.in.khol.oleh.touristweathercomparer.ui.average;

import static ua.in.khol.oleh.touristweathercomparer.Globals.MILLIS_IN_SECOND;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ua.in.khol.oleh.touristweathercomparer.data.entity.Daily;
import ua.in.khol.oleh.touristweathercomparer.data.entity.Hourly;
import ua.in.khol.oleh.touristweathercomparer.ui.widgets.DailyWidget;
import ua.in.khol.oleh.touristweathercomparer.ui.widgets.HourlyWidget;

public class AverageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int ITEM_TYPE_HOURLY = 0;
    private final int ITEM_TYPE_DAILY = 1;
    private final List<Object> items;

    public AverageAdapter() {
        items = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_HOURLY) {
            HourlyWidget hourlyWidget = new HourlyWidget(parent.getContext());
            hourlyWidget.setLayoutParams(new RecyclerView.LayoutParams(
                    RecyclerView.LayoutParams.MATCH_PARENT,
                    RecyclerView.LayoutParams.WRAP_CONTENT));
            return new HourlyHolder(hourlyWidget);
        } else {
            DailyWidget dailyWidget = new DailyWidget(parent.getContext());

            // Set the LayoutParams to match_parent width and wrap_content height
            dailyWidget.setLayoutParams(new RecyclerView.LayoutParams(
                    RecyclerView.LayoutParams.MATCH_PARENT,
                    RecyclerView.LayoutParams.WRAP_CONTENT));

            return new DailyHolder(dailyWidget);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HourlyHolder) {
            ((HourlyHolder) holder).bind((Hourly) items.get(position));
        } else if (holder instanceof DailyHolder) {
            ((DailyHolder) holder).bind((Daily) items.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position) instanceof Hourly ? ITEM_TYPE_HOURLY : ITEM_TYPE_DAILY;
    }

    static class HourlyHolder extends RecyclerView.ViewHolder {


        public HourlyHolder(@NonNull HourlyWidget widget) {
            super(widget);
        }

        public void bind(Hourly hourly) {
            ((HourlyWidget) itemView).setHourly(hourly);
        }
    }

    static class DailyHolder extends RecyclerView.ViewHolder {


        public DailyHolder(@NonNull DailyWidget widget) {
            super(widget);
        }

        public void bind(Daily daily) {
            ((DailyWidget) itemView).setDaily(daily);
        }
    }

    public void setHourlies(List<Hourly> hourlies) {
        List<Daily> dailies = splitHourliesPerDay(hourlies);

        items.clear();
        items.addAll(dailies);
        notifyDataSetChanged();
    }

    private List<Daily> splitHourliesPerDay(List<Hourly> hourlies) {
        LinkedHashMap<Integer, List<Hourly>> groupedHourlies = groupHourliesByDate(hourlies);

        List<Daily> dailies = new ArrayList<>();

        for (Map.Entry<Integer, List<Hourly>> entry : groupedHourlies.entrySet()) {
            dailies.add(calculateDaily(entry.getKey(), entry.getValue()));
        }

        return dailies;
    }

    private LinkedHashMap<Integer, List<Hourly>> groupHourliesByDate(List<Hourly> hourlyData) {
        LinkedHashMap<Integer, List<Hourly>> groupedHourlyData = new LinkedHashMap<>();

        for (Hourly hourly : hourlyData) {
            int startOfDayTimestamp = getStartOfDayTimestamp(hourly.date);

            List<Hourly> dailyHourlyData = groupedHourlyData
                    .computeIfAbsent(startOfDayTimestamp, k -> new ArrayList<>());
            dailyHourlyData.add(hourly);
        }

        return groupedHourlyData;
    }

    private int getStartOfDayTimestamp(int timestampInSeconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestampInSeconds * MILLIS_IN_SECOND);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return (int) (calendar.getTimeInMillis() / MILLIS_IN_SECOND);
    }

    private Daily calculateDaily(int date, List<Hourly> hourlies) {

        Daily daily = new Daily();
        daily.date = date;
        daily.language = hourlies.get(0).language;
        daily.units = hourlies.get(0).units;
        daily.tempMin = hourlies.get(0).tempMin;
        daily.tempMax = hourlies.get(0).tempMax;

        for (Hourly hourly : hourlies) {
            daily.tempMin = Math.min(daily.tempMin, hourly.tempMin);
            daily.tempMax = Math.max(daily.tempMax, hourly.tempMax);
            daily.pressure += hourly.pressure;
            daily.speed += hourly.speed;
            daily.degree += hourly.degree;
            daily.humidity += hourly.humidity;
        }

        int count = hourlies.size();
        daily.pressure /= count;
        daily.speed /= count;
        daily.degree /= count;
        daily.humidity /= count;

        return daily;
    }
}
