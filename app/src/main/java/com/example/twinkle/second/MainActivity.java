package com.example.twinkle.second;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class MainActivity extends AppCompatActivity {
    private DatabaseReference myDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDatabase = FirebaseDatabase.getInstance().getReference("Message");

        final TextView myText = findViewById(R.id.textView);

        myDatabase.addValueEventListener(new ValueEventListener() {
            String temp = "";
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                temp="";
                int count=0;
                for(DataSnapshot uniqueKey : dataSnapshot.getChildren()){
                    count++;
                    if(count>3)
                    temp=temp + uniqueKey.getValue().toString()+"\n\n";
                }

                /*String str = dataSnapshot.getValue().toString();
                String[] arrOfStr = str.split("[=,}]+");
                int cnt=1;
                for(String a:arrOfStr)
                {
                    if(cnt%2==0)
                        temp=temp +" "+ a+"\n\n";
                    cnt++;
                }*/
                myText.setText(temp);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                myText.setText("CANCELLED");
            }
        });
    }
    public void sendMessage(View view){
        EditText myEditText = findViewById(R.id.editText);

        myDatabase.push().setValue(myEditText.getText().toString());
        myEditText.setText("");
    }
}
