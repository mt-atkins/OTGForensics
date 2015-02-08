package com.dissertation.otgforensics.myapplication;


import android.content.Context;
import android.graphics.Color;
import android.os.Environment;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.security.Security;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import android.content.Intent;
import android.net.Uri;

import android.view.View.OnClickListener;

//#################  Fix the landscape to portrait Bug!! ####################\\
//#################  Fix the landscape to portrait Bug!! ####################\\
//#################  Fix the landscape to portrait Bug!! ####################\\

public class MainActivity extends ActionBarActivity {
//abstract HashGeneratorUtils();
HashGeneratorUtils myHashGeneratorUtils = new HashGeneratorUtils();
    Context foo;

//UsbCommunicationManager coms = new UsbCommunicationManager(foo);

private Button hashButton;
private Button imagingBtn;
//private ExpandableListView expListfh = (ExpandableListView) findViewById(R.id.expList);

    ArrayList<String> listItems=new ArrayList<String>();
    ArrayAdapter<String> adapter;
    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {

            Button ImgBtn = (Button) findViewById(R.id.ImagingBtn);
            ImgBtn.setBackgroundColor(Color.GREEN);
            return true;
        }
        //######
        // change icon or text label to indicate status
        //colour circle == green
        //colour circle if red by default
        //######



        return false;

    }

    @Override
 public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            addListenerOnButton();
            isExternalStorageReadable();




        }

 public void addListenerOnButton() {

       //Select a specific button to bundle it with the action you want
     imagingBtn = (Button) findViewById(R.id.ImagingBtn);

     imagingBtn.setOnClickListener(new OnClickListener()
     {
         @Override
         public void onClick(View view)
         {

         //declaration of the text view
             TextView TrgtDir;
             TrgtDir = (TextView)findViewById(R.id.TrgtDir);
             //creating a new object to connect the FindMntDir class to this one
             FindMntDrive mnt = new FindMntDrive();
             ReadFile fh = new ReadFile();
             //HashArray HashList = new HashArray();
             //declaration of string mntDir variable being pulled from FindMntDir

             String MntDir = mnt.DirPath;
             String filePath = fh.path;
             //outputs the dir the text field
             TrgtDir.setText("Dir: " + MntDir);
             //TrgtDir.setText("Dir; " + filePath);

             //DigestFile(result);
             //HashList.popList();
             //HashArray();
             List list = new ArrayList();
             //ReadFile objSample = new ReadFile();
             //objSample.setNewcompanyid("Any string you want");
             //expListfh. ;

               // Button ImgBtn = (Button) findViewById(R.id.ImagingBtn);
                //ImgBtn.setBackgroundColor(Color.GREEN);

             //ReadFromFile RFF = new ReadFromFile();
             //RFF.isExternalStorageReadable();

             copyFile CopyFile = new copyFile();
             try {
                 copyFile.main();
             } catch (IOException e) {
                 e.printStackTrace();
             }

         }
     });

     hashButton = (Button) findViewById(R.id.md5button);

     hashButton.setOnClickListener(new OnClickListener()
     {

                @Override
                public void onClick(View view)
                {
                    TextView md5hashOutput;
                    md5hashOutput = (TextView)findViewById(R.id.md5hashOutput);

                    TextView SHA256HashOutput;
                    SHA256HashOutput = (TextView)findViewById(R.id.SHA256HashOutput);

                    TextView SHA1HashOutput;
                    SHA1HashOutput = (TextView)findViewById(R.id.SHA1HashOutput);

                    String inputString = "hex string of file ";

                    try {
                    String md5Hash = HashGeneratorUtils.generateMD5(inputString);
                    md5hashOutput.setText("MD5 Hash: " + md5Hash);

                    String sha1Hash = HashGeneratorUtils.generateSHA1(inputString);
                    SHA1HashOutput.setText("SHA-1 Hash: " + sha1Hash);

                    String sha256Hash = HashGeneratorUtils.generateSHA256(inputString);
                    SHA256HashOutput.setText("SHA-256 Hash: " + sha256Hash);

                    //public String md5Hasha = md5Hash;
                }
                catch (HashGenerationException ex)
                    {
                        ex.printStackTrace();
                    }


                }

     });

        }


        public void HashGeneratorUtils() {

        }

        public static String generateMD5(String message) throws HashGenerationException {
            return hashString(message, "MD5");
        }

        public static String generateSHA1(String message) throws HashGenerationException {
            return hashString(message, "SHA-1");
        }

        public static String generateSHA256(String message) throws HashGenerationException {
            return hashString(message, "SHA-256");
        }

        public static String generateMD5(File file) throws HashGenerationException {
            return hashFile(file, "MD5");
        }

        public static String generateSHA1(File file) throws HashGenerationException {
            return hashFile(file, "SHA-1");
        }

        public static String generateSHA256(File file) throws HashGenerationException {
            return hashFile(file, "SHA-256");
        }

        private static String hashString(String message, String algorithm)
                throws HashGenerationException {

            try {
                MessageDigest digest = MessageDigest.getInstance(algorithm);
                byte[] hashedBytes = digest.digest(message.getBytes("UTF-8"));

                return convertByteArrayToHexString(hashedBytes);
            } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
                throw new HashGenerationException(
                        "Could not generate hash from String", ex);
            }
        }

        private static String hashFile(File file, String algorithm)
                throws HashGenerationException {
            try (FileInputStream inputStream = new FileInputStream(file)) {
                MessageDigest digest = MessageDigest.getInstance(algorithm);

                byte[] bytesBuffer = new byte[1024];
                int bytesRead = -1;

                while ((bytesRead = inputStream.read(bytesBuffer)) != -1) {
                    digest.update(bytesBuffer, 0, bytesRead);
                }

                byte[] hashedBytes = digest.digest();

                return convertByteArrayToHexString(hashedBytes);
            } catch (NoSuchAlgorithmException | IOException ex) {
                throw new HashGenerationException(
                        "Could not generate hash from file", ex);
            }
        }

        private static String convertByteArrayToHexString(byte[] arrayBytes) {
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < arrayBytes.length; i++) {
                stringBuffer.append(Integer.toString((arrayBytes[i] & 0xff) + 0x100, 16)
                        .substring(1));
            }
            return stringBuffer.toString();
        }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //@Override
 /** public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
  */
    //@Override
    public void md5button(View v)
    {
        TextView md5hashOutput;
        md5hashOutput = (TextView)findViewById(R.id.md5hashOutput);
        //MD5Digest();
        md5hashOutput.setText(md5hashOutput.getText());

    }
    public void ImagingBtn(View v)
    {


    }
}