package com.msproject.myhome.mycalendar;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.zip.Inflater;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WeeklyFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WeeklyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeeklyFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private OnFragmentInteractionListener mListener;
    GridView gridView;
    ListView listView;
    CalendarGridViewAdapter calendarGridViewAdapter;
    TextView centerText;
    DateTime dateTime;
    ListViewAdapter listViewAdapter;
    ArrayList<Event> events;

    public WeeklyFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static WeeklyFragment newInstance() {
        WeeklyFragment fragment = new WeeklyFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weekly, container, false);
        gridView = view.findViewById(R.id.grid_view);
        listView = view.findViewById(R.id.list_view);
        Button prevButton = view.findViewById(R.id.prev_week_bt);
        Button nextButton = view.findViewById(R.id.next_week_bt);
        dateTime = new DateTime();
        calendarGridViewAdapter = new CalendarGridViewAdapter(getActivity().getApplicationContext(),dateTime);
        centerText = view.findViewById(R.id.center_text);
        gridView.setAdapter(calendarGridViewAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DateTime dateTime = calendarGridViewAdapter.dateTime;
                dateTime = dateTime.minusDays((dateTime.getDayOfWeek() % 7)).plusDays(position);
                MainActivity.changeFragmentDaily(dateTime.toLocalDate());
            }
        });
        gridView.setLayoutParams(new LinearLayout.LayoutParams(container.getWidth(), container.getHeight() / 7));

        centerText.setText(dateTime.getYear() + ". " + dateTime.getMonthOfYear());
        events = new ArrayList<>();

        listViewAdapter = new ListViewAdapter(getActivity().getApplicationContext(), findEvents());
        listView.setAdapter(listViewAdapter);

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateTime = dateTime.minusWeeks(1);
                calendarGridViewAdapter = new CalendarGridViewAdapter(getActivity().getApplicationContext(), dateTime);
                gridView.setAdapter(calendarGridViewAdapter);
                calendarGridViewAdapter.notifyDataSetChanged();
                listViewAdapter = new ListViewAdapter(getActivity().getApplicationContext(), findEvents());
                listView.setAdapter(listViewAdapter);
                listViewAdapter.notifyDataSetChanged();
                centerText.setText(dateTime.getYear() + ". " + dateTime.getMonthOfYear());
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateTime = dateTime.plusWeeks(1);
                calendarGridViewAdapter = new CalendarGridViewAdapter(getActivity().getApplicationContext(), dateTime);
                gridView.setAdapter(calendarGridViewAdapter);
                calendarGridViewAdapter.notifyDataSetChanged();
                listViewAdapter = new ListViewAdapter(getActivity().getApplicationContext(), findEvents());
                listView.setAdapter(listViewAdapter);
                listViewAdapter.notifyDataSetChanged();
                centerText.setText(dateTime.getYear() + ". " + dateTime.getMonthOfYear());
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onWeeklyFragmentInteraction();
    }

    public ArrayList<Event> findEvents(){
        ArrayList<Event> events = new ArrayList<>();
        DateTime firstDay = dateTime.minusDays(dateTime.getDayOfWeek() % 7);
        SimpleDateFormat format = new SimpleDateFormat("yy.MM.dd");
        for(int i = 0; i < 7; i++){;
            SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("EVENT", Context.MODE_PRIVATE);

            String eventString = sharedPreferences.getString(format.format(firstDay.toDate()),"");
            String[] parsed = eventString.split(" ");
            for(int j = 0; j < parsed.length; j++){
                if(!parsed[j].equals("")){
                    events.add(new Event(parsed[j], firstDay));
                }
            }
            Log.d("key and value==", format.format(firstDay.toDate()) + " " + eventString);
            firstDay = firstDay.plusDays(1);
        }
        return events;
    }

    public class CalendarGridViewAdapter extends BaseAdapter{
        DateTime dateTime;
        Context context;
        LayoutInflater inflater;
        public CalendarGridViewAdapter(Context context, DateTime dateTime){
            this.dateTime = dateTime;
            this.context = context;
            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }
        @Override
        public int getCount() {
            return 7;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = inflater.inflate(R.layout.planner_item, parent, false);
            View containerView = inflater.inflate(R.layout.fragment_weekly, parent, false);

            view.setLayoutParams(new LinearLayout.LayoutParams(parent.getWidth() / 7, parent.getHeight()));
            TextView textView = view.findViewById(R.id.planner_item_day);
            TextView centerText = containerView.findViewById(R.id.center_text);
            int today = dateTime.getDayOfMonth() - (dateTime.getDayOfWeek() % 7) + position;

            if(dateTime.getDayOfWeek() == position){
                textView.setBackgroundResource(R.drawable.ic_highlight_24dp);
                centerText.setText(dateTime.getMonthOfYear() + "월 " + dateTime.getDayOfMonth() + "일");
            }
            if(today < 1){
                today = dateTime.minusMonths(1).dayOfMonth().withMaximumValue().getDayOfMonth() + today;
            }
            else if(today > dateTime.dayOfMonth().withMaximumValue().getDayOfMonth()){
                today = today - dateTime.dayOfMonth().withMaximumValue().getDayOfMonth();
            }
            textView.setText(String.valueOf(today));
            return view;
        }
    }

    public class ListViewAdapter extends BaseAdapter{
        ArrayList<Event> events;
        Context context;
        LayoutInflater inflater;

        public ListViewAdapter(Context context, ArrayList events){
            this.events = events;
            this.context = context;
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public int getCount() {
            return events.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = inflater.inflate(R.layout.event_item, parent, false);
            Event event = events.get(position);

            TextView textView = view.findViewById(R.id.event_name);
            textView.setText(event.getName());
            TextView dateText = view.findViewById(R.id.date_text);

            SimpleDateFormat format = new SimpleDateFormat("yy.MM.dd");
            String key = format.format(event.getDate().toDate());
            dateText.setText(key);
            return view;
        }
    }
}
