import g4p_controls.*;
import java.util.ArrayList;


void setup() {
  size(512,512);//1080,720);
  noSmooth();
 // background(0);
  myPoints = new ArrayList();
  createGUI();
 // staticTextGUI();
  strokeWeight(1);
  numberOfCurves = new ArrayList();
  pixelBuffer = new color[screenSizex][screenSizey];
 // print(displayDensity());
// pointDensity(1);
}

void draw() {
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
        int colorDivision = pointsFound[i].length/(numberOfPointInEachCurve[i]-1);
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
              slope = 0.0;
            }
            else{
              if(pointsFound[i][k+1] != null){
              slope = ( pointsFound[i][k+1].y - pointsFound[i][k-1].y ) / ( pointsFound[i][k+1].x - pointsFound[i][k-1].x );
              } 
          }
            float angle = atan2(slope,slope);
            //float angleNormal = (abs(angle * 180.0) / 3.14);
            //
            color c1 = lerpColor(rlColor[colorIndex], rlColor[colorIndex+1],(float)colorCount/colorDivision);
            color c2 = lerpColor(rlColor[colorIndex+totalNumberOfPoint], rlColor[colorIndex+totalNumberOfPoint+1],(float)colorCount/colorDivision);  
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
          strokeWeight(0.5);
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

void keyPressed() {
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
      setColor();
      //createPointColors(); 
       }
  
    }
     if (key == 'd' || key == 'D') {
       diffusion();
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

void mouseClicked() {
  if (!endPoints) {
    drawPoints = true;
  }
}

void mousePressed() {
  if (endPoints) {
    for (int i = 0; i < myPoints.size (); i++) { 
      // note how I made it generic 
      abstractSearchObject myPointTemp = (abstractSearchObject)myPoints.get(i);
      evaluatePointSelection(myPointTemp);
    }
    //println("pressed");
  } 
}

void mouseReleased() {
  pointsBeingDragged = null; 
  if (endPoints) {
  }
} 

void mouseDragged() {
  if ( pointsBeingDragged != null) {
    //println("dragging" + pointsBeingDragged.name);
    pointsBeingDragged.moveByMouseCoord(mouseX, mouseY);
    if (lagrangeBool || bezierBool || bSplineBool ||catmulRomBool) {
      callCurveFunction(activeCurve);
    }
  }
}  

void callCurveFunction(int activeCurveArg) {
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

void reset() {
 myPoints = null;
  myPoints = new ArrayList();
  print(myPoints.size());
  endPoints = false;
  activeCurve = 0;
  drawPoints = false;
  displayBool = true;
  lagrangeBool = bezierBool = bSplineBool = catmulRomBool =false;
  createPointColors();
 // updateGUI();
}
