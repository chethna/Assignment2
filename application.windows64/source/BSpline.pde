void bSplineCurve() {
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

    for (float t = 0; t<=knots[knotNumber-1]; t+= 0.01) {
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

double calculateN(int i, int j, float t) {
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


void increaseSize() {
  PVector[] temp = new PVector[pointsFound[0].length + 1];
  for (int i = 0; i < pointsFound[0].length; i++) {
    temp[i] = pointsFound[0][i];
  }
  pointsFound[0] = new PVector[pointsFound[0].length + 1];
  pointsFound[0] = temp;
}

