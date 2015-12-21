void downUpSize(){
   int levels = (int)(log(screenSizex)/log(2));
    downSized= new color[levels+1][][];
    downSized[0] = new color[screenSizex][screenSizey];
    //noLoop();
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
    color avgColor = color(0,0,0);
    int downsize = screenSizex;  
    //downsize them to one pixel
    for(int i = 0; i<levels-1; i++){
      downSized[i+1] = new color[downsize/2][downsize/2];
      int indexX = 0;
      int indexY = 0;
     // print("bla");
      for(int j = 0; j < downsize; j += 2){
        indexY = 0;
        for(int k = 0; k < downsize; k += 2){
          int count = 0;
          float r =0, g=0, b=0;
          if(!(red(downSized[i][j][k])<=50.0 && green(downSized[i][j][k])<=50.0 && blue(downSized[i][j][k])<=50.0)){
            avgColor = downSized[i][j][k];
            r += red(avgColor);//avgColor>>16&0xFF;
            g += green(avgColor);//avgColor>>8&0xFF;
            b += blue(avgColor);//avgColor&0xFF;
            count += 1;
          }
          if(!(red(downSized[i][j+1][k])<=50.0 && green(downSized[i][j+1][k])<=50.0 && blue(downSized[i][j+1][k])<=50.0)){
            avgColor =downSized[i][j+1][k];
            r += red(avgColor);//avgColor>>16&0xFF;
            g += green(avgColor);//avgColor>>8&0xFF;
            b += blue(avgColor);//avgColor&0xFF;
            count += 1;
          }
          if(!(red(downSized[i][j][k+1])<=50.0 && green(downSized[i][j][k+1])<=50.0 && blue(downSized[i][j][k+1])<=50.0)){
            avgColor =downSized[i][j][k+1];
            r += red(avgColor);//avgColor>>16&0xFF;
            g += green(avgColor);//avgColor>>8&0xFF;
            b += blue(avgColor);//avgColor&0xFF;
            count += 1;
          }
          if(!(red(downSized[i][j+1][k+1])<=50.0 && green(downSized[i][j+1][k+1])<=50.0 && blue(downSized[i][j+1][k+1])<=50.0)){
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
             downSized[i+1][indexX][indexY] = color(0.0,0.0,0.0);
          }
          indexY += 1;
        }
        indexX += 1;
      }
     
      downsize = downsize/2;
       //println("=================================================="+i+","+downsize+"==============");
    }
  
  //this is done to get the last downsized pixel value
   downSized[9] = new color[1][1];
   { int count = 0;
     float r =0, g=0, b=0;
     if(!(red(downSized[8][0][0])<=50.0 && green(downSized[8][0][0])<=50.0 && blue(downSized[8][0][0])<=50.0)){
            avgColor = downSized[8][0][0];
            r += red(avgColor);//avgColor>>16&0xFF;
            g += green(avgColor);//avgColor>>8&0xFF;
            b += blue(avgColor);//avgColor&0xFF;
            count += 1;
          }
          if(!(red(downSized[8][1][0])<=50.0 && green(downSized[8][1][0])<=50.0 && blue(downSized[8][1][0])<=50.0)){
            avgColor =downSized[8][1][0];
            r += red(avgColor);//avgColor>>16&0xFF;
            g += green(avgColor);//avgColor>>8&0xFF;
            b += blue(avgColor);//avgColor&0xFF;
            count += 1;
          }
          if(!(red(downSized[8][0][1])<=50.0 && green(downSized[8][0][1])<=50.0 && blue(downSized[8][0][1])<=50.0)){
            avgColor =downSized[8][0][1];
            r += red(avgColor);//avgColor>>16&0xFF;
            g += green(avgColor);//avgColor>>8&0xFF;
            b += blue(avgColor);//avgColor&0xFF;
            count += 1;
          }
          if(!(red(downSized[8][1][1])<=50.0 && green(downSized[8][1][1])<=50.0 && blue(downSized[8][1][1])<=50.0)){
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
  
  upSized = new color[levels+1][][];
  upSized[9] = new color[downsize][downsize];
  upSized[9][0][0] = downSized[9][0][0];
  downsize =2;
  for(int i=8; i>=0; i--){
    color [][]temp = new color[downsize][downsize];
    upSized[i] = new color[downsize][downsize];
    int indexX = 0;
    int indexY = 0;
    //print("bla");
    for(int j = 0; j < downsize; j += 2){
      indexY = 0;
      for(int k = 0; k < downsize; k += 2){
        if(!(red(downSized[i][j][k])<=50.0 && green(downSized[i][j][k])<=50.0 && blue(downSized[i][j][k])<=50.0)){
          //upSized[i][j][k] = downSized[i][j][k];
          temp[j][k] = downSized[i][j][k];
        }
        else{
          //upSized[i][j][k] = upSized[i+1][indexX][indexY];
          temp[j][k] = upSized[i+1][indexX][indexY];
        }
        if(!(red(downSized[i][j+1][k])<=50.0 && green(downSized[i][j+1][k])<=50.0 && blue(downSized[i][j+1][k])<=50.0)){
          //upSized[i][j+1][k] = downSized[i][j+1][k];
          temp[j+1][k] = downSized[i][j+1][k];
        }
        else{
          //upSized[i][j+1][k] = upSized[i+1][indexX][indexY];
          temp[j+1][k] = upSized[i+1][indexX][indexY]; 
        }
        if(!(red(downSized[i][j][k+1])<=50.0 && green(downSized[i][j][k+1])<=50.0 && blue(downSized[i][j][k+1])<=50.0)){
          //upSized[i][j][k+1] = downSized[i][j][k+1];
          temp[j][k+1] = downSized[i][j][k+1];
        }
        else{
          //upSized[i][j][k+1] = upSized[i+1][indexX][indexY];
          temp[j][k+1] = upSized[i+1][indexX][indexY];
        }
        if(!(red(downSized[i][j+1][k+1])<=50.0 && green(downSized[i][j+1][k+1])<=50.0 && blue(downSized[i][j+1][k+1])<=50.0)){
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
    noLoop();
    for(int l =0; l<40; l++){
      for(int j = 0; j < downsize; j++){
        for(int k = 0; k < downsize; k++){
          float sr =0, sg=0, sb=0;
          int smoothCount = 0;
          if(red(downSized[i][j][k])<=50.0 && green(downSized[i][j][k])<=50.0 && blue(downSized[i][j][k])<=50.0){
            if((j-1)>=0 ){
              sr += red(upSized[i][j-1][k]);
              sg += green(upSized[i][j-1][k]);
              sb += blue(upSized[i][j-1][k]);
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
            if((j+1)<downsize){
              sr += red(upSized[i][j+1][k]);
              sg += green(upSized[i][j+1][k]);
              sb += blue(upSized[i][j+1][k]);
              smoothCount += 1;
            }        
            sr /= (float)smoothCount;
            sg /= (float)smoothCount;
            sb /= (float)smoothCount;
            temp[j][k] = color(sr, sg, sb);
          }
          else{
            temp[j][k] = upSized[i][j][k];
          }
        }
      }
      upSized[i] = temp;
    }
    downsize *= 2;
  }
  loop();
}


void diffusion(){
  
  if(!curveSelected){
    println("Select curve by pressing 1,2 3,4");  
  }
  if(setColor&&curveSelected){
    doneBool = false;
    excuteBool = true;  
    displayBool = false;
    delay(500);
    downUpSize();
    doneBool = true;
  }
}  
