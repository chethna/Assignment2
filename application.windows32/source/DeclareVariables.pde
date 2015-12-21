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
color black = color(0.0,0.0,0.0);
color [][][]downSized;
color [][][]upSized;

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


color [][]pixelBuffer;
