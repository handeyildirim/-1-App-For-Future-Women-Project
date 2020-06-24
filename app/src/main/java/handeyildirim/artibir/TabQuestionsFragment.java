package besteburhan.artibir;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.location.LocationRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.google.android.gms.cast.CastRemoteDisplayLocalService.startService;

/**
 * Created by besteburhan on 4.8.2017.
 */

public class TabQuestionsFragment extends Fragment{




    Adapter adapter;
    ArrayList<Questions> arrayListQuestions= new ArrayList<Questions>();

    ListView listView;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.tab_questions,container,false);



        return view;
    }


    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent ıntent) {
            arrayListQuestions  = ıntent.getParcelableArrayListExtra("arrayListQuestions");
            if(getActivity()!=null){
                adapter = new Adapter(getActivity(),arrayListQuestions);
                listView = getView().findViewById(R.id.listViewQuestions);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        }
    };
    @Override
    public void onResume()
    {
        super.onResume();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(broadcastReceiver, new IntentFilter("that"));//
    }

}
