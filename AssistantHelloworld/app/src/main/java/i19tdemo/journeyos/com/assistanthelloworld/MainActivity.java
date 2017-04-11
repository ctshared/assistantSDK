package i19tdemo.journeyos.com.assistanthelloworld;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.journeyui.assistantapi.BatteryInfo;
import com.journeyui.assistantapi.Starter;
import com.journeyui.assistantapi.ITorchCallback;

public class MainActivity extends Activity  implements  View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int[] ids = {
                R.id.startCamera_CAMERA_TYPE_FRONT,
                R.id.startCamera_CAMERA_TYPE_REAR,
                R.id.startFlashLight,
                R.id.stopFlashLight,
                R.id.startTimer,
                R.id.startStopWatch,
                R.id.startCalendar,
                R.id.setScreenBrightness,
                R.id.getScreenBrightness,
                R.id.getBatteryInfo,
                R.id.openAppMarket,
                R.id.btnGetFlashStatus,
                R.id.network_switch,
                R.id.open_mobiledata,
                R.id.close_mobiledata
        };

        for (int id : ids) {
            findViewById(id).setOnClickListener(this);
        }

        //permission check
        if(!Settings.System.canWrite(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, 1);
        }
        mStarter = Starter.getInstance();
        mStarter.bindAssistantService(this);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 请求码是REQUEST_CODE1，表示本次结果是申请WRITE_SETTINGS权限的结果
        if (requestCode == 1) {
            // 判断是否有WRITE_SETTINGS权限
            if (Settings.System.canWrite(this)) {

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.startCamera_CAMERA_TYPE_FRONT:
                startCamera_CAMERA_TYPE_FRONT();
                break;
            case R.id.startCamera_CAMERA_TYPE_REAR:
                startCamera_CAMERA_TYPE_REAR();
                break;
            case R.id.startFlashLight:
                startFlashLight();
                break;
            case R.id.stopFlashLight:
                stopFlashLight();
                break;
            case R.id.startTimer:
                startTimer();
                break;
            case R.id.startStopWatch:
                startStopWatch();
                break;
            case R.id.startCalendar:
                startCalendar();
                break;
            case R.id.setScreenBrightness:
                setScreenBrightness();
                break;
            case R.id.getScreenBrightness:
                getScreenBrightness();
                break;
            case R.id.getBatteryInfo:
                getBatteryInfo();
                break;
            case R.id.openAppMarket:
                openAppMarket();
                break;
            case R.id.btnGetFlashStatus:
                getFlashStatus();
                break;
            case R.id.network_switch:
                getNetworkSwitch();
                break;
            case R.id.open_mobiledata:
                openMobileData();
                break;
            case R.id.close_mobiledata:
                closeMobileData();
            default:
                break;

        }
    }

    @Override
    public int checkPermission(String permission, int pid, int uid) {
        return super.checkPermission(permission, pid, uid);
    }

    private void startCamera_CAMERA_TYPE_FRONT() {
        mStarter.startCamera(this, Starter.CAMERA_TYPE_FRONT);
        return;
    }

    private void startCamera_CAMERA_TYPE_REAR() {
        mStarter.startCamera(this, Starter.CAMERA_TYPE_REAR);
        return;
    }

    private void startFlashLight() {
        mStarter.startFlashLight(this);
        return;
    }

    private void stopFlashLight() {
        mStarter.stopFlashLight(this);
        return;
    }

    private void startTimer() {
        String str = ((EditText) findViewById(R.id.startTimer_value)).getText().toString();
        int seconds = Integer.parseInt(str);
        mStarter.startTimer(this, seconds);
        return;
    }

    private void startStopWatch() {
        String str = ((EditText) findViewById(R.id.startStopWatch_value)).getText().toString();
        boolean enable = Integer.parseInt(str) == 0 ? false : true;
        mStarter.startStopWatch(this, enable);
        return;
    }

    private void startCalendar() {
        mStarter.startCalendar(this);
        return;
    }

    private void setScreenBrightness() {
        String str = ((EditText) findViewById(R.id.setScreenBrightness_value)).getText().toString();
        int brightness = Integer.parseInt(str);
        mStarter.setScreenBrightness(brightness);
        return;
    }

    private void getScreenBrightness() {
        int brightness = mStarter.getScreenBrightness();
        ((TextView) findViewById(R.id.getScreenBrightness_value)).setText("value = " + brightness);
        return;
    }

    private void getBatteryInfo() {
        BatteryInfo batteryInfo = mStarter.getBatteryInfo(this);
        TextView currentBatteryLevel = (TextView) findViewById(R.id.currentBatteryLevel);
        currentBatteryLevel.setText("battery level = " + batteryInfo.currentBatteryLevel);

        TextView estimatedRemainingChargingTimeSec = (TextView) findViewById(R.id.estimatedRemainingChargingTimeSec);
        estimatedRemainingChargingTimeSec.setText("remine time = " + batteryInfo.estimatedRemainingChargingTimeSec);
        return;
    }

    private void openAppMarket() {
        String packageName = ((EditText) findViewById(R.id.packageName)).getText().toString();
        mStarter.openAppMarket(this, packageName);
        return;
    }

    private void getNetworkSwitch() {
        boolean isOpen = mStarter.getMobileDataEnabled();
        ((TextView) findViewById(R.id.network_status)).setText(isOpen + "");
    }

    private void openMobileData() {
        mStarter.setMobileDataEnabled(true);
    }

    private void closeMobileData() {
        mStarter.setMobileDataEnabled(false);
    }

    ITorchCallback iTorchCallback = new ITorchCallback() {
        @Override
        public void onTorchStatusChange(boolean enabled) {
            ((TextView) findViewById(R.id.flashStatus)).setText(enabled + "");
        }
    };

    public void getFlashStatus() {
        hasRegistTorcallback = true;
        mStarter.registerTorchCallback(this, iTorchCallback);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mStarter.unbindAssistantService(this);
        if (hasRegistTorcallback) {
            mStarter.unregisterTorchCallback(this);
        }
    }
}
