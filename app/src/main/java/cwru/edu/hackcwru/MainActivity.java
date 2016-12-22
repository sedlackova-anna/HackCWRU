package cwru.edu.hackcwru;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cwru.edu.hackcwru.utils.FragmentUtils;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.main_toolbar)
    Toolbar mainToolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.drawer_list)
    ListView drawerList;
    private ActionBarDrawerToggle drawerToggle;

    @BindView(R.id.event_list)
    RecyclerView eventList;
    private EventListAdapter eventListAdapter;
    private RecyclerView.LayoutManager eventLayoutManager;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mainToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // Setup menu navigation with toolbar
        this.drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, mainToolbar, R.string.app_name, R.string.app_name);
        this.mainToolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        this.drawerLayout.addDrawerListener(drawerToggle);
        this.drawerList.setBackgroundResource(R.color.white);

        // Creating custom events
        Event e1 = new Event("Welcome", "Friday 5:00PM - 7:00PM", "Getting unpacked and stuff");
        Event e2 = new Event("Start Hacking", "Friday 7:00PM - ", "Take those computers out and start writing some code!");
        Event e3 = new Event("Ending Ceremony", "Sunday 3:00PM", "Presentation and Awards");
        Event[] events = new Event[15];
        events[0] = e1;
        events[1] = e2;
        for (int i = 2; i < events.length - 1; i++) {
            events[i] = new Event("Cool stuff", "Day time_from - time_to", "Blah blah blah");
        }
        events[14] = e3;

        // End of custom events

        eventList.setHasFixedSize(true);

        eventLayoutManager = new LinearLayoutManager(this);
        eventList.setLayoutManager(eventLayoutManager);

        eventListAdapter = new EventListAdapter(this, events);
        eventList.setAdapter(eventListAdapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_overflow:

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        FragmentUtils.closeEventDetailFragment(this);
    }
}

class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {
    private Event[] events;
    private MainActivity activity;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView eventName;
        public TextView eventTime;
        public View item;

        public ViewHolder(View v) {
            super(v);
            item = v.findViewById(R.id.event_list_item);
            eventName = (TextView) v.findViewById(R.id.event_name);
            eventTime = (TextView) v.findViewById(R.id.event_time);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public EventListAdapter(MainActivity activity, Event[] events) {
        this.events = events;
        this.activity = activity;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public EventListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.eventName.setText(events[position].getEventName());
        holder.eventTime.setText(events[position].getEventTime());
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentUtils.showEventDetailFragment(activity, events[position]);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return events.length;
    }
}

