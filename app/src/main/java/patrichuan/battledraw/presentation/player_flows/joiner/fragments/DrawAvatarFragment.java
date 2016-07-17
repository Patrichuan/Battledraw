package patrichuan.battledraw.presentation.player_flows.joiner.fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import me.panavtec.drawableview.DrawableView;
import me.panavtec.drawableview.DrawableViewConfig;
import patrichuan.battledraw.R;
import patrichuan.battledraw.presentation.player_flows.creator.activities.CreatorBaseActivity;
import patrichuan.battledraw.presentation.player_flows.joiner.activities.JoinerBaseActivity;
import patrichuan.battledraw.util.Constants;

/**
 * Created by Pat on 23/06/2016.
 */

public class DrawAvatarFragment extends Fragment {

    private JoinerBaseActivity activity;

    private String roomName;
    private DrawableView drawLayout;
    private Button btnIAmDone;


    public DrawAvatarFragment() {

    }

    public static DrawAvatarFragment newInstance(String roomName) {
        DrawAvatarFragment fragment = new DrawAvatarFragment();
        Bundle args = new Bundle();
        args.putString(Constants.ARG_ROOM, roomName);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_draw_avatar, container, false);
        instanceViews(rootView);
        return rootView;
    }


    private void instanceViews(View rootView) {
        drawLayout = (DrawableView) rootView.findViewById(R.id.drawLayout);
        btnIAmDone = (Button) rootView.findViewById(R.id.btnIAmDone);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        roomName = getArguments().getString(Constants.ARG_ROOM);

        if (drawLayout!=null) {
            drawLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    drawLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    DrawableViewConfig config = new DrawableViewConfig();
                    config.setStrokeColor(getResources().getColor(android.R.color.black));
                    config.setStrokeWidth(20.0f);
                    config.setMinZoom(1.0f);
                    config.setMaxZoom(3.0f);
                    config.setCanvasHeight(drawLayout.getHeight());
                    config.setCanvasWidth(drawLayout.getWidth());
                    drawLayout.setConfig(config);
                }
            });
        }


        btnIAmDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doUpdateAvatar();
            }
        });
    }



    private void doUpdateAvatar () {
        final FirebaseUser currentUser = activity.getmAuth().getCurrentUser();
        if (currentUser!=null) {
            final StorageReference avatarRef = activity.getStorageReference().child("avatars").child(currentUser.getUid());
            Bitmap bmp = drawLayout.obtainBitmap();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 0, baos);
            byte[] data = baos.toByteArray();

            avatarRef.putBytes(data)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            StorageMetadata metadata = taskSnapshot.getMetadata();
                            if (metadata != null && metadata.getDownloadUrl() != null) {
                                final String avatarUri = metadata.getDownloadUrl().toString();
                                final String player_uid = currentUser.getUid();
                                activity.getDatabaseReference().child("players").child(player_uid).child("picture").setValue(avatarUri);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        } else {

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
            activity = (JoinerBaseActivity) context;
        }
    }
}