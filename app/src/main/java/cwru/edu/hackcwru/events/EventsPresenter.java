package cwru.edu.hackcwru.events;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import cwru.edu.hackcwru.data.Event;
import cwru.edu.hackcwru.data.EventsList;
import cwru.edu.hackcwru.eventdetail.EventDetailContract;
import cwru.edu.hackcwru.server.HackCWRUServerCalls;
import cwru.edu.hackcwru.utils.Log;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EventsPresenter implements EventsContract.Presenter, EventDetailContract.Presenter {
    private final String LOG_TAG = "EventsPresenter";

    EventsContract.View eventsView;

    EventDetailContract.View eventDetailView;

    HackCWRUServerCalls hackCWRUServerCalls;

    List<Event> allEvents;
    List<Event> savedEvents;

    public EventsPresenter(@NonNull EventsContract.View eventsView, @NonNull EventDetailContract.View eventDetailView) {
        this.eventsView = eventsView;
        this.eventDetailView = eventDetailView;
        eventsView.setPresenter(this);
        eventDetailView.setPresenter(this);

        initializeRetrofit();

        savedEvents = new ArrayList<>();
    }

    private void initializeRetrofit(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HackCWRUServerCalls.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        hackCWRUServerCalls = retrofit.create(HackCWRUServerCalls.class);
    }

    @Override
    public void start() {
        loadEvents();
    }

    @Override
    public void openEventDetails(@NonNull Event event) {
        eventDetailView.populateEvent(event);
    }

    @Override
    public void saveEvent(@NonNull Event event) {
        if(event.isSaved()){
            event.setSaved(false);
            savedEvents.remove(event);
        }
        else{
            event.setSaved(true);
            savedEvents.add(event);
        }
    }

    @Override
    public void loadEvents() {
        Call<EventsList> loadEventsCall = hackCWRUServerCalls.getEventsFromServer();
        loadEventsCall.enqueue(new Callback<EventsList>() {
            @Override
            public void onResponse(Call<EventsList> call, Response<EventsList> response) {
                // TODO: Perform data check with server and current
                EventsList eventsResponse = response.body();
                Log.d(LOG_TAG, eventsResponse.toString());
                allEvents = eventsResponse.getEvents();
                showAllEvents();
            }

            @Override
            public void onFailure(Call<EventsList> call, Throwable t) {
                Log.e(LOG_TAG, t.toString());
            }
        });
    }

    @Override
    public void showSavedEvents() {
        processEvents(savedEvents);
    }

    @Override
    public void showAllEvents() {
        processEvents(allEvents);
    }

    private void processEvents(List<Event> events) {
        eventsView.showEvents(events);
        eventsView.onRefreshFinish();
    }
}
