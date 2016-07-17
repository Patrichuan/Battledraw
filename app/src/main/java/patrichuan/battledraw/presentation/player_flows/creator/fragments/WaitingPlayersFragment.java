package patrichuan.battledraw.presentation.player_flows.creator.fragments;

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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.Map;

import patrichuan.battledraw.presentation.player_flows.creator.activities.CreatorBaseActivity;
import patrichuan.battledraw.util.Constants;
import patrichuan.battledraw.R;


public class WaitingPlayersFragment extends Fragment {

    private CreatorBaseActivity activity;
    private TextView tv_roomname;

    private AVLoadingIndicatorView progressBar1, progressBar2, progressBar3, progressBar4, progressBar5,
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
        View rootView = inflater.inflate(R.layout.fragment_waiting_players, container, false);
        instanceViews(rootView);
        return rootView;
    }


    private void instanceViews(View rootView) {
        // ROOM
        tv_roomname = (TextView) rootView.findViewById(R.id.tv_roomname);

        // PROGRESSBARS
        progressBar1 = (AVLoadingIndicatorView) rootView.findViewById(R.id.progressBar1);
        progressBar2 = (AVLoadingIndicatorView) rootView.findViewById(R.id.progressBar2);
        progressBar3 = (AVLoadingIndicatorView) rootView.findViewById(R.id.progressBar3);
        progressBar4 = (AVLoadingIndicatorView) rootView.findViewById(R.id.progressBar4);
        progressBar5 = (AVLoadingIndicatorView) rootView.findViewById(R.id.progressBar5);
        progressBar6 = (AVLoadingIndicatorView) rootView.findViewById(R.id.progressBar6);
        progressBar7 = (AVLoadingIndicatorView) rootView.findViewById(R.id.progressBar7);
        progressBar8 = (AVLoadingIndicatorView) rootView.findViewById(R.id.progressBar8);

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
        String roomName = getArguments().getString(Constants.ARG_ROOM);
        if (roomName!=null) {
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
                                activity.addJoiner(playerKey);
                                activity.getDatabaseReference().child("players").child(playerKey).child("picture").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        String pictureUri = dataSnapshot.getValue().toString();
                                        setAvatar(activity.getNumJoiners(), pictureUri);
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
                    final String playerValue = dataSnapshot.getValue().toString();
                    final String playerKey = dataSnapshot.getKey();

                    // A player has joined (standard avatar)
                    if (playerValue.equals(Constants.PLAYER_TYPE_JOINER)) {
                        activity.getDatabaseReference().child("players").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                activity.getDatabaseReference().child("players").child(playerKey).child("picture").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        String pictureUri = dataSnapshot.getValue().toString();
                                        setAvatar(activity.getNumJoiners(), pictureUri);
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
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    final String playerValue = dataSnapshot.getValue().toString();
                    final String playerKey = dataSnapshot.getKey();

                    // A player has joined (standard avatar)
                    if (playerValue.equals(Constants.PLAYER_TYPE_JOINER)) {
                        activity.getDatabaseReference().child("players").child(playerKey).removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                // FIXME Implementar "Leave Room" option
                                setAvatar(activity.getJoinerPos(playerKey), null);
                                activity.removeJoiner(playerKey);
                            }
                        });
                    }
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                    final String playerValue = dataSnapshot.getValue().toString();
                    final String playerKey = dataSnapshot.getKey();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void setAvatar(final int numPlayer, String pictureUri) {
        String playerNumber = getString(R.string.player).concat(String.valueOf(numPlayer));
        switch (numPlayer) {
            case 1:
                if (pictureUri==null) {
                    progressBar1.setVisibility(View.INVISIBLE);
                    iv_avatar_player1.setImageDrawable(null);
                    tv_avatar_player1.setText("");
                } else if (pictureUri.equals("no_picture")) {
                    activity.doNewPlayerHasJoined();
                    progressBar1.setVisibility(View.VISIBLE);
                } else {
                    activity.doOMGthisAvatarSong();
                    progressBar1.setVisibility(View.INVISIBLE);
                    Glide.with(this).load(pictureUri).into(iv_avatar_player1);
                    tv_avatar_player1.setText(playerNumber);
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
                    tv_avatar_player2.setText(playerNumber);
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
                    tv_avatar_player3.setText(playerNumber);
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
                    tv_avatar_player4.setText(playerNumber);
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
                    tv_avatar_player5.setText(playerNumber);
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
                    tv_avatar_player6.setText(playerNumber);
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
                    tv_avatar_player7.setText(playerNumber);
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
                    tv_avatar_player8.setText(playerNumber);
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
            activity = (CreatorBaseActivity) context;
        }
    }
}