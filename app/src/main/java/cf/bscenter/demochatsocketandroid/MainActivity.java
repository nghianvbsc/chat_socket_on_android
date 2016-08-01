package cf.bscenter.demochatsocketandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private EditText mEdtMessage;
    private Button mBtnSend;

    private MessageAdapter mMessageAdapter;
    private final List<String> mMessageList = new ArrayList<>();

    private Socket mSocket;

    {
        try {
            mSocket = IO.socket("http://chatsocketdemo-nghianv.rhcloud.com/");
        } catch (URISyntaxException e) {
            Log.d(TAG, "error connect socket " + e.getMessage());
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSocket.connect();
        mMessageAdapter = new MessageAdapter(mMessageList, this);
        initViews();
        setListening();
    }

    private void setListening() {
        mBtnSend.setOnClickListener(this);

        mSocket.on("CHAT", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                try {
                    JSONObject messageJson = new JSONObject(args[0].toString());
                    String message = messageJson.getString("message");
                    mMessageList.add(0, message);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mMessageAdapter.notifyDataSetChanged();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initViews() {
        mEdtMessage = (EditText) findViewById(R.id.edtMessage);
        mBtnSend = (Button) findViewById(R.id.btnSend);
        ListView mLvMessages = (ListView) findViewById(R.id.lvMessages);
        mLvMessages.setAdapter(mMessageAdapter);
    }

    @Override
    public void onClick(View view) {
        String message = mEdtMessage.getText().toString().trim();
        mEdtMessage.setText("");
        if (!message.isEmpty()) {
            //send message
            String jsonString = "{message: " + "'" + message + "'" + "}";
            try {
                JSONObject jsonData = new JSONObject(jsonString);
                mSocket.emit("CHAT", jsonData);
            } catch (JSONException e) {
                Log.d(TAG, "error send message " + e.getMessage());
            }

        }
    }
}
