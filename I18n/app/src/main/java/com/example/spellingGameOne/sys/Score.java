package com.example.spellingGameOne.sys;

import android.content.Context;
import android.renderscript.ScriptGroup;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
/**
 * Score class to keep track and manage score
 */
public class Score  {
    private int numberOfSquares;
    private boolean readOnce = false;
    private Context context;
    private int score, highScore;
    private String classFile;
    private FileReader fr = null;
    private BufferedReader br = null;
    private PrintWriter writer = null;


    public Score(int numberOfSquares, String classFile, Context context) {
        this.numberOfSquares = numberOfSquares;
        this.classFile = classFile;
        this.context = context;
        score = numberOfSquares;
        highScore = readHighScore();
    }

    public void decrement() {score--;}

    public void endGame() {
        if (score > readHighScore()) {
            writeToFile(score);
        }
    }

    private void writeToFile(int value) {
        String text = String.valueOf(value);
        System.out.println(context.getFilesDir().toString());
        try (FileOutputStream fos = context.openFileOutput(classFile, Context.MODE_PRIVATE)) {
            fos.write(text.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int readHighScore() {
        StringBuilder sb = new StringBuilder();
        try (FileInputStream fis = context.openFileInput(classFile)) {
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String text;

            while((text = br.readLine()) != null) {
                sb.append(text);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(!sb.toString().isEmpty()) {
            return Integer.parseInt(sb.toString());
        }
        return 0;
    }

    public int getScore() {
        return score;
    }

}
