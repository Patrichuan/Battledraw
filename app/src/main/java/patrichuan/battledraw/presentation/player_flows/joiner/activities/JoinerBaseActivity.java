package patrichuan.battledraw.presentation.player_flows.joiner.activities;

import android.media.MediaPlayer;
import android.os.Bundle;

import patrichuan.battledraw.R;
import patrichuan.battledraw.presentation.BaseActivity;
import patrichuan.battledraw.presentation.player_flows.creator.fragments.WaitingPlayersFragment;
import patrichuan.battledraw.presentation.player_flows.joiner.fragments.DrawAvatarFragment;

/**
 * Created by Pat on 12/07/2016.
 */

public class JoinerBaseActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joiner_base);

        Bundle extras = getIntent().getExtras();

        DrawAvatarFragment drawAvatarFragment = new DrawAvatarFragment();
        drawAvatarFragment.setArguments(extras);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, DrawAvatarFragment.newInstance(extras.getString("ROOM_NAME")))
                .commit();
    }
}