package com.example.shubham.chatapp;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;

public class LoginActivity extends AppCompatActivity {


    private static final String APP_ID = "YOUR_APP_ID";

    private Button mConnectButton;
    private TextInputEditText mUserIdEditText, mUserNicknameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mConnectButton = (Button) findViewById(R.id.button_login);
        mUserIdEditText = (TextInputEditText) findViewById(R.id.edit_text_login_user_id);
        mUserNicknameEditText = (TextInputEditText) findViewById(R.id.edit_text_login_user_nickname);

        // Initialize the SendBird SDK.
        SendBird.init(APP_ID, this.getApplicationContext());
        SendBird.setAutoBackgroundDetection(false);

        mConnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String userId = mUserIdEditText.getText().toString();
                // Remove all spaces from userID
                userId = userId.replaceAll("\\s", "");

                String userNickname = mUserNicknameEditText.getText().toString();

                    connectToSendBird(userId, userNickname);


            }
        });
    }

    /**
     * Attempts to connect a user to SendBird.
     * @param userId The unique ID of the user.
     * @param userNickname The user's nickname, which will be displayed in chats.
     */
    private void connectToSendBird(final String userId, final String userNickname) {
        mConnectButton.setEnabled(false);

        SendBird.connect(userId, new SendBird.ConnectHandler() {
            @Override
            public void onConnected(User user, SendBirdException e) {

                if (e != null) {
                    // Error!
                    Toast.makeText(
                            LoginActivity.this, "" + e.getCode() + ": " + e.getMessage(),
                            Toast.LENGTH_SHORT)
                            .show();

                    // Show login failure snackbar
                    mConnectButton.setEnabled(true);
                    return;
                }

                // Update the user's nickname
                updateCurrentUserInfo(userNickname);




                Intent intent = new Intent(LoginActivity.this, ChatActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * Updates the user's nickname.
     * @param userNickname The new nickname of the user.
     */
    private void updateCurrentUserInfo(String userNickname) {
        SendBird.updateCurrentUserInfo(userNickname, null, new SendBird.UserInfoUpdateHandler() {
            @Override
            public void onUpdated(SendBirdException e) {
                if (e != null) {
                    // Error!
                    Toast.makeText(
                            LoginActivity.this, "" + e.getCode() + ":" + e.getMessage(),
                            Toast.LENGTH_SHORT)
                            .show();

                    return;
                }

            }
        });
    }

}
