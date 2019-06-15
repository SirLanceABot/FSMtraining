public static class Constants
    {        
        public static final int FLOOR_BUTTON = 1;
        public static final int CARGO_SHIP_CARGO_BUTTON = 2;

        public static final int BOTTOM_HATCH_BUTTON = 8;
        public static final int CENTER_HATCH_BUTTON = 9;
        public static final int TOP_HATCH_BUTTON = 10;

        public static final int BOTTOM_CARGO_BUTTON = 3;
        public static final int CENTER_CARGO_BUTTON = 4;
        public static final int TOP_CARGO_BUTTON = 5;

        public static final int WRIST_BUTTON = 6;
        public static final int SAFE_POSITION_BUTTON = 7;
        
        public static final int CARRIAGE_BUTTON = 11;
        public static final int ARM_BUTTON = 12;

        public static final int X_AXIS = 0;
        public static final int Y_AXIS = 1;

        public static final int PORT = 1;
    }


public void buttonBoardControl()
    {
        armButton = buttonBoard.getRawButton(ButtonBoard.Constants.ARM_BUTTON);
        armButtonReleased = buttonBoard.getRawButtonReleased(ButtonBoard.Constants.ARM_BUTTON);
        carriageButton = buttonBoard.getRawButton(ButtonBoard.Constants.CARRIAGE_BUTTON);
        carriageButtonReleased = buttonBoard.getRawButtonReleased(ButtonBoard.Constants.CARRIAGE_BUTTON);
        buttonBoardYAxis = buttonBoard.getRawAxis(ButtonBoard.Constants.Y_AXIS);
        buttonBoardXAxis = buttonBoard.getRawAxis(ButtonBoard.Constants.X_AXIS);

        floorButton = buttonBoard.getRawButtonPressed(ButtonBoard.Constants.FLOOR_BUTTON);
        cargoShipCargoButton = buttonBoard.getRawButtonPressed(ButtonBoard.Constants.CARGO_SHIP_CARGO_BUTTON);

        bottomHatchButton = buttonBoard.getRawButtonPressed(ButtonBoard.Constants.BOTTOM_HATCH_BUTTON);
        centerHatchButton = buttonBoard.getRawButtonPressed(ButtonBoard.Constants.CENTER_HATCH_BUTTON);
        topHatchButton = buttonBoard.getRawButtonPressed(ButtonBoard.Constants.TOP_HATCH_BUTTON);

        bottomCargoButton = buttonBoard.getRawButtonPressed(ButtonBoard.Constants.BOTTOM_CARGO_BUTTON);
        centerCargoButton = buttonBoard.getRawButtonPressed(ButtonBoard.Constants.CENTER_CARGO_BUTTON);
        topCargoButton = buttonBoard.getRawButtonPressed(ButtonBoard.Constants.TOP_CARGO_BUTTON);

        wristButton = buttonBoard.getRawButtonPressed(ButtonBoard.Constants.WRIST_BUTTON);
        safePositionButton = buttonBoard.getRawButtonPressed(ButtonBoard.Constants.SAFE_POSITION_BUTTON);
    }



private boolean floorButton;
private boolean cargoShipCargoButton;

private boolean bottomHatchButton;
private boolean centerHatchButton;
private boolean topHatchButton;

private boolean bottomCargoButton;
private boolean centerCargoButton;
private boolean topCargoButton;

private boolean wristButton;
private boolean safePositionButton;

public enum ElevatorSystemState
    {
        kBottomHatch, kMiddleHatch, kTopHatch, kBottomCargo, kMiddleCargo, kTopCargo, kFloorPickup, kCargoShipCargo, kSafePosition, kManualOverride;
    }

if(safePositionButton)
{
	targetElevatorState = ElevatorSystemState.kSafePosition;
}
else if(floorButton)
{
	targetElevatorState = ElevatorSystemState.kFloorPickup;
}
else if(cargoShipCargoButton)
{
	targetElevatorState = ElevatorSystemState.kCargoShipCargo;
}
else if(bottomHatchButton)
{
	targetElevatorState = ElevatorSystemState.kBottomHatch;
}
else if(centerHatchButton)
{
	targetElevatorState = ElevatorSystemState.kMiddleHatch;
}
else if(topHatchButton)
{
	targetElevatorState = ElevatorSystemState.kTopHatch;
}
else if(bottomCargoButton)
{
	targetElevatorState = ElevatorSystemState.kBottomCargo;
}
else if(centerCargoButton)
{
	targetElevatorState = ElevatorSystemState.kMiddleCargo;
}
else if(topCargoButton)
{
	targetElevatorState = ElevatorSystemState.kTopCargo;
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public enum ElevatorSystemState
    {
        kBottomHatch,      kMiddleHatch,      kTopHatch,      kBottomCargo,      kMiddleCargo,      kTopCargo,      kFloorPickup kCargoShipCargo, kSafePosition, kManualOverride;
    }

public enum ElevatorSystemButton
    {
        BottomHatchButton, CenterHatchButton, TopHatchButton, BottomCargoButton, CenterCargoButton, TopCargoButton, FloorButton, kCargoShipCargo, SafePositionButton, ??else??;
    }

boolean[] ButtonState;
ElevatorSystemState targetElevatorState;

for(int idx=0; idx < ButtonState.length; idx++) ButtonState[idx] = false; // initialize all buttons to false - not pressed
...
// check the buttons for an event and save which state state goes with the event happened
for(int idx=0; idx < ButtonState.length; idx++) if(ButtonState[idx]) targetElevatorState = idx;

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public void carriageControl()
    {
        int currentPotValue = getPotValue();
        switch (currentState)
        {
        case kFloor:
            switch (targetState)
            {
            case kFloor:
                holdCarriage();
                currentState = CarriageState.kFloor;
                break;
            case kCargoShipCargo:
                raiseCarriage(scaleCarriageMovement());
                currentState = CarriageState.kMovingUp;
                break;
            case kBottomHatch:
                raiseCarriage(scaleCarriageMovement());
                currentState = CarriageState.kMovingUp;
                break;
            case kCenterHatch:
                raiseCarriage(scaleCarriageMovement());
                currentState = CarriageState.kMovingUp;
                break;
            case kTopHatch:
                raiseCarriage(scaleCarriageMovement());
                currentState = CarriageState.kMovingUp;
                break;
            case kBottomCargo:
                raiseCarriage(scaleCarriageMovement());
                currentState = CarriageState.kMovingUp;
                break;
            case kCenterCargo:
                raiseCarriage(scaleCarriageMovement());
                currentState = CarriageState.kMovingUp;
                break;
            case kTopCargo:
                raiseCarriage(scaleCarriageMovement());
                currentState = CarriageState.kMovingUp;
                break;
            case kMovingDown:
                holdCarriage();
                currentState = CarriageState.kFloor;
                break;
            case kMovingUp:
                raiseCarriage(scaleCarriageMovement());
                currentState = CarriageState.kMovingUp;
                break;
            case kNotMoving:
                holdCarriage();
                currentState = CarriageState.kNotMoving;
                break;
            case kManualOverride:
                currentState = CarriageState.kManualOverride;
                break;
            case kNone:
                holdCarriage();
                currentState = CarriageState.kNone;
                break;
            }
            break;
        case kCargoShipCargo:
            switch (targetState)
            {
            case kFloor:
                lowerCarriage(scaleCarriageMovement());
                currentState = CarriageState.kMovingDown;
                break;
            case kCargoShipCargo:
                holdCarriage();
                currentState = CarriageState.kCargoShipCargo;
                break;
            case kBottomHatch:
                lowerCarriage(scaleCarriageMovement());
                currentState = CarriageState.kMovingDown;
                break;
            case kCenterHatch:
                lowerCarriage(scaleCarriageMovement());
                currentState = CarriageState.kMovingDown;
                break;
            case kTopHatch:
                raiseCarriage(scaleCarriageMovement());
                currentState = CarriageState.kMovingUp;
                break;
            case kBottomCargo:
                lowerCarriage(scaleCarriageMovement());
                currentState = CarriageState.kMovingDown;
                break;
            case kCenterCargo:
                raiseCarriage(scaleCarriageMovement());
                currentState = CarriageState.kMovingUp;
                break;
            case kTopCargo:
                raiseCarriage(scaleCarriageMovement());
                currentState = CarriageState.kMovingUp;
                break;
            case kMovingDown:
                lowerCarriage(scaleCarriageMovement());
                currentState = CarriageState.kMovingDown;
                break;
            case kMovingUp:
                raiseCarriage(scaleCarriageMovement());
                currentState = CarriageState.kMovingUp;
                break;
            case kNotMoving:
                holdCarriage();
                currentState = CarriageState.kNotMoving;
                break;
            case kNone:
                holdCarriage();
                currentState = CarriageState.kNone;
                break;
            case kManualOverride:
                currentState = CarriageState.kManualOverride;
                break;
            }
            break;
        case kBottomHatch:
            switch (targetState)
            {
            case kFloor:
                lowerCarriage(scaleCarriageMovement());
                currentState = CarriageState.kMovingDown;
                break;
            case kCargoShipCargo:
                raiseCarriage(scaleCarriageMovement());
                currentState = CarriageState.kMovingUp;
                break;
            case kBottomHatch:
                holdCarriage();
                currentState = CarriageState.kBottomHatch;
                break;
            case kCenterHatch:
                raiseCarriage(scaleCarriageMovement());
                currentState = CarriageState.kMovingUp;
                break;
            case kTopHatch:
                raiseCarriage(scaleCarriageMovement());
                currentState = CarriageState.kMovingUp;
                break;
            case kBottomCargo:
                raiseCarriage(scaleCarriageMovement());
                currentState = CarriageState.kMovingUp;
                break;
            case kCenterCargo:
                raiseCarriage(scaleCarriageMovement());
                currentState = CarriageState.kMovingUp;
                break;
            case kTopCargo:
                raiseCarriage(scaleCarriageMovement());
                currentState = CarriageState.kMovingUp;
                break;
            case kMovingDown:
                lowerCarriage(scaleCarriageMovement());
                currentState = CarriageState.kMovingDown;
                break;
            case kMovingUp:
                raiseCarriage(scaleCarriageMovement());
                currentState = CarriageState.kMovingUp;
                break;
            case kNotMoving:
                holdCarriage();
                currentState = CarriageState.kNotMoving;
                break;
            case kNone:
                holdCarriage();
                currentState = CarriageState.kNone;
                break;
            case kManualOverride:
                currentState = CarriageState.kManualOverride;
                break;
            }
            break;
        case kCenterHatch:
            switch (targetState)
            {
            case kFloor:
                lowerCarriage(scaleCarriageMovement());
                currentState = CarriageState.kMovingDown;
                break;
            case kCargoShipCargo:
                raiseCarriage(scaleCarriageMovement());
                currentState = CarriageState.kMovingUp;
                break;
            case kBottomHatch:
                lowerCarriage(scaleCarriageMovement());
                currentState = CarriageState.kMovingDown;
                break;
            case kCenterHatch:
                holdCarriage();
                currentState = CarriageState.kCenterHatch;
                break;
            case kTopHatch:
                raiseCarriage(scaleCarriageMovement());
                currentState = CarriageState.kMovingUp;
                break;
            case kBottomCargo:
                lowerCarriage(scaleCarriageMovement());
                currentState = CarriageState.kMovingDown;
                break;
            case kCenterCargo:
                lowerCarriage(scaleCarriageMovement());
                currentState = CarriageState.kMovingDown;
                break;
            case kTopCargo:
                raiseCarriage(scaleCarriageMovement());
                currentState = CarriageState.kMovingUp;
                break;
            case kMovingDown:
                lowerCarriage(scaleCarriageMovement());
                currentState = CarriageState.kMovingDown;
                break;
            case kMovingUp:
                raiseCarriage(scaleCarriageMovement());
                currentState = CarriageState.kMovingUp;
                break;
            case kNotMoving:
                holdCarriage();
                currentState = CarriageState.kNotMoving;
                break;
            case kNone:
                holdCarriage();
                currentState = CarriageState.kNone;
                break;
            case kManualOverride:
                currentState = CarriageState.kManualOverride;
                break;
            }
            break;
        case kTopHatch:
            switch (targetState)
            {
            case kFloor:
                lowerCarriage(scaleCarriageMovement());
                currentState = CarriageState.kMovingDown;
                break;
            case kCargoShipCargo:
                lowerCarriage(scaleCarriageMovement());
                currentState = CarriageState.kMovingDown;
                break;
            case kBottomHatch:
                lowerCarriage(scaleCarriageMovement());
                currentState = CarriageState.kMovingDown;
                break;
            case kCenterHatch:
                lowerCarriage(scaleCarriageMovement());
                currentState = CarriageState.kMovingDown;
                break;
            case kTopHatch:
                holdCarriage();
                currentState = CarriageState.kTopHatch;
                break;
            case kBottomCargo:
                lowerCarriage(scaleCarriageMovement());
                currentState = CarriageState.kMovingDown;
                break;
            case kCenterCargo:
                lowerCarriage(scaleCarriageMovement());
                currentState = CarriageState.kMovingDown;
                break;
            case kTopCargo:
                raiseCarriage(scaleCarriageMovement());
                currentState = CarriageState.kMovingUp;
                break;
            case kMovingDown:
                lowerCarriage(scaleCarriageMovement());
                currentState = CarriageState.kMovingDown;
                break;
            case kMovingUp:
                raiseCarriage(scaleCarriageMovement());
                currentState = CarriageState.kMovingUp;
                break;
            case kNotMoving:
                holdCarriage();
                currentState = CarriageState.kNotMoving;
                break;
            case kNone:
                holdCarriage();
                currentState = CarriageState.kNone;
                break;
            case kManualOverride:
                currentState = CarriageState.kManualOverride;
                break;
            }
            break;
        case kBottomCargo:
            switch (targetState)
            {
            case kFloor:
                lowerCarriage(scaleCarriageMovement());
                currentState = CarriageState.kMovingDown;
                break;
            case kCargoShipCargo:
                raiseCarriage(scaleCarriageMovement());
                currentState = CarriageState.kMovingUp;
                break;
            case kBottomHatch:
                lowerCarriage(scaleCarriageMovement());
                currentState = CarriageState.kMovingDown;
                break;
            case kCenterHatch:
                raiseCarriage(scaleCarriageMovement());
                currentState = CarriageState.kMovingUp;
                break;
            case kTopHatch:
                raiseCarriage(scaleCarriageMovement());
                currentState = CarriageState.kMovingUp;
                break;
            case kBottomCargo:
                holdCarriage();
                currentState = CarriageState.kBottomCargo;
                break;
            case kCenterCargo:
                raiseCarriage(scaleCarriageMovement());
                currentState = CarriageState.kMovingUp;
                break;
            case kTopCargo:
                raiseCarriage(scaleCarriageMovement());
                currentState = CarriageState.kMovingUp;
                break;
            case kMovingDown:
                lowerCarriage(scaleCarriageMovement());
                currentState = CarriageState.kMovingDown;
                break;
            case kMovingUp:
                raiseCarriage(scaleCarriageMovement());
                currentState = CarriageState.kMovingUp;
                break;
            case kNotMoving:
                holdCarriage();
                currentState = CarriageState.kNotMoving;
                break;
            case kNone:
                holdCarriage();
                currentState = CarriageState.kNone;
                break;
            case kManualOverride:
                currentState = CarriageState.kManualOverride;
                break;
            }
            break;
        case kCenterCargo:
            switch (targetState)
            {
            case kFloor:
                lowerCarriage(scaleCarriageMovement());
                currentState = CarriageState.kMovingDown;
                break;
            case kCargoShipCargo:
                lowerCarriage(scaleCarriageMovement());
                currentState = CarriageState.kMovingDown;
                break;
            case kBottomHatch:
                lowerCarriage(scaleCarriageMovement());
                currentState = CarriageState.kMovingDown;
                break;
            case kCenterHatch:
                lowerCarriage(scaleCarriageMovement());
                currentState = CarriageState.kMovingDown;
                break;
            case kTopHatch:
                raiseCarriage(scaleCarriageMovement());
                currentState = CarriageState.kMovingUp;
                break;
            case kBottomCargo:
                lowerCarriage(scaleCarriageMovement());
                currentState = CarriageState.kMovingDown;
                break;
            case kCenterCargo:
                holdCarriage();
                currentState = CarriageState.kCenterCargo;
                break;
            case kTopCargo:
                raiseCarriage(scaleCarriageMovement());
                currentState = CarriageState.kMovingUp;
                break;
            case kMovingDown:
                lowerCarriage(scaleCarriageMovement());
                currentState = CarriageState.kMovingDown;
                break;
            case kMovingUp:
                raiseCarriage(scaleCarriageMovement());
                currentState = CarriageState.kMovingUp;
                break;
            case kNotMoving:
                holdCarriage();
                currentState = CarriageState.kNotMoving;
                break;
            case kNone:
                holdCarriage();
                currentState = CarriageState.kNone;
                break;
            case kManualOverride:
                currentState = CarriageState.kManualOverride;
                break;
            }
            break;
        case kTopCargo:
            switch (targetState)
            {
            case kFloor:
                lowerCarriage(scaleCarriageMovement());
                currentState = CarriageState.kMovingDown;
                break;
            case kCargoShipCargo:
                lowerCarriage(scaleCarriageMovement());
                currentState = CarriageState.kMovingDown;
                break;
            case kBottomHatch:
                lowerCarriage(scaleCarriageMovement());
                currentState = CarriageState.kMovingDown;
                break;
            case kCenterHatch:
                lowerCarriage(scaleCarriageMovement());
                currentState = CarriageState.kMovingDown;
                break;
            case kTopHatch:
                lowerCarriage(scaleCarriageMovement());
                currentState = CarriageState.kMovingDown;
                break;
            case kBottomCargo:
                lowerCarriage(scaleCarriageMovement());
                currentState = CarriageState.kMovingDown;
                break;
            case kCenterCargo:
                lowerCarriage(scaleCarriageMovement());
                currentState = CarriageState.kMovingDown;
                break;
            case kTopCargo:
                holdCarriage();
                currentState = CarriageState.kTopCargo;
                break;
            case kMovingDown:
                lowerCarriage(scaleCarriageMovement());
                currentState = CarriageState.kMovingDown;
                break;
            case kMovingUp:
                holdCarriage();
                currentState = CarriageState.kTopCargo;
                break;
            case kNotMoving:
                holdCarriage();
                currentState = CarriageState.kNotMoving;
                break;
            case kNone:
                holdCarriage();
                currentState = CarriageState.kNone;
                break;
            case kManualOverride:
                currentState = CarriageState.kManualOverride;
                break;
            }
            break;
        case kMovingDown:
            if(targetState != CarriageState.kManualOverride)
            {
                if (currentPotValue > (carriagePositionPotValues[targetState.value] + Constants.CARRIAGE_THRESHOLD))
                {
                    lowerCarriage(scaleCarriageMovement());
                    currentState = CarriageState.kMovingDown;
                }
                else if (currentPotValue < (carriagePositionPotValues[targetState.value] - Constants.CARRIAGE_THRESHOLD))
                {
                    raiseCarriage(scaleCarriageMovement());
                    currentState = CarriageState.kMovingUp;
                }
                else
                {
                    setCarriageStartingPosition = true;
                    holdCarriage();
                    currentState = targetState;
                }
                break;
            }
            else
            {
                holdCarriage();
                currentState = CarriageState.kManualOverride;
            }
        case kMovingUp:
            if(targetState != CarriageState.kManualOverride)
            {
                if (currentPotValue > (carriagePositionPotValues[targetState.value] + Constants.CARRIAGE_THRESHOLD))
                {
                    lowerCarriage(scaleCarriageMovement());
                    currentState = CarriageState.kMovingDown;
                }
                else if (currentPotValue < (carriagePositionPotValues[targetState.value] - Constants.CARRIAGE_THRESHOLD))
                {
                    raiseCarriage(scaleCarriageMovement());
                    currentState = CarriageState.kMovingUp;
                }
                else
                {
                    setCarriageStartingPosition = true;
                    holdCarriage();
                    currentState = targetState;
                }
            }
            else
            {
                holdCarriage();
                currentState = CarriageState.kManualOverride;
            }
            break;
        case kNotMoving:
            holdCarriage();
            currentState = CarriageState.kNotMoving;
            break;
        case kManualOverride:
            switch (targetState)
            {
            case kFloor:
                currentState = CarriageState.kMovingUp;
                break;
            case kCargoShipCargo:
                currentState = CarriageState.kMovingUp;
                break;
            case kBottomHatch:
                currentState = CarriageState.kMovingUp;
                break;
            case kCenterHatch:
                currentState = CarriageState.kMovingUp;
                break;
            case kTopHatch:
                currentState = CarriageState.kMovingUp;
                break;
            case kBottomCargo:
                currentState = CarriageState.kMovingUp;
                break;
            case kCenterCargo:
                currentState = CarriageState.kMovingUp;
                break;
            case kTopCargo:
                currentState = CarriageState.kMovingUp;
                break;
            case kMovingDown:
                currentState = CarriageState.kNotMoving;
                break;
            case kMovingUp:
                currentState = CarriageState.kNotMoving;
                break;
            case kNotMoving:
                holdCarriage();
                currentState = CarriageState.kNotMoving;
                break;
            case kNone:
                holdCarriage();
                currentState = CarriageState.kNone;
                break;
            case kManualOverride:
                currentState = CarriageState.kManualOverride;
                break;
            }
            break;
        }
    }
