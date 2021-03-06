package com.qualcomm.ftcrobotcontroller.opmodes.autonomous;

/**
 * Created by fendleyc on 12/11/15.
 */
public class CClimb extends StatefulAutonomous {
    @timed_state(duration = 5.05)
    public void aDriveToRamp(){
        tank.move(-0.25f,-0.25f);
    }

    @timed_state(duration = 0.8)
    public void bDriveToRamp(){
        tank.move(0.65f,0.65f);
    }

    @timed_state(duration = 1.05)
    public void cSpinToRamp(){
        tank.move(1, -1);
    }

    @timed_state(duration = .5)
    public void dSetServoHigh(){
        winchServo.setLocation(0.62);
    }

    @timed_state(duration = 8.8)
    public void eDeployWinch(){
        winchMotor.setPower(1);
        tank.move(0.1f,0.1f);
    }

    @timed_state(duration = .5)
    public void fSetServoLow(){
        winchServo.setLocation(.3);
        winchMotor.setPower(0);
    }

    @timed_state(duration = 3)
    public void gStartClimb(){
        winchMotor.setPower(-1);
    }

    @timed_state(duration = 6)
    public void hFinishClimb(){
        tank.move(0.4f, 0.4f);
        winchMotor.setPower(-1);
    }

    @timed_state(duration = 2.3)
    public void iEndAuto(){
        winchMotor.setPower(0);
        tank.move(0,0);
    }

}
