package com.example.matthewglausfamilymapclient.userinterface.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.matthewglausfamilymapclient.R;
import com.example.matthewglausfamilymapclient.tasks.LoginTask;
import com.example.matthewglausfamilymapclient.tasks.RegisterTask;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import model.User;
import request.LoginRequest;
import request.RegisterRequest;

public class LoginFragment extends Fragment {
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            checkButtonStatus();
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private Listener listener;

    public interface Listener {
        void loginDone();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    private LoginRequest loginRequest;
    private RegisterRequest registerRequest;

    private EditText serverHost;
    private EditText serverPort;
    private EditText userName;
    private EditText password;
    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private RadioGroup genderButton;

    private Button loginButton;
    private Button registerButton;

    private User user;

    public LoginFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        user = null;
        serverHost = view.findViewById(R.id.serverHostField);
        serverHost.addTextChangedListener(textWatcher);

        serverPort = view.findViewById(R.id.serverPortField);
        serverPort.addTextChangedListener(textWatcher);

        userName = view.findViewById(R.id.userNameField);
        userName.addTextChangedListener(textWatcher);

        password = view.findViewById(R.id.passwordField);
        password.addTextChangedListener(textWatcher);

        firstName = view.findViewById(R.id.firstNameField);
        firstName.addTextChangedListener(textWatcher);

        lastName = view.findViewById(R.id.lastNameField);
        lastName.addTextChangedListener(textWatcher);

        email = view.findViewById(R.id.emailField);
        email.addTextChangedListener(textWatcher);

        genderButton = view.findViewById(R.id.RadioGender);
        genderButton.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                checkButtonStatus();
            }
        });

        loginButton = view.findViewById(R.id.loginButton);
        registerButton = view.findViewById(R.id.registerButton);
        checkButtonStatus();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                @SuppressLint("HandlerLeak") Handler loginMessageHandler = new Handler() {
                    @Override
                    public void handleMessage(Message message) {
                        Bundle bundle = message.getData();
                        String text = bundle.getString("message");
                        if(listener != null && !text.equals("Login Failed.") &&
                                !text.equals("Register Failed.")) {
                            listener.loginDone();
                        }
                        else {
                            Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
                        }
                    }
                };

                loginRequest = new LoginRequest(userName.getText().toString(),
                        password.getText().toString());
                LoginTask loginTask = new LoginTask(loginMessageHandler,
                        serverHost.getText().toString(), serverPort.getText().toString(),
                        loginRequest);
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.submit(loginTask);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                @SuppressLint("HandlerLeak") Handler registerMessageHandler = new Handler() {
                    @Override
                    public void handleMessage(Message message) {
                        Bundle bundle = message.getData();
                        String text = bundle.getString("message");
                        if(listener != null && !text.equals("Login Failed.") &&
                                !text.equals("Register Failed.")) {
                            listener.loginDone();
                        }
                        else {
                            Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
                        }
                    }
                };

                user.setUsername(userName.getText().toString());
                user.setPassword(password.getText().toString());
                user.setFirstName(firstName.getText().toString());
                user.setLastName(lastName.getText().toString());
                user.setEmail(email.getText().toString());
                if(genderButton.getCheckedRadioButtonId() == R.id.femaleRB) {
                    user.setGender("f");
                }
                if(genderButton.getCheckedRadioButtonId() == R.id.maleRB) {
                    user.setGender("m");
                }
                registerRequest = new RegisterRequest(user);

                RegisterTask registerTask = new RegisterTask(registerMessageHandler,
                        serverHost.getText().toString(), serverPort.getText().toString(),
                        registerRequest);
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.submit(registerTask);
            }
        });

        return view;
    }

    private void checkButtonStatus() {
        if(TextUtils.isEmpty(serverHost.getText()) || TextUtils.isEmpty(serverPort.getText()) ||
                TextUtils.isEmpty(userName.getText()) || TextUtils.isEmpty(password.getText()) ||
                TextUtils.isEmpty(email.getText()) || TextUtils.isEmpty(firstName.getText()) ||
                TextUtils.isEmpty(lastName.getText()) || genderButton.getCheckedRadioButtonId() == -1) {
            registerButton.setEnabled(false);
        }
        else {
            registerButton.setEnabled(true);
        }

        if(TextUtils.isEmpty(serverHost.getText()) || TextUtils.isEmpty(serverPort.getText()) ||
                TextUtils.isEmpty(userName.getText()) || TextUtils.isEmpty(password.getText())) {
            loginButton.setEnabled(false);
        }
        else {
            loginButton.setEnabled(true);
        }
    }

}

