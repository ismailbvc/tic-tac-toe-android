package com.elhardoum.tictactoe;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private boolean playerX = true;
    private final String[] grid = {
            null, null, null,
            null, null, null,
            null, null, null,
    };
    private boolean ended = false;
    private String winner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();

        TableLayout table = findViewById(R.id.main_table);
        final int numrows = table.getChildCount();

        for (int i=0; i<numrows; i++) {
            TableRow row = (TableRow) table.getChildAt(i);
            final int layoutcount = row.getChildCount();
            for (int i2=0; i2<layoutcount; i2++) {
                LinearLayout wrap = (LinearLayout) row.getChildAt(i2);
                ImageView icon = (ImageView) wrap.getChildAt(0);
                clickable(icon, i*3 + i2);
            }
        }
    }

    private void clickable(ImageView icon, final int index)
    {
        icon.setOnClickListener(ref -> {
            if ( ended ) {
                if ( null != winner ) {
                    dialog(String.format("%s wins!", winner.toUpperCase(Locale.ROOT)));
                } else {
                    dialog("Ended without a winner.");
                }
                return;
            }

            if ( null != grid[index] ) // claimed
                return;

            grid[index] = playerX ? "x" : "o";
            icon.setImageResource(playerX ? R.drawable.cross : R.drawable.circle);
            icon.setScaleX(0.5f);
            icon.setScaleY(0.5f);
            icon.animate().scaleX(.8f).scaleY(.8f);
            playerX = ! playerX;

            winner = getWinner();
            checkIfEndedWithoutWinner();

            if ( null != winner ) {
                ended = true;
                playerX = winner.equals("x"); // set next player as winner
                dialog(String.format("%s wins!", winner.toUpperCase(Locale.ROOT)));
            } else if ( ended ) {
                dialog("Ended without a winner.");
            }

        });
    }

    private String getWinner()
    {
        // horizontal
        if ( null != grid[0] && grid[0].equals(grid[1]) && grid[1].equals(grid[2]) )
            return grid[0];

        if ( null != grid[3] && grid[3].equals(grid[4]) && grid[4].equals(grid[5]) )
            return grid[3];

        if ( null != grid[6] && grid[6].equals(grid[7]) && grid[7].equals(grid[8]) )
            return grid[6];

        // vertical
        if ( null != grid[0] && grid[0].equals(grid[3]) && grid[3].equals(grid[6]) )
            return grid[0];

        if ( null != grid[1] && grid[1].equals(grid[4]) && grid[4].equals(grid[7]) )
            return grid[1];

        if ( null != grid[2] && grid[2].equals(grid[5]) && grid[5].equals(grid[8]) )
            return grid[2];

        // diagonal
        if ( null != grid[0] && grid[0].equals(grid[4]) && grid[4].equals(grid[8]) )
            return grid[0];

        if ( null != grid[2] && grid[2].equals(grid[4]) && grid[4].equals(grid[6]) )
            return grid[2];

        return null;
    }

    private void checkIfEndedWithoutWinner()
    {
        int free = 0;

        for (String s : grid) free += null == s ? 1 : 0;

        if ( 0 == free )
            ended = true;
    }

    private void dialog(String title)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(title);
        builder.setPositiveButton("OK", null);
        builder.setNegativeButton("RESTART", (dialog, which) -> restart());
        builder.create().show();
    }

    private void restart()
    {
        finish();
        startActivity(getIntent());
    }
}