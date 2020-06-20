package com.example.fileconverter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class SelectionActivity extends AppCompatActivity {

    public void help1(View view){
        Toast.makeText(this, "This will extract the words and separate them with commas", Toast.LENGTH_SHORT).show();
    }
    public void help2(View view){
        Toast.makeText(this, "This will extract Que no,Que and marks from Question papers of general format and separate them with commas", Toast.LENGTH_SHORT).show();

    }
    public void help3(View view){
        Toast.makeText(this, "This will extract Que no,Que,marks and COs from Question paper having format like WCE Question papers and separate them with commas", Toast.LENGTH_SHORT).show();

    }

    private static final int WRITE_EXTERNAL_STORAGE_CODE=1;


    private static final int PERMISSION_REQUEST_STORAGE=1000;
    private static final int READ_REQUEST_CODE=42;

    String kFileName="/Conv_File.txt";
    StringBuilder mtext,lmtext,klmtext;
    TextView textView11;
    Button b_load;
    TextView fileName;

    public void format1Opr(View view){
        mtext =new StringBuilder();
        File sdcard = Environment.getExternalStorageDirectory();
        File dir = new File(sdcard.getAbsolutePath() + "/text/");
        if (dir.exists()) {
            File ffile = new File(dir, "sample1.txt");
            FileOutputStream os = null;

            try {
                BufferedReader br = new BufferedReader(new FileReader(ffile));
                String fline;
                int i;
                char ch, prev_ch = ' ';
                boolean b = false;
                while ((i = br.read()) != (-1)) {
                    ch = (char) i;
                    if ((i >= 65 && i <= 90) || (i >= 97 && i <= 122)) {
                        fline = String.valueOf(ch);
                        mtext.append(fline);
                        b = true;
                    } else {
                        if (b == true) {
                            if (ch == '.' || ch == '?' || ch == ' '||ch==',') {
                                mtext.append(',');
                                b = false;
                            } else {
                                if (ch == '\n') {
                                    mtext.append(',');
                                    mtext.append('\n');
                                    b = false;
                                }
                            }
                        } else {
                            if (ch == '\n') {
                                mtext.append('\n');
                            }
                        }
                    }
                }
                br.close();
            } catch (Exception e) {
            }
        }
        fileName=findViewById(R.id.fileName);

        if(!(fileName.getText().toString().matches(""))){
            kFileName="/"+fileName.getText().toString()+".txt";
        }

        File kfile=Environment.getExternalStorageDirectory();
        String strFilePath=kfile.getAbsoluteFile()+ kFileName;
        try{
            FileOutputStream fileOutputStream=new FileOutputStream(strFilePath);
            FileWriter fileWriter=new FileWriter(fileOutputStream.getFD());
            fileWriter.write(mtext.toString());
            fileWriter.close();
            fileOutputStream.getFD().sync();
            fileOutputStream.close();

        }catch (IOException e){
            e.printStackTrace();
        }

        Toast.makeText(this, "File Conversion is Successful!", Toast.LENGTH_SHORT).show();

    }

    public void format2Opr(View view){
        lmtext=new StringBuilder();
        File lsdcard = Environment.getExternalStorageDirectory();
        File ldir=new File(lsdcard.getAbsolutePath()+"/text/");
        if(ldir.exists()) {
            File lffile = new File(ldir, "sample2.txt");
            FileOutputStream los = null;

            try {
                BufferedReader lbr = new BufferedReader(new FileReader(lffile));
                String lfline;
                String tmp;
                int li;
                boolean isnew=true,no_end=false,q_start=false;
                int count=0;
                char lch,prev='0',pprev='0',ppprev='0';
                boolean b = false;
                while ((li = lbr.read()) != (-1)) {
                    lch = (char) li;
                    lfline=String.valueOf(lch);
                    if(!q_start){
                        if(li>=48&&li<=57){
                            ppprev=lch;
                            pprev=(char)lbr.read();
                            prev=(char)lbr.read();
                            q_start=true;
                        }else{
                            tmp=lbr.readLine();
                        }
                    }else{
                        if(isnew){
                            if(ppprev>=48&&pprev<=57){
                                lmtext.append(ppprev);
                                ppprev=pprev;
                                pprev=prev;
                                prev=lch;
                                isnew=false;
                            }else{
                                lmtext.append(',');
                                lmtext.append(ppprev);
                                lmtext.append(pprev);
                                lmtext.append(prev);
                                lmtext.append(lch);
                                lfline=lbr.readLine();
                                lmtext.append(lfline);
                                lmtext.append(",,\n");
                                ppprev=(char)lbr.read();
                                pprev=(char)lbr.read();
                                prev=(char)lbr.read();

                            }

                        }else{
                            if(!no_end){
                                if(ppprev>=48&&ppprev<=57){
                                    lmtext.append(ppprev);
                                    ppprev=pprev;
                                    pprev=prev;
                                    prev=lch;
                                }else{
                                    lmtext.append(',');
                                    ppprev=pprev;
                                    pprev=prev;
                                    prev=lch;
                                    no_end=true;
                                }
                            }else{
                                if(lch!='\n'){
                                    lmtext.append(ppprev);
                                    ppprev=pprev;
                                    pprev=prev;
                                    prev=lch;
                                }else{
                                    if(!(ppprev>=48&&pprev<=57)){
                                        lmtext.append(ppprev);
                                        lmtext.append(',');
                                        lmtext.append(pprev);
                                        lmtext.append(',');
                                        lmtext.append(lch);
                                        isnew=true;
                                        no_end=false;
                                        ppprev=(char)lbr.read();
                                        pprev=(char)lbr.read();
                                        prev=(char)lbr.read();

                                    }else{
                                        lmtext.append(',');
                                        lmtext.append(ppprev);
                                        lmtext.append(pprev);
                                        lmtext.append(',');
                                        lmtext.append(lch);
                                        isnew=true;
                                        no_end=false;
                                        ppprev=(char)lbr.read();
                                        pprev=(char)lbr.read();
                                        prev=(char)lbr.read();
                                    }
                                }
                            }
                        }
                    }
                }

                lbr.close();
            } catch (Exception e) {
                Toast.makeText(this, "Error occured.", Toast.LENGTH_SHORT).show();
            }
        }
        fileName=findViewById(R.id.fileName);

        if(!(fileName.getText().toString().matches(""))){
            kFileName="/"+fileName.getText().toString()+".txt";
        }
        else{
            kFileName="/Conv_File2.txt";
        }
        File lkfile=Environment.getExternalStorageDirectory();
        String lstrFilePath=lkfile.getAbsoluteFile()+ kFileName;
        try{
            FileOutputStream lfileOutputStream=new FileOutputStream(lstrFilePath);
            FileWriter lfileWriter=new FileWriter(lfileOutputStream.getFD());
            lfileWriter.write(lmtext.toString());
            lfileWriter.close();
            lfileOutputStream.getFD().sync();
            lfileOutputStream.close();

        }catch (IOException e){
            e.printStackTrace();
        }
        Toast.makeText(this, "File Conversion is Successful", Toast.LENGTH_SHORT).show();

    }

    public void format3Opr(View view){
        klmtext=new StringBuilder();
        File klsdcard = Environment.getExternalStorageDirectory();
        File kldir=new File(klsdcard.getAbsolutePath()+"/text/");
        if(kldir.exists()) {
            File klffile = new File(kldir, "sample3.txt");
            FileOutputStream klos = null;

            try {
                BufferedReader klbr = new BufferedReader(new FileReader(klffile));
                String klfline;
                String ktmp;
                int kli;
                boolean kisnew=true,kno_end=false,kq_start=false;
                int kcount=0;
                char klch,p1='0',p2='0',p3='0',p4='0',p5='0',p6='0',p7='0';
                while ((kli = klbr.read()) != (-1)) {
                    klch = (char) kli;
                    klfline=String.valueOf(klch);
                    if(!kq_start){
                        if(kli=='Q'){
                            p7=klch;
                            p6=(char)klbr.read();
                            p5=(char)klbr.read();
                            p4=(char)klbr.read();
                            p3=(char)klbr.read();
                            p2=(char)klbr.read();
                            p1=(char)klbr.read();
                            kq_start=true;
                        }else{
                            ktmp=klbr.readLine();
                        }
                    }else{
                        if(kisnew){
                            if(p7=='Q'){
                                klmtext.append(p7);p7=p6;
                                p6=p5;p5=p4;p4=p3;p3=p2;p2=p1;
                                p1=klch;
                                kisnew=false;
                            }else{
                                klmtext.append(',');
                                klmtext.append(p7);
                                klmtext.append(p6);klmtext.append(p5);
                                klmtext.append(p4);klmtext.append(p3);
                                klmtext.append(p2);klmtext.append(p1);
                                klmtext.append(klch);
                                klfline=klbr.readLine();
                                klmtext.append(klfline);
                                klmtext.append(",,,\n");
                                p7=(char)klbr.read();
                                p6=(char)klbr.read();
                                p5=(char)klbr.read();
                                p4=(char)klbr.read();
                                p3=(char)klbr.read();
                                p2=(char)klbr.read();
                                p1=(char)klbr.read();

                            }

                        }else{
                            if(!kno_end){
                                if(p7!=')'){
                                    klmtext.append(p7);p7=p6;
                                    p6=p5;p5=p4;p4=p3;p3=p2;p2=p1;
                                    p1=klch;
                                }else{
                                    klmtext.append(',');p7=p6;
                                    p6=p5;p5=p4;p4=p3;p3=p2;p2=p1;
                                    p1=klch;
                                    kno_end=true;
                                }
                            }else{
                                if(klch!='\n'){
                                    klmtext.append(p7);p7=p6;
                                    p6=p5;p5=p4;p4=p3;p3=p2;p2=p1;
                                    p1=klch;
                                }else{
                                    klmtext.append(',');
                                    klmtext.append(p7);
                                    klmtext.append(p6);
                                    klmtext.append(',');
                                    klmtext.append(p5);
                                    klmtext.append(p4);
                                    klmtext.append(p3);
                                    klmtext.append(p2);
                                    klmtext.append(',');
                                    klmtext.append('\n');p7=(char)klbr.read();
                                    p6=(char)klbr.read();p5=(char)klbr.read();
                                    p4=(char)klbr.read();p3=(char)klbr.read();
                                    p2=(char)klbr.read();p1=(char)klbr.read();
                                    kisnew=true;kno_end=false;
                                }
                            }
                        }
                    }
                }

                klbr.close();
            } catch (Exception e) {
                Toast.makeText(this, "Error occured.", Toast.LENGTH_SHORT).show();
            }
        }

        fileName=findViewById(R.id.fileName);

        if(!(fileName.getText().toString().matches(""))){
            kFileName="/"+fileName.getText().toString()+".txt";
        }
        else{
            kFileName="/Conv_File3.txt";
        }
        File klkfile=Environment.getExternalStorageDirectory();
        String klstrFilePath=klkfile.getAbsoluteFile()+ kFileName;
        try{
            FileOutputStream klfileOutputStream=new FileOutputStream(klstrFilePath);
            FileWriter klfileWriter=new FileWriter(klfileOutputStream.getFD());
            klfileWriter.write(klmtext.toString());
            klfileWriter.close();
            klfileOutputStream.getFD().sync();
            klfileOutputStream.close();

        }catch (IOException e){
            e.printStackTrace();
        }
       Toast.makeText(this, "File Conversion is Successful", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PERMISSION_REQUEST_STORAGE);
        }
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},101);
            }
        }

        b_load =(Button)findViewById(R.id.b_load);

        b_load.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                performFileSearch();
                
            }
        });


    }

    private void performFileSearch(){
        Intent intent =new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/*");
        startActivityForResult(intent,READ_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
       /* mtext =new StringBuilder();
        if((requestCode==READ_REQUEST_CODE) && (resultCode== Activity.RESULT_OK)) {
            if (data != null) {
                Uri uri = data.getData();
                String path = uri.getPath();
                path = path.substring(path.indexOf(":") + 1);
                if (path.contains("emulated")) {
                    path = path.substring(path.indexOf("0") + 1);

                }
                String ftext;

                File sdcard = Environment.getExternalStorageDirectory();
                File dir = new File(sdcard.getAbsolutePath() + "/text/");
                if (dir.exists()) {
                    File ffile = new File(dir, "sample.txt");
                    FileOutputStream os = null;

                    try {
                        BufferedReader br = new BufferedReader(new FileReader(ffile));
                        String fline;
                        int i;
                        char ch,prev_ch=' ';
                        boolean b = false;
                        while ((i = br.read()) != (-1)) {
                            ch = (char) i;
                            if ((i >= 65 && i <= 90) || (i >= 97 && i <= 122)) {
                                fline = String.valueOf(ch);
                                mtext.append(fline);
                                b = true;
                            } else {
                                if (b == true) {
                                    if (ch == '.' || ch == '?' || ch == ' ') {
                                        mtext.append(',');
                                        b = false;
                                    } else {
                                        if (ch == '\n') {
                                            mtext.append(',');
                                            mtext.append('\n');
                                            b = false;
                                        }
                                    }
                                }else{
                                    if(ch=='\n'){
                                        mtext.append('\n');
                                    }
                                }
                            }
                        }
                        br.close();
                       // Toast.makeText(this, "File conversion is Successful!", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                       // Toast.makeText(this, "Error occured.", Toast.LENGTH_SHORT).show();

                    }
                }

            }
        }*/


       /* if((requestCode==READ_REQUEST_CODE) && (resultCode== Activity.RESULT_OK)){
            if(data!=null) {
                Uri uri = data.getData();
                String path = uri.getPath();
                path = path.substring(path.indexOf(":") + 1);
                if (path.contains("emulated")) {
                    path = path.substring(path.indexOf("0") + 1);

                }

                lmtext=new StringBuilder();
                File lsdcard = Environment.getExternalStorageDirectory();
                File ldir=new File(lsdcard.getAbsolutePath()+"/text/");
                if(ldir.exists()) {
                    File lffile = new File(ldir, "sample.txt");
                    FileOutputStream los = null;

                    try {
                        BufferedReader lbr = new BufferedReader(new FileReader(lffile));
                        String lfline;
                        String tmp;
                        int li;
                        boolean isnew=true,no_end=false,q_start=false;
                        int count=0;
                        char lch,prev='0',pprev='0',ppprev='0';
                        boolean b = false;
                        while ((li = lbr.read()) != (-1)) {
                            lch = (char) li;
                            lfline=String.valueOf(lch);
                            if(!q_start){
                                if(li>=48&&li<=57){
                                    ppprev=lch;
                                    pprev=(char)lbr.read();
                                    prev=(char)lbr.read();
                                    q_start=true;
                                }else{
                                    tmp=lbr.readLine();
                                }
                            }else{
                                if(isnew){
                                    if(ppprev>=48&&pprev<=57){
                                        lmtext.append(ppprev);
                                        ppprev=pprev;
                                        pprev=prev;
                                        prev=lch;
                                        isnew=false;
                                    }else{
                                       lmtext.append(',');
                                       lmtext.append(ppprev);
                                       lmtext.append(pprev);
                                       lmtext.append(prev);
                                       lmtext.append(lch);
                                       lfline=lbr.readLine();
                                       lmtext.append(lfline);
                                       lmtext.append(",,\n");
                                       ppprev=(char)lbr.read();
                                       pprev=(char)lbr.read();
                                       prev=(char)lbr.read();

                                    }

                                }else{
                                    if(!no_end){
                                        if(ppprev>=48&&ppprev<=57){
                                            lmtext.append(ppprev);
                                            ppprev=pprev;
                                            pprev=prev;
                                            prev=lch;
                                        }else{
                                            lmtext.append(',');
                                            ppprev=pprev;
                                            pprev=prev;
                                            prev=lch;
                                            no_end=true;
                                        }
                                    }else{
                                        if(lch!='\n'){
                                            lmtext.append(ppprev);
                                            ppprev=pprev;
                                            pprev=prev;
                                            prev=lch;
                                        }else{
                                            if(!(ppprev>=48&&pprev<=57)){
                                                lmtext.append(ppprev);
                                                lmtext.append(',');
                                                lmtext.append(pprev);
                                                lmtext.append(prev);
                                                lmtext.append(lch);
                                                isnew=true;
                                                no_end=false;
                                                ppprev=(char)lbr.read();
                                                pprev=(char)lbr.read();
                                                prev=(char)lbr.read();

                                            }else{
                                                lmtext.append(',');
                                                lmtext.append(ppprev);
                                                lmtext.append(pprev);
                                                lmtext.append(prev);
                                                lmtext.append(lch);
                                                isnew=true;
                                                no_end=false;
                                                ppprev=(char)lbr.read();
                                                pprev=(char)lbr.read();
                                                prev=(char)lbr.read();
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        lbr.close();
                        Toast.makeText(this, "File conversion is Successful!", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                    Toast.makeText(this, "Error occured.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }*/


    }





    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==PERMISSION_REQUEST_STORAGE){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permissons granted!", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Permissions not granted!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}
