package com.msproject.myhome.mycalendar;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DailyFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DailyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DailyFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private OnFragmentInteractionListener mListener;
    LocalDate dateTime;
    FloatingActionButton fab;
    AddEventDialog dialog;
    ListView listView;
    Button prevButton;
    Button nextButton;
    TextView dateText;
    ListViewAdapter listViewAdapter;

    public DailyFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static DailyFragment newInstance() {
        DailyFragment fragment = new DailyFragment();
        return fragment;
    }

    public void setDateTime(LocalDate dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_daily, container, false);
        dateText = view.findViewById(R.id.date_text);
        ArrayList<Event> events = new ArrayList<>();
        dateText.setText(dateTime.getYear() + ". " + dateTime.getMonthOfYear() + ". " + dateTime.getDayOfMonth() + ".");

        fab = view.findViewById(R.id.dialog_floating_bt);
        prevButton = view.findViewById(R.id.prev_week_bt);
        nextButton = view.findViewById(R.id.next_week_bt);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new AddEventDialog();
                dialog.setLocalDate(dateTime);
                FragmentManager fragmentManager = ((AppCompatActivity) container.getContext()).getSupportFragmentManager();
                dialog.show(fragmentManager, "DIALOG");
            }
        });

        listView = view.findViewById(R.id.listview);

        listViewAdapter = new ListViewAdapter(getActivity().getApplicationContext(), findEvents());
        listView.setAdapter(listViewAdapter);

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateTime = dateTime.minusDays(1);
                listViewAdapter = new ListViewAdapter(getActivity().getApplicationContext(), findEvents());
                listView.setAdapter(listViewAdapter);
                listViewAdapter.notifyDataSetChanged();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateTime = dateTime.plusDays(1);
                listViewAdapter = new ListViewAdapter(getActivity().getApplicationContext(), findEvents());
                listView.setAdapter(listViewAdapter);
                listViewAdapter.notifyDataSetChanged();
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

    public ArrayList<Event> findEvents(){
        ArrayList<Event> events = new ArrayList<>();
        SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("EVENT", Context.MODE_PRIVATE);
        SimpleDateFormat format = new SimpleDateFormat("yy.MM.dd");
        String key = format.format(dateTime.toDate());
        String todayEvents = sharedPreferences.getString(key, "");

        String[] parsed = todayEvents.split(" ");

        for(int i = 0; i < parsed.length; i++){
            if(!parsed[i].equals("")){
                events.add(new Event(parsed[i], dateTime.now().toDateTimeAtCurrentTime()));
            }
        }
        dateText.setText(dateTime.getYear() + ". " + dateTime.getMonthOfYear() + ". " + dateTime.getDayOfMonth() + ".");

        return events;
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
        void onDailyFragmentInteraction();
    }

    public class ListViewAdapter extends BaseAdapter {
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
