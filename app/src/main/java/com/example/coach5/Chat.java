package com.example.coach5;

        import android.content.Intent;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.ImageView;

        import androidx.appcompat.app.AppCompatActivity;

public class Chat extends AppCompatActivity {
    private ImageView back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //back = (ImageView) findViewById(R.id.imageView2);
        //back.setOnClickListener(this);
    }

//    @Override
//    public void onClick(View v) {
//        switch(v.getId()) {
//            case R.id.imageView2:
//                startActivity(new Intent(this, Homescreen.class));
//                break;
//        }
//    }
}