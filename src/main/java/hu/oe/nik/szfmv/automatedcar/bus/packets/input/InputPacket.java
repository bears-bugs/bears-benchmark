package hu.oe.nik.szfmv.automatedcar.bus.packets.input;

import hu.oe.nik.szfmv.automatedcar.input.enums.GearEnum;

public class InputPacket implements ReadOnlyInputPacket {

    private static InputPacket instance = null;

    private double steeringWheelPosition;

    private int gaspedalposition;

    private int brakepedalvalue;

    private GearEnum gearEnum = GearEnum.P;

    private boolean leftIndexOn;

    private boolean rightIndexOn;

    private boolean laneKeepingOn;

    private boolean parkingPilote;

    private double accDistanceValue;

    private int accSpeedValue;

    private boolean radarVizualizerState;

    private boolean cameraVizualizerState;

    private boolean ultrasonicVizualizerState;

    private boolean shapeBorderVizualizerState;

    /**
     * Inpuutpacket
     *
     * @return inputpacket
     */
    public static InputPacket getInstance() {
        if (instance == null) {
            instance = new InputPacket();
        }

        return instance;
    }

    public void setSteeringWheelPosition(double steeringWheelPosition) {
        this.steeringWheelPosition = steeringWheelPosition;
    }

    public void setGaspeadalposition(int value) {
        this.gaspedalposition = value;
    }

    public void setBrakepedalvalue(int brakepedalvalue) {
        this.brakepedalvalue = brakepedalvalue;
    }

    public void setParkingPiloteStatus(boolean value) {
        parkingPilote = value;
    }

    public void setAccDistanceValue(double value) {
        accDistanceValue = value;
    }

    public void setAccSpeedValue(int value) {
        accSpeedValue = value;
    }

    public void setLaneKeepingStatus(boolean value) {
        this.laneKeepingOn = value;
    }

    public void setRightTurnSignalStatus(boolean rightIndexOn) {
        this.rightIndexOn = rightIndexOn;
    }

    public void setRadarVizualizerState(boolean radarVizualizerState) {
        this.radarVizualizerState = radarVizualizerState;
    }

    public void setCameraVizualizerState(boolean cameraVizualizerState) {
        this.radarVizualizerState = radarVizualizerState;
    }

    public void setUltrasonicVizualizerState(boolean ultrasonicVizualizerState) {
        this.ultrasonicVizualizerState = ultrasonicVizualizerState;
    }

    public void setShapeBorderVizualizerState(boolean shapeBorderVizualizerState) {
        this.shapeBorderVizualizerState = shapeBorderVizualizerState;
    }

    public void setGearSate(GearEnum gearEnum) {
        this.gearEnum = gearEnum;
    }

    @Override
    public int getGasPedalPosition() {
        return gaspedalposition;
    }

    @Override
    public int getBreakPedalPosition() {
        return brakepedalvalue;
    }

    @Override
    public double getSteeringWheelPosition() {
        return steeringWheelPosition;
    }

    @Override
    public int getACCTargetSpeed() {
        return accSpeedValue;
    }

    @Override
    public double getACCTargetDistance() {
        return accDistanceValue;
    }

    @Override
    public boolean getLaneKeepingStatus() {
        return laneKeepingOn;
    }

    @Override
    public boolean getParkingPilotStatus() {
        return parkingPilote;
    }

    @Override
    public GearEnum getGearState() {
        return gearEnum;
    }

    @Override
    public boolean getLeftTurnSignalStatus() {
        return leftIndexOn;
    }

    public void setLeftTurnSignalStatus(boolean leftIndexOn) {
        this.leftIndexOn = leftIndexOn;
    }

    @Override
    public boolean getRightTurnSignalStatus() {
        return rightIndexOn;
    }

    @Override
    public boolean getRadarVizualizerStatus() {
        return radarVizualizerState;
    }

    @Override
    public boolean getCameraVizualizerStatus() {
        return cameraVizualizerState;
    }

    @Override
    public boolean getUltrasonicVizualizerStatus() {
        return ultrasonicVizualizerState;
    }

    @Override
    public boolean getShapeBorderVizualizerState() {
        return shapeBorderVizualizerState;
    }


}
