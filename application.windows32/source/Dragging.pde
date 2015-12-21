abstract class abstractSearchObject{
  String name;
  
  color qc;
  float qx;
  float qy;
  int dQx;
  int dQy;
 
  abstractSearchObject(String name, color tempQc, float tempQx, float tempQy,int tempdQx, int tempdQy) {
    this.name = name;
     qc = tempQc;
     qx = tempQx;
     qy = tempQy;
     dQx = tempdQx;
     dQy = tempdQy;
  }
 
  void display(int i) {
      stroke(0);
      fill(qc);
     // ellipse(qx,qy,dQx,dQy);
      noStroke();
      if(displayBool){
      if(setColor){
        color c1 = rlColor[i];
        color c2 = rlColor[i+totalNumberOfPoint];
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
  
  boolean inQuery(int x, int y){
    if((x > qx-dQx) & x < (qx+dQx)){
      if((y > qy-dQy)  & y < (qy+dQy)){
        return true;
      }
    }
    return false;
  }
  
  void moveByMouseCoord(int mausX, int mausY){
    this.qx = mausX + dragX;
    this.qy = mausY + dragY;
  }
}


class Query extends abstractSearchObject{

  ArrayList connects = new ArrayList();
  
  
  Query(String name, color tempQc, float tempQx, float tempQy,int tempdQx, int tempdQy) {
    super( name,  tempQc,  tempQx,  tempQy, tempdQx,  tempdQy);
  }
  
  void display(int i){
    super.display(i);  
  }
  
}

void evaluatePointSelection(abstractSearchObject myPointTemp){ 
  if (myPointTemp.inQuery(mouseX, mouseY) & pointsBeingDragged==null){ 
    dragX = (int)myPointTemp.qx - mouseX;
    dragY = (int)myPointTemp.qy - mouseY;
    pointsBeingDragged = myPointTemp;
  }
}
