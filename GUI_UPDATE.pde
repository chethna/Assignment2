GDropList []dropList; 
GLabel []label; 
GButton button1, button2, button3; 
//GLabel label1; 
color []rlColor;
void createPointColors(){
  rlColor = new color[totalNumberOfPoint*2];
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
  
  button3 = new GButton(Points.papplet, 106, 175+((totalNumberOfPoint+1)*25), 80, 30);
  button3.setText("RESET");
  button3.addEventHandler(this, "button1_click3");
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


void setColor(){
  rlColor = new color[totalNumberOfPoint*2];
   for(int i=0; i<totalNumberOfPoint; i++){
    rlColor[i] = color(0,0,0);
    rlColor[i+totalNumberOfPoint] = color(0,0,0);
  }
 for(int i=0; i<totalNumberOfPoint; i++){
     // println(dropList[i].getSelectedIndex());
      assignColor((int)random(1,8),i);
      assignColor((int)random(1,8),i+totalNumberOfPoint);
  }
  setColor = true; 
}

void assignColor(int listNumber, int i){
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
    delay(100);
    downUpSize();
    doneBool = true;
  }  
}

public void button1_click3(GButton source, GEvent event) { 
  myPoints = null;
  myPoints = new ArrayList();
  print(myPoints.size());
  endPoints = false;
  activeCurve = 0;
  drawPoints = false;
  displayBool = true;
  lagrangeBool = bezierBool = bSplineBool = catmulRomBool =false;
  button1 = null;
  button2 = null;
  button3 = null;
 // createPointColors();
} 

