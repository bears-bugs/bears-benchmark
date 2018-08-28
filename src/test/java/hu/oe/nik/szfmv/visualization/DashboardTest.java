package hu.oe.nik.szfmv.visualization;

import hu.oe.nik.szfmv.automatedcar.bus.packets.input.ReadOnlyInputPacket;
import hu.oe.nik.szfmv.automatedcar.input.enums.GearEnum;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class DashboardTest {

    private Dashboard dashboard = new Dashboard();
    private boolean gasPedalGetterCalled = false;
    private boolean breakPedalGetterCalled = false;

    /**
     * Sets all the boolean values that indicate method calls to false before the tests are run.
     */
    @Before
    public void setUp() {
        gasPedalGetterCalled = false;
        breakPedalGetterCalled = false;
    }

    /**
     * Tests whether all the correct getter methods have been called.
     */
    @Test
    public void allRequiredValuesReceivedOnUpdate() {
        InputPacketStub inputPacket = new InputPacketStub();
        dashboard.updateDisplayedValues(inputPacket);

        assertThat(gasPedalGetterCalled, is(true));
        assertThat(breakPedalGetterCalled, is(true));
    }

    class InputPacketStub implements ReadOnlyInputPacket {
        @Override
        public int getGasPedalPosition() {
            breakPedalGetterCalled = true;
            return 0;
        }

        @Override
        public int getBreakPedalPosition() {
            gasPedalGetterCalled = true;
            return 0;
        }

        @Override
        public double getSteeringWheelPosition() {
            return 0;
        }

        @Override
        public int getACCTargetSpeed() {
            return 0;
        }

        @Override
        public double getACCTargetDistance() {
            return 0;
        }

        @Override
        public boolean getLaneKeepingStatus() {
            return false;
        }

        @Override
        public boolean getParkingPilotStatus() {
            return false;
        }

        @Override
        public GearEnum getGearState() {
            return null;
        }

        @Override
        public boolean getLeftTurnSignalStatus() {
            return false;
        }

        @Override
        public boolean getRightTurnSignalStatus() {
            return false;
        }
    }
}