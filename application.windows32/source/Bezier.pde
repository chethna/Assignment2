void bezierCurve2(){
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
     for(float t = 0; t <= 1.01 ; t += 0.001){
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
 
void checkBoxIntersections(){
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
 
 boolean DoBoxesIntersect(PVector mina, PVector maxa, PVector minb, PVector maxb) {
  return (abs(mina.x - minb.x) * 2 < ((maxa.x-mina.x) + (maxb.x-minb.x))) &&
         (abs(mina.y - minb.y) * 2 < ((maxa.y-mina.y) + (maxb.y-minb.y)));
}
 
 void storeValue(){
   for(int i=0; i <numberOfBatches ; i++){
      abstractSearchObject myPointTemp0 = (abstractSearchObject)outer.get(i).get(0);
      maxBoundingBox[i][0] = new PVector(myPointTemp0.qx,myPointTemp0.qy);
      minBoundingBox[i][0] = new PVector(myPointTemp0.qx,myPointTemp0.qy);
   }
 }
 
 void findNumberOfBatches() {
  numberOfBatches = (int)(totalNumberOfPoint/(degree+1));
}

void splitPointsIntoBatches() {
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

void increaseSize(int n) {
   PVector[] temp = new PVector[pointsFound[n].length + 1];
   for (int i = 0; i < pointsFound[n].length; i++){
      temp[i] = pointsFound[n][i];
   }
   pointsFound[n] = new PVector[pointsFound[n].length + 1];
   pointsFound[n] = temp;
}
