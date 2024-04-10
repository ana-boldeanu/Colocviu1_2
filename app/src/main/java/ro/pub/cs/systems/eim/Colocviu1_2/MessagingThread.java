package ro.pub.cs.systems.eim.Colocviu1_2;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Date;

public class MessagingThread extends Thread {
    private boolean isRunning = true;
    private int sum = 0;
    private Context context = null;

    public MessagingThread(Context context, int sum) {
        this.context = context;
        this.sum = sum;
    }

    public void stopThread() {
        isRunning = false;
    }

    @Override
    public void run() {
        Log.d("MessagingThread", "Thread has started!");

        while (isRunning) {
            sleep(2000);
            sendMessage();
            sleep(2000);
        }
    }

    private void sendMessage() {
        Intent intent = new Intent();
        intent.setAction("action_string");
        intent.putExtra("BROADCAST_RECEIVER_EXTRA",
                new Date(System.currentTimeMillis()) + "- Sum is " + sum);
        context.sendBroadcast(intent);
    }

    private void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
