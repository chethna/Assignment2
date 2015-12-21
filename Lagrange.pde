void lagrange() {
  totalNumberOfPoint= myPoints.size();
  //findNumberOfBatches();
  splitPointsIntoBatches();

  pointsFound = new PVector[numberOfBatches][];
  for (int n=0; n<numberOfBatches; n++) {
    int difference = numberOfPointInEachCurve[n]-1;
    float sumx,sumy;
    float p;
    float[] fx,fy;
    pointsFound[n] = new PVector[(int)(difference/0.01)+1];
    int index = 0;
    for (float a = 0; a<=difference; a+= 0.01) {
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
  //  displayBool = false;
    curveSelected = true;
    activeCurve = 1;
  }
}
