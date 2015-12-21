void catmulRomCurve() {
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
      for (float t=0; t < catmulKnots[totalNumberOfPointInBatch-1]; t += 0.01*n) {

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

double calculateNcatmulrom(int i, int j, float t) {
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

void increaseSizeCatmulRom(int n0) {
  PVector[] temp = new PVector[pointsFound[n0].length + 1];
  for (int i = 0; i < pointsFound[n0].length; i++) {
    temp[i] = pointsFound[n0][i];
  }
  pointsFound[n0] = new PVector[pointsFound[n0].length + 1];
  pointsFound[n0] = temp;
}

