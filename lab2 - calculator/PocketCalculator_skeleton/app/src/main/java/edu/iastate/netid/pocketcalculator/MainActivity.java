package edu.iastate.netid.pocketcalculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    /**
     * The instance of the calculator model for use by this controller.
     */
    private final CalculationStream mCalculationStream = new CalculationStream();

    /*
     * The instance of the calculator display TextView. You can use this to update the calculator display.
     */
    private TextView mCalculatorDisplay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* TODO - uncomment the below line after you make your layout. This line locates
           the instance of the calculator display's TextView.  You need to create this TextView
           and set its ID to CalculatorDisplay in your layout resource file.
         */
        mCalculatorDisplay = findViewById(R.id.CalculatorDisplay);
    }

    /* TODO - add event listeners for your calculator's buttons. See the model's API to figure out
       what methods should be called. The equals button event listener has been done for you. Once
       you've created the layout, you'll need to add these methods as the onClick() listeners
       for the corresponding buttons in the XML layout. */

    /*
        Buttons by category:
    */

    //operands
    public void onClearClicked(View view) {
        try {
            mCalculationStream.clear();
        } finally {
            updateCalculatorDisplay();
        }
    }

    public void onEqualClicked(View view) {
        try {
            mCalculationStream.calculateResult();
        } finally {
            updateCalculatorDisplay();
        }
    }

    public void onSubtractionClicked(View view) {
        try {
            mCalculationStream.inputOperation(CalculationStream.Operation.SUBTRACT);
        } finally {
            updateCalculatorDisplay();
        }
    }

    public void onAdditionClicked(View view) {
        try {
            mCalculationStream.inputOperation(CalculationStream.Operation.ADD);
        } finally {
            updateCalculatorDisplay();
        }
    }

    public void onMultiplyClicked(View view) {
        try {
            mCalculationStream.inputOperation(CalculationStream.Operation.MULTIPLY);
        } finally {
            updateCalculatorDisplay();
        }
    }

    public void onDivideClicked(View view) {
        try {
            mCalculationStream.inputOperation(CalculationStream.Operation.DIVIDE);
        } finally {
            updateCalculatorDisplay();
        }
    }


    //numbers:
    public void onZeroClicked(View view) {
        try {
            mCalculationStream.inputDigit(CalculationStream.Digit.ZERO);
        } finally {
            updateCalculatorDisplay();
        }
    }


    public void onOneClicked(View view) {
        try {
            mCalculationStream.inputDigit(CalculationStream.Digit.ONE);
        } finally {
            updateCalculatorDisplay();
        }
    }

    public void onTwoClicked(View view) {
        try {
            mCalculationStream.inputDigit(CalculationStream.Digit.TWO);
        } finally {
            updateCalculatorDisplay();
        }
    }

    public void onThreeClicked(View view) {
        try {
            mCalculationStream.inputDigit(CalculationStream.Digit.THREE);
        } finally {
            updateCalculatorDisplay();
        }
    }

    public void onFourClicked(View view) {
        try {
            mCalculationStream.inputDigit(CalculationStream.Digit.FOUR);
        } finally {
            updateCalculatorDisplay();
        }
    }

    public void onFiveClicked(View view) {
        try {
            mCalculationStream.inputDigit(CalculationStream.Digit.FIVE);
        } finally {
            updateCalculatorDisplay();
        }
    }

    public void onSixClicked(View view) {
        try {
            mCalculationStream.inputDigit(CalculationStream.Digit.SIX);
        } finally {
            updateCalculatorDisplay();
        }
    }

    public void onSevenClicked(View view) {
        try {
            mCalculationStream.inputDigit(CalculationStream.Digit.SEVEN);
        } finally {
            updateCalculatorDisplay();
        }
    }

    public void onEightClicked(View view) {
        try {
            mCalculationStream.inputDigit(CalculationStream.Digit.EIGHT);
        } finally {
            updateCalculatorDisplay();
        }
    }

    public void onNineClicked(View view) {
        try {
            mCalculationStream.inputDigit(CalculationStream.Digit.NINE);
        } finally {
            updateCalculatorDisplay();
        }
    }

    public void onDecimalClicked(View view) {
        try {
            mCalculationStream.inputDigit(CalculationStream.Digit.DECIMAL);
        } finally {
            updateCalculatorDisplay();
        }
    }



    /**
     * Call this method after every button press to update the text display of your calculator.
     */
    public void updateCalculatorDisplay() {
        String value = getString(R.string.empty);
        try {
            value = Double.toString(mCalculationStream.getCurrentOperand());
        } catch(NumberFormatException e) {
            value = getString(R.string.error);
        } finally {
            // TODO: this value may need to be formatted first so it fits on one line... try 0.8 - 0.2
            mCalculatorDisplay.setText(value);
        }
    }
}
