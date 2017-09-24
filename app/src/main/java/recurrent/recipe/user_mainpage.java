package recurrent.recipe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class user_mainpage extends AppCompatActivity {
    private FirebaseAuth mAuth;
    Button btwSignout = (Button) findViewById(R.id.btnSignOut);
    private TextView tvUpLogin = (TextView) findViewById(R.id.tvUpLogin);
    private TextView tvUpRegister = (TextView) findViewById(R.id.tvUpRegister);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_mainpage);

        tvUpRegister.setText("registered!");

        btwSignout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mAuth.signOut();
                Intent mainActivity = new Intent(user_mainpage.this, MainActivity.class);
                mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainActivity);
            }
        });
    }
}
