package ispc.intelligentsmartphonecontroler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.UUID;
import android.os.Handler;
import android.os.Build;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

public class Tracker extends Service {
	private static final String TAG = "BeWithMe";
	int counter = 0;
	private BluetoothAdapter btAdapter = null;
	private BluetoothSocket btSocket = null;

	private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	
	//universally unique identifier (UUID)
	private static String address = "00:06:69:00:17:F9";//MAC
	static final int UPDATE_INTERVAL = 1000;
	
	private ConnectedThread mConnectedThread;
	private ConnectThread cn = new ConnectThread();
	
	InputStream is = null;
	Handler h, h2;
	long t1, t2;
	static boolean bb = true;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
		btAdapter = BluetoothAdapter.getDefaultAdapter();
		// checkBTState();
		// Connect();
		onBluetooth();
		while (!btAdapter.isEnabled())
			;
		cn.start();

		h = new Handler() {
			public void handleMessage(android.os.Message msg) { // String
																// strIncom =
																// new
																// String((byte[])
																// msg.obj, 0,
																// msg.arg1);

				Toast.makeText(getBaseContext(),
						String.valueOf(((byte[]) msg.obj).length) + "hiii", 0)
						.show();

			};
		};
		h2 = new Handler() {
			public void handleMessage(android.os.Message msg) { // String
																// strIncom =
																// new
																// String((byte[])
																// msg.obj, 0,
																// msg.arg1);

				Toast.makeText(getBaseContext(),
						String.valueOf(((byte[]) msg.obj).length) + "hello", 0)
						.show();

			};
		};

		return START_STICKY;
	}

	private BluetoothSocket createBluetoothSocket(BluetoothDevice device)
			throws IOException {
		if (Build.VERSION.SDK_INT >= 10) {
			try {
				final Method m = device.getClass().getMethod(
						"createInsecureRfcommSocketToServiceRecord",
						new Class[] { UUID.class });
				return (BluetoothSocket) m.invoke(device, MY_UUID);
			} catch (Exception e) {
				Log.e("BeWithMe",
						"Could not create Insecure RFComm Connection", e);
			}
		}
		return device.createRfcommSocketToServiceRecord(MY_UUID);
	}

	private void Connect() {
		Log.d(TAG, "...onResume - try connect...");
		
		BluetoothDevice device = btAdapter.getRemoteDevice(address);
		
		try {
			btSocket = createBluetoothSocket(device);
		} catch (IOException e) {
			errorExit("Fatal Error", "In onResume() and socket create failed: "
					+ e.getMessage() + ".");
		}
		btAdapter.cancelDiscovery();
		
		Log.d(TAG, "...Connecting...");
		
		try {
			btSocket.connect();
			Log.d(TAG, "....Connection ok...");
		} catch (IOException e) {
			try {
				btSocket.close();
			} catch (IOException e2) {
				errorExit("Fatal Error",
						"In onResume() and unable to close socket during connection failure"
								+ e2.getMessage() + ".");
			}
		}

		Log.d(TAG, "...Create Socket...");
		mConnectedThread=null;
		mConnectedThread = new ConnectedThread(btSocket);
		mConnectedThread.start();
		// new ConnectThread().start();
		// doSomethingRepeatedly(btSocket);

	}

	private void onBluetooth() {
		if (!btAdapter.isEnabled()) {
			btAdapter.enable();
			Log.i("Log", "BlueTooth Is Enabled");
		}
	}

	private void offBluetooth() {
		if (btAdapter.isEnabled()) {
			btAdapter.disable();
		}
	}

	private void errorExit(String title, String message) {
		Toast.makeText(getBaseContext(), title + " - " + message,
				Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		try {

			if (cn.isAlive()) {
				cn.stop();
				cn=null;
			}
		} catch (Exception ex) {
			Toast.makeText(this, ex.toString() + " 1", 1).show();
		}
		try {
			btSocket.close();
		} catch (IOException e2) {
			errorExit("Fatal Error", "In onPause() and failed to close socket."
					+ e2.getMessage() + ".");
		}
		try {
			if (mConnectedThread.isAlive()) {
				mConnectedThread.stop();
				mConnectedThread=null;
				mConnectedThread.cancel();
				
			}
		} catch (Exception ex) {
			Toast.makeText(this, ex.toString() + " 3", 1).show();
		}
		offBluetooth();
		Toast.makeText(this, "Service Destroyed", Toast.LENGTH_SHORT).show();

	}

	private class ConnectedThread extends Thread {
		private final InputStream mmInStream;
		private final OutputStream mmOutStream;
        private BluetoothSocket soc;
		
        public ConnectedThread(BluetoothSocket socket) {
			InputStream tmpIn = null;
			OutputStream tmpOut = null;
            soc=socket;
			// Get the input and output streams, using temp objects because
			// member streams are final
			try {
				tmpIn = socket.getInputStream();
				tmpOut = socket.getOutputStream();
			} catch (IOException e) {
			}

			mmInStream = tmpIn;
			mmOutStream = tmpOut;
		}

		public void run() {
			// byte[] buffer = new byte[2260992];
			byte[] buffer = new byte[256];
			int bytes;
			write("1254785");
			while (true) {

				try {

					bytes = mmInStream.read(buffer);
					t2 = System.currentTimeMillis();
					h.obtainMessage(1, bytes, -1, buffer).sendToTarget();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException ex) {
					}
					write("1254785");

				} catch (IOException e) {
					btSocket = null;   
					break;
				}
			}
		}

		public void cancel()
		{
			try {
				soc.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public void write(String message) {
			// Log.d(TAG, "...Data to send: " + message + "...");
			try {

				byte[] msgBuffer = message.getBytes();

				mmOutStream.write(msgBuffer);
				h2.obtainMessage(1, 256, -1, msgBuffer).sendToTarget();
				t1 = System.currentTimeMillis();
			} catch (IOException e) {
				// Log.d(TAG, "...Error data send: " + e.getMessage() + "...");

			}
		}
	}

	class ConnectThread extends Thread {

		public void run() {
			while (true) {
				if (btSocket == null) {
					Connect();
				}

			}
		}
	}

}
