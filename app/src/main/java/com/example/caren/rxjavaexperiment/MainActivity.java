package com.example.caren.rxjavaexperiment;

import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;

public class MainActivity extends AppCompatActivity {

    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText editText = (EditText) findViewById(R.id.editText);
        text = (TextView) findViewById(R.id.text);

        Subscriber textViewSubscriber = new Subscriber<TextViewTextChangeEvent>() {
            public void onCompleted() {}

            public void onError(Throwable e) {}

            public void onNext(TextViewTextChangeEvent s) {
                ((TextView) findViewById(R.id.text)).setText(s.text().toString());
            }
        };

        Subscriber toastSubscriber = new Subscriber<TextViewTextChangeEvent>() {
            public void onCompleted() {}

            public void onError(Throwable e) {}

            public void onNext(TextViewTextChangeEvent s) {
                Toast.makeText(MainActivity.this, s.text().toString(), Toast.LENGTH_SHORT).show();
            }
        };

        Action1 a = new Action1<TextViewTextChangeEvent>() {
            @Override
            public void call(TextViewTextChangeEvent email) {
                Toast.makeText(MainActivity.this, "invalid email", Toast.LENGTH_SHORT).show();

            }
        };



        rx.Observable<TextViewTextChangeEvent> textFieldObservable =
                RxTextView.textChangeEvents(editText);

        Func1<? super TextViewTextChangeEvent, Boolean> predicate =
                new Func1<TextViewTextChangeEvent, Boolean>() {
                    @Override
                    public Boolean call(TextViewTextChangeEvent s) {
                        return s.text().toString().contains("a");
                    }
                };

        textFieldObservable.subscribe(textViewSubscriber);
        textFieldObservable.filter(predicate).subscribe(a);

        findViewById(R.id.signin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SignInActivity.class);
                MainActivity.this.startActivity(i);
            }
        });
    }
}
