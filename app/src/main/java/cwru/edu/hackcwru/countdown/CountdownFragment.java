package cwru.edu.hackcwru.countdown;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import cwru.edu.hackcwru.R;

public class CountdownFragment extends Fragment implements CountdownContract.View {
    private final String LOG_TAG = "Countdown Fragment";

    CountdownContract.Presenter presenter;

    @BindView(R.id.countdown_view)
    TextView countdownView;

    public CountdownFragment() {
        // Empty Constructor
    }

    public static CountdownFragment newInstance() {
        return new CountdownFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.countdown_fragment, container, false);
        ButterKnife.bind(this, rootView);

        displayCountdown();

        return rootView;
    }

    @Override
    public void setPresenter(CountdownContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void displayCountdown() {
        final CountDownTimer countDownTimer = new CountDownTimer(Long.MAX_VALUE, 1000) {
            @Override
            public void onTick(long l) {
                String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
                countdownView.setText(currentDateTimeString);
            }

            @Override
            public void onFinish() {

            }
        };
        countDownTimer.start();
    }
}
