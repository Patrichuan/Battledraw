package patrichuan.battledraw.fragments;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daasuu.ei.Ease;
import com.daasuu.ei.EasingInterpolator;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import patrichuan.battledraw.Constants;
import patrichuan.battledraw.R;
import patrichuan.battledraw.activities.waitingplayers.NewWaitingPlayersActivity;


public class WaitingPlayersFragment extends Fragment {

    private NewWaitingPlayersActivity activity;
    private LayoutInflater inflater;

    private String roomName;
    private TextView tv_roomname;

    private ProgressBar  progressBar1, progressBar2, progressBar3, progressBar4, progressBar5,
            progressBar6, progressBar7, progressBar8;

    private ImageView iv_avatar_player1, iv_avatar_player2, iv_avatar_player3, iv_avatar_player4,
            iv_avatar_player5, iv_avatar_player6, iv_avatar_player7, iv_avatar_player8;

    private TextView tv_avatar_player1, tv_avatar_player2, tv_avatar_player3, tv_avatar_player4,
            tv_avatar_player5, tv_avatar_player6, tv_avatar_player7, tv_avatar_player8;

    public WaitingPlayersFragment() {

    }


    public static WaitingPlayersFragment newInstance(String roomName) {
        WaitingPlayersFragment fragment = new WaitingPlayersFragment();
        Bundle args = new Bundle();
        args.putString(Constants.ARG_ROOM, roomName);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        View rootView = inflater.inflate(R.layout.fragment_waiting_players, container, false);
        instanceViews(rootView);
        return rootView;
    }


    private void instanceViews(View rootView) {
        // ROOM
        tv_roomname = (TextView) rootView.findViewById(R.id.tv_roomname);

        // PROGRESSBARDS
        progressBar1 = (ProgressBar) rootView.findViewById(R.id.progressBar1);
        progressBar2 = (ProgressBar) rootView.findViewById(R.id.progressBar2);
        progressBar3 = (ProgressBar) rootView.findViewById(R.id.progressBar3);
        progressBar4 = (ProgressBar) rootView.findViewById(R.id.progressBar4);
        progressBar5 = (ProgressBar) rootView.findViewById(R.id.progressBar5);
        progressBar6 = (ProgressBar) rootView.findViewById(R.id.progressBar6);
        progressBar7 = (ProgressBar) rootView.findViewById(R.id.progressBar7);
        progressBar8 = (ProgressBar) rootView.findViewById(R.id.progressBar8);

        // AVATARS
        iv_avatar_player1 = (ImageView) rootView.findViewById(R.id.iv_avatar_player1);
        tv_avatar_player1 = (TextView) rootView.findViewById(R.id.tv_avatar_player1);
        iv_avatar_player2 = (ImageView) rootView.findViewById(R.id.iv_avatar_player2);
        tv_avatar_player2 = (TextView) rootView.findViewById(R.id.tv_avatar_player2);
        iv_avatar_player3 = (ImageView) rootView.findViewById(R.id.iv_avatar_player3);
        tv_avatar_player3 = (TextView) rootView.findViewById(R.id.tv_avatar_player3);
        iv_avatar_player4 = (ImageView) rootView.findViewById(R.id.iv_avatar_player4);
        tv_avatar_player4 = (TextView) rootView.findViewById(R.id.tv_avatar_player4);
        iv_avatar_player5 = (ImageView) rootView.findViewById(R.id.iv_avatar_player5);
        tv_avatar_player5 = (TextView) rootView.findViewById(R.id.tv_avatar_player5);
        iv_avatar_player6 = (ImageView) rootView.findViewById(R.id.iv_avatar_player6);
        tv_avatar_player6 = (TextView) rootView.findViewById(R.id.tv_avatar_player6);
        iv_avatar_player7 = (ImageView) rootView.findViewById(R.id.iv_avatar_player7);
        tv_avatar_player7 = (TextView) rootView.findViewById(R.id.tv_avatar_player7);
        iv_avatar_player8 = (ImageView) rootView.findViewById(R.id.iv_avatar_player8);
        tv_avatar_player8 = (TextView) rootView.findViewById(R.id.tv_avatar_player8);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        roomName = getArguments().getString(Constants.ARG_ROOM);

        tv_roomname.setText(roomName);
        activity.getDatabaseReference().child("rooms").child(roomName).child("players").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final String playerValue = dataSnapshot.getValue().toString();
                final String playerKey = dataSnapshot.getKey();

                // A player has joined (standard avatar)
                if (playerValue.equals(Constants.PLAYER_TYPE_JOINER)) {
                    activity.getDatabaseReference().child("players").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final int numPlayersInside = (int) dataSnapshot.getChildrenCount();
                            activity.getDatabaseReference().child("players").child(playerKey).child("picture").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String pictureUri = dataSnapshot.getValue().toString();
                                    setAvatar(numPlayersInside-1, pictureUri);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Map<String, String> players = (Map<String, String>) dataSnapshot.getValue();

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Map<String, String> players = (Map<String, String>) dataSnapshot.getValue();
                // FIXME Implementar "Leave Room" option
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Map<String, String> players = (Map<String, String>) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setAvatar(final int numPlayers, String pictureUri) {
        switch (numPlayers) {
            case 1:
                if (pictureUri.equals("no_picture")) {
                    activity.doNewPlayerHasJoined();
                    progressBar1.setVisibility(View.VISIBLE);
                } else {
                    activity.doOMGthisAvatarSong();
                    progressBar1.setVisibility(View.INVISIBLE);
                    Glide.with(this).load(pictureUri).into(iv_avatar_player1);
                    tv_avatar_player1.setText("Player 1");
                }
                break;
            case 2:
                if (pictureUri.equals("no_picture")) {
                    activity.doNewPlayerHasJoined();
                    progressBar2.setVisibility(View.VISIBLE);
                } else {
                    activity.doOMGthisAvatarSong();
                    progressBar2.setVisibility(View.INVISIBLE);
                    Glide.with(this).load(pictureUri).into(iv_avatar_player2);
                    tv_avatar_player2.setText("Player 2");
                }
                break;
            case 3:
                if (pictureUri.equals("no_picture")) {
                    activity.doNewPlayerHasJoined();
                    progressBar3.setVisibility(View.VISIBLE);
                } else {
                    activity.doOMGthisAvatarSong();
                    progressBar3.setVisibility(View.INVISIBLE);
                    Glide.with(this).load(pictureUri).into(iv_avatar_player3);
                    tv_avatar_player3.setText("Player 3");
                }
                break;
            case 4:
                if (pictureUri.equals("no_picture")) {
                    activity.doNewPlayerHasJoined();
                    progressBar4.setVisibility(View.VISIBLE);
                } else {
                    activity.doOMGthisAvatarSong();
                    progressBar4.setVisibility(View.INVISIBLE);
                    Glide.with(this).load(pictureUri).into(iv_avatar_player4);
                    tv_avatar_player4.setText("Player 4");
                }
                break;
            case 5:
                if (pictureUri.equals("no_picture")) {
                    activity.doNewPlayerHasJoined();
                    progressBar5.setVisibility(View.VISIBLE);
                } else {
                    activity.doOMGthisAvatarSong();
                    progressBar5.setVisibility(View.INVISIBLE);
                    Glide.with(this).load(pictureUri).into(iv_avatar_player5);
                    tv_avatar_player5.setText("Player 5");
                }
                break;
            case 6:
                if (pictureUri.equals("no_picture")) {
                    activity.doNewPlayerHasJoined();
                    progressBar6.setVisibility(View.VISIBLE);
                } else {
                    activity.doOMGthisAvatarSong();
                    progressBar6.setVisibility(View.INVISIBLE);
                    Glide.with(this).load(pictureUri).into(iv_avatar_player6);
                    tv_avatar_player6.setText("Player 6");
                }
                break;
            case 7:
                if (pictureUri.equals("no_picture")) {
                    activity.doNewPlayerHasJoined();
                    progressBar7.setVisibility(View.VISIBLE);
                } else {
                    activity.doOMGthisAvatarSong();
                    progressBar7.setVisibility(View.INVISIBLE);
                    Glide.with(this).load(pictureUri).into(iv_avatar_player7);
                    tv_avatar_player7.setText("Player 7");
                }
                break;
            case 8:
                if (pictureUri.equals("no_picture")) {
                    activity.doNewPlayerHasJoined();
                    progressBar8.setVisibility(View.VISIBLE);
                } else {
                    activity.doOMGthisAvatarSong();
                    progressBar8.setVisibility(View.INVISIBLE);
                    Glide.with(this).load(pictureUri).into(iv_avatar_player8);
                    tv_avatar_player8.setText("Player 8");
                }
                break;
        }
    }




    // onAttach para apis mayores y menores de la 23 -----------------------------------------------
    @TargetApi(23)
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onAttachToContext(context);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        onAttachToContext(activity);
    }

    private void onAttachToContext(Context context) {
        if (context instanceof Activity){
            activity = (NewWaitingPlayersActivity) context;
        }
    }
}