package hu.oe.nik.szfmv.automatedcar.systemcomponents;

import hu.oe.nik.szfmv.automatedcar.bus.VirtualFunctionBus;
import hu.oe.nik.szfmv.automatedcar.bus.packets.sample.SamplePacket;
import hu.oe.nik.szfmv.automatedcar.input.enums.GearEnum;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PowertrainSystemTest {
    private VirtualFunctionBus virtualFunctionBus;
    private PowertrainSystem powertrainSystem;
    private SamplePacket samplePacket;

    @Before
    public void registerComponent() {
        samplePacket = new SamplePacket();
        virtualFunctionBus = new VirtualFunctionBus();
        virtualFunctionBus.samplePacket = samplePacket;

        powertrainSystem = new PowertrainSystem(virtualFunctionBus, -10);
    }

    private void setValues(int gaspedalPosition, int brakepedalPosition, GearEnum gearState) {
        samplePacket.setGaspedalPosition(gaspedalPosition);
        samplePacket.setBrakepedalPosition(brakepedalPosition);
        samplePacket.setGearState(gearState);
    }

    @Test
    public void getRPMWithMaxGaspedalState() {
        setValues(100, 0, GearEnum.D);
        assertEquals(CarSpecifications.MAX_RPM -1, powertrainSystem.calculateExpectedRPM(powertrainSystem.virtualFunctionBus.samplePacket.getGaspedalPosition()));
    }

    @Test
    public void getRPMWithMinGaspedalState() {
        setValues(0, 0, GearEnum.D);
        assertEquals(CarSpecifications.IDLE_RPM, powertrainSystem.calculateExpectedRPM(powertrainSystem.virtualFunctionBus.samplePacket.getGaspedalPosition()));
    }

    @Test
    public void getRPMWithGaspedalState1() {
        setValues(34, 0, GearEnum.D);
        assertEquals(3004, powertrainSystem.calculateExpectedRPM(powertrainSystem.virtualFunctionBus.samplePacket.getGaspedalPosition()));
    }

    @Test
    public void getRPMWithGaspedalState2() {
        setValues(71, 0, GearEnum.D);
        assertEquals(5468, powertrainSystem.calculateExpectedRPM(powertrainSystem.virtualFunctionBus.samplePacket.getGaspedalPosition()));
    }

    @Test
    public void accelerateFullGasForwardTest() throws InterruptedException {
        powertrainSystem = new PowertrainSystem(virtualFunctionBus, 0);
        setValues(100, 0, GearEnum.D);

        for (int i = 0; i < 4500; i++) {
            //Thread.sleep(25);
            powertrainSystem.loopTest();
        }
        assertEquals(CarSpecifications.MAX_FORWARD_SPEED, powertrainSystem.virtualFunctionBus.powertrainPacket.getSpeed(), 0.03);
    }

    @Test
    public void accelerateFullGasReverseTest() throws InterruptedException {
        powertrainSystem = new PowertrainSystem(virtualFunctionBus, 0);
        setValues(100, 0, GearEnum.R);

        for (int i = 0; i < 80; i++) {
            //Thread.sleep(25);
            powertrainSystem.loopTest();
        }
        assertEquals(CarSpecifications.MAX_REVERSE_SPEED, powertrainSystem.virtualFunctionBus.powertrainPacket.getSpeed(), 0.3);
    }

    @Test
    public void engineBrakeTestForward() throws InterruptedException {
        this.powertrainSystem = new PowertrainSystem(virtualFunctionBus, 50);
        setValues(0, 0, GearEnum.D);

        for (int i = 0; i < 4500; i++) {
            //Thread.sleep(25);
            powertrainSystem.loopTest();
        }
        assertEquals(CarSpecifications.MIN_FORWARD_SPEED, powertrainSystem.virtualFunctionBus.powertrainPacket.getSpeed(), 0.1);
    }

    @Test
    public void engineBrakeTestBackward() throws InterruptedException {
        this.powertrainSystem = new PowertrainSystem(virtualFunctionBus, -9);
        setValues(0, 0, GearEnum.R);

        for (int i = 0; i < 1000; i++) {
            //Thread.sleep(25);
            powertrainSystem.loopTest();
        }
        assertEquals(CarSpecifications.MIN_REVERSE_SPEED, powertrainSystem.virtualFunctionBus.powertrainPacket.getSpeed(), 0.01);
    }

    @Test
    public void deccelerateFullBrakeTestForward() throws InterruptedException {
        powertrainSystem = new PowertrainSystem(virtualFunctionBus, 118);
        setValues(0, 80, GearEnum.D);

        powertrainSystem.loopTest();

        for (int i = 0; i < 1000; i++) {
            //Thread.sleep(25);
            powertrainSystem.loopTest();
        }
        assertEquals(0, powertrainSystem.virtualFunctionBus.powertrainPacket.getSpeed(), 0.01);
    }

    @Test
    public void deccelerateFullBrakeTestBackward() throws InterruptedException {
        powertrainSystem = new PowertrainSystem(virtualFunctionBus, -30);
        setValues(0, 80, GearEnum.R);

        powertrainSystem.loopTest();

        for (int i = 0; i < 500; i++) {
            //Thread.sleep(25);
            powertrainSystem.loopTest();
        }
        assertEquals(0, powertrainSystem.virtualFunctionBus.powertrainPacket.getSpeed(), 0.01);
    }

    @Test
    public void gearNTest() throws InterruptedException {
        powertrainSystem = new PowertrainSystem(virtualFunctionBus, 0);
        setValues(50, 0, GearEnum.N);

        for (int i = 0; i < 50; i++) {
            powertrainSystem.loopTest();
        }
        assertEquals(0, powertrainSystem.virtualFunctionBus.powertrainPacket.getSpeed(), 0.001);
    }

    @Test
    public void gearPTest() throws InterruptedException {
        powertrainSystem = new PowertrainSystem(virtualFunctionBus, 0);
        setValues(50, 0, GearEnum.N);

        for (int i = 0; i < 50; i++) {
            powertrainSystem.loopTest();
        }
        assertEquals(0, powertrainSystem.virtualFunctionBus.powertrainPacket.getSpeed(), 0.001);
    }

    @Test
    public void carCollideSlowingDownSpeedForward1() {
        powertrainSystem = new PowertrainSystem(virtualFunctionBus, 11.43245);
        setValues(50, 0, GearEnum.D);
        powertrainSystem.loopTest();

        PowertrainSystem.carCollide();
        assertEquals(8, powertrainSystem.virtualFunctionBus.powertrainPacket.getSpeed(), 0.01);
    }

    @Test
    public void carCollideSlowingDownSpeedForward2() {
        powertrainSystem = new PowertrainSystem(virtualFunctionBus, 5.124651);
        setValues(50, 0, GearEnum.D);
        powertrainSystem.loopTest();

        PowertrainSystem.carCollide();
        assertEquals(3.58, powertrainSystem.virtualFunctionBus.powertrainPacket.getSpeed(), 0.01);
    }

    @Test
    public void carCollideSlowingDownSpeedBackward1() {
        powertrainSystem = new PowertrainSystem(virtualFunctionBus, -11.43245);
        setValues(50, 0, GearEnum.D);
        powertrainSystem.loopTest();

        PowertrainSystem.carCollide();
        assertEquals(-8, powertrainSystem.virtualFunctionBus.powertrainPacket.getSpeed(), 0.01);
    }

    @Test
    public void carCollideSlowingDownSpeedBackward2() {
        powertrainSystem = new PowertrainSystem(virtualFunctionBus, -5.124651);
        setValues(50, 0, GearEnum.D);
        powertrainSystem.loopTest();

        PowertrainSystem.carCollide();
        assertEquals(-3.58, powertrainSystem.virtualFunctionBus.powertrainPacket.getSpeed(), 0.01);
    }
}