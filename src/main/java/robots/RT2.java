package robots;

import robots.dev.connection.ListenPort;
import robots.dev.connection.ParseChannels;
import robots.dev.connection.TalkPort;

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

    private final TalkPort otherChannel = new TalkPort(65431);
    private final TalkPort motorsChannel = new TalkPort(65432);
    private final TalkPort omsChannel = new TalkPort(65433);
    private final TalkPort resetsChannel = new TalkPort(65434);
    private final ListenPort encsChannel = new ListenPort(65435);
    private final ListenPort sensorsChannel = new ListenPort(65436);
    private final ListenPort buttonsChannel = new ListenPort(65437);
    private final ListenPort cameraChannel = new ListenPort(65438, true);

    public void connect()
    {
        this.otherChannel.startTalking();
        this.motorsChannel.startTalking();
        this.omsChannel.startTalking();
        this.resetsChannel.startTalking();
        this.encsChannel.startListening();
        this.sensorsChannel.startListening();
        this.buttonsChannel.startListening();
        this.cameraChannel.startListening();
    }

    public void disconnect()
    {
        this.otherChannel.stopTalking();
        this.motorsChannel.stopTalking();
        this.omsChannel.stopTalking();
        this.resetsChannel.stopTalking();
        this.encsChannel.stopListening();
        this.sensorsChannel.stopListening();
        this.buttonsChannel.stopListening();
        this.cameraChannel.stopListening();
    }

    private void updateOther()
    {
        this.otherChannel.outString = ParseChannels.JoinBoolChannels(List.of
                (
                        this.ledGreen,
                        this.ledRed
                ));
    }

    private void updateMotors()
    {
        this.motorsChannel.outString = ParseChannels.JoinFloatChannels(List.of
                (
                        this.rightMotorSpeed,
                        this.leftMotorSpeed,
                        this.backMotorSpeed
                ));
    }

    private void updateOMS()
    {
        this.omsChannel.outString = ParseChannels.JoinFloatChannels(List.of
                (
                        this.liftServoPos,
                        this.gripServoPos
                ));
    }

    private void updateResets()
    {
        this.otherChannel.outString = ParseChannels.JoinBoolChannels(List.of
                (
                        this.resetRightEnc,
                        this.resetLeftEnc,
                        this.resetBackEnc,
                        this.resetImu
                ));
    }

    private void updateEncs()
    {
        List<Float> values = ParseChannels.ParseFloatChannel(this.encsChannel.outString);
        if (values.size() == 3)
        {
            this.rightMotorEnc = values.get(0);
            this.leftMotorEnc = values.get(1);
            this.backMotorEnc = values.get(2);
        }
    }

    private void updateSensors()
    {
        List<Float> values = ParseChannels.ParseFloatChannel(this.sensorsChannel.outString);
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
        List<Boolean> values = ParseChannels.ParseBoolChannel(this.buttonsChannel.outString);
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
        if (cameraChannel.outBytes.length == 921600)
        {
            this.bytesFromCamera = cameraChannel.outBytes;
        }
//        this.bytesFromCamera = cameraChannel.outBytes;
    }
}
