package it.androidworld.devcorner.qrcode;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.content.*;
import android.net.*;
import android.hardware.*;

public class MainActivity extends Activity implements View.OnClickListener
{
	private Button btnScan;
	private TextView txtResults;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

		txtResults = (TextView)findViewById(R.id.txtResult);
		txtResults.setText("Nessun dato rilevato");
		
		btnScan = (Button)findViewById(R.id.btnScansione);
		btnScan.setOnClickListener(this);
		
		if(!isBackCameraAvailable()) btnScan.setActivated(false);
    }
	
	@Override
	public void onClick(View view){
		
		switch(view.getId()){
			case R.id.btnScansione:
				avviaScansione();
				break;
		}
	}
	
	private void avviaScansione(){
		
		try{
			Intent scanIntent = new Intent("com.google.zxing.client.android.SCAN");
			scanIntent.putExtra("SCAN_MODE","QR_CODE");
			startActivityForResult(scanIntent,0);
		}catch(Exception e){
			Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
			Intent playStoreIntent = new Intent(Intent.ACTION_VIEW,marketUri);
			startActivity(playStoreIntent);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		
		super.onActivityResult(requestCode,resultCode,data);
		
		if(requestCode == 0){
			if(resultCode == RESULT_OK)
				txtResults.setText(data.getStringExtra("SCAN_RESULT"));
		}
	}
	
	private boolean isBackCameraAvailable(){
		boolean available = false;
		
		Camera.CameraInfo ci = new Camera.CameraInfo();
		int numberOfCameras = Camera.getNumberOfCameras();
		
		for(int i = 0; i < numberOfCameras; i++){
			Camera.getCameraInfo(i,ci);
			if(ci.facing == Camera.CameraInfo.CAMERA_FACING_BACK)
				available = true;
		}
		return available;
	}
}
