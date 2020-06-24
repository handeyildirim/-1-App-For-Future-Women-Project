package besteburhan.artibir;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

public class SecondActivity extends AppCompatActivity{


    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;
    Intent mIntent;

    boolean mBounded;
    LocationChangeService mLocationChangeService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());//kontrol et??
        setContentView(R.layout.activity_second);
        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        //setupWithViewPager set up TabLayout with a viewpager
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(1).setIcon(R.drawable.questions).setText("");
        tabLayout.getTabAt(0).setIcon(R.drawable.ask_question).setText("");
        tabLayout.getTabAt(2).setIcon(R.drawable.my_question).setText("");

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mViewPager.setCurrentItem(1);
        toolbar.setTitle("Sorular");
        setSupportActionBar(toolbar);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // supportactiionbar == toolbar
                getSupportActionBar().setTitle(mSectionsPageAdapter.getPageTitle(tab.getPosition()));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        mIntent = new Intent(this, LocationChangeService.class);
        startService(mIntent);
        //bindService(mIntent, mConnection, BIND_AUTO_CREATE);
    }

    ServiceConnection mConnection = new ServiceConnection() {

        public void onServiceDisconnected(ComponentName name) {
            Toast.makeText(SecondActivity.this, "Service is disconnected", Toast.LENGTH_SHORT).show();
            mBounded = false;
            mLocationChangeService = null;
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            Toast.makeText(SecondActivity.this, "Service is connected", Toast.LENGTH_SHORT).show();
            mBounded = true;
            LocationChangeService.LocationChangeServiceBinder mLocalBinder = (LocationChangeService.LocationChangeServiceBinder) service;
            mLocationChangeService = mLocalBinder.getBinder();
        }
    };



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_second_activity,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.log_out_button:
                FacebookSdk.sdkInitialize(getApplicationContext());//kontrol et??
                //stopService(mIntent);
                LoginManager.getInstance().logOut();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(SecondActivity.this,MainActivity.class));
                finish();
        }
        return super.onOptionsItemSelected(item);
    }


    private void setupViewPager(ViewPager viewPager){

        mSectionsPageAdapter.addFragment(new TabAskQuestionFragment(),"Soru Sor");
        mSectionsPageAdapter.addFragment(new TabQuestionsFragment(),"Sorular");
        mSectionsPageAdapter.addFragment(new TabMyQuestionsFragment(),"Sorularım");
        viewPager.setAdapter(mSectionsPageAdapter);
    }

}
