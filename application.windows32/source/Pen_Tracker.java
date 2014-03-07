import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Pen_Tracker extends PApplet {

String[] rawStringFromLog;

public void loadLog()
{
  rawStringFromLog = loadStrings("log.txt"); 
  for(int i = 0; i+1 < rawStringFromLog.length; i++)
  {
    String line0 = rawStringFromLog[i];
    String line1 = rawStringFromLog[i+1];
    String[] pieces0 = split(line0, ',');
    String[] pieces1 = split(line1, ',');
          
    if(pieces0 != null && pieces1 != null)     //Trim out the raw x,y.Not the android pad x,y. The string INPUT in log represent it is android pad log.
    {  
      int xValue0 = Integer.parseInt(pieces0[0]);
      int yValue0 = Integer.parseInt(pieces0[1]);
      int x0 = (xValue0 + 15120)/96;
      int y0 = yValue0/96;
    
      int xValue1 = Integer.parseInt(pieces1[0]);
      int yValue1 = Integer.parseInt(pieces1[1]);
      int x1 = (xValue1 + 15120)/96;
      int y1 = yValue1/96;
      //Connect every point with line
      stroke(0,255,0);
//      line(x0,y0,x1,y1);
      //Draw out the point with yellow
      stroke(0xffEEEE00);
      point(x0,y0);
//      drawOutThisPoint(x0,y0);
    }
  }
  //Draw the last point with RED circle
  String lineEnd = rawStringFromLog[rawStringFromLog.length - 1];
  String[] piecesEnd = split(lineEnd, ',');
  int xValueEnd = Integer.parseInt(piecesEnd[0]);
  int yValueEnd = Integer.parseInt(piecesEnd[1]);
  int xEnd = (xValueEnd + 15120)/96;
  int yEnd = yValueEnd/96;
  stroke(255,0,0);
  drawOutThisPoint(xEnd,yEnd);
}

public void setup()
{
  size(1216,926);
  background(0,0,0); 
  textSize(18);
  loadLog();
  //Draw the central line in x.The area in the left of this line => x<0.
  stroke(126);
  line(159,0,159,225);
  line(316,0,316,225);
  line(0,225,316,225);
}

public void draw()
{
  //for msg loop,do not remove this empty function
}

public void drawOutThisPoint(int xValue, int yValue)
{
  ellipse(xValue,yValue,2,2);
}

//Get the xy's depth
public void mouseMoved()
{
  int x = 0;
  int y = 0;
  if(mouseX < 316 && mouseY <= 225){
    x = mouseX*96 - 15120;
    y = mouseY*96;
  }
  else{
    x = 0;
    y = 0;
  }
  //Text out the (x,y)
  String str = "(" + x + "," + y + ")";
  fill(0,0,0);
  noStroke();
  rectMode(CENTER);
  rect(width - 80, height - 30, 140, 22);
  fill(0, 100, 305, 204);
  text(str, width - 150, height - 22);
}

public void mousePressed()
{
  if(mouseX < 316 && mouseY <= 225){
    rectMode(CORNER);
    rect(mouseX,mouseY,36,28);
    rawStringFromLog = loadStrings("log.txt"); 
    for(int i = 0; i < rawStringFromLog.length; i++)
    {
      String line = rawStringFromLog[i];
      String[] pieces = split(line, ',');
 
      if(pieces != null)     //Trim out the raw x,y.Not the android pad x,y. The string INPUT in log represent it is android pad log.
      {  
        int xValue = Integer.parseInt(pieces[0]);
        int yValue = Integer.parseInt(pieces[1]);
//        if(xValue + 15120 >= mouseX*96 && xValue + 15120 < mouseX*96+900 && yValue >= mouseY*96 && yValue < mouseY*96 + 700)
        if(xValue + 15120 >= mouseX*96 && xValue + 15120 < mouseX*96+3600 && yValue >= mouseY*96 && yValue < mouseY*96 + 2800)
        {
          stroke(0xffEEEE00);
//          point(316 + xValue + 15120 - mouseX*96,0 + yValue - mouseY*96);
          point(316 + (xValue + 15120 - mouseX*96)/4,0 + (yValue - mouseY*96)/4);
          println("xValue:" + (316 + xValue + 15120 - mouseX*96) + "yValue:" + (0 + yValue - mouseY*96));
        }
      }
    }
  }
}

public void keyPressed() {
  if(keyCode == 32)   
  {
    //Clear the screen and redraw the points
    fill(0,0,0);
    rectMode(CORNER);
    noStroke();
    rect(0,0,1216,926);
      loadLog();
    //Draw the central line in x.The area in the left of this line => x<0.
    stroke(126);
    line(159,0,159,225);
    line(316,0,316,225);
    line(0,225,316,225);
  }
  if(keyCode == 83)   //Press the s key to save the frame
  {
    saveFrame();
  }
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Pen_Tracker" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
