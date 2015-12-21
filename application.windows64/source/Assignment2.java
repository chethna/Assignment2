import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import g4p_controls.*; 
import java.util.ArrayList; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Assignment2 extends PApplet {





public void setup() {
  size(screenSizex,screenSizey);//1080,720);
  noSmooth();
 // background(0);
  myPoints = new ArrayList();
  createGUI();
 // staticTextGUI();
  strokeWeight(1);
  smooth();
  numberOfCurves = new ArrayList();
  pixelBuffer = new int[screenSizex][screenSizey];
}

public void draw() {
  noSmooth();
 if(!doneBool){
 // stroke(0);
  if (drawPoints) {
    pointCount++;
    String name = "point"+str(pointCount);
    myPoints.add(new Query(name, color(255, 255, 255), mouseX, mouseY, pointRadius, pointRadius));
    drawPoints = false;
  }

  if (!drawPoints) {
    background(0);
    for (int i = 0; i < myPoints.size (); i++) {
      abstractSearchObject myPointTemp = (abstractSearchObject)myPoints.get(i);
      myPointTemp.display(i);
    }
    if (lagrangeBool || bezierBool || bSplineBool || catmulRomBool) {
    
      for (int i = 0; i < numberOfBatches; i++) {
        
        println(i);
        int colorDivision = pointsFound[i].length/(numberOfPointInEachCurve[i]-1);//(totalNumberOfPoint-1);
        int colorCount = 0;
        int colorIndex;
       if(i==0){
        colorIndex = 0;
       }
       else{
         colorIndex = (Integer)numberOfCurves.get(i-1);
       }
     
        for (int k =0; k < pointsFound[i].length-5; k++) {
          if(catmulRomBool && i==1){
             println(i);
             println(pointsFound[i][k]);
           }
          if (pointsFound[i][k] != null) {
            colorCount +=1;
            if(colorCount >= colorDivision){
              colorCount = 1;
              colorIndex +=1;
            }
            if(colorIndex>=totalNumberOfPoint-1){
              colorIndex = totalNumberOfPoint-2;
            }
            float x = pointsFound[i][k].x;
            float y = pointsFound[i][k].y;
            //
            float slope = 0;
            if(k == 0 || k ==(pointsFound[i].length -1)){
              slope = 0.0f;
            }
            else{
              if(pointsFound[i][k+1] != null){
              slope = ( pointsFound[i][k+1].y - pointsFound[i][k-1].y ) / ( pointsFound[i][k+1].x - pointsFound[i][k-1].x );
              } 
          }
            float angle = atan(slope);
            float angleNormal = (abs(angle * 180.0f) / 3.14f);
            //
            int c1 = lerpColor(rlColor[colorIndex], rlColor[colorIndex+1],(float)colorCount/colorDivision);
            int c2 = lerpColor(rlColor[colorIndex+totalNumberOfPoint], rlColor[colorIndex+totalNumberOfPoint+1],(float)colorCount/colorDivision);  
            fill(c1);
            pushMatrix();
            // move the origin to the pivot point
            translate(x - POINTSIZE/2, y + POINTSIZE/2); 
            // then pivot the grid
            rotate(radians(angle));
            // and draw the square at the origin
            
            fill(c2);
            rect(0 + POINTSIZE/2, 0 , POINTSIZE/2 , POINTSIZE);
            fill(c1);
            rect(0, 0, POINTSIZE/2 , POINTSIZE);
            popMatrix();
           
          }
        }
      }
      if (bezierBool && intersection) {
        for (int i = 0; i < drawBoxes.size(); i++) {
          strokeWeight(0.5f);
          stroke(color(255, 0, 0));
          noFill();
          rectMode(CORNER); 
          int x = drawBoxes.get(i);
          rect( minBoundingBox[x][0].x, minBoundingBox[x][0].y, (maxBoundingBox[x][0].x-minBoundingBox[x][0].x), (maxBoundingBox[x][0].y - minBoundingBox[x][0].y));
          //quad( minBoundingBox[i][0].x,minBoundingBox[i][0].y,maxBoundingBox[i][0].x, minBoundingBox[i][0].y, maxBoundingBox[i][0].x, maxBoundingBox[i][0].y,minBoundingBox[i][0].x, maxBoundingBox[i][0].y );
        }
      }
    }
  }

  if (!bezierBool && intersection) {
    intersection =false;
  }
  
  //store in pixel buffer
  loadPixels();
    for (int i = 0; i < screenSizex; i++) {
      for (int j = 0; j < screenSizey; j++) {
      pixelBuffer[i][j] = pixels[j*width+i];
      }
    }
 }
  else{
    loadPixels();
    for (int i = 0; i < screenSizex; i++) {
      for (int j = 0; j < screenSizey; j++) {
      pixels[i*width+j] =  color(red(upSized[0][i][j]),green(upSized[0][i][j]),blue(upSized[0][i][j]));
      }
    }
    updatePixels();
  }
}

public void keyPressed() {
  if (key == 'e' || key == 'E') {
    if(!excuteBool){
      endPoints = true;
      totalNumberOfPoint= myPoints.size();
      // adding curves
      numberOfCurves.add(myPoints.size());
      numberOfPointInEachCurve = new int[numberOfCurves.size()];
      for(int i=0; i<numberOfCurves.size(); i++){
        if(i == 0){
          numberOfPointInEachCurve[i]= (Integer)numberOfCurves.get(i);
        }
        else{
          numberOfPointInEachCurve[i] = (Integer)numberOfCurves.get(i) - (Integer)numberOfCurves.get(i-1);
        }
      }
      //
      degree = totalNumberOfPoint-1;
      createPointColors();
   
       }
  
    }
    
    if (key == 'a' || key == 'A') {
      if(!excuteBool && !endPoints){
        numberOfCurves.add((int)myPoints.size());
        numberOfCurvesAdded += 1;
        numberOfBatches += 1;
      }
    }
    
  if (key == '=' || key == '+') {
    if (lagrangeBool || bezierBool || bSplineBool) {
      degree +=1;
      if (degree>=myPoints.size()) {
        degree = myPoints.size() -1;
      }
    } else if (catmulRomBool) {
     /* catmulDegree +=1;
      if (myPoints.size()%2 == 0) {
        if (catmulDegree>((myPoints.size()/2))) {
          catmulDegree = (myPoints.size()/2);
        }
      } else {
        if (catmulDegree>((myPoints.size()/2)+1)) {
          catmulDegree = (myPoints.size()/2)+1;
        }
      }*/
    }
    callCurveFunction(activeCurve);
  }
  if (key == '-' || key == '_') {
    if (lagrangeBool || bezierBool || bSplineBool) {
      degree -=1;
      if (degree<1) {
        degree = 1;
      }
    } else if (catmulRomBool) {
      catmulDegree -=1;
      if (catmulDegree<1) {
        catmulDegree = 1;
      }
    }
    callCurveFunction(activeCurve);
  }
  if (key == '1' || key == '1') {
    if(setColor){
    activeCurve = 1;
    callCurveFunction(activeCurve);
    }
    else{
      println("Set color");
    }
  }
  if (key == '2' || key == '2') {
    if(setColor){
      activeCurve = 2;
      callCurveFunction(activeCurve);
     }
    else{
      println("Set color");
    }
  }
  if (key == '3' || key == '#') {
    if(setColor){
      activeCurve = 3;
      if (degree<2) {
        degree =2;
      }
      callCurveFunction(activeCurve);
    }
    else{
      println("Set color");
    }
  }
  if (key == '4' || key == '$') {
    if(setColor){
      activeCurve = 4;
      if (degree<2) {
        degree =2;
      }
      callCurveFunction(activeCurve);
    }
    else{
      println("Set color");
    }
  }
  if (key == 'i' || key == 'i') {
    if (activeCurve == 2) {
      intersection = true;
    }
    callCurveFunction(activeCurve);
  }
}

public void mouseClicked() {
  if (!endPoints) {
    drawPoints = true;
  }
}

public void mousePressed() {
  if (endPoints) {
    for (int i = 0; i < myPoints.size (); i++) { 
      // note how I made it generic 
      abstractSearchObject myPointTemp = (abstractSearchObject)myPoints.get(i);
      evaluatePointSelection(myPointTemp);
    }
    //println("pressed");
  }
}

public void mouseReleased() {
  pointsBeingDragged = null; 
  if (endPoints) {
  }
} 

public void mouseDragged() {
  if ( pointsBeingDragged != null) {
    //println("dragging" + pointsBeingDragged.name);
    pointsBeingDragged.moveByMouseCoord(mouseX, mouseY);
    if (lagrangeBool || bezierBool || bSplineBool ||catmulRomBool) {
      callCurveFunction(activeCurve);
    }
  }
}  

public void callCurveFunction(int activeCurveArg) {
  switch (activeCurveArg) {
  case 1:
    lagrange();
    break;
  case 2:
    bezierCurve2();
    break;
  case 3:
    bSplineCurve();
    break;
  case 4:
    catmulRomCurve();
    break;
  default:             
    println("No Curve Selected");   
    break;
  }
  //updateGUI();
}

public void reset() {
  myPoints = null;
  myPoints = new ArrayList();
  print(myPoints.size());
  endPoints = false;
  activeCurve = 0;
  drawPoints = false;
  lagrangeBool = bezierBool = bSplineBool = catmulRomBool =false;
 // updateGUI();
}




public void bSplineCurve() {
  lagrangeBool = false;
  bezierBool = false;
  bSplineBool = false;
  catmulRomBool = false;
  totalNumberOfPoint= myPoints.size();
  //numberOfBatches = 1;
   splitPointsIntoBatches();
 pointsFound = new PVector[numberOfBatches][];
 
  for (int n=0; n<numberOfBatches; n++) {
    int totalNumberOfPointInBatch=  numberOfPointInEachCurve[n];
    degree = totalNumberOfPointInBatch - 1;
    knotNumber = degree+totalNumberOfPointInBatch;

    knots = new int[knotNumber];

    for (int i=0; i<knotNumber; i++) {
      if (i<degree) {
        knots[i] = 0;
      } else if (i >= degree && i <=totalNumberOfPointInBatch) {
        knots[i] = i - degree +1;
      } else if (i>totalNumberOfPointInBatch) {
        knots[i] = totalNumberOfPointInBatch - degree + 1;
      }
    }

    int index = 0;
   
    pointsFound[n] = new PVector[100*(knots[knotNumber-1])];

    for (float t = 0; t<=knots[knotNumber-1]; t+= 0.01f) {
      float sumx =0;
      float sumy =0;
      for (int i=0; i<totalNumberOfPointInBatch; i++) {
        abstractSearchObject myPointTemp0 = (abstractSearchObject)outer.get(n).get(i);;
        double temp = calculateN(i, degree, t);
        sumx = sumx+ (myPointTemp0.qx * (float)temp);
        sumy = sumy+ (myPointTemp0.qy * (float)temp);
        //   print(sumx,sumy);
      }
      if (index >= 100*(knots[knotNumber-1])) {
        increaseSize(n);
      }
      pointsFound[n][index] = new PVector(sumx, sumy);
      //println(pointsFound[0][index]);
      index +=1;
      sumx =0;
      sumy =0;
    }
    //println("pointsFound[n]:"+pointsFound[n].length);
  }
  //println("index"+index);
  lagrangeBool = false;
  bezierBool = false;
  catmulRomBool = false;
  bSplineBool = true;
  displayBool = false;
  curveSelected = true;
  activeCurve = 3;
}

public double calculateN(int i, int j, float t) {
  double Nij;
  if (j >1) {
    double deno1 = (knots[i+j-1] - knots[i]);
    double deno2 = (knots[i+j] - knots[i+1]);
    double temp1 = 0;
    double temp2 = 0;
    if (deno1 != 0) {
      temp1 = ((t - knots[i])/deno1) * calculateN(i, j-1, t);
    }
    if (deno2 != 0) {
      temp2 =((knots[i+j] - t)/deno2) * calculateN(i+1, j-1, t);
    }
    //float temp1 = ((t - knots[i])/(knots[i+j] - knots[i]))*calculateN(i,j-1,t);
    //float temp2 = ((knots[i+j+1] - t)/(knots[i+j+1] - knots[i+1])) * calculateN(i+1,j-1,t);
    /* if(Float.isNaN(temp1)){
     temp1 =0; 
     }
     if(Float.isNaN(temp2)){
     temp2 =0; 
     }*/
    Nij = temp1 + temp2;
  } else {
    if ( (t>=knots[i]) && (t < knots[i+1])) {
      Nij = 1;
    } else {
      Nij = 0;
    }
  }
  //println(Nij);
  if (Double.isNaN(Nij)) { 
    return 0;
  } else {
    return Nij;
  }
}


public void increaseSize() {
  PVector[] temp = new PVector[pointsFound[0].length + 1];
  for (int i = 0; i < pointsFound[0].length; i++) {
    temp[i] = pointsFound[0][i];
  }
  pointsFound[0] = new PVector[pointsFound[0].length + 1];
  pointsFound[0] = temp;
}

public void bezierCurve2(){
   totalNumberOfPoint= myPoints.size();
   //findNumberOfBatches();
   splitPointsIntoBatches();
   
   pointsFound = new PVector[numberOfBatches][];
   maxBoundingBox = new PVector[numberOfBatches][1];
   minBoundingBox = new PVector[numberOfBatches][1];
   storeValue();
   
   for (int n=0; n<numberOfBatches; n++) {
     pointsFound[n] = new PVector[1002];
     int totalNumberOfPointInBatch=  numberOfPointInEachCurve[n];//degree+1;
     PVector[][] Pstorage = new PVector[totalNumberOfPointInBatch][];
     float[][] Pstoragex = new float[totalNumberOfPointInBatch][];
     float[][] Pstoragey = new float[totalNumberOfPointInBatch][];
     Pstorage[0] = new PVector[totalNumberOfPointInBatch];
     Pstoragex[0] = new float[totalNumberOfPointInBatch];
     Pstoragey[0] = new float[totalNumberOfPointInBatch];
     for(int i = 0; i < totalNumberOfPointInBatch ; i++){
       abstractSearchObject myPointTemp0 = (abstractSearchObject)outer.get(n).get(i);;
        Pstorage[0][i] = new PVector(myPointTemp0.qx,myPointTemp0.qy);
        Pstoragex[0][i] = myPointTemp0.qx;
        Pstoragey[0][i] = myPointTemp0.qy;
     }
     int bezierIndex =0;
     for(float t = 0; t <= 1.01f ; t += 0.001f){
      int index=0;
      for(int i = 1; i < Pstorage[0].length ; i++){   
        
        Pstorage[i] = new PVector[Pstorage[0].length - index];
        Pstoragex[i] = new float[Pstorage[0].length - index];
        Pstoragey[i] = new float[Pstorage[0].length - index];
        for(int j=0; j < (Pstorage[i].length-1) ; j++){
        //  println(i+" : "+j);
          Pstoragex[i][j] = Pstoragex[i-1][j]*(1-t)+Pstoragex[i-1][j+1]*t;
          Pstoragey[i][j] = Pstoragey[i-1][j]*(1-t)+Pstoragey[i-1][j+1]*t;
          Pstorage[i][j] = new PVector(Pstoragex[i][j],Pstoragey[i][j]);
        }
        index += 1;
     }
     //max
     if(Pstoragex[Pstorage[0].length-1][0] > maxBoundingBox[n][0].x){
       maxBoundingBox[n][0].x = Pstoragex[Pstorage[0].length-1][0];
     }
     if(Pstoragey[Pstorage[0].length-1][0] > maxBoundingBox[n][0].y){
       maxBoundingBox[n][0].y = Pstoragey[Pstorage[0].length-1][0];
     }
     if(Pstoragex[Pstorage[0].length-1][0] < minBoundingBox[n][0].x){
       minBoundingBox[n][0].x = Pstoragex[Pstorage[0].length-1][0];
     }
     if(Pstoragey[Pstorage[0].length-1][0] < minBoundingBox[n][0].y){
       minBoundingBox[n][0].y = Pstoragey[Pstorage[0].length-1][0];
     }
     
     if(bezierIndex >= 1002){
       increaseSize(n);
     }
     //print(Pstorage[Pstorage[0].length-1][0]);
     pointsFound[n][bezierIndex] = new PVector(Pstorage[Pstorage[0].length-1][0].x,Pstorage[Pstorage[0].length-1][0].y);
     bezierIndex +=1;
    }
    //println(maxBoundingBox[n][0].x,  maxBoundingBox[n][0].y, minBoundingBox[n][0].x, minBoundingBox[n][0].y);
   }
   
   if(intersection){
    // checkBoxIntersections();
   }
  lagrangeBool = false;
  catmulRomBool = false;
  bSplineBool = false;
  bezierBool = true;
  displayBool = false;
  curveSelected = true;
  activeCurve = 2;
 }
 
public void checkBoxIntersections(){
 drawBoxes =new ArrayList<Integer>();
   for(int i=0; i <numberOfBatches-1 ; i++){
     for(int j=i+i; j <numberOfBatches ; j++){
       if(i!=j){
         
         if(DoBoxesIntersect(minBoundingBox[i][0], maxBoundingBox[i][0], minBoundingBox[j][0], maxBoundingBox[j][0])){
           //println("boxes:"+i+" and "+j+" intersect");
           drawBoxes.add((int)i);
           drawBoxes.add((int)j);
         }
       }
     }
   }
 }
 
 public boolean DoBoxesIntersect(PVector mina, PVector maxa, PVector minb, PVector maxb) {
  return (abs(mina.x - minb.x) * 2 < ((maxa.x-mina.x) + (maxb.x-minb.x))) &&
         (abs(mina.y - minb.y) * 2 < ((maxa.y-mina.y) + (maxb.y-minb.y)));
}
 
 public void storeValue(){
   for(int i=0; i <numberOfBatches ; i++){
      abstractSearchObject myPointTemp0 = (abstractSearchObject)outer.get(i).get(0);
      maxBoundingBox[i][0] = new PVector(myPointTemp0.qx,myPointTemp0.qy);
      minBoundingBox[i][0] = new PVector(myPointTemp0.qx,myPointTemp0.qy);
   }
 }
 
 public void findNumberOfBatches() {
  numberOfBatches = (int)(totalNumberOfPoint/(degree+1));
}

public void splitPointsIntoBatches() {
  outer = new ArrayList<ArrayList<Query>>();
  int index = 0;
  for (int i=0; i<numberOfCurves.size(); i++) {
    inner = new ArrayList<Query>(); 
    for (int k=0; k<numberOfPointInEachCurve[i]; k++) {
      if (index<=(totalNumberOfPoint-1)) {
        //print(index);
        inner.add((Query)myPoints.get(index));
        index +=1;
      }
    }
    outer.add(inner);
  }
}

public void increaseSize(int n) {
   PVector[] temp = new PVector[pointsFound[n].length + 1];
   for (int i = 0; i < pointsFound[n].length; i++){
      temp[i] = pointsFound[n][i];
   }
   pointsFound[n] = new PVector[pointsFound[n].length + 1];
   pointsFound[n] = temp;
}
public void catmulRomCurve() {
  totalNumberOfPoint= myPoints.size();
  splitPointsIntoBatches();

  pointsFound = new PVector[numberOfBatches][];
  
  for (int n0=0; n0<numberOfBatches; n0++) {
    lagrangeLevels = 2+1;
    deboorLevels = 2;

    int totalNumberOfPointInBatch=  numberOfPointInEachCurve[n0];
    PVector[][] Pstorage = new PVector[lagrangeLevels][];
    float[][] Pstoragex = new float[lagrangeLevels][];
    float[][] Pstoragey = new float[lagrangeLevels][];
    Pstorage[0] = new PVector[totalNumberOfPointInBatch];
    Pstoragex[0] = new float[totalNumberOfPointInBatch];
    Pstoragey[0] = new float[totalNumberOfPointInBatch];

    for (int i = 0; i < totalNumberOfPointInBatch; i++) {
      abstractSearchObject myPointTemp0 = (abstractSearchObject)outer.get(n0).get(i);
      Pstorage[0][i] = new PVector(myPointTemp0.qx, myPointTemp0.qy);
      Pstoragex[0][i] = myPointTemp0.qx;
      Pstoragey[0][i] = myPointTemp0.qy;
    }

    catmulKnots = new int[totalNumberOfPointInBatch];
    for (int i=0; i<totalNumberOfPointInBatch; i++) {
      catmulKnots[i] = i;
    }

    int draw =0;
    if (2>1) {
      draw = 2* (2 -1);
    }


    pointsFound[n0] = new PVector[100*(catmulKnots[totalNumberOfPointInBatch-1-draw])];
    int index = 0;
    for (int n = 1; n<=1; n++) {
      pointsFound[n-1] = new PVector[n*100*(catmulKnots[totalNumberOfPointInBatch-1-draw])];
      for (float t=0; t < catmulKnots[totalNumberOfPointInBatch-1]; t += 0.01f*n) {

        //lagrange part

        int subtract=0;
        for (int i = 1; i < lagrangeLevels; i++) { 
          Pstorage[i] = new PVector[Pstorage[0].length - (subtract+1)];
          Pstoragex[i] = new float[Pstorage[0].length - (subtract+1)];
          Pstoragey[i] = new float[Pstorage[0].length - (subtract+1)];
          //println("i: " +i +", length"+Pstorage[i].length);
          for (int j=0; j < (Pstorage[i].length); j++) {
            float x1 = (catmulKnots[i+j]-t);
            float x0 = (t-catmulKnots[j]);
            float x10 = (catmulKnots[i+j]-catmulKnots[j]);
            // println("p["+(i-1)+"]["+j+"] *"+"(t"+(i+j)+"-t)");
            // println("p["+(i-1)+"]["+(j+1)+"] *"+"(t - t"+(j)+")");
            Pstoragex[i][j] = (Pstoragex[i-1][j]* x1 + Pstoragex[i-1][j+1] * x0) / x10;
            Pstoragey[i][j] = (Pstoragey[i-1][j]* x1 + Pstoragey[i-1][j+1] * x0) / x10;
            Pstorage[i][j] = new PVector(Pstoragex[i][j], Pstoragey[i][j]);
          }
          subtract += 1;
        }

        //deboor part

        float sumx =0;
        float sumy =0;
        for (int i=0; i<Pstorage[lagrangeLevels-1].length; i++) {
          double temp = calculateNcatmulrom(i, catmulDegree, t);
          sumx = sumx+ (Pstorage[lagrangeLevels-1][i].x * (float)temp);
          sumy = sumy+ (Pstorage[lagrangeLevels-1][i].y * (float)temp);
        }

        if (index >= 100*(catmulKnots[totalNumberOfPointInBatch-1-draw])) {
          increaseSizeCatmulRom(n0);
        }
        if (t>=(draw/2) && t<=(totalNumberOfPointInBatch-1-(draw/2))) {  
          //println(t); 
          pointsFound[n0][index] = new PVector(sumx, sumy);
          index +=1;
        }
        sumx =0;
        sumy =0;
      }
    }
  }
  lagrangeBool = false;
  bezierBool = false;
  bSplineBool = false;
  catmulRomBool = true;
  displayBool = false;
  curveSelected = true;
  activeCurve = 4;
}

public double calculateNcatmulrom(int i, int j, float t) {
  double Nij;
  if (j >1) {
    double deno1 = (catmulKnots[i+j-1] - catmulKnots[i]);
    double deno2 = (catmulKnots[i+j] - catmulKnots[i+1]);
    double temp1 = 0;
    double temp2 = 0;
    if (deno1 != 0) {
      temp1 = ((t - catmulKnots[i])/deno1) * calculateNcatmulrom(i, j-1, t);
    }
    if (deno2 != 0) {
      temp2 =((catmulKnots[i+j] - t)/deno2) * calculateNcatmulrom(i+1, j-1, t);
    }
    Nij = temp1 + temp2;
  } else {
    if ( (t>=catmulKnots[i]) && (t < catmulKnots[i+1])) {
      Nij = 1;
    } else {
      Nij = 0;
    }
  }
  if (Double.isNaN(Nij)) { 
    return 0;
  } else {
    return Nij;
  }
}

public void increaseSizeCatmulRom(int n0) {
  PVector[] temp = new PVector[pointsFound[n0].length + 1];
  for (int i = 0; i < pointsFound[n0].length; i++) {
    temp[i] = pointsFound[n0][i];
  }
  pointsFound[n0] = new PVector[pointsFound[n0].length + 1];
  pointsFound[n0] = temp;
}

int screenSizex = 512;
int screenSizey = 512;
int POINTSIZE = 10;
boolean drawPoints;
boolean endPoints = false;
int pointCount = 0;
int pointRadius = 25;
int degree;
int catmulDegree = 2;
//new code for dragging
int dragX;
int dragY;
ArrayList numberOfCurves;
int [] numberOfPointInEachCurve;
ArrayList myPoints;
abstractSearchObject pointsBeingDragged;
int activeCurve;
boolean intersection = false;
boolean bSplineBool = false;
boolean catmulRomBool = false;
boolean lagrangeBool = false;
boolean setColor = false;
boolean excuteBool = false;
boolean displayBool = true;
boolean curveSelected = false;
int numberOfBatches = 1, numberOfCurvesAdded=1;
PVector [][]pointsFound;
int totalNumberOfPoint;
ArrayList<ArrayList<Query>> outer;
ArrayList<Query> inner;
boolean doneBool = false;
int black = color(0.0f,0.0f,0.0f);
int [][][]downSized;
int [][][]upSized;

//bezier curve
float pointx, pointy;
boolean bezierBool =false;
PVector[][] maxBoundingBox;
PVector[][] minBoundingBox;
ArrayList<Integer> drawBoxes;


//bspline curve
int knotNumber,internalPointStart, internalPointEnd;
int[] knots;

//catmulrom
int lagrangeLevels;
int deboorLevels;
boolean special = false;
int[] catmulKnots;
boolean close =false;


int [][]pixelBuffer;
public void downUpSize(){
   int levels = (int)(log(screenSizex)/log(2));
    downSized= new int[levels+1][][];
    downSized[0] = new int[screenSizex][screenSizey];
    noLoop();
    //get the pixel values
    loadPixels();
    for(int i = 0; i<screenSizex ; i++){
      for(int j = 0; j< screenSizey; j++){
        downSized[0][i][j] = pixels[i*width+j];
        //println(red(pixels[i*width+j]),green(pixels[i*width+j]),blue(pixels[i*width+j]));
      }
    }
    loadPixels();
    for(int i = 0; i<screenSizex ; i++){
      for(int j = 0; j< screenSizey; j++){
        downSized[0][i][j] = pixels[i*width+j];
        //println(red(pixels[i*width+j]),green(pixels[i*width+j]),blue(pixels[i*width+j]));
      }
    }
    int avgColor = color(0,0,0);
    int downsize = screenSizex;  
    //downsize them to one pixel
    for(int i = 0; i<levels-1; i++){
      downSized[i+1] = new int[downsize/2][downsize/2];
      int indexX = 0;
      int indexY = 0;
     // print("bla");
      for(int j = 0; j < downsize; j += 2){
        indexY = 0;
        for(int k = 0; k < downsize; k += 2){
          int count = 0;
          float r =0, g=0, b=0;
          if(!(red(downSized[i][j][k])<=50.0f && green(downSized[i][j][k])<=50.0f && blue(downSized[i][j][k])<=50.0f)){
            avgColor = downSized[i][j][k];
            r += red(avgColor);//avgColor>>16&0xFF;
            g += green(avgColor);//avgColor>>8&0xFF;
            b += blue(avgColor);//avgColor&0xFF;
            count += 1;
          }
          if(!(red(downSized[i][j+1][k])<=50.0f && green(downSized[i][j+1][k])<=50.0f && blue(downSized[i][j+1][k])<=50.0f)){
            avgColor =downSized[i][j+1][k];
            r += red(avgColor);//avgColor>>16&0xFF;
            g += green(avgColor);//avgColor>>8&0xFF;
            b += blue(avgColor);//avgColor&0xFF;
            count += 1;
          }
          if(!(red(downSized[i][j][k+1])<=50.0f && green(downSized[i][j][k+1])<=50.0f && blue(downSized[i][j][k+1])<=50.0f)){
            avgColor =downSized[i][j][k+1];
            r += red(avgColor);//avgColor>>16&0xFF;
            g += green(avgColor);//avgColor>>8&0xFF;
            b += blue(avgColor);//avgColor&0xFF;
            count += 1;
          }
          if(!(red(downSized[i][j+1][k+1])<=50.0f && green(downSized[i][j+1][k+1])<=50.0f && blue(downSized[i][j+1][k+1])<=50.0f)){
            avgColor =downSized[i][j+1][k+1];
            r += red(avgColor);//avgColor>>16&0xFF;
            g += green(avgColor);//avgColor>>8&0xFF;
            b += blue(avgColor);//avgColor&0xFF;
            count += 1;
          }
          if(count >0){
            r /=count;
            g /=count;
            b /=count;
            downSized[i+1][indexX][indexY] = color(r,g,b);
          }
          else{
             downSized[i+1][indexX][indexY] = color(0.0f,0.0f,0.0f);
          }
          indexY += 1;
        }
        indexX += 1;
      }
     
      downsize = downsize/2;
       //println("=================================================="+i+","+downsize+"==============");
    }
  
  //this is done to get the last downsized pixel value
   downSized[9] = new int[1][1];
   { int count = 0;
     float r =0, g=0, b=0;
     if(!(red(downSized[8][0][0])<=50.0f && green(downSized[8][0][0])<=50.0f && blue(downSized[8][0][0])<=50.0f)){
            avgColor = downSized[8][0][0];
            r += red(avgColor);//avgColor>>16&0xFF;
            g += green(avgColor);//avgColor>>8&0xFF;
            b += blue(avgColor);//avgColor&0xFF;
            count += 1;
          }
          if(!(red(downSized[8][1][0])<=50.0f && green(downSized[8][1][0])<=50.0f && blue(downSized[8][1][0])<=50.0f)){
            avgColor =downSized[8][1][0];
            r += red(avgColor);//avgColor>>16&0xFF;
            g += green(avgColor);//avgColor>>8&0xFF;
            b += blue(avgColor);//avgColor&0xFF;
            count += 1;
          }
          if(!(red(downSized[8][0][1])<=50.0f && green(downSized[8][0][1])<=50.0f && blue(downSized[8][0][1])<=50.0f)){
            avgColor =downSized[8][0][1];
            r += red(avgColor);//avgColor>>16&0xFF;
            g += green(avgColor);//avgColor>>8&0xFF;
            b += blue(avgColor);//avgColor&0xFF;
            count += 1;
          }
          if(!(red(downSized[8][1][1])<=50.0f && green(downSized[8][1][1])<=50.0f && blue(downSized[8][1][1])<=50.0f)){
            avgColor =downSized[8][1][1];
            r += red(avgColor);//avgColor>>16&0xFF;
            g += green(avgColor);//avgColor>>8&0xFF;
            b += blue(avgColor);//avgColor&0xFF;
            count += 1;
          }
          if(count >0){
            r /= (float)count;
            g /= (float)count;
            b /= (float)count;
            downSized[9][0][0] = color(r,g,b);
          }
     }


  //upsize them now
  downsize =1;
  
  upSized = new int[levels+1][][];
  upSized[9] = new int[downsize][downsize];
  upSized[9][0][0] = downSized[9][0][0];
  downsize =2;
  for(int i=8; i>=0; i--){
    int [][]temp = new int[downsize][downsize];
    upSized[i] = new int[downsize][downsize];
    int indexX = 0;
    int indexY = 0;
    //print("bla");
    for(int j = 0; j < downsize; j += 2){
      indexY = 0;
      for(int k = 0; k < downsize; k += 2){
        if(!(red(downSized[i][j][k])<=50.0f && green(downSized[i][j][k])<=50.0f && blue(downSized[i][j][k])<=50.0f)){
          //upSized[i][j][k] = downSized[i][j][k];
          temp[j][k] = downSized[i][j][k];
        }
        else{
          //upSized[i][j][k] = upSized[i+1][indexX][indexY];
          temp[j][k] = upSized[i+1][indexX][indexY];
        }
        if(!(red(downSized[i][j+1][k])<=50.0f && green(downSized[i][j+1][k])<=50.0f && blue(downSized[i][j+1][k])<=50.0f)){
          //upSized[i][j+1][k] = downSized[i][j+1][k];
          temp[j+1][k] = downSized[i][j+1][k];
        }
        else{
          //upSized[i][j+1][k] = upSized[i+1][indexX][indexY];
          temp[j+1][k] = upSized[i+1][indexX][indexY]; 
        }
        if(!(red(downSized[i][j][k+1])<=50.0f && green(downSized[i][j][k+1])<=50.0f && blue(downSized[i][j][k+1])<=50.0f)){
          //upSized[i][j][k+1] = downSized[i][j][k+1];
          temp[j][k+1] = downSized[i][j][k+1];
        }
        else{
          //upSized[i][j][k+1] = upSized[i+1][indexX][indexY];
          temp[j][k+1] = upSized[i+1][indexX][indexY];
        }
        if(!(red(downSized[i][j+1][k+1])<=50.0f && green(downSized[i][j+1][k+1])<=50.0f && blue(downSized[i][j+1][k+1])<=50.0f)){
          //upSized[i][j+1][k+1] = downSized[i][j+1][k+1];
          temp[j+1][k+1] = downSized[i][j+1][k+1];
        }
        else{
          //upSized[i][j+1][k+1] = upSized[i+1][indexX][indexY];
          temp[j+1][k+1] = upSized[i+1][indexX][indexY];
        }
       upSized[i][j][k] = temp[j][k];
       upSized[i][j+1][k] = temp[j+1][k];
       upSized[i][j][k+1] = temp[j][k+1];
       upSized[i][j+1][k+1] = temp[j+1][k+1];
       
        indexY +=1;
        }
      indexX +=1;
    }
     //smoothing required
    // noLoop();
    for(int l =0; l<20; l++){
      for(int j = 0; j < downsize; j++){
        for(int k = 0; k < downsize; k++){
          float sr =0, sg=0, sb=0;
          int smoothCount = 0;
          if(red(downSized[i][j][k])<=50.0f && green(downSized[i][j][k])<=50.0f && blue(downSized[i][j][k])<=50.0f){
            if((j-1)>=0 && (k-1)>=0){
              sr += red(upSized[i][j-1][k-1]);
              sg += green(upSized[i][j-1][k-1]);
              sb += blue(upSized[i][j-1][k-1]);
              smoothCount += 1;
            }
            if((j-1)>=0 ){
              sr += red(upSized[i][j-1][k]);
              sg += green(upSized[i][j-1][k]);
              sb += blue(upSized[i][j-1][k]);
              smoothCount += 1;
            }
            if((j-1)>=0 && (k+1)<downsize){
              sr += red(upSized[i][j-1][k+1]);
              sg += green(upSized[i][j-1][k+1]);
              sb += blue(upSized[i][j-1][k+1]);
              smoothCount += 1;
            }
            if((k-1)>=0){
              sr += red(upSized[i][j][k-1]);
              sg += green(upSized[i][j][k-1]);
              sb += blue(upSized[i][j][k-1]);
              smoothCount += 1;
            }
            if((k+1)<downsize){
              sr += red(upSized[i][j][k+1]);
              sg += green(upSized[i][j][k+1]);
              sb += blue(upSized[i][j][k+1]);
              smoothCount += 1;
            }
            if((k-1)>=0 && (j+1)<downsize){
              sr += red(upSized[i][j+1][k-1]);
              sg += green(upSized[i][j+1][k-1]);
              sb += blue(upSized[i][j+1][k-1]);
              smoothCount += 1;
            }
            if((j+1)<downsize){
              sr += red(upSized[i][j+1][k]);
              sg += green(upSized[i][j+1][k]);
              sb += blue(upSized[i][j+1][k]);
              smoothCount += 1;
            }
            if((j+1)<downsize && (k+1)<downsize){
              sr += red(upSized[i][j+1][k+1]);
              sg += green(upSized[i][j+1][k+1]);
              sb += blue(upSized[i][j+1][k+1]);
              smoothCount += 1;
            }
              sr += red(upSized[i][j][k]);
              sg += green(upSized[i][j][k]);
              sb += blue(upSized[i][j][k]);
              smoothCount += 1;
              
            sr /= (float)smoothCount;
            sg /= (float)smoothCount;
            sb /= (float)smoothCount;
            upSized[i][j][k] = color(sr, sg, sb);
          }
        }
      }
    }
    downsize *= 2;
  }
  loop();
}
abstract class abstractSearchObject{
  String name;
  
  int qc;
  float qx;
  float qy;
  int dQx;
  int dQy;
 
  abstractSearchObject(String name, int tempQc, float tempQx, float tempQy,int tempdQx, int tempdQy) {
    this.name = name;
     qc = tempQc;
     qx = tempQx;
     qy = tempQy;
     dQx = tempdQx;
     dQy = tempdQy;
  }
 
  public void display(int i) {
      stroke(0);
      fill(qc);
     // ellipse(qx,qy,dQx,dQy);
      noStroke();
      if(displayBool){
      if(setColor){
        int c1 = rlColor[i];
        int c2 = rlColor[i+totalNumberOfPoint];
        fill(c1);
        beginShape();
        vertex(qx - POINTSIZE, qy + POINTSIZE);
        vertex(qx - POINTSIZE, qy - POINTSIZE);
        vertex(qx , qy - POINTSIZE);
        vertex(qx , qy + POINTSIZE);
        endShape();
        fill(c2);
        beginShape();
        vertex(qx, qy + POINTSIZE);
        vertex(qx, qy - POINTSIZE);
        vertex(qx + POINTSIZE, qy - POINTSIZE);
        vertex(qx + POINTSIZE, qy + POINTSIZE);
        endShape();
      }
      else{
        beginShape();
        vertex(qx - POINTSIZE, qy + POINTSIZE);
        vertex(qx - POINTSIZE, qy - POINTSIZE);
        vertex(qx , qy - POINTSIZE);
        vertex(qx , qy + POINTSIZE);
        endShape();
        beginShape();
        vertex(qx, qy + POINTSIZE);
        vertex(qx, qy - POINTSIZE);
        vertex(qx + POINTSIZE, qy - POINTSIZE);
        vertex(qx + POINTSIZE, qy + POINTSIZE);
        endShape();
      }
      }
  }
  
  public boolean inQuery(int x, int y){
    if((x > qx-dQx) & x < (qx+dQx)){
      if((y > qy-dQy)  & y < (qy+dQy)){
        return true;
      }
    }
    return false;
  }
  
  public void moveByMouseCoord(int mausX, int mausY){
    this.qx = mausX + dragX;
    this.qy = mausY + dragY;
  }
}


class Query extends abstractSearchObject{

  ArrayList connects = new ArrayList();
  
  
  Query(String name, int tempQc, float tempQx, float tempQy,int tempdQx, int tempdQy) {
    super( name,  tempQc,  tempQx,  tempQy, tempdQx,  tempdQy);
  }
  
  public void display(int i){
    super.display(i);  
  }
  
}

public void evaluatePointSelection(abstractSearchObject myPointTemp){ 
  if (myPointTemp.inQuery(mouseX, mouseY) & pointsBeingDragged==null){ 
    dragX = (int)myPointTemp.qx - mouseX;
    dragY = (int)myPointTemp.qy - mouseY;
    pointsBeingDragged = myPointTemp;
  }
}
GDropList []dropList; 
GLabel []label; 
GButton button1, button2; 
//GLabel label1; 
int []rlColor;
public void createPointColors(){
  rlColor = new int[totalNumberOfPoint*2];
  for(int i=0; i<totalNumberOfPoint; i++){
    rlColor[i] = color(0,0,0);
    rlColor[i+totalNumberOfPoint] = color(0,0,0);
  }
   label = new GLabel[totalNumberOfPoint];
   dropList = new GDropList[totalNumberOfPoint*2];
   for(int i=0; i<totalNumberOfPoint; i++){
      dropList[i] = new GDropList(Points.papplet, 120, 141+(i*25), 60, 154, 7);
      dropList[i].setItems(loadStrings("list_424708"), (int)random(1,8));
      dropList[i+totalNumberOfPoint] = new GDropList(Points.papplet, 120+70, 141+(i*25), 60, 154, 7);
      dropList[i+totalNumberOfPoint].setItems(loadStrings("list_424708"),(int)random(1,8));
      //dropList[i].addEventHandler(this,i, "dropList1_click1");
     // dropList[i+totalNumberOfPoint].addEventHandler(this,i+totalNumberOfPoint, "dropList1_click1");
      label[i] = new GLabel(Points.papplet, 17, 141+(i*25), 80, 20);
      label[i].setText("Point "+str(i+1));
      label[i].setOpaque(false);
   }
  button2 = new GButton(Points.papplet, 50, 141+((totalNumberOfPoint+1)*25), 80, 30);
  button2.setText("Set Color");
  button2.addEventHandler(this, "button1_click2");
  
  button1 = new GButton(Points.papplet, 156, 141+((totalNumberOfPoint+1)*25), 80, 30);
  button1.setText("Execute");
  button1.addEventHandler(this, "button1_click1");
 }
 
public void dropList1_click1(GDropList source, int index, GEvent event) { //_CODE_:dropList1:214510:
  //println("dropList1 - GDropList event occured " + System.currentTimeMillis()%10000000 );
 // println(source.getSelectedIndex ());
} //_CODE_:dropList1:214510:


public void button1_click2(GButton source, GEvent event) { //_CODE_:button1:763470:
  //println("button1 - GButton event occured " + System.currentTimeMillis()%10000000 );
  
  for(int i=0; i<totalNumberOfPoint; i++){
     // println(dropList[i].getSelectedIndex());
      assignColor(dropList[i].getSelectedIndex(),i);
      assignColor(dropList[i+totalNumberOfPoint].getSelectedIndex(),i+totalNumberOfPoint);
  }
  setColor = true;
} //_CODE_:button1:763470:

public void assignColor(int listNumber, int i){
    switch(listNumber){
        case 0:
          rlColor[i] = color(0,0,0);
          break;
        case 1:
          rlColor[i] = color(0,0,255);
          break;
        case 2:
          rlColor[i] = color(0,255,0);
          break;
        case 3:
          rlColor[i] = color(0,255,255);
          break;
        case 4:
          rlColor[i] = color(255,0,0);
          break;
        case 5:
          rlColor[i] = color(255,0,255);
          break;
        case 6:
          rlColor[i] = color(255,255,0);
          break;
        case 7:
          rlColor[i] = color(255,255,255);
          break;
        default:
          rlColor[i] = color(0,0,0);
      }
} 

public void button1_click1(GButton source, GEvent event) {
  if(!curveSelected){
    println("Select curve by pressing 1,2 3,4");  
  }
  
  if(setColor&&curveSelected){
     doneBool = false;
    excuteBool = true;  
    displayBool = false;
    downUpSize();
    doneBool = true;
  }
  
}

/*
 label1 = new GLabel(Points.papplet, 10, 9, 308, 127);
  label1.setTextAlign(GAlign.LEFT, GAlign.TOP);
  label1.setText("Click on the black screen to create new points \n Press 'a' to add points for a new curve \n Press 'e' to stop adding points and edit the curve \n Click on the 'Set Color' button to set the color for the curve points \n Press 1 - Lagrange, 2 - Bezier, 3 - BSpline, 4 - Catmul Rom, curve to be generated \n Click 'Execute' to generate the Diffusion Curve");
  label1.setOpaque(false);
  */
public void lagrange() {
  totalNumberOfPoint= myPoints.size();
  //findNumberOfBatches();
  splitPointsIntoBatches();

  pointsFound = new PVector[numberOfBatches][];
  for (int n=0; n<numberOfBatches; n++) {
    int difference = numberOfPointInEachCurve[n]-1;
    float sumx,sumy;
    float p;
    float[] fx,fy;
    pointsFound[n] = new PVector[(int)(difference/0.01f)+1];
    int index = 0;
    for (float a = 0; a<=difference; a+= 0.01f) {
      sumx = 0;
      sumy = 0;
      p = a;
      fx = new float[numberOfPointInEachCurve[n]];
      fy = new float[numberOfPointInEachCurve[n]];
      for (int i=0; i<numberOfPointInEachCurve[n]; i++)
       {
        abstractSearchObject myPointTemp0 = (abstractSearchObject)outer.get(n).get(i);
        float tempx = 1;
        float tempy = 1;
        int k = i;
        
        for (int j=0; j<numberOfPointInEachCurve[n]; j++)
        { 
          if (k==j)
          {
            continue;
          } else
          { 
            tempx= tempx * ((p - j)/(i-j));
            tempy = tempy * ((p - j)/(i-j));
          }
        }
        fx[i] = myPointTemp0.qx*tempx;
        fy[i] = myPointTemp0.qy*tempy;
      }

      for (int i=0; i<numberOfPointInEachCurve[n]; i++)
      {
        sumx += fx[i];
        sumy += fy[i];
        
      }
      for (int i=0; i<numberOfPointInEachCurve[n]; i++)
      {
        fx[i] = 0;
        fy[i] = 0;
      }
      pointsFound[n][index] = new PVector(sumx, sumy);//new PVector(p, sum);
      index += 1;
      sumx = 0;
      sumy = 0;
    }
    lagrangeBool = true;
    bezierBool = false;
    catmulRomBool = false;
    bSplineBool = false;
    displayBool = false;
    curveSelected = true;
    activeCurve = 1;
  }
}
/* =========================================================
 * ====                   WARNING                        ===
 * =========================================================
 * The code in this tab has been generated from the GUI form
 * designer and care should be taken when editing this file.
 * Only add/edit code inside the event handlers i.e. only
 * use lines between the matching comment tags. e.g.

 void myBtnEvents(GButton button) { //_CODE_:button1:12356:
     // It is safe to enter your event code here  
 } //_CODE_:button1:12356:
 
 * Do not rename this tab!
 * =========================================================
 */

synchronized public void win_draw1(GWinApplet appc, GWinData data) { //_CODE_:window1:642598:
  appc.background(230);
} //_CODE_:window1:642598:



// Create all the GUI controls. 
// autogenerated do not edit
public void createGUI(){
  G4P.messagesEnabled(false);
  G4P.setGlobalColorScheme(GCScheme.BLUE_SCHEME);
  G4P.setCursor(ARROW);
  if(frame != null)
    frame.setTitle("Diffusion Curve");
  Points = new GWindow(this, "UI", 0, 0, 323, 510, false, JAVA2D);
  Points.addDrawHandler(this, "win_draw1");
  label1 = new GLabel(Points.papplet, 10, 10, 308, 127);
  label1.setTextAlign(GAlign.LEFT, GAlign.TOP);
  label1.setText("Click on the black screen to create new points \n Press 'a' to add points for a new curve \n Press 'e' to stop adding points and edit the curve \n Click on the 'Set Color' button to set the color for the curve points \n Press 1 - Lagrange, 2 - Bezier, 3 - BSpline, 4 - Catmul Rom, curve to be generated \n Click 'Execute' to generate the Diffusion Curve");
  label1.setOpaque(false);
}

// Variable declarations 
// autogenerated do not edit
GWindow Points;
GLabel label1; 


  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Assignment2" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
