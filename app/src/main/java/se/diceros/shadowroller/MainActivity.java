package se.diceros.shadowroller;

import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.ImageButton;

import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements GridView.OnItemClickListener, View.OnClickListener{

    private TextToSpeech tts = null;
    private Random random = new Random();
    private int successes = 0;
    private int ones = 0;
    private int dice = 0;
    private boolean glitch = false;
    private boolean criticalGlitch = false;
    private String[] glitchComments = {"Oops!", "Well. That went well", "Would you like another go at it?"};
    private String[] criticalGlitchComments = {"Oh no. We are all going to die", "The fecal matter just hit the air circulation device", "Fail"};
    private String[] successComments = {"Not too bad", "You really wanted that one", "You go girl!", "You are on a roll!", "Very nice!", "Impressive"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener(){
            @Override
            public void onInit(int status) {

            }
        }
        );

        GridView grid = (GridView) findViewById(R.id.gridView);
        grid.setAdapter(new ButtonAdapter(getApplicationContext()));
        grid.setOnItemClickListener(this);

        ImageButton button = (ImageButton) findViewById(R.id.button);
        button.setOnClickListener(this);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


    }

    private void getResult(int newDice, boolean edge)
    {
        for (int i = 0; i < newDice; i++) {
            int result = 0;
            do {
                result = RollDie();
                if (result >= 5)
                    successes++;
                else if (result == 1)
                    ones++;
            } while ((result == 6) && edge);
        }

        if (ones > (dice / 2)) {
            if (successes == 0)
                criticalGlitch = true;
            else
                glitch = true;
        }
    }

    private String getGlitchComment()
    {
        return glitchComments[random.nextInt(glitchComments.length)];
    }

    private String getCriticalGlitchComment()
    {
        return criticalGlitchComments[random.nextInt(criticalGlitchComments.length)];
    }

    private String getSuccessComment()
    {
        return successComments[random.nextInt(successComments.length)];
    }

    private void updateResults() {
        String result = successes + " out of " + dice + " hits";
        String comment = "";

        if (criticalGlitch) {
            result += ". Critical glitch";
            comment = getCriticalGlitchComment();
        }
        else if (glitch) {
            result += ". Glitch";
            comment = getGlitchComment();
        }
        else if (successes > 8)
            comment = getSuccessComment();

        ((TextView) findViewById(R.id.textView)).setText(result);
        ((TextView) findViewById(R.id.textView2)).setText(comment);

        tts.setLanguage(Locale.US);
        tts.setPitch(1.3f);
        tts.setSpeechRate(1f);
        tts.speak(result + ". " + comment, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    private int RollDie() {
        return random.nextInt(6) + 1;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        boolean edge = ((CheckBox) findViewById(R.id.checkBox)).isChecked();
        ((CheckBox) findViewById(R.id.checkBox)).setChecked(false);

        dice = i + 1;
        successes = 0;
        ones = 0;
        criticalGlitch = false;
        glitch = false;

        getResult(dice, edge);
        updateResults();

        findViewById(R.id.button).setVisibility((edge?View.INVISIBLE:View.VISIBLE));


    }

    @Override
    public void onClick(View view) {
        ones = 0;
        getResult(dice - successes, false);
        updateResults();
        findViewById(R.id.button).setVisibility(View.INVISIBLE);
    }
}
