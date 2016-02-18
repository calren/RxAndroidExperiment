package com.example.caren.rxjavaexperiment;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        final EditText email = (EditText) findViewById(R.id.email);
        EditText password = (EditText) findViewById(R.id.password);
        final View submitButton = findViewById(R.id.submit);

        rx.Observable<TextViewTextChangeEvent> emailChangeObservable =
                RxTextView.textChangeEvents(email);
        rx.Observable<TextViewTextChangeEvent> passwordChangeObservable =
                RxTextView.textChangeEvents(password);

        Func2<? super TextViewTextChangeEvent, ? super TextViewTextChangeEvent, ? extends Boolean> checkEmailAndPasswordLength =
                new Func2<TextViewTextChangeEvent, TextViewTextChangeEvent, Boolean>() {
                    @Override
                    public Boolean call(TextViewTextChangeEvent t1, TextViewTextChangeEvent t2) {
                        return t1.text().length() > 5 && t2.text().length() > 5;
                    }
                };

        Action1 handleSubmitButtonState = new Action1<Boolean>() {
            @Override
            public void call(Boolean enabled) {
                submitButton.setBackgroundColor(enabled ? Color.BLUE : Color.BLACK);
            }
        };

        Action1 changeTextColor = new Action1<TextViewTextChangeEvent>() {
            @Override
            public void call(TextViewTextChangeEvent enabled) {
                email.setTextColor(enabled.text().toString().length() > 5 ? Color.GREEN : Color.RED);
            }
        };


        Observable.combineLatest(emailChangeObservable, passwordChangeObservable, checkEmailAndPasswordLength)
                .subscribe(handleSubmitButtonState);

        emailChangeObservable.subscribe(changeTextColor);
    }
}
