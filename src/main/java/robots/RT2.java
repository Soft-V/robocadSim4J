package robots;

import robots.dev.ConnectionHelper;
import robots.dev.Holder;

import java.util.List;

public class RT2
{
    private float rightMotorSpeed = 0;
    public void setRightMotorSpeed(float speed) { this.rightMotorSpeed = speed; this.updateMotors(); }
    private float leftMotorSpeed = 0;
    public void setLeftMotorSpeed(float speed) { this.leftMotorSpeed = speed; this.updateMotors(); }
    private float backMotorSpeed = 0;
    public void setBackMotorSpeed(float speed) { this.backMotorSpeed = speed; this.updateMotors(); }

    private float liftServoPos = 0;
    public void setLiftServoPos(float value) { this.liftServoPos = value; this.updateOMS(); }
    private float gripServoPos = 0;
    public void setGripServoPos(float value) { this.gripServoPos = value; this.updateOMS(); }

    private float rightMotorEnc = 0;
    public float getRightMotorEnc() { this.updateEncs(); return this.rightMotorEnc; }
    private float leftMotorEnc = 0;
    public float getLeftMotorEnc() { this.updateEncs(); return this.leftMotorEnc; }
    private float backMotorEnc = 0;
    public float getBackMotorEnc() { this.updateEncs(); return this.backMotorEnc; }

    private boolean resetImu = false;
    public void setResetImu(boolean value) { this.resetImu = value; this.updateResets(); }

    private boolean resetRightEnc = false;
    public void setResetRightEnc(boolean value) { this.resetRightEnc = value; this.updateResets(); }
    private boolean resetLeftEnc = false;
    public void setResetLeftEnc(boolean value) { this.resetLeftEnc = value; this.updateResets(); }
    private boolean resetBackEnc = false;
    public void setResetBackEnc(boolean value) { this.resetBackEnc = value; this.updateResets(); }

    private boolean buttonEMS = false;
    public boolean getButtonEMS() { this.updateButtons(); return this.buttonEMS; }
    private boolean buttonStart = false;
    public boolean getButtonStart() { this.updateButtons(); return this.buttonStart; }
    private boolean buttonReset = false;
    public boolean getButtonReset() { this.updateButtons(); return this.buttonReset; }
    private boolean buttonStop = false;
    public boolean getButtonStop() { this.updateButtons(); return this.buttonStop; }

    private boolean ledGreen = false;
    public void setLedGreen(boolean value) { this.ledGreen = value; this.updateOther(); }
    private boolean ledRed = false;
    public void setLedRed(boolean value) { this.ledRed = value; this.updateOther(); }

    private float rightUS = 0;
    public float getRightUS() { this.updateSensors(); return this.rightUS; }
    private float leftUS = 0;
    public float getLeftUS() { this.updateSensors(); return this.leftUS; }
    private float rightIR = 0;
    public float getRightIR() { this.updateSensors(); return this.rightIR; }
    private float leftIR = 0;
    public float getLeftIR() { this.updateSensors(); return this.leftIR; }
    private float imu = 0;
    public float getIMU() { this.updateSensors(); return this.imu; }

    private final LineSensor lineSensor = new LineSensor();
    public LineSensor getLineSensor() { this.updateSensors(); return this.lineSensor; }

    private byte[] bytesFromCamera = new byte[0];
    public byte[] getBytesFromCamera() { this.updateCamera(); return this.bytesFromCamera; }

    private final ConnectionHelper connectionHelper = new ConnectionHelper(Holder.CONN_OTHER |
            Holder.CONN_MOTORS_AND_ENCS | Holder.CONN_OMS | Holder.CONN_RESETS | Holder.CONN_SENS |
            Holder.CONN_BUTTONS | Holder.CONN_CAMERA);

    public void connect()
    {
        connectionHelper.startChannels();
    }

    public void disconnect()
    {
        connectionHelper.stopChannels();
    }

    private void updateOther()
    {
        connectionHelper.setOther(List.of
                (
                        this.ledGreen ? 1f : 0f,
                        this.ledRed ? 1f : 0f
                ));
    }

    private void updateMotors()
    {
        connectionHelper.setMotors(List.of
                (
                        this.rightMotorSpeed,
                        this.leftMotorSpeed,
                        this.backMotorSpeed
                ));
    }

    private void updateOMS()
    {
        connectionHelper.setOMS(List.of
                (
                        this.liftServoPos,
                        this.gripServoPos
                ));
    }

    private void updateResets()
    {
        connectionHelper.setResets(List.of
                (
                        this.resetRightEnc,
                        this.resetLeftEnc,
                        this.resetBackEnc,
                        this.resetImu
                ));
    }

    private void updateEncs()
    {
        List<Float> values = connectionHelper.getEncs();
        if (values.size() == 3)
        {
            this.rightMotorEnc = values.get(0);
            this.leftMotorEnc = values.get(1);
            this.backMotorEnc = values.get(2);
        }
    }

    private void updateSensors()
    {
        List<Float> values = connectionHelper.getSens();
        if (values.size() == 9)
        {
            this.rightUS = values.get(0);
            this.leftUS = values.get(1);
            this.rightIR = values.get(2);
            this.leftIR = values.get(3);
            this.imu = values.get(4);
            this.lineSensor.s1 = values.get(5);
            this.lineSensor.s2 = values.get(6);
            this.lineSensor.s3 = values.get(7);
            this.lineSensor.s4 = values.get(8);
        }
    }

    private void updateButtons()
    {
        List<Boolean> values = connectionHelper.getButtons();
        if (values.size() == 4)
        {
            this.buttonEMS = values.get(0);
            this.buttonStart = values.get(1);
            this.buttonReset = values.get(2);
            this.buttonStop = values.get(3);
        }
    }

    private void updateCamera()
    {
        byte[] cameraData = connectionHelper.getCamera();
        if (cameraData.length == 921600)
        {
            this.bytesFromCamera = cameraData;
        }
    }
}
