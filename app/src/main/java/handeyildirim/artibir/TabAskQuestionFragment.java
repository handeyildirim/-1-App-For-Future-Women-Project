package besteburhan.artibir;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Scroller;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static android.content.Intent.ACTION_VIEW;

/**
 * Created by besteburhan on 4.8.2017.
 */

public class TabAskQuestionFragment extends Fragment {


    EditText editTextExplanation;
    EditText editTextIssue;
    ImageButton imageButtonCamera;
    ImageButton imageButtonAddLocation;
    ImageButton imageButtonSend;
    Spinner spinner;
    LatLng latLng=null;
    int meter=0;

    FirebaseDatabase database;
    FirebaseAuth mAuth;
    DatabaseReference dbRef;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_ask_question, container, false);
        editTextExplanation = (EditText) view.findViewById(R.id.editTextExplanation);
        editTextIssue = (EditText) view.findViewById(R.id.editTextIssue);
        imageButtonAddLocation = (ImageButton) view.findViewById(R.id.imageButtonAddLocation);
        imageButtonCamera = (ImageButton) view.findViewById(R.id.imageButtonCamera);
        imageButtonSend = (ImageButton) view.findViewById(R.id.imageButtonSend);

        spinner = (Spinner) view.findViewById(R.id.dropdown_categories_Quest);

        String[] items = getResources().getStringArray(R.array.categories_spinner_array);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(),
                android.R.layout.simple_spinner_dropdown_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        database=FirebaseDatabase.getInstance();
        mAuth=FirebaseAuth.getInstance();






        imageButtonAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), Maps.class);
                startActivityForResult(intent, 1);
            }
        });



        imageButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userUid= mAuth.getCurrentUser().getUid().toString();
                String stringIssue=editTextIssue.getText().toString();
                String stringExplanation = editTextExplanation.getText().toString();
                String stringCategory= spinner.getSelectedItem().toString();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy '  ' HH:mm ");
                String stringQuestionDate = simpleDateFormat.format(new Date());

                if(stringIssue.trim().equals("") || stringExplanation.trim().equals("")){
                    Toast.makeText(getActivity(),"Boş alanları doldurunuz.",Toast.LENGTH_LONG).show();
                }
                else if(latLng==null || meter==0){
                    Toast.makeText(getActivity(),"Lütfen sormak istediğiniz konumu ve mesafeyi seçiniiz.",Toast.LENGTH_LONG).show();
                }
                else{
                    Double lat= latLng.latitude;
                    String latitude=Double.toString(lat);
                    Double lng= latLng.longitude;
                    String longitude= Double.toString(lng);
                    String stringMeter = Integer.toString(meter);

                    Object questionLocation =new QuestionLocation(latitude,longitude,stringMeter);
                    //database myQuestionsa soru ekleme
                    dbRef=database.getReference("ArtiBir/Users/"+ mAuth.getCurrentUser().getUid().toString());
                    dbRef = dbRef.child("myQuestions").push();
                    dbRef.setValue(new Questions(stringCategory,stringQuestionDate,stringExplanation
                            ,stringIssue, questionLocation,"0",userUid));

                    //database Question->kategorilere ekleme
                    DatabaseReference databaseReference=database.getReference("ArtiBir").child("Questions").child(stringCategory).child(dbRef.getKey());
                    databaseReference.setValue(new Questions(stringCategory,stringQuestionDate
                            ,stringExplanation,stringIssue, questionLocation,"0",userUid));

                    editTextIssue.setText("");
                    editTextExplanation.setText("");




                }
            }
        });

        return view;


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);// ?
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {

                latLng= (LatLng) data.getExtras().get("latLng");
                meter = data.getExtras().getInt("meter");

            }
        }
    }
}